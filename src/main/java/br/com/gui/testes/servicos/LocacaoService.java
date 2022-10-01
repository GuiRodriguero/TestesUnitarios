package br.com.gui.testes.servicos;

import java.util.Date;

import br.com.gui.testes.entidades.Filme;
import br.com.gui.testes.entidades.Locacao;
import br.com.gui.testes.entidades.Usuario;
import br.com.gui.testes.exceptions.FilmeSemEstoqueException;
import br.com.gui.testes.exceptions.LocadoraException;
import br.com.gui.testes.utils.DataUtils;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, Filme filme) throws LocadoraException, FilmeSemEstoqueException {
		if(usuario == null){
			throw new LocadoraException("Usuário vazio");
		}
		if(filme == null){
			throw new LocadoraException("Filme vazio");
		}
		if(filme.getEstoque() == 0){
			throw new FilmeSemEstoqueException("Filme sem estoque");
		}


		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());
		
		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}
}