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

import mah.com.br.cash.Comparators.comClientes;
import mah.com.br.cash.DataBase.tblClientes;
import mah.com.br.cash.DataBase.tblRecebimentos;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class frmManutencaoRecebimentos extends AppCompatActivity {

    private String mOperacao;

    private EditText edtCliente;
    private EditText edtDtRecebimento;
    private EditText edtVlrRecebimento;
    private EditText edtObservacoes;

    private int iCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_manutencao_recebimento);

        mOperacao = getIntent().getExtras().getString("Operacao");

        if (mOperacao.matches("I"))
            setTitle(getString(R.string.i_035));

        if (mOperacao.matches("A")) {
            setTitle(getString(R.string.a_014));
        }

        edtCliente = (EditText) findViewById(R.id.edtCliente);
        edtObservacoes = (EditText) findViewById(R.id.edtObservacoes);
        edtVlrRecebimento = (EditText) findViewById(R.id.edtVlrRecebimento);

        edtDtRecebimento = (EditText) findViewById(R.id.edtDtRecebimento);
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(frmManutencaoRecebimentos.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        edtDtRecebimento.setText(Funcoes.dateFormat.format(newDate.getTime()));
                    }
                }, iAno, iMes, iDia);

                datePickerDialog.show();
            }
        });

        edtCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montaComboCliente();
            }
        });

        leitura();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnu_manutencao_recebimento, menu);
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
            edtVlrRecebimento.setText("");
            iCliente = 0;
        }

        if (mOperacao.matches("A")) {

            edtCliente.setText(frmRecebimentos.mAdapter.mListRecebimentos.get(frmRecebimentos.mAdapter.mItemSelected).getCalcNomeCliente());
            edtDtRecebimento.setText(frmRecebimentos.mAdapter.mListRecebimentos.get(frmRecebimentos.mAdapter.mItemSelected).getDtRecebimento());
            edtObservacoes.setText(frmRecebimentos.mAdapter.mListRecebimentos.get(frmRecebimentos.mAdapter.mItemSelected).getObservacoes());
            iCliente = frmRecebimentos.mAdapter.mListRecebimentos.get(frmRecebimentos.mAdapter.mItemSelected).getIdCliente();

            try {
                edtVlrRecebimento.setText(String.valueOf(frmRecebimentos.mAdapter.mListRecebimentos.get(frmRecebimentos.mAdapter.mItemSelected).getVlrRecebimento()));
            } catch (Exception e) {
                edtVlrRecebimento.setText("");
            }
        }
    }

    private void grava() {

        if (!validaTela())
            return;

        tblRecebimentos record = new tblRecebimentos();

        record.setIdCliente(iCliente);
        record.setObservacoes(edtObservacoes.getText().toString());
        record.setDtRecebimento(edtDtRecebimento.getText().toString());

        try {
            record.setVlrRecebimento(Double.parseDouble(edtVlrRecebimento.getText().toString()));
        } catch (Exception e) {
            record.setVlrRecebimento(0.0);
        }

        if (mOperacao.matches("I")) {
            frmRecebimentos.mAdapter.insert(record);
        }

        if (mOperacao.matches("A")) {
            record.setIdRecebimento(frmRecebimentos.mAdapter.mListRecebimentos.get(frmRecebimentos.mAdapter.mItemSelected).getIdRecebimento());
            frmRecebimentos.mAdapter.update(record);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtCliente.getWindowToken(), 0);

        finish();
    }

    private boolean validaTela() {

        if (edtDtRecebimento.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoRecebimentos.this,
                    getString(R.string.a_004),
                    getString(R.string.i_036),
                    getString(R.string.o_002));
            return false;
        }

        if (edtCliente.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoRecebimentos.this,
                    getString(R.string.a_004),
                    getString(R.string.i_034),
                    getString(R.string.o_002));
            return false;
        }

        if (edtVlrRecebimento.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoRecebimentos.this,
                    getString(R.string.a_004),
                    getString(R.string.i_037),
                    getString(R.string.o_002));
            return false;
        }

        return true;
    }

    private void montaComboCliente() {

        int iPosition = 0;

        List<tblClientes> mList = new ArrayList<>();
        tblClientes record = new tblClientes();
        mList = record.getList();

        Collections.sort(mList, new comClientes(0));

        final String[] vNome = new String[mList.size()];
        final int[] vID = new int[mList.size()];

        for (int i = 0; i <= mList.size() - 1; i++) {

            vNome[i] = mList.get(i).getNome();
            vID[i] = mList.get(i).getIdCliente();

            if (iCliente == vID[i])
                iPosition = i;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(frmManutencaoRecebimentos.this);
        builder.setTitle(R.string.c_002);
        builder.setCancelable(false);
        builder.create();
        builder.setPositiveButton(R.string.c_002, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(frmManutencaoRecebimentos.this, frmClientes.class);
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

                edtCliente.setText(vNome[which]);
                iCliente = vID[which];

                dialog.dismiss();
            }
        });

        builder.show();
    }
}
