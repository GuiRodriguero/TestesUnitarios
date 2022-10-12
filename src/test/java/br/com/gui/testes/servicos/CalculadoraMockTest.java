package br.com.gui.testes.servicos;

import br.com.gui.testes.entidades.Calculadora;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CalculadoraMockTest {

    /**
     * Neste teste, estamos mockando a chamada do Calculadora.somar(). Acontece que
     * mockamos passando 1 e 2, e chamamos passando 1 e 8. Por ter essa diferença, o mockito retornou
     * o valor padrão do tipo do retorno deste método (int), ou seja, 0
     */
    @Test
    public void testeMockitoConfuso(){
        Calculadora calc = Mockito.mock(Calculadora.class);
        Mockito.when(calc.somar(1, 2)).thenReturn(5);
        Assert.assertThat(calc.somar(1, 8), CoreMatchers.is(0));
    }

    @Test
    public void teste(){
        Calculadora calc = Mockito.mock(Calculadora.class);
        Mockito.when(calc.somar(Mockito.anyInt(), Mockito.anyInt())).thenReturn(5);
        Assert.assertThat(calc.somar(1, 9), CoreMatchers.is(5));
    }
    @Test
    public void teste2(){
        Calculadora calc = Mockito.mock(Calculadora.class);
        Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);
        Assert.assertThat(calc.somar(1, 6), CoreMatchers.is(5));
    }
}
