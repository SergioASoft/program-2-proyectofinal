package co.edu.uniquindio.poo.finalproject.model.state;

public class EstadoPagada implements EstadoCompra{
    @Override
    public void pagar(ContextoCompra compra) {
    }

    @Override
    public void cancelar(ContextoCompra compra) {
        System.out.println("No se puede cancelar una compra ya pagada.");
    }

    @Override
    public String getNombre() { return "PAGADA"; }
}
