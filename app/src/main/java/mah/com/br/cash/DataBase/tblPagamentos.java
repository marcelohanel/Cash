package mah.com.br.cash.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import mah.com.br.cash.Diversos.Funcoes;

public class tblPagamentos {

    private String mTable = "pagamento";

    private int idPagamento = -1;
    private int idFornecedor = -1;
    private String dtPagamento = "";
    private Double vlrPagamento = 0.0;
    private String observacoes = "";
    private int dtPagamentoInv = -1;
    private int mes = -1;
    private int ano = -1;

    public int getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(int idPagamento) {
        this.idPagamento = idPagamento;
    }

    private void setIdPagamento() {
        SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT MAX(id_pagamento) FROM " + mTable);
        this.idPagamento = (int) sStatement.simpleQueryForLong() + 1;
    }

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public Double getVlrPagamento() {
        return vlrPagamento;
    }

    public void setVlrPagamento(Double vlrPagamento) {
        this.vlrPagamento = vlrPagamento;
    }

    public String getDtPagamento() {
        return dtPagamento;
    }

    public void setDtPagamento(String dtPagamento) {

        String[] vData = Funcoes.getData(dtPagamento);

        this.dtPagamento = dtPagamento;
        this.ano = Integer.valueOf(vData[3]);
        this.mes = Integer.valueOf(vData[2]);
        this.dtPagamentoInv = Integer.valueOf(vData[4]);
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public int getDtPagamentoInv() {
        return dtPagamentoInv;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }

    public String getCalcNomeFornecedor() {

        tblFornecedores record = new tblFornecedores();

        record.getRecord(getIdFornecedor());
        String sNome = record.getNome();

        return sNome;
    }

    private ContentValues getValues() {

        ContentValues values = new ContentValues();

        values.put("id_pagamento", getIdPagamento());
        values.put("id_fornecedor", getIdFornecedor());
        values.put("dt_pagamento", getDtPagamento());
        values.put("vlr_pagamento", getVlrPagamento());
        values.put("observacoes", getObservacoes());
        values.put("dt_pagamento_inv", getDtPagamentoInv());
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
            return Funcoes.mDataBase.update(mTable, getValues(), "id_pagamento = ?", new String[]{String.valueOf(getIdPagamento())});
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete() {

        try {
            return Funcoes.mDataBase.delete(mTable, "id_pagamento = ?", new String[]{String.valueOf(getIdPagamento())});
        } catch (Exception e) {
            return -1;
        }
    }

    public void getRecord(int id_pagamento) {

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_pagamento = ?", new String[]{String.valueOf(id_pagamento)}, null, null, null);

        while (cursor.moveToNext()) {
            setIdPagamento(cursor.getInt(cursor.getColumnIndex("id_pagamento")));
            setIdFornecedor(cursor.getInt(cursor.getColumnIndex("id_fornecedor")));
            setDtPagamento(cursor.getString(cursor.getColumnIndex("dt_pagamento")));
            setVlrPagamento(cursor.getDouble(cursor.getColumnIndex("vlr_pagamento")));
            setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));
        }
        cursor.close();
    }

    public List<tblPagamentos> getList(int mes, int ano) {

        List<tblPagamentos> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "mes = ? AND ano = ?", new String[]{String.valueOf(mes), String.valueOf(ano)}, null, null, null);
        while (cursor.moveToNext()) {

            tblPagamentos record = new tblPagamentos();

            record.setIdPagamento(cursor.getInt(cursor.getColumnIndex("id_pagamento")));
            record.setIdFornecedor(cursor.getInt(cursor.getColumnIndex("id_fornecedor")));
            record.setDtPagamento(cursor.getString(cursor.getColumnIndex("dt_pagamento")));
            record.setVlrPagamento(cursor.getDouble(cursor.getColumnIndex("vlr_pagamento")));
            record.setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));

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
