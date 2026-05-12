package co.edu.uniquindio.poo.finalproject.model.strategy;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExportadorReporteOperativo {
    private ReporteOperativoStrategy estrategia;

    public ExportadorReporteOperativo(ReporteOperativoStrategy estrategia) {
        this.estrategia = estrategia;
    }

    public void setEstrategia(ReporteOperativoStrategy estrategia) {
        this.estrategia = estrategia;
    }

    public void exportar(List<String[]> filas, File destino) throws IOException {
        estrategia.exportar(filas, destino);
    }
}
