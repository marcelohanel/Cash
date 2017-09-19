package mah.com.br.cash.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import mah.com.br.cash.Diversos.Funcoes;

public class tblCompraItens {

    private String mTable = "compra_itens";

    private int idCompraItem = -1;
    private int idCompra = -1;
    private int idProduto = -1;
    private Double qtItens = 0.0;
    private Double vlrUnitario = 0.0;
    private String observacoes = "";

    public int getIdItem() {
        return idCompraItem;
    }

    public void setIdItem(int idItem) {
        this.idCompraItem = idItem;
    }

    private void setIdItem() {
        SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT MAX(id_compra_item) FROM " + mTable);
        this.idCompraItem = (int) sStatement.simpleQueryForLong() + 1;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
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

    public String getDtCompra() {

        tblCompras record = new tblCompras();
        record.getRecord(getIdCompra());
        String sAux = record.getDtCompra();

        return sAux;
    }

    public int getDtCompraInv() {

        tblCompras record = new tblCompras();
        record.getRecord(getIdCompra());
        int iAux = record.getDtCompraInv();

        return iAux;
    }

    public String getCalcNomeProduto() {

        tblProdutos record = new tblProdutos();
        record.getRecord(getIdProduto());
        String sAux = record.getNome();

        return sAux;
    }

    public Double getCalcVlrTotal() {
        return getVlrUnitario() * getQtItens();
    }

    private ContentValues getValues() {

        ContentValues values = new ContentValues();

        values.put("id_compra_item", getIdItem());
        values.put("id_compra", getIdCompra());
        values.put("id_produto", getIdProduto());
        values.put("qt_itens", getQtItens());
        values.put("vlr_unitario", getVlrUnitario());
        values.put("observacoes", getObservacoes());
        values.put("dt_compra", getDtCompra());
        values.put("dt_compra_inv", getDtCompraInv());

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
            return Funcoes.mDataBase.update(mTable, getValues(), "id_compra_item = ?", new String[]{String.valueOf(getIdItem())});
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete() {

        try {
            return Funcoes.mDataBase.delete(mTable, "id_compra_item = ?", new String[]{String.valueOf(getIdItem())});
        } catch (Exception e) {
            return -1;
        }
    }

    public void getRecord(int id_compra_item) {

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_compra_item = ?", new String[]{String.valueOf(id_compra_item)}, null, null, null);
        while (cursor.moveToNext()) {
            setIdItem(cursor.getInt(cursor.getColumnIndex("id_compra_item")));
            setIdCompra(cursor.getInt(cursor.getColumnIndex("id_compra")));
            setIdProduto(cursor.getInt(cursor.getColumnIndex("id_produto")));
            setQtItens(cursor.getDouble(cursor.getColumnIndex("qt_itens")));
            setVlrUnitario(cursor.getDouble(cursor.getColumnIndex("vlr_unitario")));
            setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));
        }
        cursor.close();
    }

    public List<tblCompraItens> getList(int id_compra) {

        List<tblCompraItens> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_compra = ?", new String[]{String.valueOf(id_compra)}, null, null, null);
        while (cursor.moveToNext()) {

            tblCompraItens record = new tblCompraItens();

            record.setIdItem(cursor.getInt(cursor.getColumnIndex("id_compra_item")));
            record.setIdCompra(cursor.getInt(cursor.getColumnIndex("id_compra")));
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
