package mah.com.br.cash.Forms;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import mah.com.br.cash.DataBase.tblCompraPagamentos;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class frmManutencaoCompraPagamentos extends AppCompatActivity {

    private String mOperacao;
    private int iCompra;

    private EditText edtDtPagamento;
    private EditText edtValor;
    private EditText edtObservacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_manutencao_compra_pagamentos);

        mOperacao = getIntent().getExtras().getString("Operacao");
        iCompra = getIntent().getExtras().getInt("Compra");

        if (mOperacao.matches("I"))
            setTitle(getString(R.string.i_026));

        if (mOperacao.matches("A")) {
            setTitle(getString(R.string.a_012));
        }

        edtDtPagamento = (EditText) findViewById(R.id.edtPagamento);
        edtValor = (EditText) findViewById(R.id.edtValor);
        edtObservacoes = (EditText) findViewById(R.id.edtObservacoes);

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(frmManutencaoCompraPagamentos.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        edtDtPagamento.setText(Funcoes.dateFormat.format(newDate.getTime()));
                    }
                }, iAno, iMes, iDia);

                datePickerDialog.show();
            }
        });

        leitura();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnu_manutencao_compra_pagamentos, menu);
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

            edtValor.setText("");
        }

        if (mOperacao.matches("A")) {
            edtDtPagamento.setText(frmCompraPagamentos.mAdapter.mListPagamentos.get(frmCompraPagamentos.mAdapter.mItemSelected).getDtPagamento());
            edtObservacoes.setText(frmCompraPagamentos.mAdapter.mListPagamentos.get(frmCompraPagamentos.mAdapter.mItemSelected).getObservacoes());

            try {
                edtValor.setText(String.valueOf(frmCompraPagamentos.mAdapter.mListPagamentos.get(frmCompraPagamentos.mAdapter.mItemSelected).getVlrPagamento()));
            } catch (Exception e) {
                edtValor.setText("");
            }
        }
    }

    private void grava() {

        if (!validaTela())
            return;

        tblCompraPagamentos record = new tblCompraPagamentos();

        record.setIdCompra(iCompra);
        record.setDtPagamento(edtDtPagamento.getText().toString());
        record.setObservacoes(edtObservacoes.getText().toString());

        try {
            record.setVlrPagamento(Double.parseDouble(edtValor.getText().toString()));
        } catch (Exception e) {
            record.setVlrPagamento(0.0);
        }

        if (mOperacao.matches("I")) {
            frmCompraPagamentos.mAdapter.insert(record);
        }

        if (mOperacao.matches("A")) {
            record.setIdPagamento(frmCompraPagamentos.mAdapter.mListPagamentos.get(frmCompraPagamentos.mAdapter.mItemSelected).getIdPagamento());
            frmCompraPagamentos.mAdapter.update(record);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtDtPagamento.getWindowToken(), 0);

        finish();
    }

    private boolean validaTela() {

        if (edtDtPagamento.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoCompraPagamentos.this,
                    getString(R.string.a_004),
                    getString(R.string.i_027),
                    getString(R.string.o_002));
            return false;
        }

        if (edtValor.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoCompraPagamentos.this,
                    getString(R.string.a_004),
                    getString(R.string.i_028),
                    getString(R.string.o_002));
            return false;
        }

        return true;
    }
}
