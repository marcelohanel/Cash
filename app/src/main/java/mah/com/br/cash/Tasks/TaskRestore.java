package mah.com.br.cash.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import mah.com.br.cash.DataBase.tblClientes;
import mah.com.br.cash.DataBase.tblCompraItens;
import mah.com.br.cash.DataBase.tblCompraPagamentos;
import mah.com.br.cash.DataBase.tblCompras;
import mah.com.br.cash.DataBase.tblConfig;
import mah.com.br.cash.DataBase.tblFornecedores;
import mah.com.br.cash.DataBase.tblGruposProduto;
import mah.com.br.cash.DataBase.tblPagamentos;
import mah.com.br.cash.DataBase.tblProdutos;
import mah.com.br.cash.DataBase.tblRecebimentos;
import mah.com.br.cash.DataBase.tblVendaItens;
import mah.com.br.cash.DataBase.tblVendaRecebimentos;
import mah.com.br.cash.DataBase.tblVendas;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.Interfaces.InterfaceBackup;
import mah.com.br.cash.R;

/**
 * Created by CGI - Marcelo on 15/05/2015.
 */
public class TaskRestore extends AsyncTask<String, String, String> {

    private Context mContext;
    private ProgressDialog mProgressDialog;
    private InterfaceBackup mInterface;

    public TaskRestore(Context c, InterfaceBackup i) {
        this.mContext = c;
        this.mInterface = i;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setTitle(mContext.getString(R.string.a_005));
        mProgressDialog.setMessage(mContext.getString(R.string.r_012));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            String sAux;
            String sFile;

            sFile = existeBackup();

            if (sFile.trim().length() == 0) {
                sAux = mContext.getString(R.string.a_024);
                sAux = sAux.replace("@1", sFile);
                return sAux;
            } else {
                if (restore(sFile)) {
                    return mContext.getString(R.string.b_004);
                } else {
                    return mContext.getString(R.string.n_007);
                }
            }

        } catch (Exception e) {
            return mContext.getString(R.string.n_007);
        }
    }

    /*
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
*/

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        mProgressDialog.dismiss();
        mInterface.onFinishRestore(s);
    }

    private Boolean restore(String s) {

        try {
            File file = new File(s);
            //String sAux = "";

            StringBuilder stringBuilder = new StringBuilder();

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String sLine;

            while ((sLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(sLine);
                stringBuilder.append('\n');
            }
            bufferedReader.close();

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray;
            JSONObject jsonObjectData;

            // Testa Versão base de dados
            jsonArray = jsonObject.getJSONArray("DataBase");
            jsonObjectData = jsonArray.getJSONObject(0);
            if (Integer.valueOf(jsonObjectData.getString("Version")) != Funcoes.mDataBase.getVersion())
                return false;

            Funcoes.mDataBase.beginTransaction();

            // Apaga dados antigos
            Funcoes.mDataBase.delete("pagamento", null, null);
            Funcoes.mDataBase.delete("recebimento", null, null);
            Funcoes.mDataBase.delete("venda_recebimentos", null, null);
            Funcoes.mDataBase.delete("venda_itens", null, null);
            Funcoes.mDataBase.delete("venda", null, null);
            Funcoes.mDataBase.delete("compra_pagamentos", null, null);
            Funcoes.mDataBase.delete("compra_itens", null, null);
            Funcoes.mDataBase.delete("compra", null, null);
            Funcoes.mDataBase.delete("produto", null, null);
            Funcoes.mDataBase.delete("grupo_produto", null, null);
            Funcoes.mDataBase.delete("fornecedor", null, null);
            Funcoes.mDataBase.delete("cliente", null, null);
            Funcoes.mDataBase.delete("config", null, null);

            // Tabela config
            jsonArray = jsonObject.getJSONArray("config");
            jsonObjectData = jsonArray.getJSONObject(0);
            tblConfig recConfig = new tblConfig();
            recConfig.setChave(jsonObjectData.getString("chave"));
            recConfig.setValor(jsonObjectData.getString("valor"));
            recConfig.insert();

            // Tabela cliente
            jsonArray = jsonObject.getJSONArray("cliente");
            tblClientes recCliente = new tblClientes();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjectData = jsonArray.getJSONObject(i);
                recCliente.setIdCliente(jsonObjectData.getInt("id_cliente"));
                recCliente.setNome(jsonObjectData.getString("nome"));
                recCliente.setEndereco(jsonObjectData.getString("endereco"));
                recCliente.setFone_1(jsonObjectData.getString("fone_1"));
                recCliente.setFone_2(jsonObjectData.getString("fone_2"));
                recCliente.setObservacoes(jsonObjectData.getString("observacoes"));
                recCliente.setEmail(jsonObjectData.getString("email"));
                recCliente.setDtCadastro(jsonObjectData.getString("dt_cadastro"));
                recCliente.copy();
            }

            // Tabela fornecedor
            jsonArray = jsonObject.getJSONArray("fornecedor");
            tblFornecedores recFornecedor = new tblFornecedores();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjectData = jsonArray.getJSONObject(i);
                recFornecedor.setIdFornecedor(jsonObjectData.getInt("id_fornecedor"));
                recFornecedor.setNome(jsonObjectData.getString("nome"));
                recFornecedor.setEndereco(jsonObjectData.getString("endereco"));
                recFornecedor.setFone_1(jsonObjectData.getString("fone_1"));
                recFornecedor.setFone_2(jsonObjectData.getString("fone_2"));
                recFornecedor.setObservacoes(jsonObjectData.getString("observacoes"));
                recFornecedor.setEmail(jsonObjectData.getString("email"));
                recFornecedor.setDtCadastro(jsonObjectData.getString("dt_cadastro"));
                recFornecedor.copy();
            }

            // Tabela grupo de produto
            jsonArray = jsonObject.getJSONArray("grupo_produto");
            tblGruposProduto recGrupoProduto = new tblGruposProduto();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjectData = jsonArray.getJSONObject(i);
                recGrupoProduto.setIdGrupoProduto(jsonObjectData.getInt("id_grupo_produto"));
                recGrupoProduto.setNome(jsonObjectData.getString("nome"));
                recGrupoProduto.copy();
            }

            // Tabela produtos
            jsonArray = jsonObject.getJSONArray("produto");
            tblProdutos recProduto = new tblProdutos();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjectData = jsonArray.getJSONObject(i);
                recProduto.setIdProduto(jsonObjectData.getInt("id_produto"));
                recProduto.setNome(jsonObjectData.getString("nome"));
                recProduto.setIdGrupoProduto(jsonObjectData.getInt("id_grupo_produto"));
                recProduto.setUnidade(jsonObjectData.getString("unidade"));
                recProduto.setReferencia(jsonObjectData.getString("referencia"));
                recProduto.setVlrVenda(jsonObjectData.getDouble("vlr_venda"));
                recProduto.setDtCadastro(jsonObjectData.getString("dt_cadastro"));
                recProduto.copy();
            }

            // Tabela compra
            jsonArray = jsonObject.getJSONArray("compra");
            tblCompras recCompra = new tblCompras();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjectData = jsonArray.getJSONObject(i);
                recCompra.setIdCompra(jsonObjectData.getInt("id_compra"));
                recCompra.setIdentificacao(jsonObjectData.getString("identificacao"));
                recCompra.setIdFornecedor(jsonObjectData.getInt("id_fornecedor"));
                recCompra.setObservacoes(jsonObjectData.getString("observacoes"));
                recCompra.setDtCompra(jsonObjectData.getString("dt_compra"));
                recCompra.copy();
            }

            // Tabela compra_itens
            jsonArray = jsonObject.getJSONArray("compra_itens");
            tblCompraItens recCompraItens = new tblCompraItens();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjectData = jsonArray.getJSONObject(i);
                recCompraItens.setIdItem(jsonObjectData.getInt("id_compra_item"));
                recCompraItens.setIdCompra(jsonObjectData.getInt("id_compra"));
                recCompraItens.setIdProduto(jsonObjectData.getInt("id_produto"));
                recCompraItens.setQtItens(jsonObjectData.getDouble("qt_itens"));
                recCompraItens.setVlrUnitario(jsonObjectData.getDouble("vlr_unitario"));
                recCompraItens.setObservacoes(jsonObjectData.getString("observacoes"));
                recCompraItens.copy();
            }

            // Tabela compra_pagamentos
            jsonArray = jsonObject.getJSONArray("compra_pagamentos");
            tblCompraPagamentos recCompraPagamentos = new tblCompraPagamentos();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjectData = jsonArray.getJSONObject(i);
                recCompraPagamentos.setIdPagamento(jsonObjectData.getInt("id_compra_pagamento"));
                recCompraPagamentos.setIdCompra(jsonObjectData.getInt("id_compra"));
                recCompraPagamentos.setDtPagamento(jsonObjectData.getString("dt_pagamento"));
                recCompraPagamentos.setVlrPagamento(jsonObjectData.getDouble("vlr_pagamento"));
                recCompraPagamentos.setObservacoes(jsonObjectData.getString("observacoes"));
                recCompraPagamentos.copy();
            }

            // Tabela venda
            jsonArray = jsonObject.getJSONArray("venda");
            tblVendas recVendas = new tblVendas();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjectData = jsonArray.getJSONObject(i);
                recVendas.setIdVenda(jsonObjectData.getInt("id_venda"));
                recVendas.setIdentificacao(jsonObjectData.getString("identificacao"));
                recVendas.setIdCliente(jsonObjectData.getInt("id_cliente"));
                recVendas.setObservacoes(jsonObjectData.getString("observacoes"));
                recVendas.setDtVenda(jsonObjectData.getString("dt_venda"));
                recVendas.copy();
            }

            // Tabela venda itens
            jsonArray = jsonObject.getJSONArray("venda_itens");
            tblVendaItens recVendaItens = new tblVendaItens();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjectData = jsonArray.getJSONObject(i);
                recVendaItens.setIdItem(jsonObjectData.getInt("id_venda_item"));
                recVendaItens.setIdVenda(jsonObjectData.getInt("id_venda"));
                recVendaItens.setIdProduto(jsonObjectData.getInt("id_produto"));
                recVendaItens.setQtItens(jsonObjectData.getDouble("qt_itens"));
                recVendaItens.setVlrUnitario(jsonObjectData.getDouble("vlr_unitario"));
                recVendaItens.setObservacoes(jsonObjectData.getString("observacoes"));
                recVendaItens.copy();
            }

            // Tabela venda recebimentos
            jsonArray = jsonObject.getJSONArray("venda_recebimentos");
            tblVendaRecebimentos recVendaRecebimentos = new tblVendaRecebimentos();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjectData = jsonArray.getJSONObject(i);
                recVendaRecebimentos.setIdRecebimento(jsonObjectData.getInt("id_venda_recebimento"));
                recVendaRecebimentos.setIdVenda(jsonObjectData.getInt("id_venda"));
                recVendaRecebimentos.setDtRecebimento(jsonObjectData.getString("dt_recebimento"));
                recVendaRecebimentos.setVlrRecebimento(jsonObjectData.getDouble("vlr_recebimento"));
                recVendaRecebimentos.setObservacoes(jsonObjectData.getString("observacoes"));
                recVendaRecebimentos.copy();
            }

            // Tabela recebimentos
            jsonArray = jsonObject.getJSONArray("recebimento");
            tblRecebimentos recRecebimentos = new tblRecebimentos();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjectData = jsonArray.getJSONObject(i);
                recRecebimentos.setIdRecebimento(jsonObjectData.getInt("id_recebimento"));
                recRecebimentos.setIdCliente(jsonObjectData.getInt("id_cliente"));
                recRecebimentos.setDtRecebimento(jsonObjectData.getString("dt_recebimento"));
                recRecebimentos.setVlrRecebimento(jsonObjectData.getDouble("vlr_recebimento"));
                recRecebimentos.setObservacoes(jsonObjectData.getString("observacoes"));
                recRecebimentos.copy();
            }

            // Tabela pagamentos
            jsonArray = jsonObject.getJSONArray("pagamento");
            tblPagamentos recPagamentos = new tblPagamentos();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjectData = jsonArray.getJSONObject(i);
                recPagamentos.setIdPagamento(jsonObjectData.getInt("id_pagamento"));
                recPagamentos.setIdFornecedor(jsonObjectData.getInt("id_fornecedor"));
                recPagamentos.setDtPagamento(jsonObjectData.getString("dt_pagamento"));
                recPagamentos.setVlrPagamento(jsonObjectData.getDouble("vlr_pagamento"));
                recPagamentos.setObservacoes(jsonObjectData.getString("observacoes"));
                recPagamentos.copy();
            }

            Funcoes.mDataBase.setTransactionSuccessful();

            return true;

        } catch (Exception e) {
            return false;
        } finally {
            Funcoes.mDataBase.endTransaction();
        }

    }

    private String existeBackup() {

        if (!Funcoes.isExternalStorageReadable()) {
            return "";
        }

        try {
            File sRoot = new File(Environment.getExternalStorageDirectory(), mContext.getString(mContext.getApplicationInfo().labelRes));
            if (!sRoot.exists()) {
                return "";
            }

            File sFile = new File(sRoot, mContext.getString(R.string.b_001));
            if (!sFile.exists()) {
                return "";
            } else {
                return sFile.getAbsolutePath();
            }


        } catch (Exception e) {
            return "";
        }
    }
}
