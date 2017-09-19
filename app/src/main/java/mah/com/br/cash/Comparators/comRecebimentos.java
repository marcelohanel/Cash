package mah.com.br.cash.Comparators;

import java.util.Comparator;

import mah.com.br.cash.DataBase.tblRecebimentos;

public class comRecebimentos implements Comparator<tblRecebimentos> {

    private int mOrdem;

    public comRecebimentos(int ordem) {
        this.mOrdem = ordem;
    }

    @Override
    public int compare(tblRecebimentos lhs, tblRecebimentos rhs) {

        int iRetorno = 0;

        if (mOrdem == 0) {
            iRetorno = lhs.getCalcNomeCliente().compareToIgnoreCase(rhs.getCalcNomeCliente());
        }

        if (mOrdem == 1) {
            if ((lhs.getDtRecebimentoInv() < rhs.getDtRecebimentoInv())) iRetorno = -1;
            if ((lhs.getDtRecebimentoInv() > rhs.getDtRecebimentoInv())) iRetorno = 1;
            if ((lhs.getDtRecebimentoInv() == rhs.getDtRecebimentoInv())) iRetorno = 0;
        }

        if (mOrdem == 2) {
            if ((lhs.getVlrRecebimento() < rhs.getVlrRecebimento())) iRetorno = -1;
            if ((lhs.getVlrRecebimento() > rhs.getVlrRecebimento())) iRetorno = 1;
            if ((lhs.getVlrRecebimento() == rhs.getVlrRecebimento())) iRetorno = 0;
        }

        return iRetorno;
    }
}

