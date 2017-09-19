package mah.com.br.cash.DataBase;

import android.content.ContentValues;
import android.database.Cursor;

import mah.com.br.cash.Diversos.Funcoes;

public class tblConfig {

    private String mTable = "config";

    private String chave = "";
    private String valor = "";

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    private ContentValues getValues() {

        ContentValues values = new ContentValues();

        values.put("chave", getChave());
        values.put("valor", getValor());

        return values;
    }

    public long insert() {
        try {
            return Funcoes.mDataBase.insert(mTable, null, getValues());
        } catch (Exception e) {
            return -1;
        }
    }

    public int update() {
        try {
            return Funcoes.mDataBase.update(mTable, getValues(), "chave = ?", new String[]{String.valueOf(getChave())});
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete() {
        try {
            return Funcoes.mDataBase.delete(mTable, "chave = ?", new String[]{String.valueOf(getChave())});
        } catch (Exception e) {
            return -1;
        }
    }

    public void getRecord(String chave) {

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "chave = ?", new String[]{String.valueOf(chave)}, null, null, null);

        if (cursor.moveToNext()) {
            setChave(cursor.getString(cursor.getColumnIndex("chave")));
            setValor(cursor.getString(cursor.getColumnIndex("valor")));
        }
        cursor.close();
    }
}
