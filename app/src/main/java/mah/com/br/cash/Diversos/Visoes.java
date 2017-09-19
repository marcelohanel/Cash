package mah.com.br.cash.Diversos;

import android.app.Fragment;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import mah.com.br.cash.Fragments.fraChartBarModeloA;
import mah.com.br.cash.Fragments.fraChartPieModeloA;
import mah.com.br.cash.Fragments.fraListaModeloA;
import mah.com.br.cash.Fragments.fraListaModeloB;
import mah.com.br.cash.Fragments.fraListaModeloC;
import mah.com.br.cash.R;

/**
 * Created by CGI - Marcelo on 12/05/2015.
 */
public class Visoes {

    private final int iTamanho = 28;

    private String[] vTitulos = new String[iTamanho];
    private String mMes;
    private String mAno;
    private Context mContext;

    public Visoes(Context c) {

        this.mContext = c;

        vTitulos[0] = c.getString(R.string.d_028);
        vTitulos[1] = c.getString(R.string.c_017);
        vTitulos[2] = c.getString(R.string.a_018);
        vTitulos[3] = c.getString(R.string.p_016);
        vTitulos[4] = c.getString(R.string.a_019);
        vTitulos[5] = c.getString(R.string.a_020);
        vTitulos[6] = c.getString(R.string.m_005);
        vTitulos[7] = c.getString(R.string.m_006);
        vTitulos[8] = c.getString(R.string.m_007);
        vTitulos[9] = c.getString(R.string.v_021);
        vTitulos[10] = c.getString(R.string.a_016);
        vTitulos[11] = c.getString(R.string.r_010);
        vTitulos[12] = c.getString(R.string.s_016);
        vTitulos[13] = c.getString(R.string.m_008);
        vTitulos[14] = c.getString(R.string.m_009);
        vTitulos[15] = c.getString(R.string.m_010);
        vTitulos[16] = c.getString(R.string.c_018);
        vTitulos[17] = c.getString(R.string.a_017);
        vTitulos[18] = c.getString(R.string.p_020);
        vTitulos[19] = c.getString(R.string.s_015);
        vTitulos[20] = c.getString(R.string.p_021);
        vTitulos[21] = c.getString(R.string.p_022);
        vTitulos[22] = c.getString(R.string.c_019);
        vTitulos[23] = c.getString(R.string.v_023);
        vTitulos[24] = c.getString(R.string.p_023);
        vTitulos[25] = c.getString(R.string.p_024);
        vTitulos[26] = c.getString(R.string.c_020);
        vTitulos[27] = c.getString(R.string.v_024);
    }

    public String getTitulo(int indice) {
        try {
            return vTitulos[indice];
        } catch (Exception e) {
            return null;
        }
    }

    public void setMesAno(String mes, String ano) {
        this.mMes = mes;
        this.mAno = ano;
    }

    public Fragment getFragment(int indice) {

        switch (indice) {
            case 0:
                return fraListaModeloC.newInstance(vTitulos[indice], mMes, mAno);
            case 1:
                return fraChartBarModeloA.newInstance(vTitulos[indice], 1, mMes, mAno);
            case 2:
                return fraChartBarModeloA.newInstance(vTitulos[indice], 2, mMes, mAno);
            case 3:
                return fraChartBarModeloA.newInstance(vTitulos[indice], 3, mMes, mAno);
            case 4:
                return fraChartBarModeloA.newInstance(vTitulos[indice], 4, mMes, mAno);
            case 5:
                return fraChartBarModeloA.newInstance(vTitulos[indice], 5, mMes, mAno);
            case 6:
                return fraChartPieModeloA.newInstance(vTitulos[indice], 1, mMes, mAno);
            case 7:
                return fraChartPieModeloA.newInstance(vTitulos[indice], 2, mMes, mAno);
            case 8:
                return fraChartPieModeloA.newInstance(vTitulos[indice], 3, mMes, mAno);
            case 9:
                return fraListaModeloA.newInstance(vTitulos[indice], mMes, mAno);
            case 10:
                return fraListaModeloA.newInstance(vTitulos[indice], mMes, mAno);
            case 11:
                return fraListaModeloA.newInstance(vTitulos[indice], mMes, mAno);
            case 12:
                return fraListaModeloA.newInstance(vTitulos[indice], mMes, mAno);
            case 13:
                return fraChartPieModeloA.newInstance(vTitulos[indice], 4, mMes, mAno);
            case 14:
                return fraChartPieModeloA.newInstance(vTitulos[indice], 5, mMes, mAno);
            case 15:
                return fraChartPieModeloA.newInstance(vTitulos[indice], 1, mMes, mAno);
            case 16:
                return fraListaModeloA.newInstance(vTitulos[indice], mMes, mAno);
            case 17:
                return fraListaModeloA.newInstance(vTitulos[indice], mMes, mAno);
            case 18:
                return fraListaModeloA.newInstance(vTitulos[indice], mMes, mAno);
            case 19:
                return fraListaModeloA.newInstance(vTitulos[indice], mMes, mAno);
            case 20:
                return fraChartPieModeloA.newInstance(vTitulos[indice], 2, mMes, mAno);
            case 21:
                return fraChartPieModeloA.newInstance(vTitulos[indice], 3, mMes, mAno);
            case 22:
                return fraListaModeloA.newInstance(vTitulos[indice], mMes, mAno);
            case 23:
                return fraListaModeloA.newInstance(vTitulos[indice], mMes, mAno);
            case 24:
                return fraChartPieModeloA.newInstance(vTitulos[indice], 2, mMes, mAno);
            case 25:
                return fraChartPieModeloA.newInstance(vTitulos[indice], 3, mMes, mAno);
            case 26:
                return fraListaModeloB.newInstance(vTitulos[indice], mMes, mAno);
            case 27:
                return fraListaModeloB.newInstance(vTitulos[indice], mMes, mAno);

        }

        return null;
    }

    public List<String> getList() {

        List<String> mList = new ArrayList<>();

        for (int i = 0; i <= iTamanho - 1; i++) {
            mList.add(vTitulos[i]);
        }

        return mList;
    }
}
