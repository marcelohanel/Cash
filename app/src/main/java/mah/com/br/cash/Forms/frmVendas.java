package mah.com.br.cash.Forms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mah.com.br.cash.Adapters.adpVendas;
import mah.com.br.cash.DataBase.tblVendas;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.Diversos.SpacesItemDecoration;
import mah.com.br.cash.R;

public class frmVendas extends AppCompatActivity {

    public static adpVendas mAdapter;

    private TextView edtMes;
    private TextView edtAno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_vendas);

        edtMes = (TextView) findViewById(R.id.edtMes);
        edtAno = (TextView) findViewById(R.id.edtAno);

        String[] vData = Funcoes.getData(Funcoes.getDate());
        edtMes.setText(vData[7]);
        edtAno.setText(vData[8]);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rvVendas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(frmVendas.this));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(Funcoes.SPACE_BETWEEN_ITEMS));

        mAdapter = new adpVendas(
                frmVendas.this,
                mRecyclerView,
                Integer.valueOf(edtMes.getText().toString()),
                Integer.valueOf(edtAno.getText().toString())
        );

        mRecyclerView.setAdapter(mAdapter);

        ImageButton btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmVendas.this, frmManutencaoVendas.class);
                intent.putExtra("Operacao", "I");
                startActivity(intent);
            }
        });

        edtMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montaComboMes();
            }
        });

        edtAno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montaComboAno();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (mAdapter.mItemSelected != -1)
            mAdapter.notifyItemChanged(mAdapter.mItemSelected);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnu_vendas, menu);

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

        if (id == R.id.mnuOrdem) {
            montaComboOrdem();
        }

        if (id == R.id.mnuListagem) {
            montaComboListagem();
        }

        if (id == R.id.mnuDetalhamento) {
            if (mAdapter.mItemSelected == -1) {
                Funcoes.showMessage(frmVendas.this, getString(R.string.a_004), getString(R.string.e_014), getString(R.string.o_002));
            } else {
                montaComboListagemDetalhe();
            }
        }

        if (id == R.id.mnuItens) {
            if (mAdapter.mItemSelected == -1) {
                Funcoes.showMessage(frmVendas.this,
                        getString(R.string.a_004),
                        getString(R.string.e_014),
                        getString(R.string.o_002));
            } else {
                Intent intent = new Intent(frmVendas.this, frmVendaItens.class);
                intent.putExtra("Venda", mAdapter.mListVendas.get(mAdapter.mItemSelected).getIdVenda());
                startActivity(intent);
            }
        }

        if (id == R.id.mnuRecebimentos) {
            if (mAdapter.mItemSelected == -1) {
                Funcoes.showMessage(frmVendas.this,
                        getString(R.string.a_004),
                        getString(R.string.e_014),
                        getString(R.string.o_002));
            } else {
                Intent intent = new Intent(frmVendas.this, frmVendaRecebimentos.class);
                intent.putExtra("Venda", mAdapter.mListVendas.get(mAdapter.mItemSelected).getIdVenda());
                startActivity(intent);
            }
        }

        if (id == R.id.mnuAlterar) {

            if (mAdapter.mItemSelected == -1) {
                Funcoes.showMessage(frmVendas.this, getString(R.string.a_004), getString(R.string.e_014), getString(R.string.o_002));
            } else {
                Intent intent = new Intent(frmVendas.this, frmManutencaoVendas.class);
                intent.putExtra("Operacao", "A");
                startActivity(intent);
            }
        }

        if (id == R.id.mnuExcluir) {

            if (mAdapter.mItemSelected == -1) {
                Funcoes.showMessage(frmVendas.this, getString(R.string.a_004), getString(R.string.e_014), getString(R.string.o_002));
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(frmVendas.this);

                builder.setTitle(getString(R.string.c_008));

                String sMessage = getString(R.string.d_018);
                sMessage = sMessage.replace("@1", mAdapter.mListVendas.get(mAdapter.mItemSelected).getIdentificacao());

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
                getString(R.string.i_014),
                getString(R.string.c_011),
                getString(R.string.d_016),
                getString(R.string.v_016)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(frmVendas.this);
        builder.setTitle(R.string.o_003);
        builder.setCancelable(false);
        builder.create();

        builder.setNegativeButton(R.string.c_001, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

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

    private void montaComboMes() {

        int iPosition = 0;

        List<String> mList = new ArrayList<>();

        tblVendas record = new tblVendas();
        mList = record.getListMes();

        Collections.sort(mList);

        if (mList.size() == 0)
            return;

        final String[] vNome = new String[mList.size()];

        for (int i = 0; i <= mList.size() - 1; i++) {
            vNome[i] = String.format("%02d", Integer.valueOf(mList.get(i)));

            if (vNome[i].matches(edtMes.getText().toString()))
                iPosition = i;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(frmVendas.this);
        builder.setTitle(R.string.m_004);
        builder.setCancelable(false);
        builder.create();

        builder.setNegativeButton(R.string.c_001, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(vNome, iPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                edtMes.setText(vNome[which]);

                mAdapter.setMesAno(
                        Integer.valueOf(edtMes.getText().toString()),
                        Integer.valueOf(edtAno.getText().toString()));

                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void montaComboAno() {

        int iPosition = 0;

        List<String> mList = new ArrayList<>();

        tblVendas record = new tblVendas();
        mList = record.getListAno();

        Collections.sort(mList);

        if (mList.size() == 0)
            return;

        final String[] vNome = new String[mList.size()];

        for (int i = 0; i <= mList.size() - 1; i++) {
            vNome[i] = String.format("%04d", Integer.valueOf(mList.get(i)));

            if (vNome[i].matches(edtAno.getText().toString()))
                iPosition = i;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(frmVendas.this);
        builder.setTitle(R.string.a_010);
        builder.setCancelable(false);
        builder.create();

        builder.setNegativeButton(R.string.c_001, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(vNome, iPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                edtAno.setText(vNome[which]);

                mAdapter.setMesAno(
                        Integer.valueOf(edtMes.getText().toString()),
                        Integer.valueOf(edtAno.getText().toString()));

                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void montaComboListagem() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(frmVendas.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }
        }

        String[] vOrdem = {
                getString(R.string.r_001),
                getString(R.string.c_006)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(frmVendas.this);
        builder.setTitle(R.string.o_006);
        builder.setCancelable(false);
        builder.create();

        builder.setNegativeButton(R.string.c_001, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(vOrdem, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    mAdapter.shared("Relatorio");
                }

                if (which == 1) {
                    mAdapter.shared("Compartilhar");
                }

                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void montaComboListagemDetalhe() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(frmVendas.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }
        }

        String[] vOrdem = {
                getString(R.string.r_001),
                getString(R.string.c_006)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(frmVendas.this);
        builder.setTitle(R.string.o_006);
        builder.setCancelable(false);
        builder.create();

        builder.setNegativeButton(R.string.c_001, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(vOrdem, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    mAdapter.sharedDetalhe("Relatorio");
                }

                if (which == 1) {
                    mAdapter.sharedDetalhe("Compartilhar");
                }

                dialog.dismiss();
            }
        });

        builder.show();
    }
}
