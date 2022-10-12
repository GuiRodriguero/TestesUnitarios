package br.com.gui.testes.builders;

import br.com.gui.testes.entidades.Filme;
import br.com.gui.testes.entidades.Locacao;
import br.com.gui.testes.entidades.Usuario;
import br.com.gui.testes.utils.DataUtils;

import java.util.Arrays;
import java.util.Date;

import static br.com.gui.testes.utils.DataUtils.obterDataComDiferencaDias;

public class LocacaoBuilder {

    private Locacao locacao;
    private LocacaoBuilder(){}

    public static LocacaoBuilder umaLocacao() {
        LocacaoBuilder builder = new LocacaoBuilder();
        inicializarDadosPadroes(builder);
        return builder;
    }

    public static void inicializarDadosPadroes(LocacaoBuilder builder) {
        builder.locacao = new Locacao();
        Locacao elemento = builder.locacao;


        elemento.setUsuario(UsuarioBuilder.umUsuario().getUsuario());
        elemento.setFilmes(Arrays.asList(FilmeBuilder.umFilme().getFilme()));
        elemento.setDataLocacao(new Date());
        elemento.setDataRetorno(obterDataComDiferencaDias(1));
        elemento.setValor(4.0);
    }

    public LocacaoBuilder comUsuario(Usuario param) {
        locacao.setUsuario(param);
        return this;
    }

    public LocacaoBuilder comListaFilmes(Filme... params) {
        locacao.setFilmes(Arrays.asList(params));
        return this;
    }

    public LocacaoBuilder comDataLocacao(Date param) {
        locacao.setDataLocacao(param);
        return this;
    }

    public LocacaoBuilder comDataRetorno(Date param) {
        locacao.setDataRetorno(param);
        return this;
    }

    public LocacaoBuilder comValor(Double param) {
        locacao.setValor(param);
        return this;
    }

    public LocacaoBuilder atrasada(){
        locacao.setDataLocacao(obterDataComDiferencaDias(-4));
        locacao.setDataRetorno(obterDataComDiferencaDias(-2));
        return this;
    }

    public Locacao getLocacao() {
        return locacao;
    }
}
