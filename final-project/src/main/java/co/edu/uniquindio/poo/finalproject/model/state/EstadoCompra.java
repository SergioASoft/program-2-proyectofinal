package co.edu.uniquindio.poo.finalproject.model.state;

public interface EstadoCompra {
    void pagar(ContextoCompra compra);
    void cancelar(ContextoCompra compra);

    default void confirmar(ContextoCompra compra) {
    }

    default void reembolsar(ContextoCompra compra) {
    }

    default void reportarIncidencia(ContextoCompra compra) {
    }

    default boolean permiteModificacion() {
        return false;
    }

    String getNombre();
}
