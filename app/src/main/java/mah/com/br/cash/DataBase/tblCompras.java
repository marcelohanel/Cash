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

public class tblCompras {

    private String mTable = "compra";

    private int idCompra = -1;
    private String identificacao = "";
    private int idFornecedor = -1;
    private String observacoes = "";
    private String dtCompra = "";
    private int mes = -1;
    private int ano = -1;
    private int dtCompraInv = -1;

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    private void setIdCompra() {
        SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT MAX(id_compra) FROM " + mTable);
        this.idCompra = (int) sStatement.simpleQueryForLong() + 1;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getDtCompra() {
        return dtCompra;
    }

    public void setDtCompra(String dtCompra) {

        String[] vData = Funcoes.getData(dtCompra);

        this.dtCompra = dtCompra;
        this.ano = Integer.valueOf(vData[3]);
        this.mes = Integer.valueOf(vData[2]);
        this.dtCompraInv = Integer.valueOf(vData[4]);
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }

    public int getDtCompraInv() {
        return dtCompraInv;
    }

    public String getCalcNomeFornecedor() {

        tblFornecedores record = new tblFornecedores();
        record.getRecord(getIdFornecedor());
        String sAux = record.getNome();

        return sAux;
    }

    public Double getCalcVlrCompra() {
        try {
            Double vlrTotal = 0.0;
            Cursor cursor;

            cursor = Funcoes.mDataBase.query("compra_itens", new String[]{"qt_itens", "vlr_unitario"}, "id_compra = ?", new String[]{String.valueOf(getIdCompra())}, null, null, null);
            while (cursor.moveToNext()) {
                vlrTotal += cursor.getDouble(cursor.getColumnIndex("qt_itens")) * cursor.getDouble(cursor.getColumnIndex("vlr_unitario"));
            }
            cursor.close();

            return vlrTotal;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public int getCalcQtdeItensCompra() {
        try {
            SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT COUNT(id_compra_item) FROM compra_itens WHERE id_compra = " + String.valueOf(getIdCompra()));
            return (int) sStatement.simpleQueryForLong();
        } catch (Exception e) {
            return 0;
        }
    }

    public Double getCalcVlrPagamento() {

        try {
            Cursor cursorSum;
            Double vlrAux = 0.0;

            cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_pagamento) FROM compra_pagamentos WHERE id_compra = ?", new String[]{String.valueOf(getIdCompra())});
            if (cursorSum.getCount() > 0) {
                cursorSum.moveToFirst();
                vlrAux = cursorSum.getDouble(0);
            }
            cursorSum.close();

            return vlrAux;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public List<TempValores> getMaioresFornecedoresCompras(Context c, String mes, String ano) {

        try {

            List<TempValores> mList = new ArrayList<>();
            Cursor cursor;
            tblFornecedores recFornecedor = new tblFornecedores();
            String sSQL;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT compra.id_fornecedor, SUM(compra_itens.vlr_unitario * compra_itens.qt_itens) " +
                        " FROM compra, compra_itens " +
                        " WHERE compra_itens.id_compra = compra.id_compra " +
                        " GROUP BY compra.id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, null);
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recFornecedor.getIdFornecedor());
                    val.setNome(recFornecedor.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT compra.id_fornecedor, SUM(compra_itens.vlr_unitario * compra_itens.qt_itens) " +
                        " FROM compra, compra_itens " +
                        " WHERE compra_itens.id_compra = compra.id_compra " +
                        " AND compra.ano = ? " +
                        " GROUP BY compra.id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{ano});
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recFornecedor.getIdFornecedor());
                    val.setNome(recFornecedor.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT compra.id_fornecedor, SUM(compra_itens.vlr_unitario * compra_itens.qt_itens) " +
                        " FROM compra, compra_itens " +
                        " WHERE compra_itens.id_compra = compra.id_compra " +
                        " AND compra.mes = ? " +
                        " GROUP BY compra.id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes});
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recFornecedor.getIdFornecedor());
                    val.setNome(recFornecedor.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else {

                sSQL = " SELECT compra.id_fornecedor, SUM(compra_itens.vlr_unitario * compra_itens.qt_itens) " +
                        " FROM compra, compra_itens " +
                        " WHERE compra_itens.id_compra = compra.id_compra " +
                        " AND compra.mes = ? " +
                        " AND compra.ano = ? " +
                        " GROUP BY compra.id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes, ano});
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recFornecedor.getIdFornecedor());
                    val.setNome(recFornecedor.getNome());
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

    public List<TempValores> getMaioresFornecedoresPagar(Context c, String mes, String ano) {

        try {

            List<TempValores> mList = new ArrayList<>();
            Cursor cursor;
            tblFornecedores recFornecedor = new tblFornecedores();
            String sSQL;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT compra.id_fornecedor, SUM(compra_pagamentos.vlr_pagamento) " +
                        " FROM compra, compra_pagamentos " +
                        " WHERE compra_pagamentos.id_compra = compra.id_compra " +
                        " GROUP BY compra.id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, null);
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recFornecedor.getIdFornecedor());
                    val.setNome(recFornecedor.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT compra.id_fornecedor, SUM(compra_pagamentos.vlr_pagamento) " +
                        " FROM compra, compra_pagamentos " +
                        " WHERE compra_pagamentos.id_compra = compra.id_compra " +
                        " AND compra_pagamentos.ano = ? " +
                        " GROUP BY compra.id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{ano});
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recFornecedor.getIdFornecedor());
                    val.setNome(recFornecedor.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT compra.id_fornecedor, SUM(compra_pagamentos.vlr_pagamento) " +
                        " FROM compra, compra_pagamentos " +
                        " WHERE compra_pagamentos.id_compra = compra.id_compra " +
                        " AND compra_pagamentos.mes = ? " +
                        " GROUP BY compra.id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes});
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recFornecedor.getIdFornecedor());
                    val.setNome(recFornecedor.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else {

                sSQL = " SELECT compra.id_fornecedor, SUM(compra_pagamentos.vlr_pagamento) " +
                        " FROM compra, compra_pagamentos " +
                        " WHERE compra_pagamentos.id_compra = compra.id_compra " +
                        " AND compra_pagamentos.mes = ? " +
                        " AND compra_pagamentos.ano = ? " +
                        " GROUP BY compra.id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes, ano});
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recFornecedor.getIdFornecedor());
                    val.setNome(recFornecedor.getNome());
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

    public List<TempValores> getMaioresFornecedoresPago(Context c, String mes, String ano) {

        try {

            List<TempValores> mList = new ArrayList<>();
            Cursor cursor;
            tblFornecedores recFornecedor = new tblFornecedores();
            String sSQL;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT id_fornecedor, SUM(vlr_pagamento) " +
                        " FROM pagamento " +
                        " GROUP BY id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, null);
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recFornecedor.getIdFornecedor());
                    val.setNome(recFornecedor.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT id_fornecedor, SUM(vlr_pagamento) " +
                        " FROM pagamento " +
                        " WHERE ano = ? " +
                        " GROUP BY id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{ano});
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recFornecedor.getIdFornecedor());
                    val.setNome(recFornecedor.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT id_fornecedor, SUM(vlr_pagamento) " +
                        " FROM pagamento " +
                        " WHERE mes = ? " +
                        " GROUP BY id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes});
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recFornecedor.getIdFornecedor());
                    val.setNome(recFornecedor.getNome());
                    val.setValor(cursor.getDouble(1));

                    mList.add(val);
                }
                cursor.close();

            } else {

                sSQL = " SELECT id_fornecedor, SUM(vlr_pagamento) " +
                        " FROM pagamento " +
                        " WHERE mes = ? " +
                        " AND ano = ? " +
                        " GROUP BY id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes, ano});
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(0));
                    TempValores val = new TempValores();

                    val.setCodigo(recFornecedor.getIdFornecedor());
                    val.setNome(recFornecedor.getNome());
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

    public List<TempValores> getSaldoPagarFornecedores(Context c, String mes, String ano) {

        double dVlrAux_1;
        double dVlrAux_2;
        boolean lAchou;

        List<TempValores> mListPagar = new ArrayList<>();
        List<TempValores> mListPago = new ArrayList<>();

        mListPagar = getMaioresFornecedoresPagar(c, mes, ano);
        mListPago = getMaioresFornecedoresPago(c, mes, ano);

        for (int i = 0; i <= mListPagar.size() - 1; i++) {

            for (int a = 0; a <= mListPago.size() - 1; a++) {

                if (mListPagar.get(i).getCodigo() == mListPago.get(a).getCodigo()) {

                    dVlrAux_1 = mListPagar.get(i).getValor();
                    dVlrAux_2 = mListPago.get(a).getValor();

                    mListPagar.get(i).setValor(dVlrAux_1 - dVlrAux_2);
                    break;
                }
            }
        }

        for (int a = 0; a <= mListPago.size() - 1; a++) {

            lAchou = false;

            for (int i = 0; i <= mListPagar.size() - 1; i++) {

                if (mListPagar.get(i).getCodigo() == mListPago.get(a).getCodigo()) {
                    lAchou = true;
                    break;
                }
            }

            if (!lAchou) {
                TempValores recValor = new TempValores();

                recValor.setCodigo(mListPago.get(a).getCodigo());
                recValor.setNome(mListPago.get(a).getNome());
                recValor.setValor(mListPago.get(a).getValor() * -1);

                mListPagar.add(recValor);
            }
        }

        return mListPagar;
    }

    public List<TempValores> getMaioresProdutosCompras(Context c, String mes, String ano, String tipo) {

        try {

            List<TempValores> mList = new ArrayList<>();
            Cursor cursor;
            tblProdutos recProduto = new tblProdutos();
            String sSQL;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                if (tipo.matches("valor")) {
                    sSQL = " SELECT compra_itens.id_produto, SUM(compra_itens.vlr_unitario * compra_itens.qt_itens) " +
                            " FROM compra, compra_itens " +
                            " WHERE compra_itens.id_compra = compra.id_compra " +
                            " GROUP BY compra_itens.id_produto";
                } else {
                    sSQL = " SELECT compra_itens.id_produto, SUM(compra_itens.qt_itens) " +
                            " FROM compra, compra_itens " +
                            " WHERE compra_itens.id_compra = compra.id_compra " +
                            " GROUP BY compra_itens.id_produto";
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
                    sSQL = " SELECT compra_itens.id_produto, SUM(compra_itens.vlr_unitario * compra_itens.qt_itens) " +
                            " FROM compra, compra_itens " +
                            " WHERE compra_itens.id_compra = compra.id_compra " +
                            " AND compra.ano = ? " +
                            " GROUP BY compra_itens.id_produto";
                } else {
                    sSQL = " SELECT compra_itens.id_produto, SUM(compra_itens.qt_itens) " +
                            " FROM compra, compra_itens " +
                            " WHERE compra_itens.id_compra = compra.id_compra " +
                            " AND compra.ano = ? " +
                            " GROUP BY compra_itens.id_produto";
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
                    sSQL = " SELECT compra_itens.id_produto, SUM(compra_itens.vlr_unitario * compra_itens.qt_itens) " +
                            " FROM compra, compra_itens " +
                            " WHERE compra_itens.id_compra = compra.id_compra " +
                            " AND compra.mes = ? " +
                            " GROUP BY compra_itens.id_produto";
                } else {
                    sSQL = " SELECT compra_itens.id_produto, SUM(compra_itens.qt_itens) " +
                            " FROM compra, compra_itens " +
                            " WHERE compra_itens.id_compra = compra.id_compra " +
                            " AND compra.mes = ? " +
                            " GROUP BY compra_itens.id_produto";
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
                    sSQL = " SELECT compra_itens.id_produto, SUM(compra_itens.vlr_unitario * compra_itens.qt_itens) " +
                            " FROM compra, compra_itens " +
                            " WHERE compra_itens.id_compra = compra.id_compra " +
                            " AND compra.mes = ? " +
                            " AND compra.ano = ? " +
                            " GROUP BY compra_itens.id_produto";
                } else {
                    sSQL = " SELECT compra_itens.id_produto, SUM(compra_itens.qt_itens) " +
                            " FROM compra, compra_itens " +
                            " WHERE compra_itens.id_compra = compra.id_compra " +
                            " AND compra.mes = ? " +
                            " AND compra.ano = ? " +
                            " GROUP BY compra_itens.id_produto";
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

    public List<TempValores> getDiarioFornecedoresPagar(Context c, String mes, String ano) {

        try {

            List<TempValores> mList = new ArrayList<>();
            Cursor cursor;
            tblFornecedores recFornecedor = new tblFornecedores();
            String sSQL;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT compra_pagamentos.dt_pagamento, compra.id_fornecedor, SUM(compra_pagamentos.vlr_pagamento) " +
                        " FROM compra, compra_pagamentos " +
                        " WHERE compra_pagamentos.id_compra = compra.id_compra " +
                        " GROUP BY compra_pagamentos.dt_pagamento, compra.id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, null);
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(0);
                    val.setData(cursor.getString(0));
                    val.setNome(recFornecedor.getNome());
                    val.setValor(cursor.getDouble(2));

                    mList.add(val);
                }
                cursor.close();

            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT compra_pagamentos.dt_pagamento, compra.id_fornecedor, SUM(compra_pagamentos.vlr_pagamento) " +
                        " FROM compra, compra_pagamentos " +
                        " WHERE compra_pagamentos.id_compra = compra.id_compra " +
                        " AND compra_pagamentos.ano = ? " +
                        " GROUP BY compra_pagamentos.dt_pagamento, compra.id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{ano});
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(0);
                    val.setData(cursor.getString(0));
                    val.setNome(recFornecedor.getNome());
                    val.setValor(cursor.getDouble(2));

                    mList.add(val);
                }
                cursor.close();

            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT compra_pagamentos.dt_pagamento, compra.id_fornecedor, SUM(compra_pagamentos.vlr_pagamento) " +
                        " FROM compra, compra_pagamentos " +
                        " WHERE compra_pagamentos.id_compra = compra.id_compra " +
                        " AND compra_pagamentos.mes = ? " +
                        " GROUP BY compra_pagamentos.dt_pagamento, compra.id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes});
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(0);
                    val.setData(cursor.getString(0));
                    val.setNome(recFornecedor.getNome());
                    val.setValor(cursor.getDouble(2));

                    mList.add(val);
                }
                cursor.close();

            } else {

                sSQL = " SELECT compra_pagamentos.dt_pagamento, compra.id_fornecedor, SUM(compra_pagamentos.vlr_pagamento) " +
                        " FROM compra, compra_pagamentos " +
                        " WHERE compra_pagamentos.id_compra = compra.id_compra " +
                        " AND compra_pagamentos.mes = ? " +
                        " AND compra_pagamentos.ano = ? " +
                        " GROUP BY compra_pagamentos.dt_pagamento, compra.id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes, ano});
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(0);
                    val.setData(cursor.getString(0));
                    val.setNome(recFornecedor.getNome());
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

    public List<TempValores> getDiarioFornecedoresPago(Context c, String mes, String ano) {

        try {

            List<TempValores> mList = new ArrayList<>();
            Cursor cursor;
            tblFornecedores recFornecedor = new tblFornecedores();
            String sSQL;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT dt_pagamento, id_fornecedor, SUM(vlr_pagamento) " +
                        " FROM pagamento " +
                        " GROUP BY dt_pagamento, id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, null);
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(1);
                    val.setData(cursor.getString(0));
                    val.setNome(recFornecedor.getNome());
                    val.setValor(cursor.getDouble(2));

                    mList.add(val);
                }
                cursor.close();

            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT dt_pagamento, id_fornecedor, SUM(vlr_pagamento) " +
                        " FROM pagamento " +
                        " WHERE ano = ? " +
                        " GROUP BY dt_pagamento, id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{ano});
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(1);
                    val.setData(cursor.getString(0));
                    val.setNome(recFornecedor.getNome());
                    val.setValor(cursor.getDouble(2));

                    mList.add(val);
                }
                cursor.close();

            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {

                sSQL = " SELECT dt_pagamento, id_fornecedor, SUM(vlr_pagamento) " +
                        " FROM pagamento " +
                        " WHERE mes = ? " +
                        " GROUP BY dt_pagamento, id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes});
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(1);
                    val.setData(cursor.getString(0));
                    val.setNome(recFornecedor.getNome());
                    val.setValor(cursor.getDouble(2));

                    mList.add(val);
                }
                cursor.close();

            } else {

                sSQL = " SELECT dt_pagamento, id_fornecedor, SUM(vlr_pagamento) " +
                        " FROM pagamento " +
                        " WHERE mes = ? " +
                        " AND ano = ? " +
                        " GROUP BY dt_pagamento, id_fornecedor";

                cursor = Funcoes.mDataBase.rawQuery(sSQL, new String[]{mes, ano});
                while (cursor.moveToNext()) {
                    recFornecedor.getRecord(cursor.getInt(1));
                    TempValores val = new TempValores();

                    val.setCodigo(1);
                    val.setData(cursor.getString(0));
                    val.setNome(recFornecedor.getNome());
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

        values.put("id_compra", getIdCompra());
        values.put("identificacao", getIdentificacao());
        values.put("id_fornecedor", getIdFornecedor());
        values.put("observacoes", getObservacoes());
        values.put("dt_compra", getDtCompra());
        values.put("dt_compra_inv", getDtCompraInv());
        values.put("ano", getAno());
        values.put("mes", getMes());

        return values;
    }

    public long insert() {

        setIdCompra();

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
            SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("UPDATE compra_itens SET" +
                    " dt_compra = " + String.valueOf(getDtCompra()) +
                    ", dt_compra_inv = " + String.valueOf(getDtCompraInv()) +
                    " WHERE id_compra = " + String.valueOf(getIdCompra()));

            sStatement.executeUpdateDelete();

            return Funcoes.mDataBase.update(mTable, getValues(), "id_compra = ?", new String[]{String.valueOf(getIdCompra())});
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete() {

        try {
            return Funcoes.mDataBase.delete(mTable, "id_compra = ?", new String[]{String.valueOf(getIdCompra())});
        } catch (Exception e) {
            return -1;
        }
    }

    public void getRecord(int id_compra) {

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_compra = ?", new String[]{String.valueOf(id_compra)}, null, null, null);
        while (cursor.moveToNext()) {
            setIdCompra(cursor.getInt(cursor.getColumnIndex("id_compra")));
            setIdentificacao(cursor.getString(cursor.getColumnIndex("identificacao")));
            setIdFornecedor(cursor.getInt(cursor.getColumnIndex("id_fornecedor")));
            setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));
            setDtCompra(cursor.getString(cursor.getColumnIndex("dt_compra")));
        }
        cursor.close();
    }

    public List<tblCompras> getList(int mes, int ano) {

        List<tblCompras> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "mes = ? AND ano = ?", new String[]{String.valueOf(mes), String.valueOf(ano)}, null, null, null);
        while (cursor.moveToNext()) {

            tblCompras tblCompra = new tblCompras();

            tblCompra.setIdCompra(cursor.getInt(cursor.getColumnIndex("id_compra")));
            tblCompra.setIdentificacao(cursor.getString(cursor.getColumnIndex("identificacao")));
            tblCompra.setIdFornecedor(cursor.getInt(cursor.getColumnIndex("id_fornecedor")));
            tblCompra.setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));
            tblCompra.setDtCompra(cursor.getString(cursor.getColumnIndex("dt_compra")));

            mList.add(tblCompra);
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
