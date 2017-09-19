package mah.com.br.cash.Diversos;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import mah.com.br.cash.DataBase.DBHelper;
import mah.com.br.cash.R;

public class Funcoes {

    public static SQLiteDatabase mDataBase;
    public static int SPACE_BETWEEN_ITEMS = 10;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    //public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
    public static DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.00");
    //public static NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

    public static void openDataBase(Context c) {
        DBHelper dataBase = new DBHelper(c);
        mDataBase = dataBase.getWritableDatabase();
    }

    public static void closeDataBase() {
        if (mDataBase != null && mDataBase.isOpen())
            mDataBase.close();
    }

    /*public static String getDtAtual() {

        Calendar newDate = Calendar.getInstance();
        newDate.set(
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        return Funcoes.dateFormat.format(newDate.getTime());
    }
    */

    public static String getDate() {
        return Funcoes.dateFormat.format(Calendar.getInstance().getTime());
    }

    public static String[] getData(String data) {

        String[] vRetorno = new String[9];
        int iDia = 0;
        int iMes = 0;
        int iAno = 0;
        String sAno = "";
        String sMes = "";
        String sDia = "";

        if (data.trim().length() != 0) {
            iDia = Integer.valueOf(data.split("\\/")[0]);
            iMes = Integer.valueOf(data.split("\\/")[1]);
            iAno = Integer.valueOf(data.split("\\/")[2]);

            sDia = data.split("\\/")[0];
            sMes = data.split("\\/")[1];
            sAno = data.split("\\/")[2];
        }

        vRetorno[0] = data;
        vRetorno[1] = String.valueOf(iDia);
        vRetorno[2] = String.valueOf(iMes);
        vRetorno[3] = String.valueOf(iAno);
        vRetorno[4] = sAno + sMes + sDia;
        vRetorno[5] = sMes + "/" + sAno;
        vRetorno[6] = sDia;
        vRetorno[7] = sMes;
        vRetorno[8] = sAno;

        return vRetorno;
    }

    /*
    public static String getDateTime() {
        return Funcoes.dateTimeFormat.format(Calendar.getInstance().getTime());
    }
    */

    public static File saveFile(Context context, String filename, String dados) {

        if (!isExternalStorageWritable())
            Toast.makeText(context, context.getString(R.string.n_001), Toast.LENGTH_LONG).show();

        if (!isExternalStorageReadable()) {
            Toast.makeText(context, context.getString(R.string.n_001), Toast.LENGTH_LONG).show();
        }

        try {
            File root = new File(Environment.getExternalStorageDirectory(), context.getString(context.getApplicationInfo().labelRes));
            if (!root.exists()) {
                root.mkdirs();
            }

            File sFile = new File(root, filename);
            sFile.delete();

            FileWriter writer = new FileWriter(sFile);
            writer.append(dados);
            writer.flush();
            writer.close();

            return sFile;
        } catch (IOException e) {
            Toast.makeText(context, context.getString(R.string.n_001), Toast.LENGTH_LONG).show();
        }

        return null;
    }

    public static String addTable(Context context, String titulo, String tamanho, int colunas) {

        return String.format("<table style='border-color: %s; ", IntToHex(context.getResources().getColor(R.color.primary_text))) +
                String.format("width: %s;' border='1' cellspacing='0' cellpadding='0'>", tamanho) +
                "<tbody>" +
                "<tr>" +
                String.format("<td style='text-align: center; background-color: %s;' ", IntToHex(context.getResources().getColor(R.color.primary))) +
                String.format("colspan='%s'>", String.valueOf(tamanho)) +
                String.format("<span style='color: %s;'>", IntToHex(context.getResources().getColor(R.color.icons))) +
                String.format("<strong>%s</strong></span></td>", titulo) +
                "</tr>";
    }

    public static String IntToHex(int valor) {

        String sAux = Integer.toHexString(valor);
        sAux = sAux.replaceFirst("f", "");
        sAux = sAux.replaceFirst("f", "");
        sAux = "#" + sAux;

        return sAux;
    }

    public static String addTitulo(Context context, String tamanho, String textAlign, String verticalAlign, String titulo) {

        return String.format("<td style='width: %s; text-align: %s; vertical-align: %s; background-color: %s;'><span style='color: %s;'><strong>%s</strong></td>",
                tamanho,
                textAlign,
                verticalAlign,
                IntToHex(context.getResources().getColor(R.color.primary_light)),
                IntToHex(context.getResources().getColor(R.color.primary_text)),
                titulo
        );
    }

    public static String addResumo(Context context, String textAlign, String verticalAlign, String titulo) {

        return String.format("<td style='text-align: %s; vertical-align: %s; background-color: %s;'><span style='color: %s;'><strong>%s</strong></td>",
                textAlign,
                verticalAlign,
                IntToHex(context.getResources().getColor(R.color.primary_light)),
                IntToHex(context.getResources().getColor(R.color.primary_text)),
                titulo
        );
    }

    public static String addCabecalho(String valor) {
        return "<meta charset=\"UTF-8\">" + "<html>" + "<head>" + String.format("<title>%s</title>", valor) + "</head>" + "<body>";
    }

    public static String addRodape() {
        return "</tbody>" + "</table>" + "</body>";
    }

    public static String addDetalhe(Context context, String valor, String align) {
        return String.format("<td style='text-align: %s;'><span style='color: %s;'>%s</td>",
                align,
                IntToHex(context.getResources().getColor(R.color.primary_text)),
                valor
        );
    }

    /*
    public static String formatStr(String texto, int tamanho) {

        String sRetorno = texto;
        int iTamanho = 0;

        if (sRetorno.length() >= tamanho)
            return sRetorno.substring(1, tamanho);

        iTamanho = tamanho - sRetorno.length();

        for (int i = 0; i <= iTamanho - 1; i++) {
            sRetorno += " ";
        }

        return sRetorno;
    }
    */

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static void showMessage(Context context, String title, String message, String textButton) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(textButton,
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

    public static String getVersionName(Context context) {
        try {
            ComponentName comp = new ComponentName(context, context.getClass());
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(comp.getPackageName(), 0);
            return pinfo.versionName;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }

}
