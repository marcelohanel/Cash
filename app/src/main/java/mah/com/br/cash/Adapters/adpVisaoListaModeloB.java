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

public class adpVisaoListaModeloB extends RecyclerView.Adapter<adpVisaoListaModeloB.MyViewHolder> {

    public List<TempValores> mListValores;
    public int mOrdem;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public adpVisaoListaModeloB(Context c, List<TempValores> v) {

        this.mContext = c;
        this.mOrdem = 0;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListValores = v;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mLayoutInflater.inflate(R.layout.list_lista_modelo_b, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        if (mListValores.get(i).getCodigo() == -1) {
            myViewHolder.txtID.setText("");
            myViewHolder.txtNome.setText(mContext.getString(R.string.n_002));
            myViewHolder.txtNome_1.setText("");
            myViewHolder.txtValor.setText(mContext.getString(R.string.q_003));
            myViewHolder.txtPercentual.setText(mContext.getString(R.string.p_019));

            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.primary_light));
        } else {
            myViewHolder.txtID.setText(String.valueOf(mListValores.get(i).getCodigo()));
            myViewHolder.txtNome.setText(mListValores.get(i).getNome());
            myViewHolder.txtNome_1.setText(mListValores.get(i).getNome_1());
            myViewHolder.txtValor.setText(Funcoes.decimalFormat.format(mListValores.get(i).getValor()));
            myViewHolder.txtPercentual.setText(Funcoes.decimalFormat.format(mListValores.get(i).getValor_1()) + mContext.getString(R.string._005));

            myViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.icons));
        }
    }

    @Override
    public int getItemCount() {
        return mListValores.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtID;
        public TextView txtNome;
        public TextView txtNome_1;
        public TextView txtValor;
        public TextView txtPercentual;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtID = (TextView) itemView.findViewById(R.id.txtID);
            txtNome = (TextView) itemView.findViewById(R.id.txtNome);
            txtNome_1 = (TextView) itemView.findViewById(R.id.txtNome_1);
            txtValor = (TextView) itemView.findViewById(R.id.txtValor);
            txtPercentual = (TextView) itemView.findViewById(R.id.txtPercentual);

        }

    }
}
