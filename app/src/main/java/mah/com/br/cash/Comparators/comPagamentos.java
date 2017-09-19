package mah.com.br.cash.Comparators;

import java.util.Comparator;

import mah.com.br.cash.DataBase.tblPagamentos;

public class comPagamentos implements Comparator<tblPagamentos> {

    private int mOrdem;

    public comPagamentos(int ordem) {
        this.mOrdem = ordem;
    }

    @Override
    public int compare(tblPagamentos lhs, tblPagamentos rhs) {

        int iRetorno = 0;

        if (mOrdem == 0) {
            iRetorno = lhs.getCalcNomeFornecedor().compareToIgnoreCase(rhs.getCalcNomeFornecedor());
        }

        if (mOrdem == 1) {
            if ((lhs.getDtPagamentoInv() < rhs.getDtPagamentoInv())) iRetorno = -1;
            if ((lhs.getDtPagamentoInv() > rhs.getDtPagamentoInv())) iRetorno = 1;
            if ((lhs.getDtPagamentoInv() == rhs.getDtPagamentoInv())) iRetorno = 0;
        }

        if (mOrdem == 2) {
            if ((lhs.getVlrPagamento() < rhs.getVlrPagamento())) iRetorno = -1;
            if ((lhs.getVlrPagamento() > rhs.getVlrPagamento())) iRetorno = 1;
            if ((lhs.getVlrPagamento() == rhs.getVlrPagamento())) iRetorno = 0;
        }

        return iRetorno;
    }
}

