package mah.com.br.cash.Comparators;

import java.util.Comparator;

import mah.com.br.cash.DataBase.tblCompraItens;

public class comCompraItens implements Comparator<tblCompraItens> {

    private int mOrdem;

    public comCompraItens(int ordem) {
        this.mOrdem = ordem;
    }

    @Override
    public int compare(tblCompraItens lhs, tblCompraItens rhs) {

        int iRetorno = 0;

        if (mOrdem == 0)
            iRetorno = lhs.getCalcNomeProduto().compareToIgnoreCase(rhs.getCalcNomeProduto());

        return iRetorno;
    }
}
