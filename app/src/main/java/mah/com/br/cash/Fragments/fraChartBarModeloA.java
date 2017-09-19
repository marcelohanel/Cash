package mah.com.br.cash.Fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import mah.com.br.cash.DataBase.tblClientes;
import mah.com.br.cash.DataBase.tblFornecedores;
import mah.com.br.cash.Diversos.Funcoes;
import mah.com.br.cash.R;

public class fraChartBarModeloA extends Fragment {

    private String mMes;
    private String mAno;
    private String mTipo;
    private int mColor;
    private BarChart mChart;

    public fraChartBarModeloA() {
    }

    public static fraChartBarModeloA newInstance(String tipo, int color, String mes, String ano) {

        fraChartBarModeloA fragment = new fraChartBarModeloA();

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
        View rootView = inflater.inflate(R.layout.fra_chart_bar_modelo_a, container, false);
        mChart = (BarChart) rootView.findViewById(R.id.chart_1);
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

        String sTitulo_1 = "";
        String sTitulo_2 = "";
        double dVlrBarra_1 = 0.0;
        double dVlrBarra_2 = 0.0;
        double dVlrTotal;

        tblFornecedores recFornecedor = new tblFornecedores();
        tblClientes recCliente = new tblClientes();

        if (mTipo.matches(getString(R.string.c_017))) {

            dVlrBarra_1 = recFornecedor.getCalcCompras(getActivity(), mMes, mAno);
            dVlrBarra_2 = recCliente.getCalcVendas(getActivity(), mMes, mAno);

            sTitulo_1 = getString(R.string.c_010);
            sTitulo_2 = getString(R.string.v_005);

        } else if (mTipo.matches(getString(R.string.a_018))) {

            dVlrBarra_1 = recFornecedor.getCalcPagamentos(getActivity(), mMes, mAno);
            dVlrBarra_2 = recCliente.getCalcRecebimentos(getActivity(), mMes, mAno);

            sTitulo_1 = getString(R.string.a_021);
            sTitulo_2 = getString(R.string.a_022);

        } else if (mTipo.matches(getString(R.string.p_016))) {

            dVlrBarra_1 = recFornecedor.getCalcPago(getActivity(), mMes, mAno);
            dVlrBarra_2 = recCliente.getCalcRecebido(getActivity(), mMes, mAno);

            sTitulo_1 = getString(R.string.p_018);
            sTitulo_2 = getString(R.string.r_009);

        } else if (mTipo.matches(getString(R.string.a_019))) {

            dVlrBarra_1 = recFornecedor.getCalcPagamentos(getActivity(), mMes, mAno);
            dVlrBarra_2 = recFornecedor.getCalcPago(getActivity(), mMes, mAno);

            sTitulo_1 = getString(R.string.a_021);
            sTitulo_2 = getString(R.string.p_018);

        } else if (mTipo.matches(getString(R.string.a_020))) {

            dVlrBarra_1 = recCliente.getCalcRecebimentos(getActivity(), mMes, mAno);
            dVlrBarra_2 = recCliente.getCalcRecebido(getActivity(), mMes, mAno);

            sTitulo_1 = getString(R.string.a_022);
            sTitulo_2 = getString(R.string.r_009);
        }

        dVlrTotal = dVlrBarra_1 + dVlrBarra_2;

        mChart.setTouchEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setScaleXEnabled(false);
        mChart.setScaleYEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setHighlightEnabled(false);
        mChart.setHighlightPerDragEnabled(false);
        mChart.setHighlightIndicatorEnabled(false);
        mChart.setDragDecelerationEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setGridBackgroundColor(Color.WHITE);

        mChart.setDescription("");
        mChart.setNoDataText(getString(R.string.s_014));

        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setEnabled(false);

        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.getXAxis().setDrawAxisLine(false);
        mChart.getXAxis().setDrawGridLines(false);

        ArrayList<String> valuesX = new ArrayList<>();

        String sAux;
        double dVlrAux;

        sAux = sTitulo_1;
        if (mTipo.matches(getString(R.string.a_019)) || mTipo.matches(getString(R.string.a_020))) {
            sAux = sAux + " - 100.00" + getString(R.string._005);
        } else {
            dVlrAux = (dVlrTotal != 0) ? (dVlrBarra_1 * 100 / dVlrTotal) : 0;
            sAux = sAux + " - " + Funcoes.decimalFormat.format(dVlrAux) + getString(R.string._005);
        }
        valuesX.add(sAux);

        sAux = sTitulo_2;
        if (mTipo.matches(getString(R.string.a_019)) || mTipo.matches(getString(R.string.a_020))) {
            dVlrAux = (dVlrBarra_1 != 0) ? (dVlrBarra_2 * 100 / dVlrBarra_1) : 0;
            sAux = sAux + " - " + Funcoes.decimalFormat.format(dVlrAux) + getString(R.string._005);
        } else {
            dVlrAux = (dVlrTotal != 0) ? (dVlrBarra_2 * 100 / dVlrTotal) : 0;
            sAux = sAux + " - " + Funcoes.decimalFormat.format(dVlrAux) + getString(R.string._005);
        }
        valuesX.add(sAux);

        ArrayList<BarEntry> valuesY = new ArrayList<>();

        valuesY.add(new BarEntry(Float.valueOf(String.valueOf(dVlrBarra_1)), 0));
        valuesY.add(new BarEntry(Float.valueOf(String.valueOf(dVlrBarra_2)), 1));

        BarDataSet valuesChart = new BarDataSet(valuesY, "");

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

        BarData data = new BarData(valuesX, valuesChart);
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
