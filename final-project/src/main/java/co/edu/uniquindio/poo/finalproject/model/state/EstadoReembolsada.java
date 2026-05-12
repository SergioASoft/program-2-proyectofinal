package co.edu.uniquindio.poo.finalproject.model.state;

public class EstadoReembolsada implements EstadoCompra {
    @Override
    public void pagar(ContextoCompra compra) {
    }

    @Override
    public void cancelar(ContextoCompra compra) {
    }

    @Override
    public String getNombre() {
        return "REEMBOLSADA";
    }
}
