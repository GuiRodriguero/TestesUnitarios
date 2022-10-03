package br.com.gui.testes.utils;

import br.com.gui.testes.entidades.Filme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ListUtils {
    public static List<Filme> createFilmeListWithId(Filme... filme){
        AtomicInteger id = new AtomicInteger(1);
        List<Filme> filmes = new ArrayList<>();
        Arrays.stream(filme).forEach(f -> {filmes.add(new Filme(Integer.parseInt(id.toString()), f.getNome(), f.getEstoque(), f.getPrecoLocacao())); id.getAndIncrement();});
        return filmes;
    }
}
