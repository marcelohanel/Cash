package mah.com.br.cash.Forms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mah.com.br.cash.Comparators.comProdutos;
import mah.com.br.cash.DataBase.tblProdutos;
import mah.com.br.cash.DataBase.tblVendaItens;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class frmManutencaoVendaItens extends AppCompatActivity {

    private String mOperacao;
    private int iVenda;

    private EditText edtProduto;
    private EditText edtQtdeItens;
    private EditText edtVlrUnitario;
    private EditText edtObservacoes;

    private int iProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_manutencao_venda_itens);

        mOperacao = getIntent().getExtras().getString("Operacao");
        iVenda = getIntent().getExtras().getInt("Venda");

        if (mOperacao.matches("I"))
            setTitle(getString(R.string.i_022));

        if (mOperacao.matches("A")) {
            setTitle(getString(R.string.a_011));
        }

        edtProduto = (EditText) findViewById(R.id.edtProduto);
        edtQtdeItens = (EditText) findViewById(R.id.edtQtdeItens);
        edtVlrUnitario = (EditText) findViewById(R.id.edtVlrUnitario);
        edtObservacoes = (EditText) findViewById(R.id.edtObservacoes);

        edtProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montaComboProduto();
            }
        });

        leitura();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnu_manutencao_venda_itens, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.mnuSalvar) {
            grava();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void leitura() {

        if (mOperacao.matches("I")) {
            edtQtdeItens.setText("");
            edtVlrUnitario.setText("");
            iProduto = 0;
        }

        if (mOperacao.matches("A")) {
            edtProduto.setText(frmVendaItens.mAdapter.mListItens.get(frmVendaItens.mAdapter.mItemSelected).getCalcNomeProduto());
            edtObservacoes.setText(frmVendaItens.mAdapter.mListItens.get(frmVendaItens.mAdapter.mItemSelected).getObservacoes());
            iProduto = frmVendaItens.mAdapter.mListItens.get(frmVendaItens.mAdapter.mItemSelected).getIdProduto();

            try {
                edtVlrUnitario.setText(String.valueOf(frmVendaItens.mAdapter.mListItens.get(frmVendaItens.mAdapter.mItemSelected).getVlrUnitario()));
            } catch (Exception e) {
                edtVlrUnitario.setText("");
            }

            try {
                edtQtdeItens.setText(String.valueOf(frmVendaItens.mAdapter.mListItens.get(frmVendaItens.mAdapter.mItemSelected).getQtItens()));
            } catch (Exception e) {
                edtQtdeItens.setText("");
            }
        }
    }

    private void grava() {

        if (!validaTela())
            return;

        tblVendaItens record = new tblVendaItens();

        record.setIdVenda(iVenda);
        record.setIdProduto(iProduto);
        record.setObservacoes(edtObservacoes.getText().toString());

        try {
            record.setQtItens(Double.parseDouble(edtQtdeItens.getText().toString()));
        } catch (Exception e) {
            record.setQtItens(0.0);
        }

        try {
            record.setVlrUnitario(Double.parseDouble(edtVlrUnitario.getText().toString()));
        } catch (Exception e) {
            record.setVlrUnitario(0.0);
        }

        if (mOperacao.matches("I")) {
            frmVendaItens.mAdapter.insert(record);
        }

        if (mOperacao.matches("A")) {
            record.setIdItem(frmVendaItens.mAdapter.mListItens.get(frmVendaItens.mAdapter.mItemSelected).getIdItem());
            frmVendaItens.mAdapter.update(record);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtProduto.getWindowToken(), 0);

        finish();
    }

    private boolean validaTela() {

        if (edtProduto.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoVendaItens.this, getString(R.string.a_004), getString(R.string.i_007), getString(R.string.o_002));
            return false;
        }

        if (edtQtdeItens.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoVendaItens.this, getString(R.string.a_004), getString(R.string.i_023), getString(R.string.o_002));
            return false;
        }

        if (edtVlrUnitario.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoVendaItens.this, getString(R.string.a_004), getString(R.string.i_024), getString(R.string.o_002));
            return false;
        }

        return true;
    }

    private void montaComboProduto() {

        int iPosition = 0;

        List<tblProdutos> mList = new ArrayList<>();

        tblProdutos record = new tblProdutos();
        mList = record.getList();

        Collections.sort(mList, new comProdutos(0));

        final String[] vNome = new String[mList.size()];
        final int[] vID = new int[mList.size()];

        for (int i = 0; i <= mList.size() - 1; i++) {

            vNome[i] = mList.get(i).getNome();
            vID[i] = mList.get(i).getIdProduto();

            if (iProduto == vID[i])
                iPosition = i;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(frmManutencaoVendaItens.this);
        builder.setTitle(R.string.p_002);
        builder.setCancelable(false);
        builder.create();
        builder.setPositiveButton(R.string.p_002, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(frmManutencaoVendaItens.this, frmProdutos.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.c_001, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(vNome, iPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                edtProduto.setText(vNome[which]);
                iProduto = vID[which];

                try {
                    tblVendaItens record = new tblVendaItens();
                    edtVlrUnitario.setText(String.valueOf(record.getCalcVlrVenda(iProduto)));
                } catch (Exception e) {
                    edtVlrUnitario.setText("0");
                }

                dialog.dismiss();
            }
        });

        builder.show();
    }
}
