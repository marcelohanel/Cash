package mah.com.br.cash.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import mah.com.br.cash.Diversos.Funcoes;

public class tblCompraPagamentos {

    private String mTable = "compra_pagamentos";

    private int idCompraPagamento = -1;
    private int idCompra = -1;
    private Double vlrPagamento = 0.0;
    private String dtPagamento = "";
    private String observacoes = "";
    private int mes = -1;
    private int ano = -1;

    public int getIdPagamento() {
        return idCompraPagamento;
    }

    public void setIdPagamento(int idItem) {
        this.idCompraPagamento = idItem;
    }

    private void setIdPagamento() {
        SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT MAX(id_compra_pagamento) FROM " + mTable);
        this.idCompraPagamento = (int) sStatement.simpleQueryForLong() + 1;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public Double getVlrPagamento() {
        return vlrPagamento;
    }

    public void setVlrPagamento(Double vlrPagamento) {
        this.vlrPagamento = vlrPagamento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getDtPagamento() {
        return dtPagamento;
    }

    public void setDtPagamento(String dtPagamento) {

        String[] vData = Funcoes.getData(dtPagamento);

        this.dtPagamento = dtPagamento;
        this.ano = Integer.valueOf(vData[3]);
        this.mes = Integer.valueOf(vData[2]);
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }

    public int getCalcDtPagamentoInv() {

        String[] vData = Funcoes.getData(getDtPagamento());
        return Integer.valueOf(vData[4]);
    }

    private ContentValues getValues() {

        ContentValues values = new ContentValues();

        values.put("id_compra_pagamento", getIdPagamento());
        values.put("id_compra", getIdCompra());
        values.put("dt_pagamento", getDtPagamento());
        values.put("vlr_pagamento", getVlrPagamento());
        values.put("observacoes", getObservacoes());
        values.put("ano", getAno());
        values.put("mes", getMes());

        return values;
    }

    public long insert() {

        setIdPagamento();

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
            return Funcoes.mDataBase.update(mTable, getValues(), "id_compra_pagamento = ?", new String[]{String.valueOf(getIdPagamento())});
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete() {

        try {
            return Funcoes.mDataBase.delete(mTable, "id_compra_pagamento = ?", new String[]{String.valueOf(getIdPagamento())});
        } catch (Exception e) {
            return -1;
        }
    }

    public void getRecord(int id_compra_pagamento) {

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_compra_pagamento = ?", new String[]{String.valueOf(id_compra_pagamento)}, null, null, null);
        while (cursor.moveToNext()) {
            setIdPagamento(cursor.getInt(cursor.getColumnIndex("id_compra_pagamento")));
            setIdCompra(cursor.getInt(cursor.getColumnIndex("id_compra")));
            setDtPagamento(cursor.getString(cursor.getColumnIndex("dt_pagamento")));
            setVlrPagamento(cursor.getDouble(cursor.getColumnIndex("vlr_pagamento")));
            setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));
        }
        cursor.close();
    }

    public List<tblCompraPagamentos> getList(int id_compra) {

        List<tblCompraPagamentos> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_compra = ?", new String[]{String.valueOf(id_compra)}, null, null, null);
        while (cursor.moveToNext()) {

            tblCompraPagamentos tblPagamento = new tblCompraPagamentos();

            tblPagamento.setIdPagamento(cursor.getInt(cursor.getColumnIndex("id_compra_pagamento")));
            tblPagamento.setIdCompra(cursor.getInt(cursor.getColumnIndex("id_compra")));
            tblPagamento.setDtPagamento(cursor.getString(cursor.getColumnIndex("dt_pagamento")));
            tblPagamento.setVlrPagamento(cursor.getDouble(cursor.getColumnIndex("vlr_pagamento")));
            tblPagamento.setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));

            mList.add(tblPagamento);
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
