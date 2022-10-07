package br.com.gui.testes.builders;

import br.com.gui.testes.entidades.Filme;

public class FilmeBuilder {

    private Filme filme;

    private FilmeBuilder() {
    }

    public static FilmeBuilder umFilme(){
        FilmeBuilder builder = new FilmeBuilder();
        builder.setFilme(new Filme(1, "Filme 1", 2, 4.0));
        return builder;
    }

    public static FilmeBuilder umFilmeSemEstoque(){
        FilmeBuilder builder = new FilmeBuilder();
        builder.setFilme(new Filme(1, "Filme 1", 0, 4.0));
        return builder;
    }

    public FilmeBuilder comValor(Double valor){
        filme.setPrecoLocacao(valor);
        return this;
    }

    public FilmeBuilder comNome(String nome){
        filme.setNome(nome);
        return this;
    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }
}
