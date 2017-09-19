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

import mah.com.br.cash.Comparators.comVendaItens;
import mah.com.br.cash.DataBase.tblVendaItens;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class adpVendaItens extends RecyclerView.Adapter<adpVendaItens.MyViewHolder> implements Filterable {

    public List<tblVendaItens> mListItens;
    public int mItemSelected;
    public int mOrdem;

    private int mIdVenda;
    private List<tblVendaItens> mListItensFull;
    private Filter mFilter;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private RecyclerView mRecyclerView;

    public adpVendaItens(Context c, RecyclerView r, int id_venda) {

        this.mContext = c;
        this.mIdVenda = id_venda;
        this.mItemSelected = -1;
        this.mOrdem = 0;
        this.mRecyclerView = r;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListItens = getDados();
        this.mListItensFull = getDados();

        order();
    }

    private List<tblVendaItens> getDados() {

        List<tblVendaItens> mList;

        tblVendaItens record = new tblVendaItens();
        mList = record.getList(mIdVenda);

        return mList;
    }

    public void order() {

        comVendaItens mComparator = new comVendaItens(mOrdem);

        Collections.sort(mListItens, mComparator);
        Collections.sort(mListItensFull, mComparator);

        notifyDataSetChanged();

        setSelected(-1);
    }

    private void setSelected(int position) {

        notifyItemChanged(mItemSelected);
        this.mItemSelected = position;
        notifyItemChanged(mItemSelected);

        mRecyclerView.scrollToPosition(mItemSelected);
    }

    public void insert(tblVendaItens item) {

        int iID;

        if (item.insert() == -1) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_003),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        iID = item.getIdItem();

        mListItens.add(item);
        mListItensFull.add(item);

        order();

        setSelectedByID(iID);

        String sMessage = mContext.getString(R.string.i_019);
        sMessage = sMessage.replace("@1", item.getCalcNomeProduto());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    public void update(tblVendaItens item) {

        int iID = item.getIdItem();

        if (item.update() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_004),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListItensFull.size() - 1; i++) {
            if (mListItensFull.get(i).getIdItem() == mListItens.get(mItemSelected).getIdItem()) {
                mListItensFull.remove(i);
                break;
            }
        }

        mListItens.remove(mItemSelected);

        mListItens.add(item);
        mListItensFull.add(item);

        order();

        setSelectedByID(iID);

        String sMessage = mContext.getString(R.string.i_020);
        sMessage = sMessage.replace("@1", item.getCalcNomeProduto());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    private void setSelectedByID(int id_item) {

        int iPosition = -1;

        for (int i = 0; i <= mListItens.size() - 1; i++) {
            if (mListItens.get(i).getIdItem() == id_item) {
                iPosition = i;
                break;
            }
        }

        setSelected(iPosition);
    }

    public void remove() {

        if (mItemSelected == -1)
            return;

        tblVendaItens record = new tblVendaItens();
        record.setIdItem(mListItens.get(mItemSelected).getIdItem());

        if (record.delete() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_005),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListItensFull.size() - 1; i++) {
            if (mListItensFull.get(i).getIdItem() == mListItens.get(mItemSelected).getIdItem()) {
                mListItensFull.remove(i);
                break;
            }
        }

        String sMessage = mContext.getString(R.string.i_021);
        sMessage = sMessage.replace("@1", mListItens.get(mItemSelected).getCalcNomeProduto());

        mListItens.remove(mItemSelected);
        notifyItemRemoved(mItemSelected);

        setSelected(-1);

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mLayoutInflater.inflate(R.layout.list_venda_itens, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        myViewHolder.txtID.setText(String.valueOf(mListItens.get(i).getIdItem()));
        myViewHolder.txtProduto.setText(mListItens.get(i).getCalcNomeProduto());

        myViewHolder.txtQtde.setText(mContext.getString(R.string.q_001) +
                mContext.getString(R.string._001) + " " +
                Funcoes.decimalFormat.format(mListItens.get(i).getQtItens()));

        myViewHolder.txtVlrUnitario.setText(mContext.getString(R.string.v_006) +
                mContext.getString(R.string._001) + " " +
                Funcoes.decimalFormat.format(mListItens.get(i).getVlrUnitario()));

        myViewHolder.txtVlrTotal.setText(mContext.getString(R.string.v_007) +
                mContext.getString(R.string._001) + " " +
                Funcoes.decimalFormat.format(mListItens.get(i).getCalcVlrTotal()));

        myViewHolder.txtObservacoes.setText(mContext.getString(R.string.o_001) +
                mContext.getString(R.string._001) + " " +
                mListItens.get(i).getObservacoes());

        if (mItemSelected == i)
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.primary_light));
        else
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.icons));
    }

    @Override
    public int getItemCount() {
        return mListItens.size();
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
                List<tblVendaItens> list = new ArrayList<>(mListItensFull);
                results.count = list.size();
                results.values = list;
            } else {
                ArrayList<tblVendaItens> newValues = new ArrayList<>(mListItensFull.size());

                for (int i = 0; i < mListItensFull.size(); i++) {
                    if (mListItensFull.get(i).getCalcNomeProduto().toLowerCase().contains(constraintString.toLowerCase())) {
                        newValues.add(mListItensFull.get(i));
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
                mListItens = (ArrayList<tblVendaItens>) results.values;
            } else {
                mListItens = new ArrayList<>();
            }

            notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtID;
        public TextView txtProduto;
        public TextView txtQtde;
        public TextView txtVlrUnitario;
        public TextView txtVlrTotal;
        public TextView txtObservacoes;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtID = (TextView) itemView.findViewById(R.id.txtID);
            txtProduto = (TextView) itemView.findViewById(R.id.txtProduto);
            txtQtde = (TextView) itemView.findViewById(R.id.txtQtde);
            txtVlrUnitario = (TextView) itemView.findViewById(R.id.txtVlrUnitario);
            txtVlrTotal = (TextView) itemView.findViewById(R.id.txtVlrTotal);
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
