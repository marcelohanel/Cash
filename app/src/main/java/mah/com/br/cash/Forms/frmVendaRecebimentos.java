package mah.com.br.cash.Forms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import mah.com.br.cash.Adapters.adpVendaRecebimentos;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.Diversos.SpacesItemDecoration;
import mah.com.br.cash.R;

public class frmVendaRecebimentos extends AppCompatActivity {

    public static adpVendaRecebimentos mAdapter;
    private int iVenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_venda_recebimentos);

        iVenda = getIntent().getExtras().getInt("Venda");

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rvRecebimentos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(frmVendaRecebimentos.this));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(Funcoes.SPACE_BETWEEN_ITEMS));

        mAdapter = new adpVendaRecebimentos(frmVendaRecebimentos.this, mRecyclerView, iVenda);
        mRecyclerView.setAdapter(mAdapter);

        ImageButton btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(frmVendaRecebimentos.this, frmManutencaoVendaRecebimentos.class);
                intent.putExtra("Operacao", "I");
                intent.putExtra("Venda", iVenda);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnu_venda_recebimentos, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.mnuPesquisar).getActionView();
        searchView.setQueryHint(getString(R.string.p_001));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                invalidateOptionsMenu();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mnuOrdem) {
            montaComboOrdem();
        }

        if (id == R.id.mnuAlterar) {

            if (mAdapter.mItemSelected == -1) {
                Funcoes.showMessage(
                        frmVendaRecebimentos.this,
                        getString(R.string.a_004),
                        getString(R.string.e_013),
                        getString(R.string.o_002)
                );
            } else {
                Intent intent = new Intent(frmVendaRecebimentos.this, frmManutencaoVendaRecebimentos.class);
                intent.putExtra("Operacao", "A");
                intent.putExtra("Venda", iVenda);
                startActivity(intent);
            }
        }

        if (id == R.id.mnuExcluir) {

            if (mAdapter.mItemSelected == -1) {
                Funcoes.showMessage(
                        frmVendaRecebimentos.this,
                        getString(R.string.a_004),
                        getString(R.string.e_013),
                        getString(R.string.o_002));
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(frmVendaRecebimentos.this);

                builder.setTitle(getString(R.string.c_008));

                String sMessage = getString(R.string.d_019);
                sMessage = sMessage.replace("@1", mAdapter.mListRecebimentos.get(mAdapter.mItemSelected).getDtRecebimento());

                builder.setMessage(sMessage);
                builder.setCancelable(false);
                builder.setPositiveButton(getString(R.string.c_008),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.remove();
                                dialog.dismiss();
                            }
                        }
                );
                builder.setNegativeButton(getString(R.string.c_001),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );

                builder.create();
                builder.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void montaComboOrdem() {

        String[] vOrdem = {
                getString(R.string.d_015)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(frmVendaRecebimentos.this);
        builder.setTitle(R.string.o_003);
        builder.setCancelable(false);
        builder.create();

        builder.setSingleChoiceItems(vOrdem, mAdapter.mOrdem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter.mOrdem = which;
                mAdapter.order();
                dialog.dismiss();
            }
        });

        builder.show();
    }


}
