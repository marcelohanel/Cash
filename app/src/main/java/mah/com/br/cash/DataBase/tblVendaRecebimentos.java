package mah.com.br.cash.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import mah.com.br.cash.Diversos.Funcoes;

public class tblVendaRecebimentos {

    private String mTable = "venda_recebimentos";

    private int idVendaRecebimento = -1;
    private int idVenda = -1;
    private Double vlrRecebimento = 0.0;
    private String dtRecebimento = "";
    private String observacoes = "";
    private int mes = -1;
    private int ano = -1;

    public int getIdRecebimento() {
        return idVendaRecebimento;
    }

    public void setIdRecebimento(int idItem) {
        this.idVendaRecebimento = idItem;
    }

    private void setIdRecebimento() {
        SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT MAX(id_venda_recebimento) FROM " + mTable);
        this.idVendaRecebimento = (int) sStatement.simpleQueryForLong() + 1;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public Double getVlrRecebimento() {
        return vlrRecebimento;
    }

    public void setVlrRecebimento(Double vlrRecebimento) {
        this.vlrRecebimento = vlrRecebimento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getDtRecebimento() {
        return dtRecebimento;
    }

    public void setDtRecebimento(String dtRecebimento) {
        String[] vData = Funcoes.getData(dtRecebimento);

        this.dtRecebimento = dtRecebimento;
        this.ano = Integer.valueOf(vData[3]);
        this.mes = Integer.valueOf(vData[2]);
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }

    public int getCalcDtRecebimentoInv() {

        String[] vData = Funcoes.getData(getDtRecebimento());
        return Integer.valueOf(vData[4]);
    }

    private ContentValues getValues() {

        ContentValues values = new ContentValues();

        values.put("id_venda_recebimento", getIdRecebimento());
        values.put("id_venda", getIdVenda());
        values.put("dt_recebimento", getDtRecebimento());
        values.put("vlr_recebimento", getVlrRecebimento());
        values.put("observacoes", getObservacoes());
        values.put("ano", getAno());
        values.put("mes", getMes());

        return values;
    }

    public long insert() {

        setIdRecebimento();

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
            return Funcoes.mDataBase.update(mTable, getValues(), "id_venda_recebimento = ?", new String[]{String.valueOf(getIdRecebimento())});
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete() {

        try {
            return Funcoes.mDataBase.delete(mTable, "id_venda_recebimento = ?", new String[]{String.valueOf(getIdRecebimento())});
        } catch (Exception e) {
            return -1;
        }
    }

    public void getRecord(int id_venda_recebimento) {

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_venda_recebimento = ?", new String[]{String.valueOf(id_venda_recebimento)}, null, null, null);
        while (cursor.moveToNext()) {
            setIdRecebimento(cursor.getInt(cursor.getColumnIndex("id_venda_recebimento")));
            setIdVenda(cursor.getInt(cursor.getColumnIndex("id_venda")));
            setDtRecebimento(cursor.getString(cursor.getColumnIndex("dt_recebimento")));
            setVlrRecebimento(cursor.getDouble(cursor.getColumnIndex("vlr_recebimento")));
            setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));
        }
        cursor.close();
    }

    public List<tblVendaRecebimentos> getList(int id_venda) {

        List<tblVendaRecebimentos> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_venda = ?", new String[]{String.valueOf(id_venda)}, null, null, null);
        while (cursor.moveToNext()) {

            tblVendaRecebimentos record = new tblVendaRecebimentos();

            record.setIdRecebimento(cursor.getInt(cursor.getColumnIndex("id_venda_recebimento")));
            record.setIdVenda(cursor.getInt(cursor.getColumnIndex("id_venda")));
            record.setDtRecebimento(cursor.getString(cursor.getColumnIndex("dt_recebimento")));
            record.setVlrRecebimento(cursor.getDouble(cursor.getColumnIndex("vlr_recebimento")));
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
