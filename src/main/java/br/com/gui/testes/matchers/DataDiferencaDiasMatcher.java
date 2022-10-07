package br.com.gui.testes.matchers;

import br.com.gui.testes.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Date;

public class DataDiferencaDiasMatcher extends TypeSafeMatcher {

    private Integer quantidadeDias;

    public DataDiferencaDiasMatcher(Integer quantidadeDias) {
        this.quantidadeDias = quantidadeDias;
    }

    @Override
    protected boolean matchesSafely(Object item) {
        return DataUtils.isMesmaData((Date) item, DataUtils.obterDataComDiferencaDias(quantidadeDias));
    }

    @Override
    public void describeTo(Description description) {

    }
}
