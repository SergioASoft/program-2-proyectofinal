package co.edu.uniquindio.poo.finalproject.model;

public enum TipoServicioAdicional {
    VIP("VIP", 50000),
    SEGURO("Seguro", 15000),
    MERCHANDISING("Merchandising", 30000),
    PARQUEADERO("Parqueadero", 20000),
    ACCESO_PREFERENCIAL("Acceso preferencial", 25000);

    private final String nombre;
    private final int precio;

    TipoServicioAdicional(String nombre, int precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return nombre + " ($" + precio + ")";
    }
}
