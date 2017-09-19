package mah.com.br.cash.Comparators;

import java.util.Comparator;

import mah.com.br.cash.DataBase.tblCompras;

public class comCompras implements Comparator<tblCompras> {

    private int mOrdem;

    public comCompras(int ordem) {
        this.mOrdem = ordem;
    }

    @Override
    public int compare(tblCompras lhs, tblCompras rhs) {

        int iRetorno = 0;

        if (mOrdem == 0) {
            iRetorno = lhs.getIdentificacao().compareToIgnoreCase(rhs.getIdentificacao());
        }

        if (mOrdem == 1) {
            iRetorno = lhs.getCalcNomeFornecedor().compareToIgnoreCase(rhs.getCalcNomeFornecedor());
        }

        if (mOrdem == 2) {
            if ((lhs.getDtCompraInv() < rhs.getDtCompraInv())) iRetorno = -1;
            if ((lhs.getDtCompraInv() > rhs.getDtCompraInv())) iRetorno = 1;
            if ((lhs.getDtCompraInv() == rhs.getDtCompraInv())) iRetorno = 0;
        }

        if (mOrdem == 3) {
            if ((lhs.getCalcVlrCompra() < rhs.getCalcVlrCompra())) iRetorno = -1;
            if ((lhs.getCalcVlrCompra() > rhs.getCalcVlrCompra())) iRetorno = 1;
            if ((lhs.getCalcVlrCompra() == rhs.getCalcVlrCompra())) iRetorno = 0;
        }

        return iRetorno;
    }
}

