package br.com.gui.testes.servicos;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.gui.testes.dao.LocacaoDAO;
import br.com.gui.testes.entidades.Filme;
import br.com.gui.testes.entidades.Locacao;
import br.com.gui.testes.entidades.Usuario;
import br.com.gui.testes.exceptions.FilmeSemEstoqueException;
import br.com.gui.testes.exceptions.LocadoraException;
import br.com.gui.testes.utils.DataUtils;

import static br.com.gui.testes.utils.DataUtils.adicionarDias;
import static br.com.gui.testes.utils.DataUtils.verificarDiaSemana;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

public class LocacaoService {

	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;

	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws LocadoraException, FilmeSemEstoqueException {
		if(usuario == null){
			throw new LocadoraException("Usuario vazio");
		}
		if(isEmpty(filmes)){
			throw new LocadoraException("Filme vazio");
		}
		if(filmes.stream().anyMatch(f -> f.getEstoque() == 0)){
			throw new FilmeSemEstoqueException("Filme sem estoque");
		}

		boolean isUsuarioNegativado;
		try {
			isUsuarioNegativado = spcService.possuiNegativacao(usuario);
		} catch (Exception e) {
			throw new LocadoraException("SPC fora do ar");
		}
		if(isUsuarioNegativado){
			throw new LocadoraException("Usuario negativado");
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		setDesconto(filmes);
		locacao.setValor(filmes.stream().mapToDouble(Filme::getPrecoLocacao).sum());
		
		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
		if(verificarDiaSemana(dataEntrega, Calendar.SUNDAY)){
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...
		dao.salvar(locacao);

		return locacao;
	}

	/**
	 * "Manda email" para locações cujas datas de retorno sejam anteriores à data atual
	 */
	public void notificarAtrasos(){
		dao.obterLocacoesPendentes().stream()
				.filter(locacao -> locacao.getDataRetorno().before(new Date()))
				.forEach(l -> emailService.notificarAtraso(l.getUsuario()));
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