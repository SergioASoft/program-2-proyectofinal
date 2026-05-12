package co.edu.uniquindio.poo.finalproject.model.strategy;

import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExportadorReporteCompras {
    private ReporteCompraStrategy estrategia;

    public ExportadorReporteCompras(ReporteCompraStrategy estrategia) {
        this.estrategia = estrategia;
    }

    public void setEstrategia(ReporteCompraStrategy estrategia) {
        this.estrategia = estrategia;
    }

    public void exportar(List<ContextoCompra> compras, File destino) throws IOException {
        estrategia.exportar(compras, destino);
    }
}
