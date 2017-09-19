package mah.com.br.cash.Forms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import mah.com.br.cash.DataBase.tblConfig;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.Diversos.Visoes;
import mah.com.br.cash.Interfaces.InterfaceBackup;
import mah.com.br.cash.R;
import mah.com.br.cash.Tasks.TaskBackup;
import mah.com.br.cash.Tasks.TaskRestore;

public class frmConfiguracoes extends AppCompatActivity implements InterfaceBackup {

    private int mPositionVisao = 0;

    private EditText edtVisao;
    private TextView edtVersao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_configuracoes);

        edtVisao = (EditText) findViewById(R.id.edtVisao);
        edtVisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montaComboVisao();
            }
        });

        edtVersao = (TextView) findViewById(R.id.edtVersao);

        Button btnBackup = (Button) findViewById(R.id.btnBackup);
        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backup();
            }
        });

        Button btnRestore = (Button) findViewById(R.id.btnRestore);
        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restore();
            }
        });

        leitura();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnu_configuracoes, menu);
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
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void backup() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(frmConfiguracoes.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(frmConfiguracoes.this);
        builder.setTitle(R.string.c_008);
        builder.setMessage(R.string.d_026);
        builder.setCancelable(false);
        builder.create();

        builder.setPositiveButton(R.string.c_008, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                TaskBackup taskBackup = new TaskBackup(frmConfiguracoes.this, frmConfiguracoes.this);
                taskBackup.execute();

            }
        });
        builder.setNegativeButton(R.string.c_001, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void restore() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(frmConfiguracoes.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(frmConfiguracoes.this);
        builder.setTitle(R.string.c_008);
        builder.setMessage(R.string.d_027);
        builder.setCancelable(false);
        builder.create();

        builder.setPositiveButton(R.string.c_008, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                TaskRestore taskRestore = new TaskRestore(frmConfiguracoes.this, frmConfiguracoes.this);
                taskRestore.execute();

            }
        });
        builder.setNegativeButton(R.string.c_001, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
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

        edtVisao.setText(new Visoes(frmConfiguracoes.this).getTitulo(mPositionVisao));
        edtVersao.setText(Funcoes.getVersionName(frmConfiguracoes.this));
    }

    private void grava() {

        tblConfig recConfig = new tblConfig();

        recConfig.setChave("visao");
        recConfig.delete();

        recConfig.setChave("visao");
        recConfig.setValor(String.valueOf(mPositionVisao));
        recConfig.insert();
    }

    private void montaComboVisao() {

        List<String> mList = new Visoes(frmConfiguracoes.this).getList();

        final String[] vTitulo = new String[mList.size()];

        for (int i = 0; i <= mList.size() - 1; i++) {
            vTitulo[i] = mList.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(frmConfiguracoes.this);
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

                edtVisao.setText(vTitulo[which]);
                mPositionVisao = which;
                dialog.dismiss();
            }
        });

        builder.show();
    }

    @Override
    public void onFinishBackup(final File s) {

        if (s != null) {
            String sMessage = getString(R.string.b_002);
            sMessage += System.getProperty("line.separator");
            sMessage += System.getProperty("line.separator") + s.getAbsolutePath();
            sMessage += System.getProperty("line.separator");
            sMessage += System.getProperty("line.separator") + getString(R.string.d_022);

            AlertDialog.Builder builder = new AlertDialog.Builder(frmConfiguracoes.this);
            builder.setTitle(R.string.a_004);
            builder.setMessage(sMessage);
            builder.setCancelable(false);
            builder.create();

            builder.setPositiveButton(R.string.c_008, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.b_003));
                    intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.s_013));
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(s));

                    startActivity(Intent.createChooser(intent, getString(R.string.c_006)));

                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(R.string.c_001, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        } else {
            Funcoes.showMessage(
                    frmConfiguracoes.this,
                    getResources().getString(R.string.e_006),
                    getResources().getString(R.string.n_006),
                    getResources().getString(R.string.o_002));
        }
    }

    @Override
    public void onFinishRestore(String s) {
        Funcoes.showMessage(
                frmConfiguracoes.this,
                getResources().getString(R.string.a_004),
                s,
                getResources().getString(R.string.o_002));
    }
}
