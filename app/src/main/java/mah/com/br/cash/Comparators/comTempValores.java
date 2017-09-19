package mah.com.br.cash.Comparators;

import java.util.Comparator;

import mah.com.br.cash.Diversos.TempValores;

public class comTempValores implements Comparator<TempValores> {

    private int mOrdem;

    public comTempValores(int ordem) {
        this.mOrdem = ordem;
    }

    @Override
    public int compare(TempValores lhs, TempValores rhs) {

        int iRetorno = 0;

        if (mOrdem == 0) {
            if ((lhs.getValor() < rhs.getValor())) iRetorno = -1;
            if ((lhs.getValor() > rhs.getValor())) iRetorno = 1;
            if ((lhs.getValor() == rhs.getValor())) iRetorno = 0;
        }

        if (mOrdem == 1) {
            if ((lhs.getData_inv() < rhs.getData_inv())) iRetorno = -1;
            if ((lhs.getData_inv() > rhs.getData_inv())) iRetorno = 1;
            if ((lhs.getData_inv() == rhs.getData_inv())) iRetorno = 0;
        }


        return iRetorno;
    }
}

