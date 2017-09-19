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
import mah.com.br.cash.Comparators.comCompraItens;
import mah.com.br.cash.Comparators.comCompraPagamentos;
import mah.com.br.cash.Comparators.comCompras;
import mah.com.br.cash.DataBase.tblCompraItens;
import mah.com.br.cash.DataBase.tblCompraPagamentos;
import mah.com.br.cash.DataBase.tblCompras;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class adpCompras extends RecyclerView.Adapter<adpCompras.MyViewHolder> implements Filterable {

    public List<tblCompras> mListCompras;
    public int mItemSelected;
    public int mOrdem;

    private int mMes;
    private int mAno;
    private List<tblCompras> mListComprasFull;
    private Filter mFilter;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private RecyclerView mRecyclerView;

    public adpCompras(Context c, RecyclerView r, int mes, int ano) {

        this.mContext = c;
        this.mMes = mes;
        this.mAno = ano;
        this.mItemSelected = -1;
        this.mOrdem = 0;
        this.mRecyclerView = r;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListCompras = getDados();
        this.mListComprasFull = getDados();

        order();
    }

    public void setMesAno(int mes, int ano) {
        this.mMes = mes;
        this.mAno = ano;

        this.mListCompras = getDados();
        this.mListComprasFull = getDados();

        order();
    }

    private List<tblCompras> getDados() {

        List<tblCompras> mList;

        tblCompras record = new tblCompras();
        mList = record.getList(mMes, mAno);

        return mList;
    }

    public void order() {

        comCompras mComparator = new comCompras(mOrdem);

        Collections.sort(mListCompras, mComparator);
        Collections.sort(mListComprasFull, mComparator);

        notifyDataSetChanged();

        setSelected(-1);
    }

    private void setSelected(int position) {

        notifyItemChanged(mItemSelected);
        this.mItemSelected = position;
        notifyItemChanged(mItemSelected);

        mRecyclerView.scrollToPosition(mItemSelected);
    }

    public void insert(tblCompras compra) {

        int iID;

        if (compra.insert() == -1) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_003),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        if (mMes == compra.getMes() & mAno == compra.getAno()) {

            iID = compra.getIdCompra();

            mListCompras.add(compra);
            mListComprasFull.add(compra);

            order();

            setSelectedByID(iID);
        }

        String sMessage = mContext.getString(R.string.c_012);
        sMessage = sMessage.replace("@1", compra.getIdentificacao());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    public void update(tblCompras compra) {

        int iID = compra.getIdCompra();

        if (compra.update() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_004),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListComprasFull.size() - 1; i++) {
            if (mListComprasFull.get(i).getIdCompra() == mListCompras.get(mItemSelected).getIdCompra()) {
                mListComprasFull.remove(i);
                break;
            }
        }

        mListCompras.remove(mItemSelected);

        if (mMes == compra.getMes() & mAno == compra.getAno()) {

            mListCompras.add(compra);
            mListComprasFull.add(compra);

            order();

            setSelectedByID(iID);
        } else {
            notifyItemRemoved(mItemSelected);
            setSelected(-1);
        }

        String sMessage = mContext.getString(R.string.c_013);
        sMessage = sMessage.replace("@1", compra.getIdentificacao());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    private void setSelectedByID(int id_compra) {

        int iPosition = -1;

        for (int i = 0; i <= mListCompras.size() - 1; i++) {
            if (mListCompras.get(i).getIdCompra() == id_compra) {
                iPosition = i;
                break;
            }
        }

        setSelected(iPosition);
    }

    public void remove() {

        if (mItemSelected == -1)
            return;

        tblCompras record = new tblCompras();
        record.setIdCompra(mListCompras.get(mItemSelected).getIdCompra());

        if (record.delete() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_005),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListComprasFull.size() - 1; i++) {
            if (mListComprasFull.get(i).getIdCompra() == mListCompras.get(mItemSelected).getIdCompra()) {
                mListComprasFull.remove(i);
                break;
            }
        }

        String sMessage = mContext.getString(R.string.c_014);
        sMessage = sMessage.replace("@1", mListCompras.get(mItemSelected).getIdentificacao());

        mListCompras.remove(mItemSelected);
        notifyItemRemoved(mItemSelected);

        setSelected(-1);

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    public void shared(final String tipo) {

        final ProgressDialog pd;
        pd = ProgressDialog.show(mContext,
                mContext.getString(R.string.a_005),
                mContext.getString(R.string.g_006),
                true);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);

                    StringBuilder sBuilder = new StringBuilder();

                    sBuilder.append(Funcoes.addCabecalho(mContext.getString(R.string.l_005)));
                    sBuilder.append(Funcoes.addTable(mContext, mContext.getString(R.string.l_005), "800px", 1));

                    sBuilder.append("<tr>");
                    sBuilder.append(Funcoes.addTitulo(mContext, "20%", "center", "middle", mContext.getString(R.string.i_014)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "25%", "center", "middle", mContext.getString(R.string.f_004)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "15%", "center", "middle", mContext.getString(R.string.d_006)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "15%", "center", "middle", mContext.getString(R.string.v_001)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "15%", "center", "middle", mContext.getString(R.string.v_010)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "10%", "center", "middle", mContext.getString(R.string.q_002)));
                    sBuilder.append("</tr>");

                    for (int i = 0; i <= mListCompras.size() - 1; i++) {
                        sBuilder.append("<tr>");
                        sBuilder.append(Funcoes.addDetalhe(mContext, mListCompras.get(i).getIdentificacao(), "left"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, mListCompras.get(i).getCalcNomeFornecedor(), "left"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, mListCompras.get(i).getDtCompra(), "center"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(mListCompras.get(i).getCalcVlrCompra()), "right"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(mListCompras.get(i).getCalcVlrPagamento()), "right"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Integer.toString(mListCompras.get(i).getCalcQtdeItensCompra()), "right"));
                        sBuilder.append("</tr>");
                    }

                    Funcoes.addRodape();

                    if (tipo.matches("Relatorio")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setDataAndType(FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", Funcoes.saveFile(mContext, mContext.getString(R.string.c_015), sBuilder.toString())), "text/html");
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            intent.setDataAndType(Uri.fromFile(Funcoes.saveFile(mContext, mContext.getString(R.string.c_015), sBuilder.toString())), "text/html");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                        mContext.startActivity(intent);
                    }

                    if (tipo.matches("Compartilhar")) {

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.l_005));
                        intent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.s_008));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", Funcoes.saveFile(mContext, mContext.getString(R.string.c_015), sBuilder.toString())));
                        } else {
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(Funcoes.saveFile(mContext, mContext.getString(R.string.c_015), sBuilder.toString())));
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

    public void sharedDetalhe(final String tipo) {

        final ProgressDialog pd;
        pd = ProgressDialog.show(mContext,
                mContext.getString(R.string.a_005),
                mContext.getString(R.string.g_006),
                true);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);

                    String sAux;
                    StringBuilder sBuilder = new StringBuilder();

                    sBuilder.append(Funcoes.addCabecalho(mContext.getString(R.string.d_011)));
                    sBuilder.append(Funcoes.addTable(mContext, mContext.getString(R.string.d_011), "600px", 1));

                    sBuilder.append("<tr>");
                    sBuilder.append(Funcoes.addTitulo(mContext, "1000%", "center", "middle", mContext.getString(R.string.i_029)));
                    sBuilder.append("</tr>");

                    sBuilder.append("<tr>");
                    sAux = mContext.getString(R.string.i_014) + mContext.getString(R.string._001) + " " + mListCompras.get(mItemSelected).getIdentificacao() + "<br>";
                    sAux += mContext.getString(R.string.f_004) + mContext.getString(R.string._001) + " " + mListCompras.get(mItemSelected).getCalcNomeFornecedor() + "<br>";
                    sAux += mContext.getString(R.string.d_006) + mContext.getString(R.string._001) + " " + mListCompras.get(mItemSelected).getDtCompra() + "<br>";
                    sAux += mContext.getString(R.string.v_001) + mContext.getString(R.string._001) + " " + Funcoes.decimalFormat.format(mListCompras.get(mItemSelected).getCalcVlrCompra()) + "<br>";
                    sAux += mContext.getString(R.string.v_010) + mContext.getString(R.string._001) + " " + Funcoes.decimalFormat.format(mListCompras.get(mItemSelected).getCalcVlrPagamento()) + "<br>";
                    sAux += mContext.getString(R.string.q_002) + mContext.getString(R.string._001) + " " + Integer.toString(mListCompras.get(mItemSelected).getCalcQtdeItensCompra()) + "<br>";
                    sAux += mContext.getString(R.string.o_001) + mContext.getString(R.string._001) + " " + mListCompras.get(mItemSelected).getObservacoes();
                    sBuilder.append(Funcoes.addDetalhe(mContext, sAux, "left"));
                    sBuilder.append("</tr>");
                    sBuilder.append("</tbody></table>");

                    sBuilder.append("<br>");

                    sBuilder.append(Funcoes.addTable(mContext, mContext.getString(R.string.d_012), "600px", 5));

                    tblCompraItens record = new tblCompraItens();
                    List<tblCompraItens> lstCompraItens;
                    lstCompraItens = record.getList(mListCompras.get(mItemSelected).getIdCompra());

                    comCompraItens mComparatorCI = new comCompraItens(0);
                    Collections.sort(lstCompraItens, mComparatorCI);

                    sBuilder.append("<tr>");
                    sBuilder.append(Funcoes.addTitulo(mContext, "20%", "center", "middle", mContext.getString(R.string.p_006)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "15%", "center", "middle", mContext.getString(R.string.q_001)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "15%", "center", "middle", mContext.getString(R.string.v_006)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "15%", "center", "middle", mContext.getString(R.string.v_007)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "35%", "center", "middle", mContext.getString(R.string.o_001)));
                    sBuilder.append("</tr>");

                    for (int i = 0; i <= lstCompraItens.size() - 1; i++) {
                        sBuilder.append("<tr>");
                        sBuilder.append(Funcoes.addDetalhe(mContext, lstCompraItens.get(i).getCalcNomeProduto(), "left"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(lstCompraItens.get(i).getQtItens()), "right"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(lstCompraItens.get(i).getVlrUnitario()), "right"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(lstCompraItens.get(i).getCalcVlrTotal()), "right"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, lstCompraItens.get(i).getObservacoes(), "left"));
                        sBuilder.append("</tr>");
                    }

                    sBuilder.append("</tbody></table>");

                    sBuilder.append("<br>");

                    sBuilder.append(Funcoes.addTable(mContext, mContext.getString(R.string.d_013), "600px", 5));

                    tblCompraPagamentos recPagamento = new tblCompraPagamentos();
                    List<tblCompraPagamentos> lstCompraPagamentos;
                    lstCompraPagamentos = recPagamento.getList(mListCompras.get(mItemSelected).getIdCompra());

                    comCompraPagamentos mComparatorCP = new comCompraPagamentos(0);
                    Collections.sort(lstCompraPagamentos, mComparatorCP);

                    sBuilder.append("<tr>");
                    sBuilder.append(Funcoes.addTitulo(mContext, "25%", "center", "middle", mContext.getString(R.string.d_009)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "25%", "center", "middle", mContext.getString(R.string.v_008)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "50%", "center", "middle", mContext.getString(R.string.o_001)));
                    sBuilder.append("</tr>");

                    for (int i = 0; i <= lstCompraPagamentos.size() - 1; i++) {
                        sBuilder.append("<tr>");
                        sBuilder.append(Funcoes.addDetalhe(mContext, lstCompraPagamentos.get(i).getDtPagamento(), "center"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(lstCompraPagamentos.get(i).getVlrPagamento()), "right"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, lstCompraPagamentos.get(i).getObservacoes(), "left"));
                        sBuilder.append("</tr>");
                    }

                    Funcoes.addRodape();

                    if (tipo.matches("Relatorio")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setDataAndType(FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", Funcoes.saveFile(mContext, mContext.getString(R.string.c_015), sBuilder.toString())), "text/html");
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            intent.setDataAndType(Uri.fromFile(Funcoes.saveFile(mContext, mContext.getString(R.string.c_015), sBuilder.toString())), "text/html");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                        mContext.startActivity(intent);
                    }

                    if (tipo.matches("Compartilhar")) {

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.l_005));
                        intent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.s_008));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", Funcoes.saveFile(mContext, mContext.getString(R.string.c_015), sBuilder.toString())));
                        } else {
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(Funcoes.saveFile(mContext, mContext.getString(R.string.c_015), sBuilder.toString())));
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
        View v = mLayoutInflater.inflate(R.layout.list_compras, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        myViewHolder.txtID.setText(String.valueOf(mListCompras.get(i).getIdCompra()));
        myViewHolder.txtIdentificacao.setText(mListCompras.get(i).getIdentificacao());

        myViewHolder.txtFornecedor.setText(mContext.getString(R.string.f_004) +
                mContext.getString(R.string._001) + " " +
                mListCompras.get(i).getCalcNomeFornecedor());

        myViewHolder.txtData.setText(mContext.getString(R.string.d_006) +
                mContext.getString(R.string._001) + " " +
                mListCompras.get(i).getDtCompra());

        myViewHolder.txtValor.setText(mContext.getString(R.string.v_001) +
                mContext.getString(R.string._001) + " " +
                Funcoes.decimalFormat.format(mListCompras.get(i).getCalcVlrCompra()));

        myViewHolder.txtVlrPagamento.setText(mContext.getString(R.string.v_010) +
                mContext.getString(R.string._001) + " " +
                Funcoes.decimalFormat.format(mListCompras.get(i).getCalcVlrPagamento()));

        myViewHolder.txtQtdeItens.setText(mContext.getString(R.string.q_002) +
                mContext.getString(R.string._001) + " " +
                Integer.toString(mListCompras.get(i).getCalcQtdeItensCompra()));

        myViewHolder.txtObservacoes.setText(mContext.getString(R.string.o_001) +
                mContext.getString(R.string._001) + " " +
                mListCompras.get(i).getObservacoes());

        if (mItemSelected == i)
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.primary_light));
        else
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.icons));
    }

    @Override
    public int getItemCount() {
        return mListCompras.size();
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
                List<tblCompras> list = new ArrayList<>(mListComprasFull);
                results.count = list.size();
                results.values = list;
            } else {
                ArrayList<tblCompras> newValues = new ArrayList<>(mListComprasFull.size());

                for (int i = 0; i < mListComprasFull.size(); i++) {
                    if (mListComprasFull.get(i).getIdentificacao().toLowerCase().contains(constraintString.toLowerCase())) {
                        newValues.add(mListComprasFull.get(i));
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
                mListCompras = (ArrayList<tblCompras>) results.values;
            } else {
                mListCompras = new ArrayList<>();
            }

            notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtID;
        public TextView txtIdentificacao;
        public TextView txtFornecedor;
        public TextView txtData;
        public TextView txtValor;
        public TextView txtQtdeItens;
        public TextView txtObservacoes;
        public TextView txtVlrPagamento;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtID = (TextView) itemView.findViewById(R.id.txtID);
            txtIdentificacao = (TextView) itemView.findViewById(R.id.txtIdentificacao);
            txtFornecedor = (TextView) itemView.findViewById(R.id.txtFornecedor);
            txtData = (TextView) itemView.findViewById(R.id.txtData);
            txtValor = (TextView) itemView.findViewById(R.id.txtValor);
            txtQtdeItens = (TextView) itemView.findViewById(R.id.txtQtdeItens);
            txtObservacoes = (TextView) itemView.findViewById(R.id.txtObservacoes);
            txtVlrPagamento = (TextView) itemView.findViewById(R.id.txtValorPagamento);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(getAdapterPosition());
                }
            });
        }
    }
}
