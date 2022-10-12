package br.com.gui.testes.servicos;

import br.com.gui.testes.builders.FilmeBuilder;
import br.com.gui.testes.builders.LocacaoBuilder;
import br.com.gui.testes.builders.UsuarioBuilder;
import br.com.gui.testes.dao.LocacaoDAO;
import br.com.gui.testes.entidades.Filme;
import br.com.gui.testes.entidades.Locacao;
import br.com.gui.testes.entidades.Usuario;
import br.com.gui.testes.exceptions.FilmeSemEstoqueException;
import br.com.gui.testes.exceptions.LocadoraException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.com.gui.testes.builders.LocacaoBuilder.umaLocacao;
import static br.com.gui.testes.matchers.MyMatchers.*;
import static br.com.gui.testes.utils.DataUtils.*;
import static br.com.gui.testes.utils.ListUtils.createFilmeListWithId;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

public class LocacaoServiceTest {

	private LocacaoService service;
	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;
	
	@Rule
	public ErrorCollector error = new ErrorCollector(); //Iremos mudar de assertThat para checkThat

	@Rule
	public ExpectedException exception = ExpectedException.none();

	/**
	 * É executado antes de cada teste
	 */
	@Before
	public void setup(){
		service = new LocacaoService();
		dao = Mockito.mock(LocacaoDAO.class);
		service.setLocacaoDAO(dao);
		spcService = Mockito.mock(SPCService.class);
		service.setSpcService(spcService);
		emailService = Mockito.mock(EmailService.class);
		service.setEmailService(emailService);
	}

	/**
	 * Existe um debate sobre isso pois se fossemos separar nosso teste em 3 (1 para cada assertiva),
	 * teríamos o mesmo cenário. Alguns dizem que não precisamos separar porque o teste nunca irá passar
	 * estando com erros, então de um jeito ou de outro, você encontraria o erro e o corrigiria.
	 * Já outros preferem separar por conta da rastreabilidade. Outro problema que pode ser levantado sobre tratar
	 * tudo em um teste só, é que, em caso de falhas, ele sempre irá falhar na primeira assertiva, então não saberemos
	 * como as demais estão a menos que a primeira seja corrigida (para se precaver disso, podemos usar @Rule).
	 */
	@Test
	public void testeLocacaoRule() throws Exception{
		assumeFalse(verificarDiaSemana(new Date(), Calendar.SATURDAY));

		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().getUsuario();
		Filme filme = FilmeBuilder.umFilme().comNome("Filme 1").comValor(5.0).getFilme();
		Filme filme2 = FilmeBuilder.umFilme().comNome("Filme 2").comValor(14.0).getFilme();
		List<Filme> filmes = new ArrayList<>();
		filmes.add(filme);
		filmes.add(filme2);

		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		//Em caso de falha, irá mostrar todos com falha
		error.checkThat(locacao.getValor(), is(equalTo(19.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDeDias(1));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
	}

	@Test
	public void testeLocacao() throws Exception {
		assumeFalse(verificarDiaSemana(new Date(), Calendar.SATURDAY));

		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().getUsuario();
		Filme filme = new Filme("Filme 1", 2, 5.0);
		Filme filme2 = new Filme("Filme 2", 2, 5.0);
		List<Filme> filmes = new ArrayList<>();
		filmes.add(filme);
		filmes.add(filme2);

		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		//verificacao
		//Em caso de falha, irá parar no primeiro que tiver falha
		assertThat(locacao.getValor(), is(equalTo(10.0)));
		assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
	}

	//****************************************************************************************************************//

	//Tratando Exceções

	/**
	 * Teste para verificar se a exceção está sendo lançada corretamente.
	 * Esse modo funciona bem quando sabemos que a só terá uma exceção possível de ser lançada (que é a que colocamos na anotação)
	 */
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		Usuario usuario = UsuarioBuilder.umUsuario().getUsuario();
		Filme filme = FilmeBuilder.umFilmeSemEstoque().getFilme();

		service.alugarFilme(usuario, asList(filme));
	}

	/**
	 * Teste para verificar se a exceção está sendo lançada corretamente (outra maneira)
	 * Fazendo desse modo, podemos ter mais controle sobre o que queremos verificar na exceção
	 */
	@Test
	public void naoDeveAlugarFilmeSemEstoque2() {
		Usuario usuario = UsuarioBuilder.umUsuario().getUsuario();
		Filme filme = FilmeBuilder.umFilmeSemEstoque().getFilme();
		Filme filme2 = FilmeBuilder.umFilme().comNome("Filme 1").comValor(5.0).getFilme();
		Filme filme3 = FilmeBuilder.umFilmeSemEstoque().getFilme();
		List<Filme> filmes = new ArrayList<>();
		filmes.add(filme);
		filmes.add(filme2);
		filmes.add(filme3);

		try {
			service.alugarFilme(usuario, filmes);
			fail("Deveria ter lançado uma exceção... o cenário criado acima deve estar errado para este teste");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme sem estoque"));
		}
	}

	/**
	 * Teste para verificar se a exceção está sendo lançada corretamente (via @Rule)
	 */
	@Test
	public void naoDeveAlugarFilmeSemEstoque3() throws Exception {
		Usuario usuario = UsuarioBuilder.umUsuario().getUsuario();
		Filme filme = FilmeBuilder.umFilmeSemEstoque().getFilme();

		//Quando usamos a Rule, fazemos os expects antes da ação, como se eles fossem parte do cenário
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");

		service.alugarFilme(usuario, asList(filme));
	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		Filme filme = FilmeBuilder.umFilme().comNome("Filme 1").comValor(5.0).getFilme();

		try {
			service.alugarFilme(null, asList(filme));
			fail("Deveria ter lançado uma exceção... o cenário criado acima deve estar errado para este teste");
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}

		System.out.println("Aqui continua");
	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = UsuarioBuilder.umUsuario().getUsuario();

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		service.alugarFilme(usuario, null);

		System.out.println("Aqui não continua");
	}

	//****************************************************************************************************************//

	//TDD

	@Test
	public void desconto_3itens() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = UsuarioBuilder.umUsuario().getUsuario();
		List<Filme> filmes = createFilmeListWithId(
				new Filme("Filme 1", 5, 20.0),
				new Filme("Filme 2", 4, 10.0),
				new Filme("Filme 3", 7, 8.0)); //6 com desconto

		Locacao locacao = service.alugarFilme(usuario, filmes);
		assertThat(locacao.getValor(), is(equalTo(36.0)));
	}

	@Test
	public void desconto_4itens() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = UsuarioBuilder.umUsuario().getUsuario();
		List<Filme> filmes = createFilmeListWithId(
						new Filme("Filme 1", 5, 20.0),
						new Filme("Filme 2", 4, 10.0),
						new Filme("Filme 3", 7, 8.0), //6 com desconto
						new Filme("Filme 4", 3, 10.0)); //5 com desconto

		Locacao locacao = service.alugarFilme(usuario, filmes);
		assertThat(locacao.getValor(), is(equalTo(41.0)));
	}

	@Test
	public void desconto_5itens() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = UsuarioBuilder.umUsuario().getUsuario();
		List<Filme> filmes = createFilmeListWithId(
				new Filme("Filme 1", 5, 20.0),
				new Filme("Filme 2", 4, 10.0),
				new Filme("Filme 3", 7, 8.0), //6 com desconto
				new Filme("Filme 4", 3, 10.0), //5 com desconto
				new Filme("Filme 5", 3, 40.0)); //10 com desconto

		Locacao locacao = service.alugarFilme(usuario, filmes);
		assertThat(locacao.getValor(), is(equalTo(51.0)));
	}

	@Test
	public void desconto_6itens() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = UsuarioBuilder.umUsuario().getUsuario();
		List<Filme> filmes = createFilmeListWithId(
				new Filme("Filme 1", 5, 20.0),
				new Filme("Filme 2", 4, 10.0),
				new Filme("Filme 3", 7, 8.0), //6 com desconto
				new Filme("Filme 4", 3, 10.0), //5 com desconto
				new Filme("Filme 5", 3, 40.0), //10 com desconto
				new Filme("Filme 6", 3, 20.0)); //0 com desconto

		Locacao locacao = service.alugarFilme(usuario, filmes);
		assertThat(locacao.getValor(), is(equalTo(51.0)));
	}

	@Test
	public void naoDeveDevolverFilmeNoDomingoSeAlugadoNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		assumeTrue(verificarDiaSemana(new Date(), Calendar.SATURDAY));
		Usuario usuario = UsuarioBuilder.umUsuario().getUsuario();
		List<Filme> filmes = asList(new Filme("Filme 3", 7, 8.0));
		Locacao locacao = service.alugarFilme(usuario, filmes);

		assertTrue(verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY));
		assertThat(locacao.getDataRetorno(), caiEm(Calendar.MONDAY)); //Mesma coisa feita acima, mas com Matcher próprio
		assertThat(locacao.getDataRetorno(), caNumaSegunda()); //Mesma coisa feita acima, mas com Matcher próprio
	}

	//****************************************************************************************************************//

	//Mock

	@Test
	public void naoDeveAlugarFilmeParaUsuarioNegativado() throws FilmeSemEstoqueException {
		Usuario usuario = UsuarioBuilder.umUsuario().getUsuario();
		Filme filme = FilmeBuilder.umFilme().comNome("Filme 1").comValor(5.0).getFilme();

		Mockito.when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

		try {
			service.alugarFilme(usuario, asList(filme));
			fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario negativado"));
		}

		//Verificando se o método possuiNegativacao foi chamado para ESTE usuário
		Mockito.verify(spcService).possuiNegativacao(usuario);
	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas(){
		Usuario usuario = UsuarioBuilder.umUsuario().getUsuario();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario 2").getUsuario();
		Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Usuario 3").getUsuario();
		List<Locacao> locacoes = asList(
				umaLocacao().comUsuario(usuario).atrasada().getLocacao(),
				umaLocacao().comUsuario(usuario2).getLocacao(),
				umaLocacao().comUsuario(usuario3).atrasada().getLocacao(),
				umaLocacao().comUsuario(usuario3).atrasada().getLocacao());
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

		service.notificarAtrasos();

		//Verifica se o método emailService foi chamado para o mock emailService passando quaisquer instâncias da classe Usuario (seja usuario, ususario2, etc)
		Mockito.verify(emailService, Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));
		//Verificando se o método notificarAtraso foi chamado para ESTES usuário
		Mockito.verify(emailService).notificarAtraso(usuario);
		Mockito.verify(emailService, Mockito.atLeastOnce()).notificarAtraso(usuario3);
		//Verificando que o método notificarAtraso nunca foi chamado para ESTE usuário
		Mockito.verify(emailService, Mockito.never()).notificarAtraso(usuario2);
		//Verificando que NENHUM outro email, além dos dois priemiros, foi enviado
		Mockito.verifyNoMoreInteractions(emailService);
		//Verificando que esse mock não teve nenhuma interação
		Mockito.verifyZeroInteractions(spcService);
	}

}
