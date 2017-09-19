package mah.com.br.cash.Comparators;

import java.util.Comparator;

import mah.com.br.cash.DataBase.tblCompraPagamentos;

public class comCompraPagamentos implements Comparator<tblCompraPagamentos> {

    private int mOrdem;

    public comCompraPagamentos(int ordem) {
        this.mOrdem = ordem;
    }

    @Override
    public int compare(tblCompraPagamentos lhs, tblCompraPagamentos rhs) {

        int iRetorno = 0;

        if (mOrdem == 0) {
            if ((lhs.getCalcDtPagamentoInv() < rhs.getCalcDtPagamentoInv())) iRetorno = -1;
            if ((lhs.getCalcDtPagamentoInv() > rhs.getCalcDtPagamentoInv())) iRetorno = 1;
            if ((lhs.getCalcDtPagamentoInv() == rhs.getCalcDtPagamentoInv())) iRetorno = 0;
        }

        return iRetorno;
    }
}
