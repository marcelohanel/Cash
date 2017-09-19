package mah.com.br.cash.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.Interfaces.InterfaceBackup;
import mah.com.br.cash.R;

/**
 * Created by CGI - Marcelo on 15/05/2015.
 */
public class TaskBackup extends AsyncTask<String, String, File> {

    private Context mContext;
    private ProgressDialog mProgressDialog;
    private InterfaceBackup mInterface;

    public TaskBackup(Context c, InterfaceBackup i) {
        this.mContext = c;
        this.mInterface = i;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setTitle(mContext.getString(R.string.a_005));
        mProgressDialog.setMessage(mContext.getString(R.string.f_011));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected File doInBackground(String... params) {

        try {
            JSONObject jBackup = new JSONObject();

            jBackup.put("DataBase", geraBackupItem("DataBase"));
            jBackup.put("config", geraBackupItem("config"));
            jBackup.put("cliente", geraBackupItem("cliente"));
            jBackup.put("fornecedor", geraBackupItem("fornecedor"));
            jBackup.put("grupo_produto", geraBackupItem("grupo_produto"));
            jBackup.put("produto", geraBackupItem("produto"));
            jBackup.put("compra", geraBackupItem("compra"));
            jBackup.put("compra_itens", geraBackupItem("compra_itens"));
            jBackup.put("compra_pagamentos", geraBackupItem("compra_pagamentos"));
            jBackup.put("venda", geraBackupItem("venda"));
            jBackup.put("venda_itens", geraBackupItem("venda_itens"));
            jBackup.put("venda_recebimentos", geraBackupItem("venda_recebimentos"));
            jBackup.put("recebimento", geraBackupItem("recebimento"));
            jBackup.put("pagamento", geraBackupItem("pagamento"));

            return Funcoes.saveFile(mContext, mContext.getString(R.string.b_001), jBackup.toString());

        } catch (Exception e) {
            return null;
        }
    }

    /*
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
*/

    @Override
    protected void onPostExecute(File s) {
        super.onPostExecute(s);

        mProgressDialog.dismiss();
        mInterface.onFinishBackup(s);
    }

    private JSONArray geraBackupItem(String tabela) {

        JSONArray resultSet = new JSONArray();

        try {
            Cursor cursor;

            if (tabela.matches("DataBase")) {
                JSONObject rowObject = new JSONObject();
                rowObject.put("Version", Funcoes.mDataBase.getVersion());
                resultSet.put(rowObject);
            } else {
                cursor = Funcoes.mDataBase.query(tabela, new String[]{"*"}, null, null, null, null, null);
                while (cursor.moveToNext()) {

                    JSONObject rowObject = new JSONObject();

                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) != null)
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        else
                            rowObject.put(cursor.getColumnName(i), "");

                    }
                    resultSet.put(rowObject);
                }
                cursor.close();
            }

        } catch (Exception ignored) {
        }

        return resultSet;
    }
}
