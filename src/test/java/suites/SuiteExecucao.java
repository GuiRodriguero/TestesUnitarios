package suites;

import br.com.gui.testes.servicos.CalculadoraTest;
import br.com.gui.testes.servicos.CalculoValorLocacaoTest;
import br.com.gui.testes.servicos.LocacaoServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CalculadoraTest.class,
        CalculoValorLocacaoTest.class,
        LocacaoServiceTest.class})
public class SuiteExecucao {
}
