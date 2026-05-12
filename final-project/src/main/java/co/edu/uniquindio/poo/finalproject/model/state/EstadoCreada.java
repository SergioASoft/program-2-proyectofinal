package co.edu.uniquindio.poo.finalproject.model.state;

public class EstadoCreada implements EstadoCompra{
    @Override
    public void pagar(ContextoCompra compra) {
        compra.setEstado(new EstadoPagada());
    }

    @Override
    public void cancelar(ContextoCompra compra) {
        compra.setEstado(new EstadoCancelada());
    }

    @Override
    public void reportarIncidencia(ContextoCompra compra) {
        compra.setEstado(new EstadoIncidencia());
    }

    @Override
    public boolean permiteModificacion() {
        return true;
    }

    @Override
    public String getNombre() {
        return "CREADA";
    }
}
