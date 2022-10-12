package br.com.gui.testes.builders;

import br.com.gui.testes.entidades.Usuario;

public class UsuarioBuilder {

    private Usuario usuario;

    private UsuarioBuilder() {
    }

    public static UsuarioBuilder umUsuario(){
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.setUsuario(new Usuario());
        builder.getUsuario().setNome("Usuario 1");
        return builder;
    }

    public UsuarioBuilder comNome(String nome){
        usuario.setNome(nome);
        return this;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
