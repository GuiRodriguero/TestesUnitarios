package br.com.gui.testes.dao;

import br.com.gui.testes.entidades.Locacao;

import java.util.List;

@Deprecated
public class LocacaoDAOFake implements LocacaoDAO{
    @Override
    public void salvar(Locacao locacao) {

    }

    @Override
    public List<Locacao> obterLocacoesPendentes() {
        return null;
    }
}
