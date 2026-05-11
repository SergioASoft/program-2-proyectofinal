package co.edu.uniquindio.poo.finalproject.model.state;

public interface EstadoCompra {
    void pagar(ContextoCompra compra);
    void cancelar(ContextoCompra compra);
    String getNombre();
}
