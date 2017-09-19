package mah.com.br.cash.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import mah.com.br.cash.Diversos.Funcoes;

public class tblVendaItens {

    private String mTable = "venda_itens";

    private int idVendaItem = -1;
    private int idVenda = -1;
    private int idProduto = -1;
    private Double qtItens = 0.0;
    private Double vlrUnitario = 0.0;
    private String observacoes = "";

    public int getIdItem() {
        return idVendaItem;
    }

    public void setIdItem(int idItem) {
        this.idVendaItem = idItem;
    }

    private void setIdItem() {
        SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT MAX(id_venda_item) FROM " + mTable);
        this.idVendaItem = (int) sStatement.simpleQueryForLong() + 1;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public Double getQtItens() {
        return qtItens;
    }

    public void setQtItens(Double qtItens) {
        this.qtItens = qtItens;
    }

    public Double getVlrUnitario() {
        return vlrUnitario;
    }

    public void setVlrUnitario(Double vlrUnitario) {
        this.vlrUnitario = vlrUnitario;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getDtVenda() {

        tblVendas record = new tblVendas();
        record.getRecord(getIdVenda());
        String sAux = record.getDtVenda();

        return sAux;
    }

    public int getDtVendaInv() {

        tblVendas record = new tblVendas();
        record.getRecord(getIdVenda());
        int iAux = record.getDtVendaInv();

        return iAux;
    }

    public String getCalcNomeProduto() {

        tblProdutos record = new tblProdutos();
        record.getRecord(getIdProduto());
        String sAux = record.getNome();

        return sAux;
    }

    public Double getCalcVlrVenda(int id_produto) {

        tblProdutos record = new tblProdutos();
        record.getRecord(id_produto);
        Double dAux = record.getVlrVenda();

        return dAux;
    }


    public Double getCalcVlrTotal() {
        return getVlrUnitario() * getQtItens();
    }

    private ContentValues getValues() {

        ContentValues values = new ContentValues();

        values.put("id_venda_item", getIdItem());
        values.put("id_venda", getIdVenda());
        values.put("id_produto", getIdProduto());
        values.put("qt_itens", getQtItens());
        values.put("vlr_unitario", getVlrUnitario());
        values.put("observacoes", getObservacoes());
        values.put("dt_venda", getDtVenda());
        values.put("dt_venda_inv", getDtVendaInv());

        return values;
    }

    public long insert() {

        setIdItem();

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
            return Funcoes.mDataBase.update(mTable, getValues(), "id_venda_item = ?", new String[]{String.valueOf(getIdItem())});
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete() {

        try {
            return Funcoes.mDataBase.delete(mTable, "id_venda_item = ?", new String[]{String.valueOf(getIdItem())});
        } catch (Exception e) {
            return -1;
        }
    }

    public void getRecord(int id_venda_item) {

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_venda_item = ?", new String[]{String.valueOf(id_venda_item)}, null, null, null);
        while (cursor.moveToNext()) {
            setIdItem(cursor.getInt(cursor.getColumnIndex("id_venda_item")));
            setIdVenda(cursor.getInt(cursor.getColumnIndex("id_venda")));
            setIdProduto(cursor.getInt(cursor.getColumnIndex("id_produto")));
            setQtItens(cursor.getDouble(cursor.getColumnIndex("qt_itens")));
            setVlrUnitario(cursor.getDouble(cursor.getColumnIndex("vlr_unitario")));
            setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));
        }
        cursor.close();
    }

    public List<tblVendaItens> getList(int id_venda) {

        List<tblVendaItens> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_venda = ?", new String[]{String.valueOf(id_venda)}, null, null, null);
        while (cursor.moveToNext()) {

            tblVendaItens record = new tblVendaItens();

            record.setIdItem(cursor.getInt(cursor.getColumnIndex("id_venda_item")));
            record.setIdVenda(cursor.getInt(cursor.getColumnIndex("id_venda")));
            record.setIdProduto(cursor.getInt(cursor.getColumnIndex("id_produto")));
            record.setQtItens(cursor.getDouble(cursor.getColumnIndex("qt_itens")));
            record.setVlrUnitario(cursor.getDouble(cursor.getColumnIndex("vlr_unitario")));
            record.setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));

            mList.add(record);
        }
        cursor.close();

        return mList;
    }
}
