package co.edu.uniquindio.poo.finalproject.model.state;

public class EstadoIncidencia implements EstadoCompra {
    @Override
    public void pagar(ContextoCompra compra) {
    }

    @Override
    public void cancelar(ContextoCompra compra) {
    }

    @Override
    public void confirmar(ContextoCompra compra) {
        compra.setEstado(new EstadoConfirmada());
    }

    @Override
    public void reembolsar(ContextoCompra compra) {
        compra.setEstado(new EstadoReembolsada());
    }

    @Override
    public String getNombre() {
        return "INCIDENCIA";
    }
}
