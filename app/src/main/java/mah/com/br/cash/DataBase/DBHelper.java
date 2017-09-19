package mah.com.br.cash.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Cash.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CONFIG = "CREATE TABLE " +
            "IF NOT EXISTS config (" +
            " chave TEXT PRIMARY KEY, " +
            " valor TEXT " +
            ")";

    private static final String TABLE_CLIENTE = "CREATE TABLE " +
            "IF NOT EXISTS cliente (" +
            " id_cliente INTEGER NOT NULL PRIMARY KEY, " +
            " nome TEXT NOT NULL, " +
            " endereco TEXT, " +
            " fone_1 TEXT, " +
            " fone_2 TEXT, " +
            " observacoes TEXT, " +
            " email TEXT, " +
            " dt_cadastro TEXT NOT NULL " +
            ")";

    private static final String TABLE_FORNECEDOR = "CREATE TABLE " +
            "IF NOT EXISTS fornecedor (" +
            " id_fornecedor INTEGER NOT NULL PRIMARY KEY, " +
            " nome TEXT NOT NULL, " +
            " endereco TEXT, " +
            " fone_1 TEXT, " +
            " fone_2 TEXT, " +
            " observacoes TEXT, " +
            " email TEXT, " +
            " dt_cadastro TEXT NOT NULL " +
            ")";

    private static final String TABLE_GRUPO_PRODUTO = "CREATE TABLE " +
            "IF NOT EXISTS grupo_produto (" +
            " id_grupo_produto INTEGER NOT NULL PRIMARY KEY, " +
            " nome TEXT NOT NULL " +
            ")";

    private static final String TABLE_PRODUTO = "CREATE TABLE " +
            "IF NOT EXISTS produto (" +
            " id_produto INTEGER NOT NULL PRIMARY KEY, " +
            " nome TEXT NOT NULL, " +
            " id_grupo_produto INTEGER NOT NULL, " +
            " unidade TEXT NOT NULL, " +
            " referencia TEXT, " +
            " vlr_venda NUMERIC NOT NULL, " +
            " dt_cadastro TEXT NOT NULL, " +
            " FOREIGN KEY(id_grupo_produto) REFERENCES grupo_produto (id_grupo_produto) ON DELETE RESTRICT ON UPDATE CASCADE " +
            ")";

    private static final String TABLE_COMPRA = "CREATE TABLE " +
            "IF NOT EXISTS compra (" +
            " id_compra INTEGER NOT NULL PRIMARY KEY, " +
            " identificacao TEXT NOT NULL, " +
            " id_fornecedor INTEGER NOT NULL, " +
            " observacoes TEXT, " +
            " dt_compra TEXT NOT NULL, " +
            " dt_compra_inv INTEGER NOT NULL, " +
            " mes INTEGER NOT NULL, " +
            " ano INTEGER NOT NULL, " +
            " FOREIGN KEY(id_fornecedor) REFERENCES fornecedor (id_fornecedor) ON DELETE RESTRICT ON UPDATE CASCADE " +
            ")";

    private static final String TABLE_COMPRA_ITENS = "CREATE TABLE " +
            "IF NOT EXISTS compra_itens (" +
            " id_compra_item INTEGER NOT NULL PRIMARY KEY, " +
            " id_compra INTEGER NOT NULL, " +
            " id_produto INTEGER NOT NULL, " +
            " qt_itens NUMERIC NOT NULL, " +
            " vlr_unitario NUMERIC NOT NULL, " +
            " observacoes TEXT, " +
            " dt_compra TEXT NOT NULL, " +
            " dt_compra_inv INTEGER NOT NULL, " +
            " FOREIGN KEY(id_compra) REFERENCES compra (id_compra) ON DELETE CASCADE ON UPDATE CASCADE, " +
            " FOREIGN KEY(id_produto) REFERENCES produto (id_produto) ON DELETE RESTRICT ON UPDATE CASCADE " +
            ")";

    private static final String TABLE_COMPRA_PAGAMENTOS = "CREATE TABLE " +
            "IF NOT EXISTS compra_pagamentos (" +
            " id_compra_pagamento INTEGER NOT NULL PRIMARY KEY, " +
            " id_compra INTEGER NOT NULL, " +
            " dt_pagamento TEXT NOT NULL, " +
            " vlr_pagamento NUMERIC NOT NULL, " +
            " observacoes TEXT, " +
            " mes INTEGER NOT NULL, " +
            " ano INTEGER NOT NULL, " +
            " FOREIGN KEY(id_compra) REFERENCES compra (id_compra) ON DELETE CASCADE ON UPDATE CASCADE " +
            ")";

    private static final String TABLE_VENDA = "CREATE TABLE " +
            "IF NOT EXISTS venda (" +
            " id_venda INTEGER NOT NULL PRIMARY KEY, " +
            " identificacao TEXT NOT NULL, " +
            " id_cliente INTEGER NOT NULL, " +
            " observacoes TEXT, " +
            " dt_venda TEXT NOT NULL, " +
            " dt_venda_inv INTEGER NOT NULL, " +
            " mes INTEGER NOT NULL, " +
            " ano INTEGER NOT NULL, " +
            " FOREIGN KEY(id_cliente) REFERENCES cliente (id_cliente) ON DELETE RESTRICT ON UPDATE CASCADE " +
            ")";

    private static final String TABLE_VENDA_ITENS = "CREATE TABLE " +
            "IF NOT EXISTS venda_itens (" +
            " id_venda_item INTEGER NOT NULL PRIMARY KEY, " +
            " id_venda INTEGER NOT NULL, " +
            " id_produto INTEGER NOT NULL, " +
            " qt_itens NUMERIC NOT NULL, " +
            " vlr_unitario NUMERIC NOT NULL, " +
            " observacoes TEXT, " +
            " dt_venda TEXT NOT NULL, " +
            " dt_venda_inv INTEGER NOT NULL, " +
            " FOREIGN KEY(id_venda) REFERENCES venda (id_venda) ON DELETE CASCADE ON UPDATE CASCADE, " +
            " FOREIGN KEY(id_produto) REFERENCES produto (id_produto) ON DELETE RESTRICT ON UPDATE CASCADE " +
            ")";

    private static final String TABLE_VENDA_RECEBIMENTOS = "CREATE TABLE " +
            "IF NOT EXISTS venda_recebimentos (" +
            " id_venda_recebimento INTEGER NOT NULL PRIMARY KEY, " +
            " id_venda INTEGER NOT NULL, " +
            " dt_recebimento TEXT NOT NULL, " +
            " vlr_recebimento NUMERIC NOT NULL, " +
            " observacoes TEXT, " +
            " mes INTEGER NOT NULL, " +
            " ano INTEGER NOT NULL, " +
            " FOREIGN KEY(id_venda) REFERENCES venda (id_venda) ON DELETE CASCADE ON UPDATE CASCADE " +
            ")";

    private static final String TABLE_RECEBIMENTO = "CREATE TABLE " +
            "IF NOT EXISTS recebimento (" +
            " id_recebimento INTEGER NOT NULL PRIMARY KEY, " +
            " id_cliente INTEGER NOT NULL, " +
            " dt_recebimento TEXT NOT NULL, " +
            " vlr_recebimento NUMERIC NOT NULL, " +
            " observacoes TEXT, " +
            " dt_recebimento_inv INTEGER NOT NULL, " +
            " mes INTEGER NOT NULL, " +
            " ano INTEGER NOT NULL, " +
            " FOREIGN KEY(id_cliente) REFERENCES cliente (id_cliente) ON DELETE RESTRICT ON UPDATE CASCADE " +
            ")";

    private static final String TABLE_PAGAMENTO = "CREATE TABLE " +
            "IF NOT EXISTS pagamento (" +
            " id_pagamento INTEGER NOT NULL PRIMARY KEY, " +
            " id_fornecedor INTEGER NOT NULL, " +
            " dt_pagamento TEXT NOT NULL, " +
            " vlr_pagamento NUMERIC NOT NULL, " +
            " observacoes TEXT, " +
            " dt_pagamento_inv INTEGER NOT NULL, " +
            " mes INTEGER NOT NULL, " +
            " ano INTEGER NOT NULL, " +
            " FOREIGN KEY(id_fornecedor) REFERENCES fornecedor (id_fornecedor) ON DELETE RESTRICT ON UPDATE CASCADE " +
            ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CONFIG);
        db.execSQL(TABLE_CLIENTE);
        db.execSQL(TABLE_FORNECEDOR);
        db.execSQL(TABLE_GRUPO_PRODUTO);
        db.execSQL(TABLE_PRODUTO);
        db.execSQL(TABLE_COMPRA);
        db.execSQL(TABLE_COMPRA_ITENS);
        db.execSQL(TABLE_COMPRA_PAGAMENTOS);
        db.execSQL(TABLE_VENDA);
        db.execSQL(TABLE_VENDA_ITENS);
        db.execSQL(TABLE_VENDA_RECEBIMENTOS);
        db.execSQL(TABLE_RECEBIMENTO);
        db.execSQL(TABLE_PAGAMENTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
/*
        if (oldVersion < 2){
            db.execSQL(TABLE_QUESTIONARIO_1);
        }
*/
    }
}
