package mah.com.br.cash.Comparators;

import java.util.Comparator;

import mah.com.br.cash.DataBase.tblVendas;

public class comVendas implements Comparator<tblVendas> {

    private int mOrdem;

    public comVendas(int ordem) {
        this.mOrdem = ordem;
    }

    @Override
    public int compare(tblVendas lhs, tblVendas rhs) {

        int iRetorno = 0;

        if (mOrdem == 0) {
            iRetorno = lhs.getIdentificacao().compareToIgnoreCase(rhs.getIdentificacao());
        }

        if (mOrdem == 1) {
            iRetorno = lhs.getCalcNomeCliente().compareToIgnoreCase(rhs.getCalcNomeCliente());
        }

        if (mOrdem == 2) {
            if ((lhs.getDtVendaInv() < rhs.getDtVendaInv())) iRetorno = -1;
            if ((lhs.getDtVendaInv() > rhs.getDtVendaInv())) iRetorno = 1;
            if ((lhs.getDtVendaInv() == rhs.getDtVendaInv())) iRetorno = 0;
        }

        if (mOrdem == 3) {
            if ((lhs.getCalcVlrVenda() < rhs.getCalcVlrVenda())) iRetorno = -1;
            if ((lhs.getCalcVlrVenda() > rhs.getCalcVlrVenda())) iRetorno = 1;
            if ((lhs.getCalcVlrVenda() == rhs.getCalcVlrVenda())) iRetorno = 0;
        }

        return iRetorno;
    }
}

