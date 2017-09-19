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
import mah.com.br.cash.Comparators.comGruposProduto;
import mah.com.br.cash.DataBase.tblGruposProduto;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class adpGruposProduto extends RecyclerView.Adapter<adpGruposProduto.MyViewHolder> implements Filterable {

    public List<tblGruposProduto> mListGruposProduto;
    public int mItemSelected;
    public int mOrdem;

    private List<tblGruposProduto> mListGruposProdutoFull;
    private Filter mFilter;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private RecyclerView mRecyclerView;

    public adpGruposProduto(Context c, RecyclerView r) {

        this.mContext = c;

        this.mItemSelected = -1;
        this.mOrdem = 0;
        this.mRecyclerView = r;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListGruposProduto = getDados();
        this.mListGruposProdutoFull = getDados();

        order();
    }

    private List<tblGruposProduto> getDados() {

        List<tblGruposProduto> mList;

        tblGruposProduto record = new tblGruposProduto();
        mList = record.getList();

        return mList;
    }

    public void order() {

        comGruposProduto mComparator = new comGruposProduto(mOrdem);

        Collections.sort(mListGruposProduto, mComparator);
        Collections.sort(mListGruposProdutoFull, mComparator);

        notifyDataSetChanged();

        setSelected(-1);
    }

    private void setSelected(int position) {

        notifyItemChanged(mItemSelected);
        this.mItemSelected = position;
        notifyItemChanged(mItemSelected);

        mRecyclerView.scrollToPosition(mItemSelected);
    }

    public void insert(tblGruposProduto grupo) {

        int iID;

        if (grupo.insert() == -1) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_003),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        iID = grupo.getIdGrupoProduto();

        mListGruposProduto.add(grupo);
        mListGruposProdutoFull.add(grupo);

        order();

        setSelectedByID(iID);

        String sMessage = mContext.getString(R.string.g_003);
        sMessage = sMessage.replace("@1", mListGruposProduto.get(mItemSelected).getNome());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    public void update(tblGruposProduto grupo) {

        int iID = grupo.getIdGrupoProduto();

        if (grupo.update() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_004),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListGruposProdutoFull.size() - 1; i++) {
            if (mListGruposProdutoFull.get(i).getIdGrupoProduto() == mListGruposProduto.get(mItemSelected).getIdGrupoProduto()) {
                mListGruposProdutoFull.remove(i);
                break;
            }
        }

        mListGruposProduto.remove(mItemSelected);

        mListGruposProduto.add(grupo);
        mListGruposProdutoFull.add(grupo);

        order();

        setSelectedByID(iID);

        String sMessage = mContext.getString(R.string.g_002);
        sMessage = sMessage.replace("@1", mListGruposProduto.get(mItemSelected).getNome());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    private void setSelectedByID(int id_grupo) {

        int iPosition = -1;

        for (int i = 0; i <= mListGruposProduto.size() - 1; i++) {
            if (mListGruposProduto.get(i).getIdGrupoProduto() == id_grupo) {
                iPosition = i;
                break;
            }
        }

        setSelected(iPosition);
    }

    public void remove() {

        if (mItemSelected == -1)
            return;

        tblGruposProduto record = new tblGruposProduto();
        record.setIdGrupoProduto(mListGruposProduto.get(mItemSelected).getIdGrupoProduto());

        if (record.delete() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_005),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListGruposProdutoFull.size() - 1; i++) {
            if (mListGruposProdutoFull.get(i).getIdGrupoProduto() == mListGruposProduto.get(mItemSelected).getIdGrupoProduto()) {
                mListGruposProdutoFull.remove(i);
                break;
            }
        }

        String sMessage = mContext.getString(R.string.g_004);
        sMessage = sMessage.replace("@1", mListGruposProduto.get(mItemSelected).getNome());

        mListGruposProduto.remove(mItemSelected);
        notifyItemRemoved(mItemSelected);

        setSelected(-1);

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    public void shared(final String tipo) {

        final ProgressDialog pd;
        pd = ProgressDialog.show(mContext, mContext.getString(R.string.a_005), mContext.getString(R.string.g_006), true);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);

                    StringBuilder sBuilder = new StringBuilder();

                    sBuilder.append(Funcoes.addCabecalho(mContext.getString(R.string.l_002)));
                    sBuilder.append(Funcoes.addTable(mContext, mContext.getString(R.string.l_002), "300px", 1));

                    sBuilder.append("<tr>");
                    sBuilder.append(Funcoes.addTitulo(mContext, "100%", "center", "middle", mContext.getString(R.string.n_002)));
                    sBuilder.append("</tr>");

                    for (int i = 0; i <= mListGruposProduto.size() - 1; i++) {
                        sBuilder.append("<tr>");
                        sBuilder.append(Funcoes.addDetalhe(mContext, mListGruposProduto.get(i).getNome(), "left"));
                        sBuilder.append("</tr>");
                    }

                    Funcoes.addRodape();

                    if (tipo.matches("Relatorio")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setDataAndType(FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", Funcoes.saveFile(mContext, mContext.getString(R.string.g_005), sBuilder.toString())), "text/html");
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            intent.setDataAndType(Uri.fromFile(Funcoes.saveFile(mContext, mContext.getString(R.string.g_005), sBuilder.toString())), "text/html");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                        mContext.startActivity(intent);
                    }

                    if (tipo.matches("Compartilhar")) {

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.l_002));
                        intent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.s_004));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", Funcoes.saveFile(mContext, mContext.getString(R.string.g_005), sBuilder.toString())));
                        } else {
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(Funcoes.saveFile(mContext, mContext.getString(R.string.g_005), sBuilder.toString())));
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
        View v = mLayoutInflater.inflate(R.layout.list_grupos, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        myViewHolder.txtID.setText(String.valueOf(mListGruposProduto.get(i).getIdGrupoProduto()));
        myViewHolder.txtNome.setText(mListGruposProduto.get(i).getNome());

        if (mItemSelected == i)
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.primary_light));
        else
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.icons));
    }

    @Override
    public int getItemCount() {
        return mListGruposProduto.size();
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
                List<tblGruposProduto> list = new ArrayList<>(mListGruposProdutoFull);
                results.count = list.size();
                results.values = list;
            } else {
                ArrayList<tblGruposProduto> newValues = new ArrayList<>(mListGruposProdutoFull.size());

                for (int i = 0; i < mListGruposProdutoFull.size(); i++) {
                    if (mListGruposProdutoFull.get(i).getNome().toLowerCase().contains(constraintString.toLowerCase())) {
                        newValues.add(mListGruposProdutoFull.get(i));
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
                mListGruposProduto = (ArrayList<tblGruposProduto>) results.values;
            } else {
                mListGruposProduto = new ArrayList<>();
            }

            notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtID;
        public TextView txtNome;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtID = (TextView) itemView.findViewById(R.id.txtID);
            txtNome = (TextView) itemView.findViewById(R.id.txtNome);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(getAdapterPosition());
                }
            });
        }
    }
}
