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
import mah.com.br.cash.Comparators.comVendaItens;
import mah.com.br.cash.Comparators.comVendaRecebimentos;
import mah.com.br.cash.Comparators.comVendas;
import mah.com.br.cash.DataBase.tblVendaItens;
import mah.com.br.cash.DataBase.tblVendaRecebimentos;
import mah.com.br.cash.DataBase.tblVendas;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class adpVendas extends RecyclerView.Adapter<adpVendas.MyViewHolder> implements Filterable {

    public List<tblVendas> mListVendas;
    public int mItemSelected;
    public int mOrdem;

    private int mMes;
    private int mAno;
    private List<tblVendas> mListVendasFull;
    private Filter mFilter;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private RecyclerView mRecyclerView;

    public adpVendas(Context c, RecyclerView r, int mes, int ano) {

        this.mContext = c;
        this.mMes = mes;
        this.mAno = ano;
        this.mItemSelected = -1;
        this.mOrdem = 0;
        this.mRecyclerView = r;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListVendas = getDados();
        this.mListVendasFull = getDados();

        order();
    }

    public void setMesAno(int mes, int ano) {
        this.mMes = mes;
        this.mAno = ano;

        this.mListVendas = getDados();
        this.mListVendasFull = getDados();

        order();
    }

    private List<tblVendas> getDados() {

        List<tblVendas> mList;

        tblVendas record = new tblVendas();
        mList = record.getList(mMes, mAno);

        return mList;
    }

    public void order() {

        comVendas mComparator = new comVendas(mOrdem);

        Collections.sort(mListVendas, mComparator);
        Collections.sort(mListVendasFull, mComparator);

        notifyDataSetChanged();

        setSelected(-1);
    }

    private void setSelected(int position) {

        notifyItemChanged(mItemSelected);
        this.mItemSelected = position;
        notifyItemChanged(mItemSelected);

        mRecyclerView.scrollToPosition(mItemSelected);
    }

    public void insert(tblVendas venda) {

        int iID;

        if (venda.insert() == -1) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_003),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        if (mMes == venda.getMes() & mAno == venda.getAno()) {

            iID = venda.getIdVenda();

            mListVendas.add(venda);
            mListVendasFull.add(venda);

            order();

            setSelectedByID(iID);
        }

        String sMessage = mContext.getString(R.string.v_011);
        sMessage = sMessage.replace("@1", venda.getIdentificacao());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    public void update(tblVendas venda) {

        int iID = venda.getIdVenda();

        if (venda.update() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_004),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListVendasFull.size() - 1; i++) {
            if (mListVendasFull.get(i).getIdVenda() == mListVendas.get(mItemSelected).getIdVenda()) {
                mListVendasFull.remove(i);
                break;
            }
        }

        mListVendas.remove(mItemSelected);

        if (mMes == venda.getMes() & mAno == venda.getAno()) {

            mListVendas.add(venda);
            mListVendasFull.add(venda);

            order();

            setSelectedByID(iID);
        } else {
            notifyItemRemoved(mItemSelected);
            setSelected(-1);
        }

        String sMessage = mContext.getString(R.string.v_012);
        sMessage = sMessage.replace("@1", venda.getIdentificacao());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    private void setSelectedByID(int id_venda) {

        int iPosition = -1;

        for (int i = 0; i <= mListVendas.size() - 1; i++) {
            if (mListVendas.get(i).getIdVenda() == id_venda) {
                iPosition = i;
                break;
            }
        }

        setSelected(iPosition);
    }

    public void remove() {

        if (mItemSelected == -1)
            return;

        tblVendas record = new tblVendas();
        record.setIdVenda(mListVendas.get(mItemSelected).getIdVenda());

        if (record.delete() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_006),
                    mContext.getResources().getString(R.string.n_005),
                    mContext.getResources().getString(R.string.o_002)
            );

            return;
        }

        for (int i = 0; i <= mListVendasFull.size() - 1; i++) {
            if (mListVendasFull.get(i).getIdVenda() == mListVendas.get(mItemSelected).getIdVenda()) {
                mListVendasFull.remove(i);
                break;
            }
        }

        String sMessage = mContext.getString(R.string.v_013);
        sMessage = sMessage.replace("@1", mListVendas.get(mItemSelected).getIdentificacao());

        mListVendas.remove(mItemSelected);
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

                    sBuilder.append(Funcoes.addCabecalho(mContext.getString(R.string.l_006)));
                    sBuilder.append(Funcoes.addTable(mContext, mContext.getString(R.string.l_006), "800px", 1));

                    sBuilder.append("<tr>");
                    sBuilder.append(Funcoes.addTitulo(mContext, "20%", "center", "middle", mContext.getString(R.string.i_014)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "25%", "center", "middle", mContext.getString(R.string.c_011)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "15%", "center", "middle", mContext.getString(R.string.d_016)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "15%", "center", "middle", mContext.getString(R.string.v_002)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "15%", "center", "middle", mContext.getString(R.string.v_014)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "10%", "center", "middle", mContext.getString(R.string.q_002)));
                    sBuilder.append("</tr>");

                    for (int i = 0; i <= mListVendas.size() - 1; i++) {
                        sBuilder.append("<tr>");
                        sBuilder.append(Funcoes.addDetalhe(mContext, mListVendas.get(i).getIdentificacao(), "left"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, mListVendas.get(i).getCalcNomeCliente(), "left"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, mListVendas.get(i).getDtVenda(), "center"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(mListVendas.get(i).getCalcVlrVenda()), "right"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(mListVendas.get(i).getCalcVlrRecebimento()), "right"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Integer.toString(mListVendas.get(i).getCalcQtdeItensVenda()), "right"));
                        sBuilder.append("</tr>");
                    }

                    Funcoes.addRodape();

                    if (tipo.matches("Relatorio")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setDataAndType(FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", Funcoes.saveFile(mContext, mContext.getString(R.string.v_015), sBuilder.toString())), "text/html");
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            intent.setDataAndType(Uri.fromFile(Funcoes.saveFile(mContext, mContext.getString(R.string.v_015), sBuilder.toString())), "text/html");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                        mContext.startActivity(intent);
                    }

                    if (tipo.matches("Compartilhar")) {

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.l_006));
                        intent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.s_010));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", Funcoes.saveFile(mContext, mContext.getString(R.string.v_015), sBuilder.toString())));
                        } else {
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(Funcoes.saveFile(mContext, mContext.getString(R.string.v_015), sBuilder.toString())));
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

                    sBuilder.append(Funcoes.addCabecalho(mContext.getString(R.string.d_014)));
                    sBuilder.append(Funcoes.addTable(mContext, mContext.getString(R.string.d_014), "600px", 1));

                    sBuilder.append("<tr>");
                    sBuilder.append(Funcoes.addTitulo(mContext, "1000%", "center", "middle", mContext.getString(R.string.i_030)));
                    sBuilder.append("</tr>");

                    sBuilder.append("<tr>");
                    sAux = mContext.getString(R.string.i_014) + mContext.getString(R.string._001) + " " + mListVendas.get(mItemSelected).getIdentificacao() + "<br>";
                    sAux += mContext.getString(R.string.c_011) + mContext.getString(R.string._001) + " " + mListVendas.get(mItemSelected).getCalcNomeCliente() + "<br>";
                    sAux += mContext.getString(R.string.d_016) + mContext.getString(R.string._001) + " " + mListVendas.get(mItemSelected).getDtVenda() + "<br>";
                    sAux += mContext.getString(R.string.v_002) + mContext.getString(R.string._001) + " " + Funcoes.decimalFormat.format(mListVendas.get(mItemSelected).getCalcVlrVenda()) + "<br>";
                    sAux += mContext.getString(R.string.v_014) + mContext.getString(R.string._001) + " " + Funcoes.decimalFormat.format(mListVendas.get(mItemSelected).getCalcVlrRecebimento()) + "<br>";
                    sAux += mContext.getString(R.string.q_002) + mContext.getString(R.string._001) + " " + Integer.toString(mListVendas.get(mItemSelected).getCalcQtdeItensVenda()) + "<br>";
                    sAux += mContext.getString(R.string.o_001) + mContext.getString(R.string._001) + " " + mListVendas.get(mItemSelected).getObservacoes();
                    sBuilder.append(Funcoes.addDetalhe(mContext, sAux, "left"));
                    sBuilder.append("</tr>");
                    sBuilder.append("</tbody></table>");

                    sBuilder.append("<br>");

                    sBuilder.append(Funcoes.addTable(mContext, mContext.getString(R.string.d_012), "600px", 5));

                    tblVendaItens recItens = new tblVendaItens();
                    List<tblVendaItens> lstVendaItens;
                    lstVendaItens = recItens.getList(mListVendas.get(mItemSelected).getIdVenda());

                    comVendaItens mComparatorVI = new comVendaItens(0);
                    Collections.sort(lstVendaItens, mComparatorVI);

                    sBuilder.append("<tr>");
                    sBuilder.append(Funcoes.addTitulo(mContext, "20%", "center", "middle", mContext.getString(R.string.p_006)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "15%", "center", "middle", mContext.getString(R.string.q_001)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "15%", "center", "middle", mContext.getString(R.string.v_006)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "15%", "center", "middle", mContext.getString(R.string.v_007)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "35%", "center", "middle", mContext.getString(R.string.o_001)));
                    sBuilder.append("</tr>");

                    for (int i = 0; i <= lstVendaItens.size() - 1; i++) {
                        sBuilder.append("<tr>");
                        sBuilder.append(Funcoes.addDetalhe(mContext, lstVendaItens.get(i).getCalcNomeProduto(), "left"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(lstVendaItens.get(i).getQtItens()), "right"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(lstVendaItens.get(i).getVlrUnitario()), "right"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(lstVendaItens.get(i).getCalcVlrTotal()), "right"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, lstVendaItens.get(i).getObservacoes(), "left"));
                        sBuilder.append("</tr>");
                    }

                    sBuilder.append("</tbody></table>");

                    sBuilder.append("<br>");

                    sBuilder.append(Funcoes.addTable(mContext, mContext.getString(R.string.d_017), "600px", 5));

                    tblVendaRecebimentos recRecebimento = new tblVendaRecebimentos();
                    List<tblVendaRecebimentos> lstVendaRecebimento;
                    lstVendaRecebimento = recRecebimento.getList(mListVendas.get(mItemSelected).getIdVenda());

                    comVendaRecebimentos mComparatorVR = new comVendaRecebimentos(0);
                    Collections.sort(lstVendaRecebimento, mComparatorVR);

                    sBuilder.append("<tr>");
                    sBuilder.append(Funcoes.addTitulo(mContext, "25%", "center", "middle", mContext.getString(R.string.d_015)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "25%", "center", "middle", mContext.getString(R.string.v_008)));
                    sBuilder.append(Funcoes.addTitulo(mContext, "50%", "center", "middle", mContext.getString(R.string.o_001)));
                    sBuilder.append("</tr>");

                    for (int i = 0; i <= lstVendaRecebimento.size() - 1; i++) {
                        sBuilder.append("<tr>");
                        sBuilder.append(Funcoes.addDetalhe(mContext, lstVendaRecebimento.get(i).getDtRecebimento(), "center"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, Funcoes.decimalFormat.format(lstVendaRecebimento.get(i).getVlrRecebimento()), "right"));
                        sBuilder.append(Funcoes.addDetalhe(mContext, lstVendaRecebimento.get(i).getObservacoes(), "left"));
                        sBuilder.append("</tr>");
                    }

                    Funcoes.addRodape();

                    if (tipo.matches("Relatorio")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setDataAndType(FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", Funcoes.saveFile(mContext, mContext.getString(R.string.v_015), sBuilder.toString())), "text/html");
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            intent.setDataAndType(Uri.fromFile(Funcoes.saveFile(mContext, mContext.getString(R.string.v_015), sBuilder.toString())), "text/html");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                        mContext.startActivity(intent);
                    }

                    if (tipo.matches("Compartilhar")) {

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.l_006));
                        intent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.s_010));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", Funcoes.saveFile(mContext, mContext.getString(R.string.v_015), sBuilder.toString())));
                        } else {
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(Funcoes.saveFile(mContext, mContext.getString(R.string.v_015), sBuilder.toString())));
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
        View v = mLayoutInflater.inflate(R.layout.list_vendas, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        myViewHolder.txtID.setText(String.valueOf(mListVendas.get(i).getIdVenda()));
        myViewHolder.txtIdentificacao.setText(mListVendas.get(i).getIdentificacao());

        myViewHolder.txtCliente.setText(mContext.getString(R.string.c_011) +
                mContext.getString(R.string._001) + " " +
                mListVendas.get(i).getCalcNomeCliente());

        myViewHolder.txtData.setText(mContext.getString(R.string.d_016) +
                mContext.getString(R.string._001) + " " +
                mListVendas.get(i).getDtVenda());

        myViewHolder.txtValor.setText(mContext.getString(R.string.v_016) +
                mContext.getString(R.string._001) + " " +
                Funcoes.decimalFormat.format(mListVendas.get(i).getCalcVlrVenda()));

        myViewHolder.txtVlrRecebimento.setText(mContext.getString(R.string.v_014) +
                mContext.getString(R.string._001) + " " +
                Funcoes.decimalFormat.format(mListVendas.get(i).getCalcVlrRecebimento()));

        myViewHolder.txtQtdeItens.setText(mContext.getString(R.string.q_002) +
                mContext.getString(R.string._001) + " " +
                Integer.toString(mListVendas.get(i).getCalcQtdeItensVenda()));

        myViewHolder.txtObservacoes.setText(mContext.getString(R.string.o_001) +
                mContext.getString(R.string._001) + " " +
                mListVendas.get(i).getObservacoes());

        if (mItemSelected == i)
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.primary_light));
        else
            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.icons));
    }

    @Override
    public int getItemCount() {
        return mListVendas.size();
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
                List<tblVendas> list = new ArrayList<>(mListVendasFull);
                results.count = list.size();
                results.values = list;
            } else {
                ArrayList<tblVendas> newValues = new ArrayList<>(mListVendasFull.size());

                for (int i = 0; i < mListVendasFull.size(); i++) {
                    if (mListVendasFull.get(i).getIdentificacao().toLowerCase().contains(constraintString.toLowerCase())) {
                        newValues.add(mListVendasFull.get(i));
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
                mListVendas = (ArrayList<tblVendas>) results.values;
            } else {
                mListVendas = new ArrayList<>();
            }

            notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtID;
        public TextView txtIdentificacao;
        public TextView txtCliente;
        public TextView txtData;
        public TextView txtValor;
        public TextView txtQtdeItens;
        public TextView txtObservacoes;
        public TextView txtVlrRecebimento;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtID = (TextView) itemView.findViewById(R.id.txtID);
            txtIdentificacao = (TextView) itemView.findViewById(R.id.txtIdentificacao);
            txtCliente = (TextView) itemView.findViewById(R.id.txtCliente);
            txtData = (TextView) itemView.findViewById(R.id.txtData);
            txtValor = (TextView) itemView.findViewById(R.id.txtValor);
            txtQtdeItens = (TextView) itemView.findViewById(R.id.txtQtdeItens);
            txtObservacoes = (TextView) itemView.findViewById(R.id.txtObservacoes);
            txtVlrRecebimento = (TextView) itemView.findViewById(R.id.txtValorRecebimento);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(getAdapterPosition());
                }
            });
        }
    }
}
