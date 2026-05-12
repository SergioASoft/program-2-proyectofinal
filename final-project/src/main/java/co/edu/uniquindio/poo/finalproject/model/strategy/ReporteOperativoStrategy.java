package co.edu.uniquindio.poo.finalproject.model.strategy;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ReporteOperativoStrategy {
    void exportar(List<String[]> filas, File destino) throws IOException;
}
