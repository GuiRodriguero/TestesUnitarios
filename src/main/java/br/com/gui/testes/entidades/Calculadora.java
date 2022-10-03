package br.com.gui.testes.entidades;

import br.com.gui.testes.exceptions.DivisaoPorZeroException;

public class Calculadora {

    public int somar(int a, int b) {
        return a + b;
    }

    public int subtrair(int a, int b) {
        return a - b;
    }

    public int dividir(int a, int b) throws DivisaoPorZeroException {
        if(a == 0 || b == 0){
            throw new DivisaoPorZeroException();
        }
        return a / b;
    }
}
