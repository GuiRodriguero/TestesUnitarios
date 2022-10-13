package br.com.gui.testes.servicos;

import br.com.gui.testes.entidades.Calculadora;
import br.com.gui.testes.runners.ParallelRunner;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

import static org.hamcrest.CoreMatchers.is;

@RunWith(ParallelRunner.class)
public class CalculadoraMockTest {

    @Mock
    private Calculadora calcMock;
    @Spy
    private Calculadora calcSpy;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Neste teste, estamos mockando a chamada do Calculadora.somar(). Acontece que
     * mockamos passando 1 e 2, e chamamos passando 1 e 8. Por ter essa diferença, o mockito retornou
     * o valor padrão do tipo do retorno deste método (int), ou seja, 0
     */
    @Test
    public void testeMockitoConfuso(){
        Calculadora calc = Mockito.mock(Calculadora.class);
        Mockito.when(calc.somar(1, 2)).thenReturn(5);
        Assert.assertThat(calc.somar(1, 8), is(0));
    }

    @Test
    public void teste(){
        Calculadora calc = Mockito.mock(Calculadora.class);
        Mockito.when(calc.somar(Mockito.anyInt(), Mockito.anyInt())).thenReturn(5);
        Assert.assertThat(calc.somar(1, 9), is(5));
    }
    @Test
    public void teste2(){
        Calculadora calc = Mockito.mock(Calculadora.class);
        Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);
        Assert.assertThat(calc.somar(1, 6), is(5));
    }

    @Test
    public void teste3(){
        Calculadora calc = Mockito.mock(Calculadora.class);
        ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
        Mockito.when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);

        Assert.assertThat(calc.somar(1, 6), is(5));
//        System.out.println("CalculadoraMockTest#teste3: " + argCapt.getAllValues());
    }

    @Test
    public void mockVsSpy(){
        //calcMock.somar(1, 2); Retorna 0 (pois o mock vê que o método retorna um int e retona o valor default de um int)
        //calcSpy.somar(1, 2); Retorna 3 (o spy verifica como o método se comporta e replica esse comportamento)
        //calcMock.imprime(); Como imprime não tem retorno, só um sout, não faz nada
        //calcSpy.imprime(); Caiu no sout e imprime
    }
}
