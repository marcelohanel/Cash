package mah.com.br.cash.Forms;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import mah.com.br.cash.Comparators.comFornecedores;
import mah.com.br.cash.DataBase.tblFornecedores;
import mah.com.br.cash.DataBase.tblPagamentos;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class frmManutencaoPagamentos extends AppCompatActivity {

    private String mOperacao;

    private EditText edtFornecedor;
    private EditText edtDtPagamento;
    private EditText edtVlrPagamento;
    private EditText edtObservacoes;

    private int iFornecedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_manutencao_pagamento);

        mOperacao = getIntent().getExtras().getString("Operacao");

        if (mOperacao.matches("I"))
            setTitle(getString(R.string.i_038));

        if (mOperacao.matches("A")) {
            setTitle(getString(R.string.a_015));
        }

        edtFornecedor = (EditText) findViewById(R.id.edtFornecedor);
        edtObservacoes = (EditText) findViewById(R.id.edtObservacoes);
        edtVlrPagamento = (EditText) findViewById(R.id.edtVlrPagamento);

        edtDtPagamento = (EditText) findViewById(R.id.edtDtPagamento);
        edtDtPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int iAno = Calendar.getInstance().get(Calendar.YEAR);
                int iMes = Calendar.getInstance().get(Calendar.MONTH);
                int iDia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                if (edtDtPagamento.getText().toString().trim().length() != 0) {
                    iDia = Integer.valueOf(edtDtPagamento.getText().toString().split("\\/")[0]);
                    iMes = Integer.valueOf(edtDtPagamento.getText().toString().split("\\/")[1]) - 1;
                    iAno = Integer.valueOf(edtDtPagamento.getText().toString().split("\\/")[2]);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(frmManutencaoPagamentos.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        edtDtPagamento.setText(Funcoes.dateFormat.format(newDate.getTime()));
                    }
                }, iAno, iMes, iDia);

                datePickerDialog.show();
            }
        });

        edtFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montaComboFornecedor();
            }
        });

        leitura();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnu_manutencao_pagamento, menu);
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

            Calendar newDate = Calendar.getInstance();
            newDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

            edtDtPagamento.setText(Funcoes.dateFormat.format(newDate.getTime()));
            edtVlrPagamento.setText("");
            iFornecedor = 0;
        }

        if (mOperacao.matches("A")) {

            edtFornecedor.setText(frmPagamentos.mAdapter.mListPagamentos.get(frmPagamentos.mAdapter.mItemSelected).getCalcNomeFornecedor());
            edtDtPagamento.setText(frmPagamentos.mAdapter.mListPagamentos.get(frmPagamentos.mAdapter.mItemSelected).getDtPagamento());
            edtObservacoes.setText(frmPagamentos.mAdapter.mListPagamentos.get(frmPagamentos.mAdapter.mItemSelected).getObservacoes());
            iFornecedor = frmPagamentos.mAdapter.mListPagamentos.get(frmPagamentos.mAdapter.mItemSelected).getIdFornecedor();

            try {
                edtVlrPagamento.setText(String.valueOf(frmPagamentos.mAdapter.mListPagamentos.get(frmPagamentos.mAdapter.mItemSelected).getVlrPagamento()));
            } catch (Exception e) {
                edtVlrPagamento.setText("");
            }
        }
    }

    private void grava() {

        if (!validaTela())
            return;

        tblPagamentos record = new tblPagamentos();

        record.setIdFornecedor(iFornecedor);
        record.setObservacoes(edtObservacoes.getText().toString());
        record.setDtPagamento(edtDtPagamento.getText().toString());

        try {
            record.setVlrPagamento(Double.parseDouble(edtVlrPagamento.getText().toString()));
        } catch (Exception e) {
            record.setVlrPagamento(0.0);
        }

        if (mOperacao.matches("I")) {
            frmPagamentos.mAdapter.insert(record);
        }

        if (mOperacao.matches("A")) {
            record.setIdPagamento(frmPagamentos.mAdapter.mListPagamentos.get(frmPagamentos.mAdapter.mItemSelected).getIdPagamento());
            frmPagamentos.mAdapter.update(record);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtFornecedor.getWindowToken(), 0);

        finish();
    }

    private boolean validaTela() {

        if (edtDtPagamento.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoPagamentos.this,
                    getString(R.string.a_004),
                    getString(R.string.i_027),
                    getString(R.string.o_002));
            return false;
        }

        if (edtFornecedor.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoPagamentos.this,
                    getString(R.string.a_004),
                    getString(R.string.i_018),
                    getString(R.string.o_002));
            return false;
        }

        if (edtVlrPagamento.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoPagamentos.this,
                    getString(R.string.a_004),
                    getString(R.string.i_028),
                    getString(R.string.o_002));
            return false;
        }

        return true;
    }

    private void montaComboFornecedor() {

        int iPosition = 0;

        List<tblFornecedores> mList = new ArrayList<>();
        tblFornecedores record = new tblFornecedores();
        mList = record.getList();

        Collections.sort(mList, new comFornecedores(0));

        final String[] vNome = new String[mList.size()];
        final int[] vID = new int[mList.size()];

        for (int i = 0; i <= mList.size() - 1; i++) {

            vNome[i] = mList.get(i).getNome();
            vID[i] = mList.get(i).getIdFornecedor();

            if (iFornecedor == vID[i])
                iPosition = i;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(frmManutencaoPagamentos.this);
        builder.setTitle(R.string.f_005);
        builder.setCancelable(false);
        builder.create();
        builder.setPositiveButton(R.string.c_002, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(frmManutencaoPagamentos.this, frmFornecedores.class);
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

                edtFornecedor.setText(vNome[which]);
                iFornecedor = vID[which];

                dialog.dismiss();
            }
        });

        builder.show();
    }
}
