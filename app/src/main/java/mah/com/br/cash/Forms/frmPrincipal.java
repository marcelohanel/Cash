package mah.com.br.cash.Forms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mah.com.br.cash.Adapters.adpDrawerPrincipal;
import mah.com.br.cash.DataBase.tblCompraPagamentos;
import mah.com.br.cash.DataBase.tblCompras;
import mah.com.br.cash.DataBase.tblConfig;
import mah.com.br.cash.DataBase.tblPagamentos;
import mah.com.br.cash.DataBase.tblRecebimentos;
import mah.com.br.cash.DataBase.tblVendaRecebimentos;
import mah.com.br.cash.DataBase.tblVendas;
import mah.com.br.cash.Diversos.DrawerPrincipal;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.Diversos.Visoes;
import mah.com.br.cash.R;

public class frmPrincipal extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private TextView edtMes;
    private TextView edtAno;
    private TextView edtTitulo;
    private int mPositionVisao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_principal);

        Funcoes.openDataBase(frmPrincipal.this);

        edtMes = (TextView) findViewById(R.id.edtMes);
        edtAno = (TextView) findViewById(R.id.edtAno);
        edtTitulo = (TextView) findViewById(R.id.edtTitulo);

        String[] vData = Funcoes.getData(Funcoes.getDate());
        edtMes.setText(vData[7]);
        edtAno.setText(vData[8]);

        List<DrawerPrincipal> mList = new ArrayList<>();
        mList.add(new DrawerPrincipal(getString(R.string.v_005), R.drawable.ic_cart_outline_dark));
        mList.add(new DrawerPrincipal(getString(R.string.c_010), R.drawable.ic_tag_text_outline_dark));
        mList.add(new DrawerPrincipal(getString(R.string.r_003), R.drawable.ic_cash_multiple_dark));
        mList.add(new DrawerPrincipal(getString(R.string.p_008), R.drawable.ic_credit_card_dark));
        mList.add(new DrawerPrincipal(getString(R.string.c_002), R.drawable.ic_account_multiple_dark));
        mList.add(new DrawerPrincipal(getString(R.string.f_005), R.drawable.ic_factory_dark));
        mList.add(new DrawerPrincipal(getString(R.string.p_002), R.drawable.ic_gift_dark));
        mList.add(new DrawerPrincipal(getString(R.string.g_001), R.drawable.ic_stackoverflow_dark));
        mList.add(new DrawerPrincipal(getString(R.string.c_007), R.drawable.ic_settings_dark));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);

        adpDrawerPrincipal adapter = new adpDrawerPrincipal(frmPrincipal.this, mList);
        mDrawerList.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_light);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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

        leitura();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Funcoes.closeDataBase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                mDrawerLayout.openDrawer(Gravity.LEFT);
            else
                mDrawerLayout.closeDrawer(Gravity.LEFT);
        }

        if (id == R.id.mnuVisao) {
            montaComboVisao();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setVisao(int indice) {

        Visoes recVisao = new Visoes(frmPrincipal.this);
        recVisao.setMesAno(edtMes.getText().toString(), edtAno.getText().toString());

        edtTitulo.setText(recVisao.getTitulo(indice));

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, recVisao.getFragment(indice))
                .commit();
    }

    private void leitura() {

        mPositionVisao = 0;

        tblConfig recConfig = new tblConfig();
        recConfig.getRecord("visao");

        try {
            mPositionVisao = Integer.valueOf(recConfig.getValor());
        } catch (Exception e) {
            mPositionVisao = 0;
        }

        setVisao(mPositionVisao);
    }

    private void montaComboVisao() {

        Visoes recVisao = new Visoes(frmPrincipal.this);
        List<String> mList = recVisao.getList();

        final String[] vTitulo = new String[mList.size()];
        for (int i = 0; i <= mList.size() - 1; i++) {
            vTitulo[i] = mList.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(frmPrincipal.this);
        builder.setTitle(R.string.v_020);
        builder.setCancelable(false);
        builder.create();
        builder.setNegativeButton(R.string.c_001, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(vTitulo, mPositionVisao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPositionVisao = which;
                setVisao(which);
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void montaComboMes() {

        int iPosition = 0;

        List<String> mListFull = new ArrayList<>();
        List<String> mList;

        mList = new tblCompras().getListMes();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        mList = new tblVendas().getListMes();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        mList = new tblPagamentos().getListMes();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        mList = new tblRecebimentos().getListMes();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        mList = new tblCompraPagamentos().getListMes();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        mList = new tblVendaRecebimentos().getListMes();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        if (mListFull.size() == 0)
            return;

        mListFull.add(getString(R.string.t_003));

        Collections.sort(mListFull);

        final String[] vNome = new String[mListFull.size()];

        for (int i = 0; i <= mListFull.size() - 1; i++) {

            if (!mListFull.get(i).matches(getString(R.string.t_003))) {
                vNome[i] = String.format("%02d", Integer.valueOf(mListFull.get(i)));
            } else {
                vNome[i] = mListFull.get(i);
            }

            if (vNome[i].matches(edtMes.getText().toString()))
                iPosition = i;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(frmPrincipal.this);
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
                setVisao(mPositionVisao);
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void montaComboAno() {

        int iPosition = 0;

        List<String> mListFull = new ArrayList<>();
        List<String> mList;

        mList = new tblCompras().getListAno();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        mList = new tblVendas().getListAno();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        mList = new tblPagamentos().getListAno();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        mList = new tblRecebimentos().getListAno();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        mList = new tblCompraPagamentos().getListAno();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        mList = new tblVendaRecebimentos().getListAno();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        if (mListFull.size() == 0)
            return;

        mListFull.add(getString(R.string.t_003));

        Collections.sort(mListFull);

        final String[] vNome = new String[mListFull.size()];

        for (int i = 0; i <= mListFull.size() - 1; i++) {

            if (!mListFull.get(i).matches(getString(R.string.t_003))) {
                vNome[i] = String.format("%04d", Integer.valueOf(mListFull.get(i)));
            } else {
                vNome[i] = mListFull.get(i);
            }

            if (vNome[i].matches(edtAno.getText().toString()))
                iPosition = i;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(frmPrincipal.this);
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
                setVisao(mPositionVisao);
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            mDrawerLayout.closeDrawer(Gravity.LEFT);

            switch (position) {
                case 0: // Vendas
                    startActivity(new Intent(frmPrincipal.this, frmVendas.class));
                    break;
                case 1: // Compras
                    startActivity(new Intent(frmPrincipal.this, frmCompras.class));
                    break;
                case 2: // Recebimentos
                    startActivity(new Intent(frmPrincipal.this, frmRecebimentos.class));
                    break;
                case 3: // Pagamentos
                    startActivity(new Intent(frmPrincipal.this, frmPagamentos.class));
                    break;
                case 4: // Clientes
                    startActivity(new Intent(frmPrincipal.this, frmClientes.class));
                    break;
                case 5: // Fornecedores
                    startActivity(new Intent(frmPrincipal.this, frmFornecedores.class));
                    break;
                case 6: // Produtos
                    startActivity(new Intent(frmPrincipal.this, frmProdutos.class));
                    break;
                case 7: // Grupos de Produtos
                    startActivity(new Intent(frmPrincipal.this, frmGruposProduto.class));
                    break;
                case 8: // Config
                    startActivity(new Intent(frmPrincipal.this, frmConfiguracoes.class));
                    break;
                default:
                    Toast.makeText(frmPrincipal.this, getString(R.string.s_009), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

}
