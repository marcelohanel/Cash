package mah.com.br.cash.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import mah.com.br.cash.Diversos.Funcoes;

public class tblRecebimentos {

    private String mTable = "recebimento";

    private int idRecebimento = -1;
    private int idCliente = -1;
    private String dtRecebimento = "";
    private Double vlrRecebimento = 0.0;
    private String observacoes = "";
    private int dtRecebimentoInv = -1;
    private int mes = -1;
    private int ano = -1;

    public int getIdRecebimento() {
        return idRecebimento;
    }

    public void setIdRecebimento(int idRecebimento) {
        this.idRecebimento = idRecebimento;
    }

    private void setIdRecebimento() {
        SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT MAX(id_recebimento) FROM " + mTable);
        this.idRecebimento = (int) sStatement.simpleQueryForLong() + 1;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public Double getVlrRecebimento() {
        return vlrRecebimento;
    }

    public void setVlrRecebimento(Double vlrRecebimento) {
        this.vlrRecebimento = vlrRecebimento;
    }

    public String getDtRecebimento() {
        return dtRecebimento;
    }

    public void setDtRecebimento(String dtRecebimento) {

        String[] vData = Funcoes.getData(dtRecebimento);

        this.dtRecebimento = dtRecebimento;
        this.ano = Integer.valueOf(vData[3]);
        this.mes = Integer.valueOf(vData[2]);
        this.dtRecebimentoInv = Integer.valueOf(vData[4]);
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public int getDtRecebimentoInv() {
        return dtRecebimentoInv;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }

    public String getCalcNomeCliente() {

        tblClientes record = new tblClientes();

        record.getRecord(getIdCliente());
        String sNome = record.getNome();

        return sNome;
    }

    private ContentValues getValues() {

        ContentValues values = new ContentValues();

        values.put("id_recebimento", getIdRecebimento());
        values.put("id_cliente", getIdCliente());
        values.put("dt_recebimento", getDtRecebimento());
        values.put("vlr_recebimento", getVlrRecebimento());
        values.put("observacoes", getObservacoes());
        values.put("dt_recebimento_inv", getDtRecebimentoInv());
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
            return Funcoes.mDataBase.update(mTable, getValues(), "id_recebimento = ?", new String[]{String.valueOf(getIdRecebimento())});
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete() {

        try {
            return Funcoes.mDataBase.delete(mTable, "id_recebimento = ?", new String[]{String.valueOf(getIdRecebimento())});
        } catch (Exception e) {
            return -1;
        }
    }

    public void getRecord(int id_recebimento) {

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_recebimento = ?", new String[]{String.valueOf(id_recebimento)}, null, null, null);

        while (cursor.moveToNext()) {
            setIdRecebimento(cursor.getInt(cursor.getColumnIndex("id_recebimento")));
            setIdCliente(cursor.getInt(cursor.getColumnIndex("id_cliente")));
            setDtRecebimento(cursor.getString(cursor.getColumnIndex("dt_recebimento")));
            setVlrRecebimento(cursor.getDouble(cursor.getColumnIndex("vlr_recebimento")));
            setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));
        }
        cursor.close();
    }

    public List<tblRecebimentos> getList(int mes, int ano) {

        List<tblRecebimentos> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "mes = ? AND ano = ?", new String[]{String.valueOf(mes), String.valueOf(ano)}, null, null, null);
        while (cursor.moveToNext()) {

            tblRecebimentos record = new tblRecebimentos();

            record.setIdRecebimento(cursor.getInt(cursor.getColumnIndex("id_recebimento")));
            record.setIdCliente(cursor.getInt(cursor.getColumnIndex("id_cliente")));
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
