package mah.com.br.cash.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mah.com.br.cash.BuildConfig;
import mah.com.br.cash.Comparators.comClientes;
import mah.com.br.cash.DataBase.tblClientes;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class adpClientes extends RecyclerView.Adapter<adpClientes.MyViewHolder> implements Filterable {

    public List<tblClientes> mListClientes;
    public int mItemSelected;
    public int mOrdem;

    private List<tblClientes> mListClientesFull;
    private Filter mFilter;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private RecyclerView mRecyclerView;

    public adpClientes(Context c, RecyclerView r) {

        this.mContext = c;

        this.mItemSelected = -1;
        this.mOrdem = 0;
        this.mRecyclerView = r;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListClientes = getDados();
        this.mListClientesFull = getDados();

        order();
    }

    private List<tblClientes> getDados() {

        List<tblClientes> mList;

        tblClientes record = new tblClientes();
        mList = record.getList();

        return mList;
    }

    public void order() {

        comClientes mComparator = new comClientes(mOrdem);

        Collections.sort(mListClientes, mComparator);
        Collections.sort(mListClientesFull, mComparator);

        notifyDataSetChanged();

        setSelected(-1);
    }

    private void setSelected(int position) {

        notifyItemChanged(mItemSelected);
        this.mItemSelected = position;
        notifyItemChanged(mItemSelected);

        mRecyclerView.scrollToPosition(mItemSelected);
    }

    public void insert(tblClientes cliente) {

        int iID;

        if (cliente.insert() == -1) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_003),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        iID = cliente.getIdCliente();

        mListClientes.add(cliente);
        mListClientesFull.add(cliente);

        order();

        setSelectedByID(iID);

        String sMessage;
        sMessage = mContext.getString(R.string.c_004);
        sMessage = sMessage.replace("@1", mListClientes.get(mItemSelected).getNome());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    public void update(tblClientes cliente) {

        int iID = cliente.getIdCliente();

        if (cliente.update() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_004),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListClientesFull.size() - 1; i++) {
            if (mListClientesFull.get(i).getIdCliente() == mListClientes.get(mItemSelected).getIdCliente()) {
                mListClientesFull.remove(i);
                break;
            }
        }

        mListClientes.remove(mItemSelected);

        mListClientes.add(cliente);
        mListClientesFull.add(cliente);

        order();

        setSelectedByID(iID);

        String sMessage;
        sMessage = mContext.getString(R.string.c_003);
        sMessage = sMessage.replace("@1", mListClientes.get(mItemSelected).getNome());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    private void setSelectedByID(int id_cliente) {

        int iPosition = -1;

        for (int i = 0; i <= mListClientes.size() - 1; i++) {
            if (mListClientes.get(i).getIdCliente() == id_cliente) {
                iPosition = i;
                break;
            }
        }

        setSelected(iPosition);
    }

    public void remove() {

        if (mItemSelected == -1)
            return;

        tblClientes record = new tblClientes();
        record.setIdCliente(mListClientes.get(mItemSelected).getIdCliente());

        if (record.delete() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_005),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListClientesFull.size() - 1; i++) {
            if (mListClientesFull.get(i).getIdCliente() == mListClientes.get(mItemSelected).getIdCliente()) {
                mListClientesFull.remove(i);
                break;
            }
        }

        String sMessage;
        sMessage = mContext.getString(R.string.c_005);
        sMessage = sMessage.replace("@1", mListClientes.get(mItemSelected).getNome());

        mListClientes.remove(mItemSelected);
        notifyItemRemoved(mItemSelected);

        setSelected(-1);

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    public void shared(final String tipo) {

        final ProgressDialog pd = ProgressDialog.show(mContext, mContext.getString(R.string.a_005), mContext.getString(R.string.g_006), true);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                    sleep(1000);

                    double dTotal = 0.0;

                    StringBuilder sBuilder = new StringBuilder();

                    sBuilder.append(Funcoes.addCabecalho(mContext.getString(R.string.l_001)));
                    sBuilder.append(Funcoes.addTable(mContext, mContext.getString(R.string.l_001), "800px", 5));

                    sBuilder.append("<tr>");
                    sBuilder.append(Funcoes.addTitulo(mContext, "30%", "center", "middle", mContext.getString(R.string.n_002)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "15%", "center", "middle", mContext.getString(R.string.f_002)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "15%", "center", "middle", mContext.getString(R.string.f_003)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "25%", "center", "middle", mContext.getString(R.string.e_003)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "20%", "center", "middle", mContext.getString(R.string.s_002)));
                    sBuilder.append("</tr>");

                    for (int i = 0; i <= mListClientes.size() - 1; i++) {

                        sBuilder.append("<tr>");

                        sBuilder.append(Funcoes.addDetalhe(mContext, mListClientes.get(i).getNome(), "left"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, mListClientes.get(i).getFone_1(), "center"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, mListClientes.get(i).getFone_2(), "center"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, mListClientes.get(i).getEmail(), "left"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(mListClientes.get(i).getCalcSaldo()), "right"));

                        sBuilder.append("</tr>");

                        dTotal += mListClientes.get(i).getCalcSaldo();
                    }

                    sBuilder.append("<tr>");
                    sBuilder.append(Funcoes.addResumo(mContext, "center", "middle", ""));
                    sBuilder.append(Funcoes.addResumo(mContext, "center", "middle", ""));
                    sBuilder.append(Funcoes.addResumo(mContext, "center", "middle", ""));
                    sBuilder.append(Funcoes.addResumo(mContext, "center", "middle", mContext.getString(R.string._002)));
                    sBuilder.append(Funcoes.addResumo(mContext, "right", "middle", Funcoes.decimalFormat.format(dTotal)));
                    sBuilder.append("</tr>");

                    Funcoes.addRodape();

                    if (tipo.matches("Relatorio")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setDataAndType(FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", Funcoes.saveFile(mContext, mContext.getString(R.string.c_009), sBuilder.toString())), "text/html");
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            intent.setDataAndType(Uri.fromFile(Funcoes.saveFile(mContext, mContext.getString(R.string.c_009), sBuilder.toString())), "text/html");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                        mContext.startActivity(intent);
                    }

                    if (tipo.matches("Compartilhar")) {

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.l_001));
                        intent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.s_003));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", Funcoes.saveFile(mContext, mContext.getString(R.string.c_009), sBuilder.toString())));
                        } else {
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(Funcoes.saveFile(mContext, mContext.getString(R.string.c_009), sBuilder.toString())));
                        }

                        mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.c_006)));
                    }

                } catch (Exception e) {
                }

                pd.dismiss();
            }
        };

        thread.start();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mLayoutInflater.inflate(R.layout.list_clientes, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        myViewHolder.txtID.setText(String.valueOf(mListClientes.get(i).getIdCliente()));
        myViewHolder.txtNome.setText(mListClientes.get(i).getNome());

        String sFone_1 = mListClientes.get(i).getFone_1();
        String sFone_2 = mListClientes.get(i).getFone_2();

        if (sFone_2.trim().length() != 0) {
            if (sFone_1.trim().length() != 0)
                sFone_1 = sFone_1 + " - " + sFone_2;
            else
                sFone_1 = sFone_2;
        }

        myViewHolder.txtFone.setText(mContext.getString(R.string.f_001) + mContext.getString(R.string._001) + " " + sFone_1);
        myViewHolder.txtEndereco.setText(mContext.getString(R.string.e_004) + mContext.getString(R.string._001) + " " + mListClientes.get(i).getEndereco());
        myViewHolder.txtObservacoes.setText(mContext.getString(R.string.o_001) + mContext.getString(R.string._001) + " " + mListClientes.get(i).getObservacoes());
        myViewHolder.txtEmail.setText(mContext.getString(R.string.e_003) + mContext.getString(R.string._001) + " " + mListClientes.get(i).getEmail());
        myViewHolder.txtSaldo.setText(mContext.getString(R.string.s_002) + mContext.getString(R.string._001) + " " + Funcoes.decimalFormat.format(mListClientes.get(i).getCalcSaldo()));

        if (mItemSelected == i)
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.primary_light));
        else
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.icons));
    }

    @Override
    public int getItemCount() {
        return mListClientes.size();
    }

    @Override
    public Filter getFilter() {

        if (mFilter == null)
            mFilter = new DomainFilter();

        setSelected(-1);

        return mFilter;
    }

    private class DomainFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            String constraintString = (constraint + "").toLowerCase();

            if (constraint == null || constraint.length() == 0) {
                List<tblClientes> list = new ArrayList<>(mListClientesFull);
                results.count = list.size();
                results.values = list;
            } else {
                ArrayList<tblClientes> newValues = new ArrayList<>(mListClientesFull.size());

                for (int i = 0; i < mListClientesFull.size(); i++) {
                    if (mListClientesFull.get(i).getNome().toLowerCase().contains(constraintString.toLowerCase())) {
                        newValues.add(mListClientesFull.get(i));
                    }
                }

                results.count = newValues.size();
                results.values = newValues;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.values != null) {
                mListClientes = (ArrayList<tblClientes>) results.values;
            } else {
                mListClientes = new ArrayList<>();
            }

            notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtID;
        public TextView txtNome;
        public TextView txtFone;
        public TextView txtSaldo;
        public TextView txtEndereco;
        public TextView txtObservacoes;
        public TextView txtEmail;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtID = (TextView) itemView.findViewById(R.id.txtID);
            txtNome = (TextView) itemView.findViewById(R.id.txtNome);
            txtFone = (TextView) itemView.findViewById(R.id.txtFone);
            txtSaldo = (TextView) itemView.findViewById(R.id.txtSaldo);
            txtEndereco = (TextView) itemView.findViewById(R.id.txtEndereco);
            txtObservacoes = (TextView) itemView.findViewById(R.id.txtObservacoes);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(getAdapterPosition());
                }
            });
        }
    }
}
