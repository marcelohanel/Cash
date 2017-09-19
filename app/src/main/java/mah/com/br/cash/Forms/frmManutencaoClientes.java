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

import mah.com.br.cash.DataBase.tblClientes;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.Diversos.Mask;
import mah.com.br.cash.R;

public class frmManutencaoClientes extends AppCompatActivity {

    private String mOperacao;

    private EditText edtNome;
    private EditText edtFone_1;
    private EditText edtFone_2;
    private EditText edtEmail;
    private EditText edtEndereco;
    private EditText edtObservacoes;
    private EditText edtDtCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_manutencao_cliente);

        mOperacao = getIntent().getExtras().getString("Operacao");

        if (mOperacao.matches("I"))
            setTitle(getString(R.string.i_001));

        if (mOperacao.matches("A")) {
            setTitle(getString(R.string.a_002));
        }

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtFone_1 = (EditText) findViewById(R.id.edtFone_1);
        edtFone_2 = (EditText) findViewById(R.id.edtFone_2);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);
        edtObservacoes = (EditText) findViewById(R.id.edtObservacoes);

        edtFone_1.addTextChangedListener(Mask.insert("(##) ####-####", edtFone_1));
        edtFone_2.addTextChangedListener(Mask.insert("(##) ####-####", edtFone_2));

        edtDtCadastro = (EditText) findViewById(R.id.edtDtCadastro);
        edtDtCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int iAno = Calendar.getInstance().get(Calendar.YEAR);
                int iMes = Calendar.getInstance().get(Calendar.MONTH);
                int iDia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                if (edtDtCadastro.getText().toString().trim().length() != 0) {
                    iDia = Integer.valueOf(edtDtCadastro.getText().toString().split("\\/")[0]);
                    iMes = Integer.valueOf(edtDtCadastro.getText().toString().split("\\/")[1]) - 1;
                    iAno = Integer.valueOf(edtDtCadastro.getText().toString().split("\\/")[2]);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(frmManutencaoClientes.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        edtDtCadastro.setText(Funcoes.dateFormat.format(newDate.getTime()));
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
        getMenuInflater().inflate(R.menu.mnu_manutencao_cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
            edtDtCadastro.setText(Funcoes.dateFormat.format(newDate.getTime()));
        }

        if (mOperacao.matches("A")) {
            edtNome.setText(frmClientes.mAdapter.mListClientes.get(frmClientes.mAdapter.mItemSelected).getNome());
            edtFone_1.setText(frmClientes.mAdapter.mListClientes.get(frmClientes.mAdapter.mItemSelected).getFone_1());
            edtFone_2.setText(frmClientes.mAdapter.mListClientes.get(frmClientes.mAdapter.mItemSelected).getFone_2());
            edtEndereco.setText(frmClientes.mAdapter.mListClientes.get(frmClientes.mAdapter.mItemSelected).getEndereco());
            edtObservacoes.setText(frmClientes.mAdapter.mListClientes.get(frmClientes.mAdapter.mItemSelected).getObservacoes());
            edtEmail.setText(frmClientes.mAdapter.mListClientes.get(frmClientes.mAdapter.mItemSelected).getEmail());
            edtDtCadastro.setText(frmClientes.mAdapter.mListClientes.get(frmClientes.mAdapter.mItemSelected).getDtCadastro());
        }
    }

    private void grava() {

        if (!validaTela())
            return;

        tblClientes record = new tblClientes();

        record.setNome(edtNome.getText().toString());
        record.setFone_1(edtFone_1.getText().toString());
        record.setFone_2(edtFone_2.getText().toString());
        record.setEmail(edtEmail.getText().toString());
        record.setEndereco(edtEndereco.getText().toString());
        record.setObservacoes(edtObservacoes.getText().toString());
        record.setDtCadastro(edtDtCadastro.getText().toString());

        if (mOperacao.matches("I")) {
            frmClientes.mAdapter.insert(record);
        }

        if (mOperacao.matches("A")) {
            record.setIdCliente(frmClientes.mAdapter.mListClientes.get(frmClientes.mAdapter.mItemSelected).getIdCliente());
            frmClientes.mAdapter.update(record);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtNome.getWindowToken(), 0);

        finish();
    }

    private boolean validaTela() {

        if (edtNome.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoClientes.this, getString(R.string.a_004), getString(R.string.i_004), getString(R.string.o_002));
            return false;
        }

        if (edtDtCadastro.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoClientes.this, getString(R.string.a_004), getString(R.string.i_003), getString(R.string.o_002));
            return false;
        }

        return true;
    }
}
