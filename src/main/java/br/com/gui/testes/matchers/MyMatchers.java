package br.com.gui.testes.matchers;

import java.util.Calendar;

public class MyMatchers {

    public static DiaSemanaMatcher caiEm(Integer diaSemana){
        return new DiaSemanaMatcher(diaSemana);
    }
    public static DiaSemanaMatcher caNumaSegunda(){
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }
    public static DataDiferencaDiasMatcher ehHojeComDiferencaDeDias(Integer quantidadeDias){
        return new DataDiferencaDiasMatcher(quantidadeDias);
    }
    public static DataDiferencaDiasMatcher ehHoje(){
        return new DataDiferencaDiasMatcher(0);
    }
}
