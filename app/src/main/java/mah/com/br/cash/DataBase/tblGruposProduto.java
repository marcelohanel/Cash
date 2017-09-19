package mah.com.br.cash.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import mah.com.br.cash.Diversos.Funcoes;

public class tblGruposProduto {

    private String mTable = "grupo_produto";

    private int idGrupoProduto = -1;
    private String nome = "";

    public int getIdGrupoProduto() {
        return this.idGrupoProduto;
    }

    public void setIdGrupoProduto(int idGrupoProduto) {
        this.idGrupoProduto = idGrupoProduto;
    }

    private void setIdGrupoProduto() {
        SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT MAX(id_grupo_produto) FROM " + mTable);
        this.idGrupoProduto = (int) sStatement.simpleQueryForLong() + 1;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    private ContentValues getValues() {

        ContentValues values = new ContentValues();

        values.put("id_grupo_produto", getIdGrupoProduto());
        values.put("nome", getNome());

        return values;
    }

    public long insert() {

        setIdGrupoProduto();

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
            return Funcoes.mDataBase.update(mTable, getValues(), "id_grupo_produto = ?", new String[]{String.valueOf(getIdGrupoProduto())});
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete() {
        try {
            return Funcoes.mDataBase.delete(mTable, "id_grupo_produto = ?", new String[]{String.valueOf(getIdGrupoProduto())});
        } catch (Exception e) {
            return -1;
        }
    }

    public void getRecord(int id_grupo_produto) {

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_grupo_produto = ?", new String[]{String.valueOf(id_grupo_produto)}, null, null, null);

        if (cursor.moveToNext()) {
            setIdGrupoProduto(cursor.getInt(cursor.getColumnIndex("id_grupo_produto")));
            setNome(cursor.getString(cursor.getColumnIndex("nome")));
        }
        cursor.close();
    }

    public List<tblGruposProduto> getList() {

        List<tblGruposProduto> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, null, null, null, null, null);
        while (cursor.moveToNext()) {

            tblGruposProduto tblGrupoProduto = new tblGruposProduto();

            tblGrupoProduto.setIdGrupoProduto(cursor.getInt(cursor.getColumnIndex("id_grupo_produto")));
            tblGrupoProduto.setNome(cursor.getString(cursor.getColumnIndex("nome")));

            mList.add(tblGrupoProduto);
        }
        cursor.close();

        return mList;
    }

}
