package mah.com.br.cash.Fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mah.com.br.cash.Comparators.comTempValores;
import mah.com.br.cash.DataBase.tblCompras;
import mah.com.br.cash.DataBase.tblVendas;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.Diversos.TempValores;
import mah.com.br.cash.R;

public class fraChartPieModeloA extends Fragment {

    private String mMes;
    private String mAno;
    private String mTipo;
    private int mColor;
    private PieChart mChart;

    public fraChartPieModeloA() {
    }

    public static fraChartPieModeloA newInstance(String tipo, int color, String mes, String ano) {

        fraChartPieModeloA fragment = new fraChartPieModeloA();

        Bundle args = new Bundle();
        args.putString("mes", mes);
        args.putString("ano", ano);
        args.putString("tipo", tipo);
        args.putInt("color", color);

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
            mColor = getArguments().getInt("color");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_chart_pie_modelo_a, container, false);
        mChart = (PieChart) rootView.findViewById(R.id.chart_1);
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

        tblVendas recVendas = new tblVendas();
        tblCompras recCompras = new tblCompras();

        double dVlrTotal = 0.0;
        double dVlrOutros = 0.0;
        int iTamanho = 4;
        String sNome_1 = "";

        mChart.setTouchEnabled(false);
        mChart.setHighlightEnabled(false);
        mChart.setDragDecelerationEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.getLegend().setEnabled(false);

        mChart.setDescription("");
        mChart.setNoDataText(getString(R.string.s_014));

        List<TempValores> mList = new ArrayList<>();

        if (mTipo.matches(getString(R.string.m_005))) {
            mList = recVendas.getMaioresClientesVendas(getActivity(), mMes, mAno);
            sNome_1 = getString(R.string.d_023);
        } else if (mTipo.matches(getString(R.string.m_006))) {
            mList = recVendas.getMaioresClientesReceber(getActivity(), mMes, mAno);
            sNome_1 = getString(R.string.d_023);
        } else if (mTipo.matches(getString(R.string.m_007))) {
            mList = recVendas.getMaioresClientesRecebido(getActivity(), mMes, mAno);
            sNome_1 = getString(R.string.d_023);
        } else if (mTipo.matches(getString(R.string.m_008))) {
            mList = recCompras.getMaioresFornecedoresCompras(getActivity(), mMes, mAno);
            sNome_1 = getString(R.string.d_024);
        } else if (mTipo.matches(getString(R.string.m_009))) {
            mList = recCompras.getMaioresFornecedoresPagar(getActivity(), mMes, mAno);
            sNome_1 = getString(R.string.d_024);
        } else if (mTipo.matches(getString(R.string.m_010))) {
            mList = recCompras.getMaioresFornecedoresPago(getActivity(), mMes, mAno);
            sNome_1 = getString(R.string.d_024);
        } else if (mTipo.matches(getString(R.string.p_021))) {
            mList = recCompras.getMaioresProdutosCompras(getActivity(), mMes, mAno, "valor");
            sNome_1 = getString(R.string.d_025);
        } else if (mTipo.matches(getString(R.string.p_022))) {
            mList = recVendas.getMaioresProdutosVendas(getActivity(), mMes, mAno, "valor");
            sNome_1 = getString(R.string.d_025);
        } else if (mTipo.matches(getString(R.string.p_023))) {
            mList = recCompras.getMaioresProdutosCompras(getActivity(), mMes, mAno, "qtde");
            sNome_1 = getString(R.string.d_025);
        } else if (mTipo.matches(getString(R.string.p_024))) {
            mList = recVendas.getMaioresProdutosVendas(getActivity(), mMes, mAno, "qtde");
            sNome_1 = getString(R.string.d_025);
        }

        if (mList == null)
            return;

        Collections.sort(mList, new comTempValores(0));
        Collections.reverse(mList);

        ArrayList<String> valuesX = new ArrayList<>();
        ArrayList<Entry> valuesY = new ArrayList<>();

        if (mList.size() <= iTamanho)
            iTamanho = mList.size();

        for (int i = 0; i <= mList.size() - 1; i++) {
            dVlrTotal += Float.valueOf(String.valueOf(mList.get(i).getValor()));
        }

        for (int i = 0; i <= iTamanho - 1; i++) {

            double dVlrAux = (dVlrTotal != 0) ? (mList.get(i).getValor() * 100 / dVlrTotal) : 0;
            String sAux = mList.get(i).getNome() + " - " + Funcoes.decimalFormat.format(dVlrAux) + getString(R.string._005);

            valuesX.add(sAux);
            valuesY.add(new BarEntry(Float.valueOf(String.valueOf(mList.get(i).getValor())), i));

            dVlrOutros += Float.valueOf(String.valueOf(mList.get(i).getValor()));
        }

        dVlrOutros = dVlrTotal - dVlrOutros;

        if (dVlrOutros != 0) {

            double dVlrAux = (dVlrTotal != 0) ? (dVlrOutros * 100 / dVlrTotal) : 0;
            String sAux = sNome_1 + " - " + Funcoes.decimalFormat.format(dVlrAux) + getString(R.string._005);

            valuesX.add(sAux);
            valuesY.add(new BarEntry(Float.valueOf(String.valueOf(dVlrOutros)), iTamanho));
        }

        PieDataSet valuesChart = new PieDataSet(valuesY, "");

        switch (mColor) {
            case 1:
                valuesChart.setColors(ColorTemplate.LIBERTY_COLORS);
                break;
            case 2:
                valuesChart.setColors(ColorTemplate.JOYFUL_COLORS);
                break;
            case 3:
                valuesChart.setColors(ColorTemplate.PASTEL_COLORS);
                break;
            case 4:
                valuesChart.setColors(ColorTemplate.COLORFUL_COLORS);
                break;
            case 5:
                valuesChart.setColors(ColorTemplate.VORDIPLOM_COLORS);
                break;
        }

        valuesChart.setAxisDependency(YAxis.AxisDependency.LEFT);
        valuesChart.setValueFormatter(new MyValueFormatter());
        valuesChart.setValueTextSize(10f);

        PieData data = new PieData(valuesX, valuesChart);
        mChart.setData(data);

        mChart.invalidate();
    }

    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = Funcoes.decimalFormat;
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value);
        }
    }
}
