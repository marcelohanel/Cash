package mah.com.br.cash.Comparators;

import java.util.Comparator;

import mah.com.br.cash.DataBase.tblVendaRecebimentos;

public class comVendaRecebimentos implements Comparator<tblVendaRecebimentos> {

    private int mOrdem;

    public comVendaRecebimentos(int ordem) {
        this.mOrdem = ordem;
    }

    @Override
    public int compare(tblVendaRecebimentos lhs, tblVendaRecebimentos rhs) {

        int iRetorno = 0;

        if (mOrdem == 0) {
            if ((lhs.getCalcDtRecebimentoInv() < rhs.getCalcDtRecebimentoInv())) iRetorno = -1;
            if ((lhs.getCalcDtRecebimentoInv() > rhs.getCalcDtRecebimentoInv())) iRetorno = 1;
            if ((lhs.getCalcDtRecebimentoInv() == rhs.getCalcDtRecebimentoInv())) iRetorno = 0;
        }

        return iRetorno;
    }
}
