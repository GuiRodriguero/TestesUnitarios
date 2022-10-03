package br.com.gui.testes.servicos;

import java.util.Date;
import java.util.List;

import br.com.gui.testes.entidades.Filme;
import br.com.gui.testes.entidades.Locacao;
import br.com.gui.testes.entidades.Usuario;
import br.com.gui.testes.exceptions.FilmeSemEstoqueException;
import br.com.gui.testes.exceptions.LocadoraException;
import br.com.gui.testes.utils.DataUtils;
import org.apache.commons.collections4.CollectionUtils;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws LocadoraException, FilmeSemEstoqueException {
		if(usuario == null){
			throw new LocadoraException("Usuário vazio");
		}
		if(isEmpty(filmes)){
			throw new LocadoraException("Filme vazio");
		}
		if(filmes.stream().anyMatch(f -> f.getEstoque() == 0)){
			throw new FilmeSemEstoqueException("Filme sem estoque");
		}

		Locacao locacao = new Locacao();
		locacao.setFilme(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		setDesconto(filmes);
		locacao.setValor(filmes.stream().mapToDouble(Filme::getPrecoLocacao).sum());
		
		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

	private void setDesconto(List<Filme> filmes) {
		for (Filme filme : filmes) {
			if(filme.getId() == 3){
			 	filme.setPrecoLocacao(filme.getPrecoLocacao() - filme.getPrecoLocacao() * 0.25);
			}
			if(filme.getId() == 4){
				filme.setPrecoLocacao(filme.getPrecoLocacao() - filme.getPrecoLocacao() * 0.50);
			}
			if(filme.getId() == 5){
				filme.setPrecoLocacao(filme.getPrecoLocacao() - filme.getPrecoLocacao() * 0.75);
			}
			if(filme.getId() == 6){
				filme.setPrecoLocacao(0.0);
			}
		}
	}
}