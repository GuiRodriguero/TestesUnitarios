package br.com.gui.testes.servicos;

import br.com.gui.testes.dao.LocacaoDAO;
import br.com.gui.testes.dao.LocacaoDAOFake;
import br.com.gui.testes.entidades.Filme;
import br.com.gui.testes.entidades.Locacao;
import br.com.gui.testes.entidades.Usuario;
import br.com.gui.testes.exceptions.FilmeSemEstoqueException;
import br.com.gui.testes.exceptions.LocadoraException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    private LocacaoService service;
    private LocacaoDAO dao;
    private SPCService spcService;

    @Parameterized.Parameter //default é 0
    public List<Filme> filmes;
    @Parameterized.Parameter(value = 1)
    public Double valorLocacao;
    @Parameterized.Parameter(value = 2)
    public String descricaoTeste;

    @Before
    public void setup(){
        service = new LocacaoService();
        dao = Mockito.mock(LocacaoDAO.class);
        service.setLocacaoDAO(dao);
        spcService = Mockito.mock(SPCService.class);
        service.setSpcService(spcService);
    }

    private static Filme filme1 = new Filme(1, "Filme 1", 2, 4.0);
    private static Filme filme2 = new Filme(2, "Filme 2", 3, 4.0);
    private static Filme filme3 = new Filme(3, "Filme 3", 4, 4.0);
    private static Filme filme4 = new Filme(3, "Filme 4", 5, 4.0);
    private static Filme filme5 = new Filme(4, "Filme 5", 6, 4.0);
    private static Filme filme6 = new Filme(3, "Filme 6", 7, 4.0);
    private static Filme filme7 = new Filme(4, "Filme 1", 2, 4.0);
    private static Filme filme8 = new Filme(5, "Filme 2", 3, 4.0);
    private static Filme filme9 = new Filme(3, "Filme 3", 4, 4.0);
    private static Filme filme10 = new Filme(4, "Filme 4", 5, 4.0);
    private static Filme filme11 = new Filme(5, "Filme 5", 6, 4.0);
    private static Filme filme12 = new Filme(6, "Filme 6", 7, 4.0);

    @Parameterized.Parameters(name = "{2}")
    public static Collection<Object[]> getParametros(){
        return Arrays.asList(new Object[][]{
                {Arrays.asList(filme1, filme2), 8.0, "2 Filmes: Sem Desconto"},
                {Arrays.asList(filme1, filme2, filme3), 11.0, "3 Filmes: 25%"},
                {Arrays.asList(filme1, filme2, filme4, filme5), 13.0, "4 Filmes: 50%"},
                {Arrays.asList(filme1, filme2, filme6, filme7, filme8), 14.0, "5 Filmes: 75%"},
                {Arrays.asList(filme1, filme2, filme9, filme10, filme11, filme12), 14.0, "6 Filmes: 100%"},
        });
    }

    @Test
    public void calcularValorLocacao() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");
        Locacao locacao = service.alugarFilme(usuario, filmes);
        assertThat(locacao.getValor(), is(equalTo(valorLocacao)));
    }

}
