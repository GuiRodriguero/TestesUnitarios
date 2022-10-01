package br.com.gui.testes.exceptions;

public class FilmeSemEstoqueException extends Exception {
    private static final long serialVersionUID = -3451197940998584831L;

    public FilmeSemEstoqueException(String message) {
        super(message);
    }
}
