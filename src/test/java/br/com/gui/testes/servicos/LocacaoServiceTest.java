package br.com.gui.testes.servicos;

import java.util.Date;

import br.com.gui.testes.entidades.Filme;
import br.com.gui.testes.entidades.Locacao;
import br.com.gui.testes.entidades.Usuario;
import br.com.gui.testes.utils.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static br.com.gui.testes.utils.DataUtils.isMesmaData;
import static br.com.gui.testes.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class LocacaoServiceTest {

	@Rule
	public ErrorCollector error = new ErrorCollector(); //Iremos mudar de assertThat para checkThat

	/**
	 * Existe um debate sobre isso pois se fossemos separar nosso teste em 3 (1 para cada assertiva),
	 * teríamos o mesmo cenário. Alguns dizem que não precisamos separar porque o teste nunca irá passar
	 * estando com erros, então de um jeito ou de outro, você encontraria o erro e o corrigiria.
	 * Já outros preferem separar por conta da rastreabilidade. Outro problema que pode ser levantado sobre tratar
	 * tudo em um teste só, é que, em caso de falhas, ele sempre irá falhar na primeira assertiva, então não saberemos
	 * como as demais estão a menos que a primeira seja corrigida (para se precaver disso, podemos usar @Rule).
	 */
	@Test
	public void testeLocacaoRule() {
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
	public void testeLocacao() {
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

}
