package mah.com.br.cash.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mah.com.br.cash.Adapters.adpVisaoListaModeloC;
import mah.com.br.cash.Comparators.comTempValores;
import mah.com.br.cash.DataBase.tblCompras;
import mah.com.br.cash.DataBase.tblVendas;
import mah.com.br.cash.Diversos.TempValores;
import mah.com.br.cash.R;

public class fraListaModeloC extends Fragment {

    public adpVisaoListaModeloC mAdapter;
    private String mMes;
    private String mAno;
    private String mTipo;
    private RecyclerView mRecyclerView;

    public fraListaModeloC() {
    }

    public static fraListaModeloC newInstance(String tipo, String mes, String ano) {

        fraListaModeloC fragment = new fraListaModeloC();

        Bundle args = new Bundle();
        args.putString("mes", mes);
        args.putString("ano", ano);
        args.putString("tipo", tipo);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMes = getArguments().getString("mes");
            mAno = getArguments().getString("ano");
            mTipo = getArguments().getString("tipo");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fra_lista_modelo_c, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvValores);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        leitura();
    }

    public void setMesAno(String mes, String ano) {
        this.mMes = mes;
        this.mAno = ano;
    }

    private void leitura() {

        TempValores recValor;

        tblVendas recVendas = new tblVendas();
        tblCompras recCompras = new tblCompras();

        comTempValores mComparator = new comTempValores(1);

        List<TempValores> mListRelatorio = new ArrayList<>();
        List<TempValores> mList = new ArrayList<>();
        List<TempValores> mListPagar = new ArrayList<>();
        List<TempValores> mListPago = new ArrayList<>();
        List<TempValores> mListReceber = new ArrayList<>();
        List<TempValores> mListRecebido = new ArrayList<>();

        if (mTipo.matches(getString(R.string.d_028))) {
            mListPagar = recCompras.getDiarioFornecedoresPagar(getActivity(), mMes, mAno);
            mListPago = recCompras.getDiarioFornecedoresPago(getActivity(), mMes, mAno);
            mListReceber = recVendas.getDiarioClientesReceber(getActivity(), mMes, mAno);
            mListRecebido = recVendas.getDiarioClientesRecebido(getActivity(), mMes, mAno);
        }

        for (int i = 0; i <= mListPagar.size() - 1; i++)
            mList.add(mListPagar.get(i));

        for (int i = 0; i <= mListPago.size() - 1; i++)
            mList.add(mListPago.get(i));

        for (int i = 0; i <= mListReceber.size() - 1; i++)
            mList.add(mListReceber.get(i));

        for (int i = 0; i <= mListRecebido.size() - 1; i++)
            mList.add(mListRecebido.get(i));

        Collections.sort(mList, mComparator);

        recValor = new TempValores();
        recValor.setCodigo(-1);
        mListRelatorio.add(recValor);

        for (int i = 0; i <= mList.size() - 1; i++) {

            recValor = new TempValores();

            recValor.setCodigo(mList.get(i).getCodigo());
            recValor.setNome(mList.get(i).getNome());
            recValor.setData(mList.get(i).getData());
            recValor.setValor(mList.get(i).getValor());

            mListRelatorio.add(recValor);
        }

        mAdapter = new adpVisaoListaModeloC(getActivity(), mListRelatorio);
        mRecyclerView.setAdapter(mAdapter);

    }
}
