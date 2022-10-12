package br.com.gui.testes.servicos;

import br.com.gui.testes.entidades.Usuario;

public interface EmailService {
    void notificarAtraso(Usuario usuario);
}
