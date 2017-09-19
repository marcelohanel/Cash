package mah.com.br.cash.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import mah.com.br.cash.Comparators.comProdutos;
import mah.com.br.cash.DataBase.tblProdutos;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class adpProdutos extends RecyclerView.Adapter<adpProdutos.MyViewHolder> implements Filterable {

    public List<tblProdutos> mListProdutos;
    public int mItemSelected;
    public int mOrdem;

    private List<tblProdutos> mListProdutosFull;
    private Filter mFilter;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private RecyclerView mRecyclerView;

    public adpProdutos(Context c, RecyclerView r) {

        this.mContext = c;

        this.mItemSelected = -1;
        this.mOrdem = 0;
        this.mRecyclerView = r;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListProdutos = getDados();
        this.mListProdutosFull = getDados();

        order();
    }

    private List<tblProdutos> getDados() {

        List<tblProdutos> mList;

        tblProdutos record = new tblProdutos();
        mList = record.getList();

        return mList;
    }

    public void order() {

        comProdutos mComparator = new comProdutos(mOrdem);

        Collections.sort(mListProdutos, mComparator);
        Collections.sort(mListProdutosFull, mComparator);

        if (mOrdem == 7) {
            Collections.reverse(mListProdutos);
            Collections.reverse(mListProdutosFull);
        }

        notifyDataSetChanged();

        setSelected(-1);
    }

    private void setSelected(int position) {

        notifyItemChanged(mItemSelected);
        this.mItemSelected = position;
        notifyItemChanged(mItemSelected);

        mRecyclerView.scrollToPosition(mItemSelected);
    }

    public void insert(tblProdutos produto) {

        int iID;

        if (produto.insert() == -1) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_003),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        iID = produto.getIdProduto();

        mListProdutos.add(produto);
        mListProdutosFull.add(produto);

        order();

        setSelectedByID(iID);

        String sMessage = mContext.getString(R.string.p_003);
        sMessage = sMessage.replace("@1", mListProdutos.get(mItemSelected).getNome());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    public void update(tblProdutos produto) {

        int iID = produto.getIdProduto();

        if (produto.update() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_004),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListProdutosFull.size() - 1; i++) {
            if (mListProdutosFull.get(i).getIdProduto() == mListProdutos.get(mItemSelected).getIdProduto()) {
                mListProdutosFull.remove(i);
                break;
            }
        }

        mListProdutos.remove(mItemSelected);

        mListProdutos.add(produto);
        mListProdutosFull.add(produto);

        order();

        setSelectedByID(iID);

        String sMessage = mContext.getString(R.string.p_004);
        sMessage = sMessage.replace("@1", mListProdutos.get(mItemSelected).getNome());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    private void setSelectedByID(int id_produto) {

        int iPosition = -1;

        for (int i = 0; i <= mListProdutos.size() - 1; i++) {
            if (mListProdutos.get(i).getIdProduto() == id_produto) {
                iPosition = i;
                break;
            }
        }

        setSelected(iPosition);
    }

    public void remove() {

        if (mItemSelected == -1)
            return;

        tblProdutos record = new tblProdutos();
        record.setIdProduto(mListProdutos.get(mItemSelected).getIdProduto());
        if (record.delete() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_005),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListProdutosFull.size() - 1; i++) {
            if (mListProdutosFull.get(i).getIdProduto() == mListProdutos.get(mItemSelected).getIdProduto()) {
                mListProdutosFull.remove(i);
                break;
            }
        }

        String sMessage = mContext.getString(R.string.p_005);
        sMessage = sMessage.replace("@1", mListProdutos.get(mItemSelected).getNome());

        mListProdutos.remove(mItemSelected);
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

                    double dAux = 0;
                    double dTotalEstoqueCompra = 0;
                    double dTotalEstoqueVenda = 0;

                    StringBuilder sBuilder = new StringBuilder();

                    sBuilder.append(Funcoes.addCabecalho(mContext.getString(R.string.l_003)));
                    sBuilder.append(Funcoes.addTable(mContext, mContext.getString(R.string.l_003), "1000px", 10));

                    sBuilder.append("<tr>");
                    sBuilder.append(Funcoes.addTitulo(mContext, "15%", "center", "middle", mContext.getString(R.string.p_006)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "10%", "center", "middle", mContext.getString(R.string.g_007)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "5%", "center", "middle", mContext.getString(R.string.r_002)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "10%", "center", "middle", mContext.getString(R.string.f_004)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "10%", "center", "middle", mContext.getString(R.string.v_003)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "10%", "center", "middle", mContext.getString(R.string.v_002)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "10%", "center", "middle", mContext.getString(R.string.e_007)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "10%", "center", "middle", mContext.getString(R.string.m_002)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "10%", "center", "middle", mContext.getString(R.string.v_026)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "10%", "center", "middle", mContext.getString(R.string.v_027)));

                    sBuilder.append("</tr>");

                    for (int i = 0; i <= mListProdutos.size() - 1; i++) {
                        sBuilder.append("<tr>");
                        sBuilder.append(Funcoes.addDetalhe(mContext, mListProdutos.get(i).getNome(), "left"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, mListProdutos.get(i).getCalcNomeGrupoProduto(), "left"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, mListProdutos.get(i).getReferencia(), "left"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, mListProdutos.get(i).getCalcFornecedor(), "left"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(mListProdutos.get(i).getCalcVlrCompra()), "right"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(mListProdutos.get(i).getVlrVenda()), "right"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(mListProdutos.get(i).getCalcEstoque()) + " " + mListProdutos.get(i).getUnidade(), "right"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(mListProdutos.get(i).getCalcMargem()) + "%", "right"));

                        dAux = mListProdutos.get(i).getCalcVlrCompra() * mListProdutos.get(i).getCalcEstoque();
                        dTotalEstoqueCompra += dAux;
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(dAux), "right"));

                        dAux = mListProdutos.get(i).getVlrVenda() * mListProdutos.get(i).getCalcEstoque();
                        dTotalEstoqueVenda += dAux;
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(dAux), "right"));

                        sBuilder.append("</tr>");
                    }

                    sBuilder.append("<tr>");
                    sBuilder.append(Funcoes.addResumo(mContext, "center", "middle", mContext.getString(R.string._002)));
                    sBuilder.append(Funcoes.addResumo(mContext, "center", "middle", ""));
                    sBuilder.append(Funcoes.addResumo(mContext, "center", "middle", ""));
                    sBuilder.append(Funcoes.addResumo(mContext, "center", "middle", ""));
                    sBuilder.append(Funcoes.addResumo(mContext, "center", "middle", ""));
                    sBuilder.append(Funcoes.addResumo(mContext, "center", "middle", ""));
                    sBuilder.append(Funcoes.addResumo(mContext, "center", "middle", ""));
                    sBuilder.append(Funcoes.addResumo(mContext, "center", "middle", ""));
                    sBuilder.append(Funcoes.addResumo(mContext, "right", "middle", Funcoes.decimalFormat.format(dTotalEstoqueCompra)));
                    sBuilder.append(Funcoes.addResumo(mContext, "right", "middle", Funcoes.decimalFormat.format(dTotalEstoqueVenda)));
                    sBuilder.append("</tr>");

                    Funcoes.addRodape();

                    if (tipo.matches("Relatorio")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setDataAndType(FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", Funcoes.saveFile(mContext, mContext.getString(R.string.p_007), sBuilder.toString())), "text/html");
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            intent.setDataAndType(Uri.fromFile(Funcoes.saveFile(mContext, mContext.getString(R.string.p_007), sBuilder.toString())), "text/html");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                        mContext.startActivity(intent);
                    }

                    if (tipo.matches("Compartilhar")) {

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.l_003));
                        intent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.s_005));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", Funcoes.saveFile(mContext, mContext.getString(R.string.p_007), sBuilder.toString())));
                        } else {
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(Funcoes.saveFile(mContext, mContext.getString(R.string.p_007), sBuilder.toString())));
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
        View v = mLayoutInflater.inflate(R.layout.list_produtos, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        double dMargem = 0.0;
        double dEstoque = 0.0;

        myViewHolder.txtID.setText(String.valueOf(mListProdutos.get(i).getIdProduto()));
        myViewHolder.txtNome.setText(mListProdutos.get(i).getNome());
        myViewHolder.txtGrupo.setText(mContext.getString(R.string.g_007) + mContext.getString(R.string._001) + " " + mListProdutos.get(i).getCalcNomeGrupoProduto());
        myViewHolder.txtReferencia.setText(mContext.getString(R.string.r_002) + mContext.getString(R.string._001) + " " + mListProdutos.get(i).getReferencia());
        myViewHolder.txtFornecedor.setText(mContext.getString(R.string.f_004) + mContext.getString(R.string._001) + " " + mListProdutos.get(i).getCalcFornecedor());
        myViewHolder.txtCompra.setText(mContext.getString(R.string.v_003) + mContext.getString(R.string._001) + " " + Funcoes.decimalFormat.format(mListProdutos.get(i).getCalcVlrCompra()));
        myViewHolder.txtVenda.setText(mContext.getString(R.string.v_002) + mContext.getString(R.string._001) + " " + Funcoes.decimalFormat.format(mListProdutos.get(i).getVlrVenda()));

        dEstoque = mListProdutos.get(i).getCalcEstoque();
        myViewHolder.txtEstoque.setText(mContext.getString(R.string.e_007) + mContext.getString(R.string._001) + " " + Funcoes.decimalFormat.format(dEstoque) + " " + mListProdutos.get(i).getUnidade());
        if (dEstoque < 0)
            myViewHolder.txtEstoque.setTextColor(Color.RED);
        else
            myViewHolder.txtEstoque.setTextColor(myViewHolder.txtGrupo.getTextColors());

        dMargem = mListProdutos.get(i).getCalcMargem();
        myViewHolder.txtMargem.setText(mContext.getString(R.string.m_002) + mContext.getString(R.string._001) + " " + Funcoes.decimalFormat.format(dMargem) + "%");
        if (dMargem < 0)
            myViewHolder.txtMargem.setTextColor(Color.RED);
        else
            myViewHolder.txtMargem.setTextColor(myViewHolder.txtGrupo.getTextColors());

        if (mItemSelected == i)
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.primary_light));
        else
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.icons));
    }

    @Override
    public int getItemCount() {
        return mListProdutos.size();
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
                List<tblProdutos> list = new ArrayList<>(mListProdutosFull);
                results.count = list.size();
                results.values = list;
            } else {
                ArrayList<tblProdutos> newValues = new ArrayList<>(mListProdutosFull.size());

                for (int i = 0; i < mListProdutosFull.size(); i++) {
                    if (mListProdutosFull.get(i).getNome().toLowerCase().contains(constraintString.toLowerCase())) {
                        newValues.add(mListProdutosFull.get(i));
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
                mListProdutos = (ArrayList<tblProdutos>) results.values;
            } else {
                mListProdutos = new ArrayList<>();
            }

            notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtID;
        public TextView txtNome;
        public TextView txtGrupo;
        public TextView txtReferencia;
        public TextView txtFornecedor;
        public TextView txtEstoque;
        public TextView txtCompra;
        public TextView txtVenda;
        public TextView txtMargem;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtID = (TextView) itemView.findViewById(R.id.txtID);
            txtNome = (TextView) itemView.findViewById(R.id.txtNome);
            txtGrupo = (TextView) itemView.findViewById(R.id.txtGrupo);
            txtReferencia = (TextView) itemView.findViewById(R.id.txtReferencia);
            txtFornecedor = (TextView) itemView.findViewById(R.id.txtFornecedor);
            txtEstoque = (TextView) itemView.findViewById(R.id.txtEstoque);
            txtCompra = (TextView) itemView.findViewById(R.id.txtCompra);
            txtVenda = (TextView) itemView.findViewById(R.id.txtVenda);
            txtMargem = (TextView) itemView.findViewById(R.id.txtMargem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(getAdapterPosition());
                }
            });
        }
    }
}
