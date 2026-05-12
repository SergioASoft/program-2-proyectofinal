package co.edu.uniquindio.poo.finalproject.model.command;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;

public class ConfirmarCompraCommand implements CompraCommand {
    private final ControladorFacade facade;
    private final ContextoCompra compra;

    public ConfirmarCompraCommand(ControladorFacade facade, ContextoCompra compra) {
        this.facade = facade;
        this.compra = compra;
    }

    @Override
    public void ejecutar() {
        facade.confirmarCompra(compra);
    }
}
