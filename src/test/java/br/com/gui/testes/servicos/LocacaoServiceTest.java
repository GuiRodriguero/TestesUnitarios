package br.com.gui.testes.servicos;

import java.util.Date;

import br.com.gui.testes.entidades.Filme;
import br.com.gui.testes.entidades.Locacao;
import br.com.gui.testes.entidades.Usuario;
import br.com.gui.testes.exceptions.FilmeSemEstoqueException;
import br.com.gui.testes.exceptions.LocadoraException;
import br.com.gui.testes.utils.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import static br.com.gui.testes.utils.DataUtils.isMesmaData;
import static br.com.gui.testes.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class LocacaoServiceTest {

	@Rule
	public ErrorCollector error = new ErrorCollector(); //Iremos mudar de assertThat para checkThat

	@Rule
	public ExpectedException exception = ExpectedException.none();

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
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filme);
		
		//verificacao
		//Em caso de falha, irá mostrar todos com falha
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
	}

	@Test
	public void testeLocacao() throws Exception {
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);

		//acao
		Locacao locacao = service.alugarFilme(usuario, filme);

		//verificacao
		//Em caso de falha, irá parar no primeiro que tiver falha
		assertThat(locacao.getValor(), is(equalTo(5.0)));
		assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
	}

	//************************************************************************************************************//

	/**
	 * Teste para verificar se a exceção está sendo lançada corretamente.
	 * Esse modo funciona bem quando sabemos que a só terá uma exceção possível de ser lançada (que é a que colocamos na anotação)
	 */
	@Test(expected = FilmeSemEstoqueException.class)
	public void testeLocacao_filmeSemEstoque() throws Exception {
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		service.alugarFilme(usuario, filme);
	}

	/**
	 * Teste para verificar se a exceção está sendo lançada corretamente (outra maneira)
	 * Fazendo desse modo, podemos ter mais controle sobre o que queremos verificar na exceção
	 */
	@Test
	public void testeLocacao_filmeSemEstoque2() {
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		try {
			service.alugarFilme(usuario, filme);
			fail("Deveria ter lançado uma exceção... o cenário criado acima deve estar errado para este teste");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme sem estoque"));
		}
	}

	/**
	 * Teste para verificar se a exceção está sendo lançada corretamente (via @Rule)
	 */
	@Test
	public void testeLocacao_filmeSemEstoque3() throws Exception {
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		//Quando usamos a Rule, fazemos os expects antes da ação, como se eles fossem parte do cenário
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");

		service.alugarFilme(usuario, filme);
	}

	@Test
	public void testeLocacao_usuarioVazio() throws FilmeSemEstoqueException {
		LocacaoService service = new LocacaoService();
		Filme filme = new Filme("Filme 1", 2, 5.0);

		try {
			service.alugarFilme(null, filme);
			fail("Deveria ter lançado uma exceção... o cenário criado acima deve estar errado para este teste");
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuário vazio"));
		}

		System.out.println("Aqui continua");
	}

	@Test
	public void testeLocacao_filmeVazio() throws FilmeSemEstoqueException, LocadoraException {
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		service.alugarFilme(usuario, null);

		System.out.println("Aqui não continua");
	}
}
