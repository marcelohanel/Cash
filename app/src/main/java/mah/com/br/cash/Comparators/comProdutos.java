package mah.com.br.cash.Comparators;

import java.util.Comparator;

import mah.com.br.cash.DataBase.tblProdutos;

public class comProdutos implements Comparator<tblProdutos> {

    private int mOrdem;

    public comProdutos(int ordem) {
        this.mOrdem = ordem;
    }

    @Override
    public int compare(tblProdutos lhs, tblProdutos rhs) {

        int iRetorno = 0;

        if (mOrdem == 0)
            iRetorno = lhs.getNome().compareToIgnoreCase(rhs.getNome());

        if (mOrdem == 1)
            iRetorno = lhs.getCalcNomeGrupoProduto().compareToIgnoreCase(rhs.getCalcNomeGrupoProduto());

        if (mOrdem == 2)
            iRetorno = lhs.getReferencia().compareToIgnoreCase(rhs.getReferencia());

        if (mOrdem == 3)
            iRetorno = lhs.getCalcFornecedor().compareToIgnoreCase(rhs.getCalcFornecedor());

        if (mOrdem == 4) {
            if ((lhs.getVlrVenda() < rhs.getVlrVenda())) iRetorno = -1;
            if ((lhs.getVlrVenda() > rhs.getVlrVenda())) iRetorno = 1;
            if ((lhs.getVlrVenda() == rhs.getVlrVenda())) iRetorno = 0;
        }

        if (mOrdem == 5) {
            if ((lhs.getCalcVlrCompra() < rhs.getCalcVlrCompra())) iRetorno = -1;
            if ((lhs.getCalcVlrCompra() > rhs.getCalcVlrCompra())) iRetorno = 1;
            if ((lhs.getCalcVlrCompra() == rhs.getCalcVlrCompra())) iRetorno = 0;
        }

        if (mOrdem == 6) {
            if ((lhs.getCalcEstoque() < rhs.getCalcEstoque())) iRetorno = -1;
            if ((lhs.getCalcEstoque() > rhs.getCalcEstoque())) iRetorno = 1;
            if ((lhs.getCalcEstoque() == rhs.getCalcEstoque())) iRetorno = 0;
        }

        if (mOrdem == 7) {
            if ((lhs.getCalcMargem() < rhs.getCalcMargem())) iRetorno = -1;
            if ((lhs.getCalcMargem() > rhs.getCalcMargem())) iRetorno = 1;
            if ((lhs.getCalcMargem() == rhs.getCalcMargem())) iRetorno = 0;
        }
        return iRetorno;
    }
}
