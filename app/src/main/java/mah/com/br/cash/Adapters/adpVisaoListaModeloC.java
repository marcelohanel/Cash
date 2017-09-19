package mah.com.br.cash.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.Diversos.TempValores;
import mah.com.br.cash.R;

public class adpVisaoListaModeloC extends RecyclerView.Adapter<adpVisaoListaModeloC.MyViewHolder> {

    public List<TempValores> mListValores;
    public int mOrdem;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public adpVisaoListaModeloC(Context c, List<TempValores> v) {

        this.mContext = c;
        this.mOrdem = 0;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListValores = v;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mLayoutInflater.inflate(R.layout.list_lista_modelo_c, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        if (mListValores.get(i).getCodigo() == -1) {
            myViewHolder.txtData.setText(mContext.getString(R.string.d_029));
            myViewHolder.txtNome.setText(mContext.getString(R.string.n_002));
            myViewHolder.txtValor.setText(mContext.getString(R.string.v_009));

            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.primary_light));
        } else {
            myViewHolder.txtData.setText(mListValores.get(i).getData());

            if (mListValores.get(i).getCodigo() == 0) { // A Pagar
                myViewHolder.txtNome.setText(mContext.getString(R.string.a_025) + " " + mListValores.get(i).getNome());
                myViewHolder.txtValor.setText(Funcoes.decimalFormat.format(mListValores.get(i).getValor() * -1));
            } else if (mListValores.get(i).getCodigo() == 1) { // Pago
                myViewHolder.txtNome.setText(mContext.getString(R.string.p_025) + " " + mListValores.get(i).getNome());
                myViewHolder.txtValor.setText(Funcoes.decimalFormat.format(mListValores.get(i).getValor() * -1));
            } else if (mListValores.get(i).getCodigo() == 2) { // A Receber
                myViewHolder.txtNome.setText(mContext.getString(R.string.a_026) + " " + mListValores.get(i).getNome());
                myViewHolder.txtValor.setText(Funcoes.decimalFormat.format(mListValores.get(i).getValor()));
            } else if (mListValores.get(i).getCodigo() == 3) { // Recebido
                myViewHolder.txtNome.setText(mContext.getString(R.string.r_013) + " " + mListValores.get(i).getNome());
                myViewHolder.txtValor.setText(Funcoes.decimalFormat.format(mListValores.get(i).getValor()));
            }

            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.icons));
        }
    }

    @Override
    public int getItemCount() {
        return mListValores.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtData;
        public TextView txtNome;
        public TextView txtValor;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtData = (TextView) itemView.findViewById(R.id.txtData);
            txtNome = (TextView) itemView.findViewById(R.id.txtNome);
            txtValor = (TextView) itemView.findViewById(R.id.txtValor);

        }

    }
}
