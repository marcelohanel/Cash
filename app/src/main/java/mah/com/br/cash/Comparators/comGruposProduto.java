package mah.com.br.cash.Comparators;

import java.util.Comparator;

import mah.com.br.cash.DataBase.tblGruposProduto;

public class comGruposProduto implements Comparator<tblGruposProduto> {

    private int mOrdem;

    public comGruposProduto(int ordem) {
        this.mOrdem = ordem;
    }

    @Override
    public int compare(tblGruposProduto lhs, tblGruposProduto rhs) {

        int iRetorno = 0;

        if (mOrdem == 0) {
            iRetorno = lhs.getNome().compareToIgnoreCase(rhs.getNome());
        }

        return iRetorno;
    }
}
