package co.edu.uniquindio.poo.finalproject.model.command;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.TipoIncidencia;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;

public class ReportarIncidenciaCompraCommand implements CompraCommand {
    private final ControladorFacade facade;
    private final ContextoCompra compra;
    private final TipoIncidencia tipo;
    private final String descripcion;

    public ReportarIncidenciaCompraCommand(ControladorFacade facade, ContextoCompra compra,
                                           TipoIncidencia tipo, String descripcion) {
        this.facade = facade;
        this.compra = compra;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    @Override
    public void ejecutar() {
        facade.reportarIncidencia(compra, tipo, descripcion);
    }
}
