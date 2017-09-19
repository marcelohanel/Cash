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

import mah.com.br.cash.Comparators.comGruposProduto;
import mah.com.br.cash.DataBase.tblGruposProduto;
import mah.com.br.cash.DataBase.tblProdutos;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class frmManutencaoProdutos extends AppCompatActivity {

    private String mOperacao;

    private EditText edtNome;
    private EditText edtGrupoProduto;
    private EditText edtReferencia;
    private EditText edtVlrVenda;
    private EditText edtDtCadastro;
    private EditText edtUnidade;

    private int iGrupoProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_manutencao_produto);

        mOperacao = getIntent().getExtras().getString("Operacao");

        if (mOperacao.matches("I"))
            setTitle(getString(R.string.i_006));

        if (mOperacao.matches("A")) {
            setTitle(getString(R.string.a_006));
        }

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtGrupoProduto = (EditText) findViewById(R.id.edtGrupo);
        edtReferencia = (EditText) findViewById(R.id.edtReferencia);
        edtUnidade = (EditText) findViewById(R.id.edtUnidade);
        edtVlrVenda = (EditText) findViewById(R.id.edtVlrVenda);

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(frmManutencaoProdutos.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        edtDtCadastro.setText(Funcoes.dateFormat.format(newDate.getTime()));
                    }
                }, iAno, iMes, iDia);

                datePickerDialog.show();
            }
        });

        edtGrupoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montaComboGrupo();
            }
        });

        leitura();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnu_manutencao_produto, menu);
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

            edtDtCadastro.setText(Funcoes.dateFormat.format(newDate.getTime()));
            edtVlrVenda.setText("");

            iGrupoProduto = 0;
        }

        if (mOperacao.matches("A")) {
            edtNome.setText(frmProdutos.mAdapter.mListProdutos.get(frmProdutos.mAdapter.mItemSelected).getNome());
            edtGrupoProduto.setText(frmProdutos.mAdapter.mListProdutos.get(frmProdutos.mAdapter.mItemSelected).getCalcNomeGrupoProduto());
            edtReferencia.setText(frmProdutos.mAdapter.mListProdutos.get(frmProdutos.mAdapter.mItemSelected).getReferencia());
            edtUnidade.setText(frmProdutos.mAdapter.mListProdutos.get(frmProdutos.mAdapter.mItemSelected).getUnidade());
            iGrupoProduto = frmProdutos.mAdapter.mListProdutos.get(frmProdutos.mAdapter.mItemSelected).getIdGrupoProduto();

            try {
                edtVlrVenda.setText(String.valueOf(frmProdutos.mAdapter.mListProdutos.get(frmProdutos.mAdapter.mItemSelected).getVlrVenda()));
            } catch (Exception e) {
                edtVlrVenda.setText("");
            }

            edtDtCadastro.setText(frmProdutos.mAdapter.mListProdutos.get(frmProdutos.mAdapter.mItemSelected).getDtCadastro());
        }
    }

    private void grava() {

        if (!validaTela())
            return;

        tblProdutos record = new tblProdutos();

        record.setNome(edtNome.getText().toString());
        record.setIdGrupoProduto(iGrupoProduto);
        record.setReferencia(edtReferencia.getText().toString());
        record.setUnidade(edtUnidade.getText().toString());

        try {
            record.setVlrVenda(Double.parseDouble(edtVlrVenda.getText().toString()));
        } catch (Exception e) {
            record.setVlrVenda(0.0);
        }

        record.setDtCadastro(edtDtCadastro.getText().toString());

        if (mOperacao.matches("I")) {
            frmProdutos.mAdapter.insert(record);
        }

        if (mOperacao.matches("A")) {
            record.setIdProduto(frmProdutos.mAdapter.mListProdutos.get(frmProdutos.mAdapter.mItemSelected).getIdProduto());
            frmProdutos.mAdapter.update(record);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtNome.getWindowToken(), 0);

        finish();
    }

    private boolean validaTela() {

        if (edtNome.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoProdutos.this, getString(R.string.a_004), getString(R.string.i_007), getString(R.string.o_002));
            return false;
        }

        if (edtDtCadastro.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoProdutos.this, getString(R.string.a_004), getString(R.string.i_003), getString(R.string.o_002));
            return false;
        }

        if (edtGrupoProduto.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoProdutos.this, getString(R.string.a_004), getString(R.string.i_008), getString(R.string.o_002));
            return false;
        }

        if (edtUnidade.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoProdutos.this, getString(R.string.a_004), getString(R.string.i_011), getString(R.string.o_002));
            return false;
        }

        if (edtVlrVenda.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(frmManutencaoProdutos.this, getString(R.string.a_004), getString(R.string.i_009), getString(R.string.o_002));
            return false;
        }

        return true;
    }

    private void montaComboGrupo() {

        int iPosition = 0;

        List<tblGruposProduto> mList = new ArrayList<>();

        tblGruposProduto record = new tblGruposProduto();
        mList = record.getList();

        Collections.sort(mList, new comGruposProduto(0));

        final String[] vNomeGrupo = new String[mList.size()];
        final int[] vID = new int[mList.size()];

        for (int i = 0; i <= mList.size() - 1; i++) {

            vNomeGrupo[i] = mList.get(i).getNome();
            vID[i] = mList.get(i).getIdGrupoProduto();

            if (iGrupoProduto == vID[i])
                iPosition = i;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(frmManutencaoProdutos.this);
        builder.setTitle(R.string.g_001);
        builder.setCancelable(false);
        builder.create();
        builder.setPositiveButton(R.string.g_001, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(frmManutencaoProdutos.this, frmGruposProduto.class);
                startActivity(intent);

            }
        });
        builder.setNegativeButton(R.string.c_001, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(vNomeGrupo, iPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                edtGrupoProduto.setText(vNomeGrupo[which]);
                iGrupoProduto = vID[which];

                dialog.dismiss();
            }
        });

        builder.show();
    }
}
