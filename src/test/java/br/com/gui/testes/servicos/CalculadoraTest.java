package br.com.gui.testes.servicos;

import br.com.gui.testes.entidades.Calculadora;
import br.com.gui.testes.exceptions.DivisaoPorZeroException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculadoraTest {

    private Calculadora calc;

    @Before
    public void setup(){
        calc = new Calculadora();
    }

    @Test
    public void somarDoisValores(){
        int a = 5;
        int b = 3;

        int resultado = calc.somar(a, b);

        assertEquals(8, resultado);
    }

    @Test
    public void subtrairDoisValores(){
        int a = 5;
        int b = 3;

        int resultado = calc.subtrair(a, b);

        assertEquals(2, resultado);
    }

    @Test
    public void dividirDoisValores() throws DivisaoPorZeroException {
        int a = 10;
        int b = 5;

        int resultado = calc.dividir(a, b);

        assertEquals(2, resultado);
    }

    @Test(expected = DivisaoPorZeroException.class)
    public void excecaoDividirPorZero() throws DivisaoPorZeroException {
        int a = 10;
        int b = 0;

        int resultado = calc.dividir(a, b);

        assertEquals(2, resultado);
    }
}
