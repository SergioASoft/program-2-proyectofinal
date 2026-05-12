package co.edu.uniquindio.poo.finalproject.model.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReporteOperativoAdapter {
    public List<String[]> desdeMapa(String titulo, Map<?, ?> metricas) {
        List<String[]> filas = new ArrayList<>();
        filas.add(new String[]{titulo, "Valor"});
        metricas.forEach((clave, valor) -> filas.add(new String[]{String.valueOf(clave), String.valueOf(valor)}));
        return filas;
    }

    public List<String[]> desdeValor(String titulo, double valor) {
        List<String[]> filas = new ArrayList<>();
        filas.add(new String[]{titulo, "Valor"});
        filas.add(new String[]{"Resultado", String.format("%.2f", valor)});
        return filas;
    }
}
