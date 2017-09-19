package mah.com.br.cash.Comparators;

import java.util.Comparator;

import mah.com.br.cash.DataBase.tblVendaItens;

public class comVendaItens implements Comparator<tblVendaItens> {

    private int mOrdem;

    public comVendaItens(int ordem) {
        this.mOrdem = ordem;
    }

    @Override
    public int compare(tblVendaItens lhs, tblVendaItens rhs) {

        int iRetorno = 0;

        if (mOrdem == 0)
            iRetorno = lhs.getCalcNomeProduto().compareToIgnoreCase(rhs.getCalcNomeProduto());

        return iRetorno;
    }
}
