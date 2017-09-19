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

import mah.com.br.cash.Comparators.comVendaRecebimentos;
import mah.com.br.cash.DataBase.tblVendaRecebimentos;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class adpVendaRecebimentos extends RecyclerView.Adapter<adpVendaRecebimentos.MyViewHolder> implements Filterable {

    public List<tblVendaRecebimentos> mListRecebimentos;
    public int mItemSelected;
    public int mOrdem;

    private int mIdVenda;
    private List<tblVendaRecebimentos> mListRecebimentosFull;
    private Filter mFilter;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private RecyclerView mRecyclerView;

    public adpVendaRecebimentos(Context c, RecyclerView r, int id_venda) {

        this.mContext = c;
        this.mIdVenda = id_venda;
        this.mItemSelected = -1;
        this.mOrdem = 0;
        this.mRecyclerView = r;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListRecebimentos = getDados();
        this.mListRecebimentosFull = getDados();

        order();
    }

    private List<tblVendaRecebimentos> getDados() {

        List<tblVendaRecebimentos> mList;

        tblVendaRecebimentos record = new tblVendaRecebimentos();
        mList = record.getList(mIdVenda);

        return mList;
    }

    public void order() {

        comVendaRecebimentos mComparator = new comVendaRecebimentos(mOrdem);

        Collections.sort(mListRecebimentos, mComparator);
        Collections.sort(mListRecebimentosFull, mComparator);

        notifyDataSetChanged();

        setSelected(-1);
    }

    private void setSelected(int position) {

        notifyItemChanged(mItemSelected);
        this.mItemSelected = position;
        notifyItemChanged(mItemSelected);

        mRecyclerView.scrollToPosition(mItemSelected);
    }

    public void insert(tblVendaRecebimentos recebimento) {

        int iID;

        if (recebimento.insert() == -1) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_003),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        iID = recebimento.getIdRecebimento();

        mListRecebimentos.add(recebimento);
        mListRecebimentosFull.add(recebimento);

        order();

        setSelectedByID(iID);

        String sMessage = mContext.getString(R.string.r_004);
        sMessage = sMessage.replace("@1", recebimento.getDtRecebimento());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    public void update(tblVendaRecebimentos recebimento) {

        int iID = recebimento.getIdRecebimento();

        if (recebimento.update() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_004),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListRecebimentosFull.size() - 1; i++) {
            if (mListRecebimentosFull.get(i).getIdRecebimento() == mListRecebimentos.get(mItemSelected).getIdRecebimento()) {
                mListRecebimentosFull.remove(i);
                break;
            }
        }

        mListRecebimentos.remove(mItemSelected);

        mListRecebimentos.add(recebimento);
        mListRecebimentosFull.add(recebimento);

        order();

        setSelectedByID(iID);

        String sMessage = mContext.getString(R.string.r_005);
        sMessage = sMessage.replace("@1", recebimento.getDtRecebimento());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    private void setSelectedByID(int id_recebimento) {

        int iPosition = -1;

        for (int i = 0; i <= mListRecebimentos.size() - 1; i++) {
            if (mListRecebimentos.get(i).getIdRecebimento() == id_recebimento) {
                iPosition = i;
                break;
            }
        }

        setSelected(iPosition);
    }

    public void remove() {

        if (mItemSelected == -1)
            return;

        tblVendaRecebimentos record = new tblVendaRecebimentos();
        record.setIdRecebimento(mListRecebimentos.get(mItemSelected).getIdRecebimento());

        if (record.delete() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_005),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListRecebimentosFull.size() - 1; i++) {
            if (mListRecebimentosFull.get(i).getIdRecebimento() == mListRecebimentos.get(mItemSelected).getIdRecebimento()) {
                mListRecebimentosFull.remove(i);
                break;
            }
        }

        String sMessage = mContext.getString(R.string.r_006);
        sMessage = sMessage.replace("@1", mListRecebimentos.get(mItemSelected).getDtRecebimento());

        mListRecebimentos.remove(mItemSelected);
        notifyItemRemoved(mItemSelected);

        setSelected(-1);

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mLayoutInflater.inflate(R.layout.list_venda_recebimentos, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        myViewHolder.txtID.setText(String.valueOf(mListRecebimentos.get(i).getIdRecebimento()));
        myViewHolder.txtData.setText(mListRecebimentos.get(i).getDtRecebimento());

        myViewHolder.txtValor.setText(mContext.getString(R.string.v_008) +
                mContext.getString(R.string._001) + " " +
                Funcoes.decimalFormat.format(mListRecebimentos.get(i).getVlrRecebimento()));

        myViewHolder.txtObservacoes.setText(mContext.getString(R.string.o_001) +
                mContext.getString(R.string._001) + " " +
                mListRecebimentos.get(i).getObservacoes());

        if (mItemSelected == i)
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.primary_light));
        else
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.icons));
    }

    @Override
    public int getItemCount() {
        return mListRecebimentos.size();
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
                List<tblVendaRecebimentos> list = new ArrayList<>(mListRecebimentosFull);
                results.count = list.size();
                results.values = list;
            } else {
                ArrayList<tblVendaRecebimentos> newValues = new ArrayList<>(mListRecebimentosFull.size());

                for (int i = 0; i < mListRecebimentosFull.size(); i++) {
                    if (mListRecebimentosFull.get(i).getDtRecebimento().toLowerCase().contains(constraintString.toLowerCase())) {
                        newValues.add(mListRecebimentosFull.get(i));
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
                mListRecebimentos = (ArrayList<tblVendaRecebimentos>) results.values;
            } else {
                mListRecebimentos = new ArrayList<>();
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
