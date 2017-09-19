package mah.com.br.cash.Adapters;

import android.content.Context;
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

import mah.com.br.cash.Comparators.comCompraPagamentos;
import mah.com.br.cash.DataBase.tblCompraPagamentos;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class adpCompraPagamentos extends RecyclerView.Adapter<adpCompraPagamentos.MyViewHolder> implements Filterable {

    public List<tblCompraPagamentos> mListPagamentos;
    public int mItemSelected;
    public int mOrdem;

    private int mIdCompra;
    private List<tblCompraPagamentos> mListPagamentosFull;
    private Filter mFilter;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private RecyclerView mRecyclerView;

    public adpCompraPagamentos(Context c, RecyclerView r, int id_compra) {

        this.mContext = c;
        this.mIdCompra = id_compra;
        this.mItemSelected = -1;
        this.mOrdem = 0;
        this.mRecyclerView = r;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListPagamentos = getDados();
        this.mListPagamentosFull = getDados();

        order();
    }

    private List<tblCompraPagamentos> getDados() {

        List<tblCompraPagamentos> mList;

        tblCompraPagamentos record = new tblCompraPagamentos();
        mList = record.getList(mIdCompra);

        return mList;
    }

    public void order() {

        comCompraPagamentos mComparator = new comCompraPagamentos(mOrdem);

        Collections.sort(mListPagamentos, mComparator);
        Collections.sort(mListPagamentosFull, mComparator);

        notifyDataSetChanged();

        setSelected(-1);
    }

    private void setSelected(int position) {

        notifyItemChanged(mItemSelected);
        this.mItemSelected = position;
        notifyItemChanged(mItemSelected);

        mRecyclerView.scrollToPosition(mItemSelected);
    }

    public void insert(tblCompraPagamentos pagamento) {

        int iID;

        if (pagamento.insert() == -1) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_003),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        iID = pagamento.getIdPagamento();

        mListPagamentos.add(pagamento);
        mListPagamentosFull.add(pagamento);

        order();

        setSelectedByID(iID);

        String sMessage = mContext.getString(R.string.p_009);
        sMessage = sMessage.replace("@1", pagamento.getDtPagamento());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    public void update(tblCompraPagamentos pagamento) {

        int iID = pagamento.getIdPagamento();

        if (pagamento.update() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_004),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListPagamentosFull.size() - 1; i++) {
            if (mListPagamentosFull.get(i).getIdPagamento() == mListPagamentos.get(mItemSelected).getIdPagamento()) {
                mListPagamentosFull.remove(i);
                break;
            }
        }

        mListPagamentos.remove(mItemSelected);

        mListPagamentos.add(pagamento);
        mListPagamentosFull.add(pagamento);

        order();

        setSelectedByID(iID);

        String sMessage = mContext.getString(R.string.p_010);
        sMessage = sMessage.replace("@1", pagamento.getDtPagamento());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    private void setSelectedByID(int id_item) {

        int iPosition = -1;

        for (int i = 0; i <= mListPagamentos.size() - 1; i++) {
            if (mListPagamentos.get(i).getIdPagamento() == id_item) {
                iPosition = i;
                break;
            }
        }

        setSelected(iPosition);
    }

    public void remove() {

        if (mItemSelected == -1)
            return;

        tblCompraPagamentos record = new tblCompraPagamentos();
        record.setIdPagamento(mListPagamentos.get(mItemSelected).getIdPagamento());

        if (record.delete() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_005),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListPagamentosFull.size() - 1; i++) {
            if (mListPagamentosFull.get(i).getIdPagamento() == mListPagamentos.get(mItemSelected).getIdPagamento()) {
                mListPagamentosFull.remove(i);
                break;
            }
        }

        String sMessage = mContext.getString(R.string.p_011);
        sMessage = sMessage.replace("@1", mListPagamentos.get(mItemSelected).getDtPagamento());

        mListPagamentos.remove(mItemSelected);
        notifyItemRemoved(mItemSelected);

        setSelected(-1);

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mLayoutInflater.inflate(R.layout.list_compra_pagamentos, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        myViewHolder.txtID.setText(String.valueOf(mListPagamentos.get(i).getIdPagamento()));
        myViewHolder.txtData.setText(mListPagamentos.get(i).getDtPagamento());

        myViewHolder.txtValor.setText(mContext.getString(R.string.v_008) +
                mContext.getString(R.string._001) + " " +
                Funcoes.decimalFormat.format(mListPagamentos.get(i).getVlrPagamento()));

        myViewHolder.txtObservacoes.setText(mContext.getString(R.string.o_001) +
                mContext.getString(R.string._001) + " " +
                mListPagamentos.get(i).getObservacoes());

        if (mItemSelected == i)
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.primary_light));
        else
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.icons));
    }

    @Override
    public int getItemCount() {
        return mListPagamentos.size();
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
                List<tblCompraPagamentos> list = new ArrayList<>(mListPagamentosFull);
                results.count = list.size();
                results.values = list;
            } else {
                ArrayList<tblCompraPagamentos> newValues = new ArrayList<>(mListPagamentosFull.size());

                for (int i = 0; i < mListPagamentosFull.size(); i++) {
                    if (mListPagamentosFull.get(i).getDtPagamento().toLowerCase().contains(constraintString.toLowerCase())) {
                        newValues.add(mListPagamentosFull.get(i));
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
                mListPagamentos = (ArrayList<tblCompraPagamentos>) results.values;
            } else {
                mListPagamentos = new ArrayList<>();
            }

            notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtID;
        public TextView txtData;
        public TextView txtValor;
        public TextView txtObservacoes;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtID = (TextView) itemView.findViewById(R.id.txtID);
            txtData = (TextView) itemView.findViewById(R.id.txtData);
            txtValor = (TextView) itemView.findViewById(R.id.txtValor);
            txtObservacoes = (TextView) itemView.findViewById(R.id.txtObservacoes);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(getAdapterPosition());
                }
            });
        }
    }
}
