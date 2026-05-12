package co.edu.uniquindio.poo.finalproject.model.strategy;

import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ReporteCompraStrategy {
    void exportar(List<ContextoCompra> compras, File destino) throws IOException;
}
