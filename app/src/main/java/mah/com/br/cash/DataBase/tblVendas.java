package mah.com.br.cash.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.Diversos.TempValores;
import mah.com.br.cash.R;

public class tblVendas {

    private String mTable = "venda";

    private int idVenda = -1;
    private String identificacao = "";
    private int idCliente = -1;
    private String observacoes = "";
    private String dtVenda = "";
    private int mes = -1;
    private int ano = -1;
    private int dtVendaInv = -1;

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    private void setIdVenda() {
        SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT MAX(id_venda) FROM " + mTable);
        this.idVenda = (int) sStatement.simpleQueryForLong() + 1;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getDtVenda() {
        return dtVenda;
    }

    public void setDtVenda(String dtVenda) {

        String[] vData = Funcoes.getData(dtVenda);

        this.dtVenda = dtVenda;
        this.ano = Integer.valueOf(vData[3]);
        this.mes = Integer.valueOf(vData[2]);
        this.dtVendaInv = Integer.valueOf(vData[4]);
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }

    public int getDtVendaInv() {
        return dtVendaInv;
    }

    public String getCalcNomeCliente() {

        tblClientes record = new tblClientes();

        record.getRecord(getIdCliente());
        String sNome = record.getNome();

        return sNome;
    }

    public Double getCalcVlrVenda() {
        try {
            Double vlrTotal = 0.0;
            Cursor cursor;

            cursor = Funcoes.mDataBase.query("venda_itens", new String[]{"qt_itens", "vlr_unitario"}, "id_venda = ?", new String[]{String.valueOf(getIdVenda())}, null, null, null);
            while (cursor.moveToNext()) {
                vlrTotal += cursor.getDouble(cursor.getColumnIndex("qt_itens")) * cursor.getDouble(cursor.getColumnIndex("vlr_unitario"));
            }
            cursor.close();

            return vlrTotal;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public int getCalcQtdeItensVenda() {
        try {
            SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT COUNT(id_venda_item) FROM venda_itens WHERE id_venda = " + String.valueOf(getIdVenda()));
            return (int) sStatement.simpleQueryForLong();
        } catch (Exception e) {
            return 0;
        }
    }

    public Double getCalcVlrRecebimento() {

        try {
            Double dAux = 0.0;
            Cursor cursorSum;

            cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_recebimento) FROM venda_recebimentos WHERE id_venda = ?", new String[]{String.valueOf(getIdVenda())});
            if (cursorSum.getCount() > 0) {
                cursorSum.moveToFirst();
                dAux = cursorSum.getDouble(0);
            }
            cursorSum.close();

            return dAux;

        } catch (Exception e) {
            return 0.0;
        }
    }

    public List<TempValores> getMaioresClientesVendas(Context c, String mes, String ano) {

        try {

            List<TempValores> mList = new ArrayList<>();
            Cursor cursor;
            tblClientes recCliente = new tblClientes();
            String sSQL;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT venda.id_cliente, SUM(venda_itens.vlr_unitario * venda_itens.qt_itens) " +
                        " FROM venda, venda_itens " +
                        " WHERE venda_itens.id_venda = venda.id_venda " +
                        " GROUP BY venda.id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, null);
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recCliente.getIdCliente());
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT venda.id_cliente, SUM(venda_itens.vlr_unitario * venda_itens.qt_itens) " +
                        " FROM venda, venda_itens " +
                        " WHERE venda_itens.id_venda = venda.id_venda " +
                        " AND venda.ano = ? " +
                        " GROUP BY venda.id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{ano});
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recCliente.getIdCliente());
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT venda.id_cliente, SUM(venda_itens.vlr_unitario * venda_itens.qt_itens) " +
                        " FROM venda, venda_itens " +
                        " WHERE venda_itens.id_venda = venda.id_venda " +
                        " AND venda.mes = ? " +
                        " GROUP BY venda.id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes});
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recCliente.getIdCliente());
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else {

                sSQL = " SELECT venda.id_cliente, SUM(venda_itens.vlr_unitario * venda_itens.qt_itens) " +
                        " FROM venda, venda_itens " +
                        " WHERE venda_itens.id_venda = venda.id_venda " +
                        " AND venda.mes = ? " +
                        " AND venda.ano = ? " +
                        " GROUP BY venda.id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes, ano});
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recCliente.getIdCliente());
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();
            }

            return mList;

        } catch (Exception e) {
            return null;
        }
    }

    public List<TempValores> getMaioresClientesReceber(Context c, String mes, String ano) {

        try {

            List<TempValores> mList = new ArrayList<>();
            Cursor cursor;
            tblClientes recCliente = new tblClientes();
            String sSQL;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT venda.id_cliente, SUM(venda_recebimentos.vlr_recebimento) " +
                        " FROM venda, venda_recebimentos " +
                        " WHERE venda_recebimentos.id_venda = venda.id_venda " +
                        " GROUP BY venda.id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, null);
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recCliente.getIdCliente());
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT venda.id_cliente, SUM(venda_recebimentos.vlr_recebimento) " +
                        " FROM venda, venda_recebimentos " +
                        " WHERE venda_recebimentos.id_venda = venda.id_venda " +
                        " AND venda_recebimentos.ano = ? " +
                        " GROUP BY venda.id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{ano});
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recCliente.getIdCliente());
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT venda.id_cliente, SUM(venda_recebimentos.vlr_recebimento) " +
                        " FROM venda, venda_recebimentos " +
                        " WHERE venda_recebimentos.id_venda = venda.id_venda " +
                        " AND venda_recebimentos.mes = ? " +
                        " GROUP BY venda.id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes});
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recCliente.getIdCliente());
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else {

                sSQL = " SELECT venda.id_cliente, SUM(venda_recebimentos.vlr_recebimento) " +
                        " FROM venda, venda_recebimentos " +
                        " WHERE venda_recebimentos.id_venda = venda.id_venda " +
                        " AND venda_recebimentos.mes = ? " +
                        " AND venda_recebimentos.ano = ? " +
                        " GROUP BY venda.id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes, ano});
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recCliente.getIdCliente());
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();
            }

            return mList;

        } catch (Exception e) {
            return null;
        }
    }

    public List<TempValores> getMaioresClientesRecebido(Context c, String mes, String ano) {

        try {

            List<TempValores> mList = new ArrayList<>();
            Cursor cursor;
            tblClientes recCliente = new tblClientes();
            String sSQL;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT id_cliente, SUM(vlr_recebimento) " +
                        " FROM recebimento " +
                        " GROUP BY id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, null);
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recCliente.getIdCliente());
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT id_cliente, SUM(vlr_recebimento) " +
                        " FROM recebimento " +
                        " WHERE ano = ? " +
                        " GROUP BY id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{ano});
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recCliente.getIdCliente());
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT id_cliente, SUM(vlr_recebimento) " +
                        " FROM recebimento " +
                        " WHERE mes = ? " +
                        " GROUP BY id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes});
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recCliente.getIdCliente());
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else {

                sSQL = " SELECT id_cliente, SUM(vlr_recebimento) " +
                        " FROM recebimento " +
                        " WHERE mes = ? " +
                        " AND ano = ? " +
                        " GROUP BY id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes, ano});
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recCliente.getIdCliente());
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();
            }

            return mList;

        } catch (Exception e) {
            return null;
        }
    }

    public List<TempValores> getSaldoReceberClientes(Context c, String mes, String ano) {

        double dVlrAux_1;
        double dVlrAux_2;
        boolean lAchou;

        List<TempValores> mListReceber = new ArrayList<>();
        List<TempValores> mListRecebido = new ArrayList<>();

        mListReceber = getMaioresClientesReceber(c, mes, ano);
        mListRecebido = getMaioresClientesRecebido(c, mes, ano);

        for (int i = 0; i <= mListReceber.size() - 1; i++) {

            for (int a = 0; a <= mListRecebido.size() - 1; a++) {

                if (mListReceber.get(i).getCodigo() == mListRecebido.get(a).getCodigo()) {

                    dVlrAux_1 = mListReceber.get(i).getValor();
                    dVlrAux_2 = mListRecebido.get(a).getValor();

                    mListReceber.get(i).setValor(dVlrAux_1 - dVlrAux_2);
                    break;
                }
            }
        }

        for (int a = 0; a <= mListRecebido.size() - 1; a++) {

            lAchou = false;

            for (int i = 0; i <= mListReceber.size() - 1; i++) {

                if (mListReceber.get(i).getCodigo() == mListRecebido.get(a).getCodigo()) {
                    lAchou = true;
                    break;
                }
            }

            if (!lAchou) {
                TempValores recValor = new TempValores();

                recValor.setCodigo(mListRecebido.get(a).getCodigo());
                recValor.setNome(mListRecebido.get(a).getNome());
                recValor.setValor(mListRecebido.get(a).getValor() * -1);

                mListReceber.add(recValor);
            }
        }


        return mListReceber;
    }

    public List<TempValores> getMaioresProdutosVendas(Context c, String mes, String ano, String tipo) {

        try {

            List<TempValores> mList = new ArrayList<>();
            Cursor cursor;
            tblProdutos recProduto = new tblProdutos();
            String sSQL;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                if (tipo.matches("valor")) {
                    sSQL = " SELECT venda_itens.id_produto, SUM(venda_itens.vlr_unitario * venda_itens.qt_itens) " +
                            " FROM venda, venda_itens " +
                            " WHERE venda_itens.id_venda = venda.id_venda " +
                            " GROUP BY venda_itens.id_produto";
                } else {
                    sSQL = " SELECT venda_itens.id_produto, SUM(venda_itens.qt_itens) " +
                            " FROM venda, venda_itens " +
                            " WHERE venda_itens.id_venda = venda.id_venda " +
                            " GROUP BY venda_itens.id_produto";
                }


                cursor = Funcoes.mDataBase.rawQuery(sSQL, null);
                while (cursor.moveToNext()) {
                    recProduto.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recProduto.getIdProduto());
                    val.setNome(recProduto.getNome());
                    val.setNome_1(recProduto.getUnidade());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {

                if (tipo.matches("valor")) {
                    sSQL = " SELECT venda_itens.id_produto, SUM(venda_itens.vlr_unitario * venda_itens.qt_itens) " +
                            " FROM venda, venda_itens " +
                            " WHERE venda_itens.id_venda = venda.id_venda " +
                            " AND venda.ano = ? " +
                            " GROUP BY venda_itens.id_produto";
                } else {
                    sSQL = " SELECT venda_itens.id_produto, SUM(venda_itens.qt_itens) " +
                            " FROM venda, venda_itens " +
                            " WHERE venda_itens.id_venda = venda.id_venda " +
                            " AND venda.ano = ? " +
                            " GROUP BY venda_itens.id_produto";
                }

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{ano});
                while (cursor.moveToNext()) {
                    recProduto.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recProduto.getIdProduto());
                    val.setNome(recProduto.getNome());
                    val.setNome_1(recProduto.getUnidade());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                if (tipo.matches("valor")) {
                    sSQL = " SELECT venda_itens.id_produto, SUM(venda_itens.vlr_unitario * venda_itens.qt_itens) " +
                            " FROM venda, venda_itens " +
                            " WHERE venda_itens.id_venda = venda.id_venda " +
                            " AND venda.mes = ? " +
                            " GROUP BY venda_itens.id_produto";
                } else {
                    sSQL = " SELECT venda_itens.id_produto, SUM(venda_itens.qt_itens) " +
                            " FROM venda, venda_itens " +
                            " WHERE venda_itens.id_venda = venda.id_venda " +
                            " AND venda.mes = ? " +
                            " GROUP BY venda_itens.id_produto";
                }

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes});
                while (cursor.moveToNext()) {
                    recProduto.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recProduto.getIdProduto());
                    val.setNome(recProduto.getNome());
                    val.setNome_1(recProduto.getUnidade());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else {
                if (tipo.matches("valor")) {
                    sSQL = " SELECT venda_itens.id_produto, SUM(venda_itens.vlr_unitario * venda_itens.qt_itens) " +
                            " FROM venda, venda_itens " +
                            " WHERE venda_itens.id_venda = venda.id_venda " +
                            " AND venda.mes = ? " +
                            " AND venda.ano = ? " +
                            " GROUP BY venda_itens.id_produto";
                } else {
                    sSQL = " SELECT venda_itens.id_produto, SUM(venda_itens.qt_itens) " +
                            " FROM venda, venda_itens " +
                            " WHERE venda_itens.id_venda = venda.id_venda " +
                            " AND venda.mes = ? " +
                            " AND venda.ano = ? " +
                            " GROUP BY venda_itens.id_produto";
                }

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes, ano});
                while (cursor.moveToNext()) {
                    recProduto.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recProduto.getIdProduto());
                    val.setNome(recProduto.getNome());
                    val.setNome_1(recProduto.getUnidade());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();
            }

            return mList;

        } catch (Exception e) {
            return null;
        }
    }

    public List<TempValores> getDiarioClientesReceber(Context c, String mes, String ano) {

        try {

            List<TempValores> mList = new ArrayList<>();
            Cursor cursor;
            tblClientes recCliente = new tblClientes();
            String sSQL;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT venda_recebimentos.dt_recebimento, venda.id_cliente, SUM(venda_recebimentos.vlr_recebimento) " +
                        " FROM venda, venda_recebimentos " +
                        " WHERE venda_recebimentos.id_venda = venda.id_venda " +
                        " GROUP BY venda_recebimentos.dt_recebimento, venda.id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, null);
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(2);
                    val.setData(cursor.getString(0));
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(2));

                    mList.add(val);
                }
                cursor.close();

            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT venda_recebimentos.dt_recebimento, venda.id_cliente, SUM(venda_recebimentos.vlr_recebimento) " +
                        " FROM venda, venda_recebimentos " +
                        " WHERE venda_recebimentos.id_venda = venda.id_venda " +
                        " AND venda_recebimentos.ano = ? " +
                        " GROUP BY venda_recebimentos.dt_recebimento, venda.id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{ano});
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(2);
                    val.setData(cursor.getString(0));
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(2));

                    mList.add(val);
                }
                cursor.close();

            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT venda_recebimentos.dt_recebimento, venda.id_cliente, SUM(venda_recebimentos.vlr_recebimento) " +
                        " FROM venda, venda_recebimentos " +
                        " WHERE venda_recebimentos.id_venda = venda.id_venda " +
                        " AND venda_recebimentos.mes = ? " +
                        " GROUP BY venda_recebimentos.dt_recebimento, venda.id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes});
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(2);
                    val.setData(cursor.getString(0));
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(2));

                    mList.add(val);
                }
                cursor.close();

            } else {

                sSQL = " SELECT venda_recebimentos.dt_recebimento, venda.id_cliente, SUM(venda_recebimentos.vlr_recebimento) " +
                        " FROM venda, venda_recebimentos " +
                        " WHERE venda_recebimentos.id_venda = venda.id_venda " +
                        " AND venda_recebimentos.mes = ? " +
                        " AND venda_recebimentos.ano = ? " +
                        " GROUP BY venda_recebimentos.dt_recebimento, venda.id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes, ano});
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(2);
                    val.setData(cursor.getString(0));
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(2));

                    mList.add(val);
                }
                cursor.close();
            }

            return mList;

        } catch (Exception e) {
            return null;
        }
    }

    public List<TempValores> getDiarioClientesRecebido(Context c, String mes, String ano) {

        try {

            List<TempValores> mList = new ArrayList<>();
            Cursor cursor;
            tblClientes recCliente = new tblClientes();
            String sSQL;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT dt_recebimento, id_cliente, SUM(vlr_recebimento) " +
                        " FROM recebimento " +
                        " GROUP BY dt_recebimento, id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, null);
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(3);
                    val.setData(cursor.getString(0));
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(2));

                    mList.add(val);
                }
                cursor.close();

            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT dt_recebimento, id_cliente, SUM(vlr_recebimento) " +
                        " FROM recebimento " +
                        " WHERE ano = ? " +
                        " GROUP BY dt_recebimento, id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{ano});
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(3);
                    val.setData(cursor.getString(0));
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(2));

                    mList.add(val);
                }
                cursor.close();

            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT dt_recebimento, id_cliente, SUM(vlr_recebimento) " +
                        " FROM recebimento " +
                        " WHERE mes = ? " +
                        " GROUP BY dt_recebimento, id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes});
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(3);
                    val.setData(cursor.getString(0));
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(2));

                    mList.add(val);
                }
                cursor.close();

            } else {

                sSQL = " SELECT dt_recebimento, id_cliente, SUM(vlr_recebimento) " +
                        " FROM recebimento " +
                        " WHERE mes = ? " +
                        " AND ano = ? " +
                        " GROUP BY dt_recebimento, id_cliente";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes, ano});
                while (cursor.moveToNext()) {
                    recCliente.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(3);
                    val.setData(cursor.getString(0));
                    val.setNome(recCliente.getNome());
                    val.setValor(cursor.getDouble(2));

                    mList.add(val);
                }
                cursor.close();
            }

            return mList;

        } catch (Exception e) {
            return null;
        }
    }

    private ContentValues getValues() {

        ContentValues values = new ContentValues();

        values.put("id_venda", getIdVenda());
        values.put("identificacao", getIdentificacao());
        values.put("id_cliente", getIdCliente());
        values.put("observacoes", getObservacoes());
        values.put("dt_venda", getDtVenda());
        values.put("dt_venda_inv", getDtVendaInv());
        values.put("ano", getAno());
        values.put("mes", getMes());

        return values;
    }

    public long insert() {

        setIdVenda();

        try {
            return Funcoes.mDataBase.insert(mTable, null, getValues());
        } catch (Exception e) {
            return -1;
        }
    }

    public long copy() {
        try {
            return Funcoes.mDataBase.insert(mTable, null, getValues());
        } catch (Exception e) {
            return -1;
        }
    }

    public int update() {

        try {
            SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("UPDATE venda_itens SET" +
                    " dt_venda = " + String.valueOf(getDtVenda()) +
                    ", dt_venda_inv = " + String.valueOf(getDtVendaInv()) +
                    " WHERE id_venda = " + String.valueOf(getIdVenda()));

            sStatement.executeUpdateDelete();

            return Funcoes.mDataBase.update(mTable, getValues(), "id_venda = ?", new String[]{String.valueOf(getIdVenda())});
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete() {

        try {
            return Funcoes.mDataBase.delete(mTable, "id_venda = ?", new String[]{String.valueOf(getIdVenda())});
        } catch (Exception e) {
            return -1;
        }
    }

    public void getRecord(int id_venda) {

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_venda = ?", new String[]{String.valueOf(id_venda)}, null, null, null);
        while (cursor.moveToNext()) {
            setIdVenda(cursor.getInt(cursor.getColumnIndex("id_venda")));
            setIdentificacao(cursor.getString(cursor.getColumnIndex("identificacao")));
            setIdCliente(cursor.getInt(cursor.getColumnIndex("id_cliente")));
            setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));
            setDtVenda(cursor.getString(cursor.getColumnIndex("dt_venda")));
        }
        cursor.close();
    }

    public List<tblVendas> getList(int mes, int ano) {

        List<tblVendas> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "mes = ? AND ano = ?", new String[]{String.valueOf(mes), String.valueOf(ano)}, null, null, null);
        while (cursor.moveToNext()) {

            tblVendas record = new tblVendas();

            record.setIdVenda(cursor.getInt(cursor.getColumnIndex("id_venda")));
            record.setIdentificacao(cursor.getString(cursor.getColumnIndex("identificacao")));
            record.setIdCliente(cursor.getInt(cursor.getColumnIndex("id_cliente")));
            record.setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));
            record.setDtVenda(cursor.getString(cursor.getColumnIndex("dt_venda")));

            mList.add(record);
        }
        cursor.close();

        return mList;
    }

    public List<String> getListMes() {

        List<String> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"mes"}, null, null, "mes", null, null);
        while (cursor.moveToNext()) {
            mList.add(cursor.getString(cursor.getColumnIndex("mes")));
        }
        cursor.close();

        return mList;
    }

    public List<String> getListAno() {

        List<String> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"ano"}, null, null, "ano", null, null);
        while (cursor.moveToNext()) {
            mList.add(cursor.getString(cursor.getColumnIndex("ano")));
        }
        cursor.close();

        return mList;
    }
}
