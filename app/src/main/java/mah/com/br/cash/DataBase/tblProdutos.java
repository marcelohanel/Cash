package mah.com.br.cash.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import mah.com.br.cash.Diversos.Funcoes;

public class tblProdutos {

    private String mTable = "produto";

    private int idProduto = -1;
    private String nome = "";
    private int idGrupoProduto = -1;
    private String referencia = "";
    private String unidade = "";
    private double vlrVenda = 0.0;
    private String dtCadastro = "";

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    private void setIdProduto() {
        SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT MAX(id_produto) FROM " + mTable);
        this.idProduto = (int) sStatement.simpleQueryForLong() + 1;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public double getVlrVenda() {
        return vlrVenda;
    }

    public void setVlrVenda(double vlrVenda) {
        this.vlrVenda = vlrVenda;
    }

    public String getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(String dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public int getIdGrupoProduto() {
        return idGrupoProduto;
    }

    public void setIdGrupoProduto(int idGrupoProduto) {
        this.idGrupoProduto = idGrupoProduto;
    }

    public String getCalcNomeGrupoProduto() {

        tblGruposProduto record = new tblGruposProduto();
        record.getRecord(getIdGrupoProduto());
        String sAux = record.getNome();

        return sAux;
    }

    public Double getCalcMargem() {

        try {
            double dRetorno;
            double dVlrCompra = getCalcVlrCompra();
            double dVlrVenda = getVlrVenda();

            dRetorno = dVlrVenda - dVlrCompra;

            if (dVlrCompra != 0)
                dRetorno = dRetorno * 100 / dVlrCompra;
            else {
                if (dVlrVenda != 0)
                    dRetorno = 100.0;
                else
                    dRetorno = 0;
            }

            return dRetorno;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public Double getCalcEstoque() {
        try {
            Double qtdeCompra = 0.0;
            Double qtdeVenda = 0.0;
            Cursor cursorSum;

            cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(qt_itens) FROM compra_itens WHERE id_produto = ?", new String[]{String.valueOf(getIdProduto())});
            if (cursorSum.getCount() > 0) {
                cursorSum.moveToFirst();
                qtdeCompra = cursorSum.getDouble(0);
            }
            cursorSum.close();

            cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(qt_itens) FROM venda_itens WHERE id_produto = ?", new String[]{String.valueOf(getIdProduto())});
            if (cursorSum.getCount() > 0) {
                cursorSum.moveToFirst();
                qtdeVenda = cursorSum.getDouble(0);
            }
            cursorSum.close();

            return qtdeCompra - qtdeVenda;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public String getCalcFornecedor() {
        try {
            Long lAux;

            SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement(
                    "SELECT id_compra FROM compra_itens WHERE" +
                            " id_produto = " + String.valueOf(getIdProduto()) +
                            " ORDER BY dt_compra_inv DESC LIMIT 1"
            );

            lAux = sStatement.simpleQueryForLong();
            tblCompras record = new tblCompras();
            record.getRecord(lAux.intValue());

            return record.getCalcNomeFornecedor();
        } catch (Exception e) {
            return "";
        }
    }

    public double getCalcVlrCompra() {
        try {
            Double dAux = 0.0;
            Cursor cursorSum;

            cursorSum = Funcoes.mDataBase.rawQuery("SELECT vlr_unitario FROM compra_itens WHERE id_produto = " + String.valueOf(getIdProduto()) + " ORDER BY dt_compra_inv DESC LIMIT 1", null);
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

    private ContentValues getValues() {

        ContentValues values = new ContentValues();

        values.put("id_produto", getIdProduto());
        values.put("nome", getNome());
        values.put("id_grupo_produto", getIdGrupoProduto());
        values.put("referencia", getReferencia());
        values.put("unidade", getUnidade());
        values.put("vlr_venda", getVlrVenda());
        values.put("dt_cadastro", getDtCadastro());

        return values;
    }

    public long insert() {

        setIdProduto();

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
            return Funcoes.mDataBase.update(mTable, getValues(), "id_produto = ?", new String[]{String.valueOf(getIdProduto())});
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete() {

        try {
            return Funcoes.mDataBase.delete(mTable, "id_produto = ?", new String[]{String.valueOf(getIdProduto())});
        } catch (Exception e) {
            return -1;
        }
    }

    public void getRecord(int id_produto) {

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_produto = ?", new String[]{String.valueOf(id_produto)}, null, null, null);
        while (cursor.moveToNext()) {
            setIdProduto(cursor.getInt(cursor.getColumnIndex("id_produto")));
            setIdGrupoProduto(cursor.getInt(cursor.getColumnIndex("id_grupo_produto")));
            setNome(cursor.getString(cursor.getColumnIndex("nome")));
            setReferencia(cursor.getString(cursor.getColumnIndex("referencia")));
            setUnidade(cursor.getString(cursor.getColumnIndex("unidade")));
            setVlrVenda(cursor.getDouble(cursor.getColumnIndex("vlr_venda")));
            setDtCadastro(cursor.getString(cursor.getColumnIndex("dt_cadastro")));
        }
        cursor.close();
    }

    public List<tblProdutos> getList() {

        List<tblProdutos> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, null, null, null, null, null);
        while (cursor.moveToNext()) {

            tblProdutos tblProduto = new tblProdutos();

            tblProduto.setIdProduto(cursor.getInt(cursor.getColumnIndex("id_produto")));
            tblProduto.setIdGrupoProduto(cursor.getInt(cursor.getColumnIndex("id_grupo_produto")));
            tblProduto.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            tblProduto.setReferencia(cursor.getString(cursor.getColumnIndex("referencia")));
            tblProduto.setUnidade(cursor.getString(cursor.getColumnIndex("unidade")));
            tblProduto.setVlrVenda(cursor.getDouble(cursor.getColumnIndex("vlr_venda")));
            tblProduto.setDtCadastro(cursor.getString(cursor.getColumnIndex("dt_cadastro")));

            mList.add(tblProduto);
        }
        cursor.close();

        return mList;
    }
}
