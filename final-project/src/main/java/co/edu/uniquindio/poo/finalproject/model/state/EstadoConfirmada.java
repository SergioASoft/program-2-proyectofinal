package co.edu.uniquindio.poo.finalproject.model.state;

public class EstadoConfirmada implements EstadoCompra {
    @Override
    public void pagar(ContextoCompra compra) {
    }

    @Override
    public void cancelar(ContextoCompra compra) {
    }

    @Override
    public void reembolsar(ContextoCompra compra) {
        compra.setEstado(new EstadoReembolsada());
    }

    @Override
    public void reportarIncidencia(ContextoCompra compra) {
        compra.setEstado(new EstadoIncidencia());
    }

    @Override
    public String getNombre() {
        return "CONFIRMADA";
    }
}
