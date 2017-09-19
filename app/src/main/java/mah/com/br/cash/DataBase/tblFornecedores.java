package mah.com.br.cash.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class tblFornecedores {

    private String mTable = "fornecedor";

    private int idFornecedor = -1;
    private String nome = "";
    private String endereco = "";
    private String fone_1 = "";
    private String fone_2 = "";
    private String observacoes = "";
    private String email = "";
    private String dtCadastro = "";

    public int getIdFornecedor() {
        return this.idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    private void setidFornecedor() {
        SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT MAX(id_fornecedor) FROM " + mTable);
        this.idFornecedor = (int) sStatement.simpleQueryForLong() + 1;
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

    public double getCalcCompras(Context c, String mes, String ano) {

        try {
            Cursor cursor;
            Cursor cursorSum;
            Double vlrAux = 0.0;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {
                cursor = Funcoes.mDataBase.query("compra", new String[]{"id_compra"}, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_unitario * qt_itens) FROM compra_itens WHERE id_compra = ?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex("id_compra")))});
                    if (cursorSum.getCount() > 0) {
                        cursorSum.moveToFirst();
                        vlrAux += cursorSum.getDouble(0);
                    }
                    cursorSum.close();
                }
                cursor.close();
            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {
                cursor = Funcoes.mDataBase.query("compra", new String[]{"id_compra"}, "ano = ?", new String[]{ano}, null, null, null);
                while (cursor.moveToNext()) {
                    cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_unitario * qt_itens) FROM compra_itens WHERE id_compra = ?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex("id_compra")))});
                    if (cursorSum.getCount() > 0) {
                        cursorSum.moveToFirst();
                        vlrAux += cursorSum.getDouble(0);
                    }
                    cursorSum.close();
                }
                cursor.close();
            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {
                cursor = Funcoes.mDataBase.query("compra", new String[]{"id_compra"}, "mes = ?", new String[]{mes}, null, null, null);
                while (cursor.moveToNext()) {
                    cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_unitario * qt_itens) FROM compra_itens WHERE id_compra = ?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex("id_compra")))});
                    if (cursorSum.getCount() > 0) {
                        cursorSum.moveToFirst();
                        vlrAux += cursorSum.getDouble(0);
                    }
                    cursorSum.close();
                }
                cursor.close();
            } else {
                cursor = Funcoes.mDataBase.query("compra", new String[]{"id_compra"}, "mes = ? AND ano = ?", new String[]{mes, ano}, null, null, null);
                while (cursor.moveToNext()) {
                    cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_unitario * qt_itens) FROM compra_itens WHERE id_compra = ?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex("id_compra")))});
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

    public double getCalcPagamentos(Context c, String mes, String ano) {

        try {
            Double vlrAux = 0.0;
            Cursor cursorSum;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_pagamento) FROM compra_pagamentos", null);
                if (cursorSum.getCount() > 0) {
                    cursorSum.moveToFirst();
                    vlrAux = cursorSum.getDouble(0);
                }
                cursorSum.close();
            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_pagamento) FROM compra_pagamentos WHERE ano = ?", new String[]{ano});
                if (cursorSum.getCount() > 0) {
                    cursorSum.moveToFirst();
                    vlrAux = cursorSum.getDouble(0);
                }
                cursorSum.close();
            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_pagamento) FROM compra_pagamentos WHERE mes = ?", new String[]{mes});
                if (cursorSum.getCount() > 0) {
                    cursorSum.moveToFirst();
                    vlrAux = cursorSum.getDouble(0);
                }
                cursorSum.close();
            } else {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_pagamento) FROM compra_pagamentos WHERE mes = ? AND ano = ?", new String[]{mes, ano});
                if (cursorSum.getCount() > 0) {
                    cursorSum.moveToFirst();
                    vlrAux = cursorSum.getDouble(0);
                }
                cursorSum.close();
            }

            return vlrAux;

        } catch (Exception e) {
            return 0.0;
        }
    }

    public double getCalcPago(Context c, String mes, String ano) {

        try {
            Double vlrAux = 0.0;
            Cursor cursorSum;

            if (mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_pagamento) FROM pagamento", null);
                if (cursorSum.getCount() > 0) {
                    cursorSum.moveToFirst();
                    vlrAux = cursorSum.getDouble(0);
                }
                cursorSum.close();
            } else if (mes.matches(c.getString(R.string.t_003)) && !ano.matches(c.getString(R.string.t_003))) {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_pagamento) FROM pagamento WHERE ano = ?", new String[]{ano});
                if (cursorSum.getCount() > 0) {
                    cursorSum.moveToFirst();
                    vlrAux = cursorSum.getDouble(0);
                }
                cursorSum.close();
            } else if (!mes.matches(c.getString(R.string.t_003)) && ano.matches(c.getString(R.string.t_003))) {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_pagamento) FROM pagamento WHERE mes = ?", new String[]{mes});
                if (cursorSum.getCount() > 0) {
                    cursorSum.moveToFirst();
                    vlrAux = cursorSum.getDouble(0);
                }
                cursorSum.close();
            } else {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_pagamento) FROM pagamento WHERE mes = ? AND ano = ?", new String[]{mes, ano});
                if (cursorSum.getCount() > 0) {
                    cursorSum.moveToFirst();
                    vlrAux = cursorSum.getDouble(0);
                }
                cursorSum.close();
            }

            return vlrAux;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public Double getCalcSaldo() {

        try {
            Double vlrPagar = 0.0;
            Double vlrPago = 0.0;
            Cursor cursor;
            Cursor cursorSum;

            cursor = Funcoes.mDataBase.query("compra", new String[]{"*"}, "id_fornecedor = ?", new String[]{String.valueOf(getIdFornecedor())}, null, null, null);
            while (cursor.moveToNext()) {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_pagamento) FROM compra_pagamentos WHERE id_compra = ?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex("id_compra")))});
                if (cursorSum.getCount() > 0) {
                    cursorSum.moveToFirst();
                    vlrPagar += cursorSum.getDouble(0);
                }
                cursorSum.close();
            }
            cursor.close();

            cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_pagamento) FROM pagamento WHERE id_fornecedor = ?", new String[]{String.valueOf(getIdFornecedor())});
            if (cursorSum.getCount() > 0) {
                cursorSum.moveToFirst();
                vlrPago = cursorSum.getDouble(0);
            }
            cursorSum.close();

            return vlrPagar - vlrPago;

        } catch (Exception e) {
            return 0.0;
        }
    }

    private ContentValues getValues() {

        ContentValues values = new ContentValues();

        values.put("id_fornecedor", getIdFornecedor());
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
        setidFornecedor();

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
            return Funcoes.mDataBase.update(mTable, getValues(), "id_fornecedor = ?", new String[]{String.valueOf(getIdFornecedor())});
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete() {
        try {
            return Funcoes.mDataBase.delete(mTable, "id_fornecedor = ?", new String[]{String.valueOf(getIdFornecedor())});
        } catch (Exception e) {
            return -1;
        }
    }

    public void getRecord(int id_fornecedor) {
        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "id_fornecedor = ?", new String[]{String.valueOf(id_fornecedor)}, null, null, null);
        while (cursor.moveToNext()) {
            setIdFornecedor(cursor.getInt(cursor.getColumnIndex("id_fornecedor")));
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

    public List<tblFornecedores> getList() {

        List<tblFornecedores> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, null, null, null, null, null);
        while (cursor.moveToNext()) {

            tblFornecedores tblFornecedor = new tblFornecedores();

            tblFornecedor.setIdFornecedor(cursor.getInt(cursor.getColumnIndex("id_fornecedor")));
            tblFornecedor.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            tblFornecedor.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            tblFornecedor.setFone_1(cursor.getString(cursor.getColumnIndex("fone_1")));
            tblFornecedor.setFone_2(cursor.getString(cursor.getColumnIndex("fone_2")));
            tblFornecedor.setObservacoes(cursor.getString(cursor.getColumnIndex("observacoes")));
            tblFornecedor.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            tblFornecedor.setDtCadastro(cursor.getString(cursor.getColumnIndex("dt_cadastro")));

            mList.add(tblFornecedor);
        }
        cursor.close();

        return mList;
    }
}
