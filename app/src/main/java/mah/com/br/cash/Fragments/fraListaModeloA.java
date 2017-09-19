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

import mah.com.br.cash.Adapters.adpVisaoListaModeloA;
import mah.com.br.cash.Comparators.comTempValores;
import mah.com.br.cash.DataBase.tblCompras;
import mah.com.br.cash.DataBase.tblVendas;
import mah.com.br.cash.Diversos.TempValores;
import mah.com.br.cash.R;

public class fraListaModeloA extends Fragment {

    public adpVisaoListaModeloA mAdapter;
    private String mMes;
    private String mAno;
    private String mTipo;
    private RecyclerView mRecyclerView;

    public fraListaModeloA() {
    }

    public static fraListaModeloA newInstance(String tipo, String mes, String ano) {

        fraListaModeloA fragment = new fraListaModeloA();

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

        View rootView = inflater.inflate(R.layout.fra_lista_modelo_a, container, false);

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

        double dVlrTotal = 0.0;
        double dVlrAux;

        TempValores recValor;

        tblVendas recVendas = new tblVendas();
        tblCompras recCompras = new tblCompras();

        comTempValores mComparator = new comTempValores(0);
        List<TempValores> mListRelatorio = new ArrayList<>();
        List<TempValores> mList = new ArrayList<>();

        if (mTipo.matches(getString(R.string.v_021)))
            mList = recVendas.getMaioresClientesVendas(getActivity(), mMes, mAno);
        else if (mTipo.matches(getString(R.string.a_016)))
            mList = recVendas.getMaioresClientesReceber(getActivity(), mMes, mAno);
        else if (mTipo.matches(getString(R.string.r_010)))
            mList = recVendas.getMaioresClientesRecebido(getActivity(), mMes, mAno);
        else if (mTipo.matches(getString(R.string.c_018)))
            mList = recCompras.getMaioresFornecedoresCompras(getActivity(), mMes, mAno);
        else if (mTipo.matches(getString(R.string.a_017)))
            mList = recCompras.getMaioresFornecedoresPagar(getActivity(), mMes, mAno);
        else if (mTipo.matches(getString(R.string.p_020)))
            mList = recCompras.getMaioresFornecedoresPago(getActivity(), mMes, mAno);
        else if (mTipo.matches(getString(R.string.s_015)))
            mList = recCompras.getSaldoPagarFornecedores(getActivity(), mMes, mAno);
        else if (mTipo.matches(getString(R.string.s_016)))
            mList = recVendas.getSaldoReceberClientes(getActivity(), mMes, mAno);
        else if (mTipo.matches(getString(R.string.c_019)))
            mList = recCompras.getMaioresProdutosCompras(getActivity(), mMes, mAno, "valor");
        else if (mTipo.matches(getString(R.string.v_023)))
            mList = recVendas.getMaioresProdutosVendas(getActivity(), mMes, mAno, "valor");
        else if (mTipo.matches(getString(R.string.c_020)))
            mList = recCompras.getMaioresProdutosCompras(getActivity(), mMes, mAno, "qtde");
        else if (mTipo.matches(getString(R.string.v_024)))
            mList = recVendas.getMaioresProdutosVendas(getActivity(), mMes, mAno, "qtde");

        Collections.sort(mList, mComparator);
        Collections.reverse(mList);

        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mList.get(i).getValor() > 0) {
                dVlrTotal += mList.get(i).getValor();
            }
        }

        recValor = new TempValores();
        recValor.setCodigo(-1);

        mListRelatorio.add(recValor);

        for (int i = 0; i <= mList.size() - 1; i++) {

            if (mList.get(i).getValor() >= 0) {
                dVlrAux = (dVlrTotal != 0) ? (mList.get(i).getValor() * 100 / dVlrTotal) : 0;
            } else {
                dVlrAux = 0;
            }

            recValor = new TempValores();

            recValor.setCodigo(mList.get(i).getCodigo());
            recValor.setNome(mList.get(i).getNome());
            recValor.setValor(mList.get(i).getValor());
            recValor.setValor_1(dVlrAux);

            mListRelatorio.add(recValor);
        }

        mAdapter = new adpVisaoListaModeloA(getActivity(), mListRelatorio);
        mRecyclerView.setAdapter(mAdapter);
    }
}
