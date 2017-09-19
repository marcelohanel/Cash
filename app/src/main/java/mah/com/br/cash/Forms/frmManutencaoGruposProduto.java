package mah.com.br.cash.Forms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import mah.com.br.cash.DataBase.tblGruposProduto;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class frmManutencaoGruposProduto extends AppCompatActivity {

    private String mOperacao;
    private EditText edtNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_manutencao_grupos_produto);

        mOperacao = getIntent().getExtras().getString("Operacao");

        if (mOperacao.matches("I"))
            setTitle(getString(R.string.i_002));

        if (mOperacao.matches("A")) {
            setTitle(getString(R.string.a_003));
        }

        edtNome = (EditText) findViewById(R.id.edtNome);

        leitura();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnu_manutencao_grupos_produto, menu);
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

        if (mOperacao.matches("A")) {
            edtNome.setText(frmGruposProduto.mAdapter.mListGruposProduto.get(frmGruposProduto.mAdapter.mItemSelected).getNome());
        }
    }

    private void grava() {

        if (!validaTela())
            return;

        tblGruposProduto record = new tblGruposProduto();

        record.setNome(edtNome.getText().toString());

        if (mOperacao.matches("I")) {
            frmGruposProduto.mAdapter.insert(record);
        }

        if (mOperacao.matches("A")) {
            record.setIdGrupoProduto(frmGruposProduto.mAdapter.mListGruposProduto.get(frmGruposProduto.mAdapter.mItemSelected).getIdGrupoProduto());
            frmGruposProduto.mAdapter.update(record);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtNome.getWindowToken(), 0);

        finish();
    }

    private boolean validaTela() {

        if (edtNome.getText().toString().trim().length() == 0) {
            Funcoes.showMessage(
                    frmManutencaoGruposProduto.this,
                    getString(R.string.a_004),
                    getString(R.string.i_005),
                    getString(R.string.o_002)
            );
            return false;
        }

        return true;
    }
}
