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

import mah.com.br.cash.DataBase.tblVendaRecebimentos;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class frmManutencaoVendaRecebimentos extends AppCompatActivity {

    private String mOperacao;
    private int iVenda;

    private EditText edtDtRecebimento;
    private EditText edtValor;
    private EditText edtObservacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_manutencao_venda_recebimentos);

        mOperacao = getIntent().getExtras().getString("Operacao");
        iVenda = getIntent().getExtras().getInt("Venda");

        if (mOperacao.matches("I"))
            setTitle(getString(R.string.i_035));

        if (mOperacao.matches("A")) {
            setTitle(getString(R.string.a_014));
        }

        edtDtRecebimento = (EditText) findViewById(R.id.edtRecebimento);
        edtValor = (EditText) findViewById(R.id.edtValor);
        edtObservacoes = (EditText) findViewById(R.id.edtObservacoes);

        edtDtRecebimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int iAno = Calendar.getInstance().get(Calendar.YEAR);
                int iMes = Calendar.getInstance().get(Calendar.MONTH);
                int iDia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                if (edtDtRecebimento.getText().toString().trim().length() != 0) {
                    iDia = Integer.valueOf(edtDtRecebimento.getText().toString().split("\\/")[0]);
                    iMes = Integer.valueOf(edtDtRecebimento.getText().toString().split("\\/")[1]) - 1;
                    iAno = Integer.valueOf(edtDtRecebimento.getText().toString().split("\\/")[2]);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(frmManutencaoVendaRecebimentos.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        edtDtRecebimento.setText(Funcoes.dateFormat.format(newDate.getTime()));
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
        getMenuInflater().inflate(R.menu.mnu_manutencao_venda_recebimentos, menu);
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
            edtDtRecebimento.setText(Funcoes.dateFormat.format(newDate.getTime()));

            edtValor.setText("");
        }

        if (mOperacao.matches("A")) {
            edtDtRecebimento.setText(frmVendaRecebimentos.mAdapter.mListRecebimentos.get(frmVendaRecebimentos.mAdapter.mItemSelected).getDtRecebimento());
            edtObservacoes.setText(frmVendaRecebimentos.mAdapter.mListRecebimentos.get(frmVendaRecebimentos.mAdapter.mItemSelected).getObservacoes());

            try {
                edtValor.setText(String.valueOf(frmVendaRecebimentos.mAdapter.mListRecebimentos.get(frmVendaRecebimentos.mAdapter.mItemSelected).getVlrRecebimento()));
            } catch (Exception e) {
                edtValor.setText("");
            }
        }
    }

    private void grava() {

        if (!validaTela())
            return;

        tblVendaRecebimentos record = new tblVendaRecebimentos();

        record.setIdVenda(iVenda);
        record.setDtRecebimento(edtDtRecebimento.getText().toString());
        record.setObservacoes(edtObservacoes.getText().toString());

        try {
            record.setVlrRecebimento(Double.parseDouble(edtValor.getText().toString()));
        } catch (Exception e) {
            record.setVlrRecebimento(0.0);
        }

        if (mOperacao.matches("I")) {
            frmVendaRecebimentos.mAdapter.insert(record);
        }

        if (mOperacao.matches("A")) {
            record.setIdRecebimento(frmVendaRecebimentos.mAdapter.mListRecebimentos.get(frmVendaRecebimentos.mAdapter.mItemSelected).getIdRecebimento());
            frmVendaRecebimentos.mAdapter.update(record);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtDtRecebimento.getWindowToken(), 0);

        finish();
    }

    private boolean validaTela() {

        if (edtDtRecebimento.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoVendaRecebimentos.this,
                    getString(R.string.a_004),
                    getString(R.string.i_036),
                    getString(R.string.o_002));
            return false;
        }

        if (edtValor.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoVendaRecebimentos.this,
                    getString(R.string.a_004),
                    getString(R.string.i_037),
                    getString(R.string.o_002));
            return false;
        }

        return true;
    }
}
