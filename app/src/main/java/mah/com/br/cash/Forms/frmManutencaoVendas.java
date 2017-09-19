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
import mah.com.br.cash.DataBase.tblVendas;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class frmManutencaoVendas extends AppCompatActivity {

    private String mOperacao;

    private EditText edtIdentificacao;
    private EditText edtCliente;
    private EditText edtObservacoes;
    private EditText edtDtVenda;

    private int iCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_manutencao_venda);

        mOperacao = getIntent().getExtras().getString("Operacao");

        if (mOperacao.matches("I"))
            setTitle(getString(R.string.i_031));

        if (mOperacao.matches("A")) {
            setTitle(getString(R.string.a_013));
        }

        edtIdentificacao = (EditText) findViewById(R.id.edtIdentificacao);
        edtCliente = (EditText) findViewById(R.id.edtCliente);
        edtObservacoes = (EditText) findViewById(R.id.edtObservacoes);

        edtDtVenda = (EditText) findViewById(R.id.edtDtVenda);
        edtDtVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int iAno = Calendar.getInstance().get(Calendar.YEAR);
                int iMes = Calendar.getInstance().get(Calendar.MONTH);
                int iDia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                if (edtDtVenda.getText().toString().trim().length() != 0) {
                    iDia = Integer.valueOf(edtDtVenda.getText().toString().split("\\/")[0]);
                    iMes = Integer.valueOf(edtDtVenda.getText().toString().split("\\/")[1]) - 1;
                    iAno = Integer.valueOf(edtDtVenda.getText().toString().split("\\/")[2]);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(frmManutencaoVendas.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        edtDtVenda.setText(Funcoes.dateFormat.format(newDate.getTime()));
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
        getMenuInflater().inflate(R.menu.mnu_manutencao_venda, menu);
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

            edtDtVenda.setText(Funcoes.dateFormat.format(newDate.getTime()));
            iCliente = 0;
        }

        if (mOperacao.matches("A")) {

            edtIdentificacao.setText(frmVendas.mAdapter.mListVendas.get(frmVendas.mAdapter.mItemSelected).getIdentificacao());
            edtCliente.setText(frmVendas.mAdapter.mListVendas.get(frmVendas.mAdapter.mItemSelected).getCalcNomeCliente());
            edtObservacoes.setText(frmVendas.mAdapter.mListVendas.get(frmVendas.mAdapter.mItemSelected).getObservacoes());
            iCliente = frmVendas.mAdapter.mListVendas.get(frmVendas.mAdapter.mItemSelected).getIdCliente();
            edtDtVenda.setText(frmVendas.mAdapter.mListVendas.get(frmVendas.mAdapter.mItemSelected).getDtVenda());
        }
    }

    private void grava() {

        if (!validaTela())
            return;

        tblVendas record = new tblVendas();

        record.setIdentificacao(edtIdentificacao.getText().toString());
        record.setIdCliente(iCliente);
        record.setObservacoes(edtObservacoes.getText().toString());
        record.setDtVenda(edtDtVenda.getText().toString());

        if (mOperacao.matches("I")) {
            frmVendas.mAdapter.insert(record);
        }

        if (mOperacao.matches("A")) {
            record.setIdVenda(frmVendas.mAdapter.mListVendas.get(frmVendas.mAdapter.mItemSelected).getIdVenda());
            frmVendas.mAdapter.update(record);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtIdentificacao.getWindowToken(), 0);

        finish();
    }

    private boolean validaTela() {

        if (edtIdentificacao.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoVendas.this,
                    getString(R.string.a_004),
                    getString(R.string.i_032),
                    getString(R.string.o_002));
            return false;
        }

        if (edtDtVenda.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoVendas.this,
                    getString(R.string.a_004),
                    getString(R.string.i_033),
                    getString(R.string.o_002));
            return false;
        }

        if (edtCliente.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoVendas.this,
                    getString(R.string.a_004),
                    getString(R.string.i_034),
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

        AlertDialog.Builder builder = new AlertDialog.Builder(frmManutencaoVendas.this);
        builder.setTitle(R.string.c_002);
        builder.setCancelable(false);
        builder.create();
        builder.setPositiveButton(R.string.c_002, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(frmManutencaoVendas.this, frmClientes.class);
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
