package mah.com.br.cash.Comparators;

import java.util.Comparator;

import mah.com.br.cash.DataBase.tblClientes;

public class comClientes implements Comparator<tblClientes> {

    private int mOrdem;

    public comClientes(int ordem) {
        this.mOrdem = ordem;
    }

    @Override
    public int compare(tblClientes lhs, tblClientes rhs) {

        int iRetorno = 0;

        if (mOrdem == 0) {
            iRetorno = lhs.getNome().compareToIgnoreCase(rhs.getNome());
        }

        if (mOrdem == 1) {
            iRetorno = lhs.getCalcSaldo().compareTo(rhs.getCalcSaldo());
        }

        if (mOrdem == 2) {
            if ((lhs.getCalcDtCadastroInv() < rhs.getCalcDtCadastroInv())) iRetorno = -1;
            if ((lhs.getCalcDtCadastroInv() > rhs.getCalcDtCadastroInv())) iRetorno = 1;
            if ((lhs.getCalcDtCadastroInv() == rhs.getCalcDtCadastroInv())) iRetorno = 0;
        }

        return iRetorno;
    }
}

