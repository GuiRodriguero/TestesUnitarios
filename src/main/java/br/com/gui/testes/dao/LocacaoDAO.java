package br.com.gui.testes.dao;

import br.com.gui.testes.entidades.Locacao;

import java.util.List;

public interface LocacaoDAO {
    void salvar(Locacao locacao);

    List<Locacao> obterLocacoesPendentes();
}
