package mah.com.br.cash.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class tblClientes {

    private String mTable = "cliente";

    private int idCliente = -1;
    private String nome = "";
    private String endereco = "";
    private String fone_1 = "";
    private String fone_2 = "";
    private String observacoes = "";
    private String email = "";
    private String dtCadastro = "";

    public int getIdCliente() {
        return this.idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    private void setIdCliente() {
        SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT MAX(id_cliente) FROM " + mTable);
        this.idCliente = (int) sStatement.simpleQueryForLong() + 1;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getFone_1() {
        return fone_1;
    }

    public void setFone_1(String fone_1) {
        this.fone_1 = fone_1;
    }

    public String getFone_2() {
        return fone_2;
    }

    public void setFone_2(String fone_2) {
        this.fone_2 = fone_2;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(String dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public int getCalcDtCadastroInv() {
        String[] vData = Funcoes.getData(getDtCadastro());
        return Integer.valueOf(vData[4]);
    }

    public double getCalcVendas(Context c, String mes, String ano) {

        try {
            Cursor cursor;
            Cursor cursorSum;
            Double vlrAux = 0.0;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {
                cursor = Funcoes.mDataBase.query("venda", new String[]{"id_venda"}, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_unitario * qt_itens) FROM venda_itens WHERE id_venda = ?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex("id_venda")))});
                    if (cursorSum.getCount() > 0) {
                        cursorSum.moveToFirst();
                        vlrAux += cursorSum.getDouble(0);
                    }
                    cursorSum.close();
                }
                cursor.close();
            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {
                cursor = Funcoes.mDataBase.query("venda", new String[]{"id_venda"}, "ano = ?", new String[]{ano}, null, null, null);
                while (cursor.moveToNext()) {
                    cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_unitario * qt_itens) FROM venda_itens WHERE id_venda = ?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex("id_venda")))});
                    if (cursorSum.getCount() > 0) {
                        cursorSum.moveToFirst();
                        vlrAux += cursorSum.getDouble(0);
                    }
                    cursorSum.close();
                }
                cursor.close();
            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {
                cursor = Funcoes.mDataBase.query("venda", new String[]{"id_venda"}, "mes = ?", new String[]{mes}, null, null, null);
                while (cursor.moveToNext()) {
                    cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_unitario * qt_itens) FROM venda_itens WHERE id_venda = ?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex("id_venda")))});
                    if (cursorSum.getCount() > 0) {
                        cursorSum.moveToFirst();
                        vlrAux += cursorSum.getDouble(0);
                    }
                    cursorSum.close();
                }
                cursor.close();
            } else {
                cursor = Funcoes.mDataBase.query("venda", new String[]{"id_venda"}, "mes = ? AND ano = ?", new String[]{mes, ano}, null, null, null);
                while (cursor.moveToNext()) {
                    cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_unitario * qt_itens) FROM venda_itens WHERE id_venda = ?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex("id_venda")))});
                    if (cursorSum.getCount() > 0) {
                        cursorSum.moveToFirst();
                        vlrAux += cursorSum.getDouble(0);
                    }
                    cursorSum.close();
                }
                cursor.close();
            }
            return vlrAux;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public double getCalcRecebimentos(Context c, String mes, String ano) {

        try {
            Double vlrAux;
            Cursor cursor;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {
                cursor = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_recebimento) FROM venda_recebimentos", null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    vlrAux = cursor.getDouble(0);
                } else {
                    vlrAux = 0.0;
                }
                cursor.close();
            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {
                cursor = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_recebimento) FROM venda_recebimentos WHERE ano = ?", new String[]{ano});
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    vlrAux = cursor.getDouble(0);
                } else {
                    vlrAux = 0.0;
                }
                cursor.close();
            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {
                cursor = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_recebimento) FROM venda_recebimentos WHERE mes = ?", new String[]{mes});
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    vlrAux = cursor.getDouble(0);
                } else {
                    vlrAux = 0.0;
                }
                cursor.close();
            } else {
                cursor = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_recebimento) FROM venda_recebimentos WHERE mes = ? AND ano = ?", new String[]{mes, ano});
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    vlrAux = cursor.getDouble(0);
                } else {
                    vlrAux = 0.0;
                }
                cursor.close();
            }

            return vlrAux;

        } catch (Exception e) {
            return 0.0;
        }
    }

    public double getCalcRecebido(Context c, String mes, String ano) {

        try {
            Double vlrAux;
            Cursor cursor;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {
                cursor = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_recebimento) FROM recebimento", null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    vlrAux = cursor.getDouble(0);
                } else {
                    vlrAux = 0.0;
                }
                cursor.close();
            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {
                cursor = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_recebimento) FROM recebimento WHERE ano = ?", new String[]{ano});
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    vlrAux = cursor.getDouble(0);
                } else {
                    vlrAux = 0.0;
                }
                cursor.close();
            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {
                cursor = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_recebimento) FROM recebimento WHERE mes = ?", new String[]{mes});
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    vlrAux = cursor.getDouble(0);
                } else {
                    vlrAux = 0.0;
                }
                cursor.close();
            } else {
                cursor = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_recebimento) FROM recebimento WHERE mes = ? AND ano = ?", new String[]{mes, ano});
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    vlrAux = cursor.getDouble(0);
                } else {
                    vlrAux = 0.0;
                }
                cursor.close();
            }

            return vlrAux;

        } catch (Exception e) {
            return 0.0;
        }
    }

    public Double getCalcSaldo() {

        try {
            Double lAux;
            Double vlrReceber = 0.0;
            Double vlrRecebido;

            Cursor cursor;
            Cursor cursorSum;
            SQLiteStatement sStatement;

            cursor = Funcoes.mDataBase.query("venda", new String[]{"*"}, "id_cliente = ?", new String[]{String.valueOf(getIdCliente())}, null, null, null);
            while (cursor.moveToNext()) {

                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_recebimento) FROM venda_recebimentos WHERE id_venda = ?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex("id_venda")))});
                if (cursorSum.getCount() > 0) {
                    cursorSum.moveToFirst();
                    lAux = cursorSum.getDouble(0);
                } else {
                    lAux = 0.0;
                }
                cursorSum.close();
                vlrReceber += lAux;
            }
            cursor.close();

            cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_recebimento) FROM recebimento WHERE id_cliente = ?", new String[]{String.valueOf(getIdCliente())});
            if (cursorSum.getCount() > 0) {
                cursorSum.moveToFirst();
                lAux = cursorSum.getDouble(0);
            } else {
                lAux = 0.0;
            }
            cursorSum.close();
            vlrRecebido = lAux;

            return vlrReceber - vlrRecebido;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private ContentValues getValues() {

        ContentValues values = new ContentValues();

        values.put("id_cliente", getIdCliente());
        values.put("nome", getNome());
        values.put("endereco", getEndereco());
        values.put("fone_1", getFone_1());
        values.put("fone_2", getFone_2());
        values.put("observacoes", getObservacoes());
        values.put("email", getEmail());
        values.put("dt_cadastro", getDtCadastro());

        return values;
    }

    public long insert() {
        setIdCliente();

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
            return Funcoes.mDataBase.update(mTable, getValues(), "id_cliente = ?", new String[]{String.valueOf(getIdCliente())});
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete() {
        try {
            return Funcoes.mDataBase.delete(mTable, "id_cliente = ?", new String[]{String.valueOf(getIdCliente())});
        } catch (Exception e) {
            return -1;
        }
    }

    public void getRecord(int id_cliente) {

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_cliente = ?", new String[]{String.valueOf(id_cliente)}, null, null, null);
        while (cursor.moveToNext()) {
            setIdCliente(cursor.getInt(cursor.getColumnIndex("id_cliente")));
            setNome(cursor.getString(cursor.getColumnIndex("nome")));
            setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            setFone_1(cursor.getString(cursor.getColumnIndex("fone_1")));
            setFone_2(cursor.getString(cursor.getColumnIndex("fone_2")));
            setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));
            setEmail(cursor.getString(cursor.getColumnIndex("email")));
            setDtCadastro(cursor.getString(cursor.getColumnIndex("dt_cadastro")));
        }
        cursor.close();
    }

    public List<tblClientes> getList() {

        List<tblClientes> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, null, null, null, null, null);
        while (cursor.moveToNext()) {

            tblClientes record = new tblClientes();

            record.setIdCliente(cursor.getInt(cursor.getColumnIndex("id_cliente")));
            record.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            record.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            record.setFone_1(cursor.getString(cursor.getColumnIndex("fone_1")));
            record.setFone_2(cursor.getString(cursor.getColumnIndex("fone_2")));
            record.setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));
            record.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            record.setDtCadastro(cursor.getString(cursor.getColumnIndex("dt_cadastro")));

            mList.add(record);
        }
        cursor.close();

        return mList;
    }

}
