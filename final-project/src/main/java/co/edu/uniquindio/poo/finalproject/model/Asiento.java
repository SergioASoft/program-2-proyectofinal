package co.edu.uniquindio.poo.finalproject.model;

public class Asiento {
    String idAsiento;
    int fila;
    int numero;
    EstadoAsiento estadoAsiento;
    int precio;

    public Asiento(String idAsiento, int fila, int numero, EstadoAsiento estadoAsiento, int precio) {
        this.idAsiento = idAsiento;
        this.fila = fila;
        this.numero = numero;
        this.estadoAsiento = estadoAsiento;
        this.precio = precio;
    }

    public String getIdAsiento() {
        return idAsiento;
    }

    public int getFila() {
        return fila;
    }

    public int getNumero() {
        return numero;
    }

    public EstadoAsiento getEstadoAsiento() {
        return estadoAsiento;
    }

    public int getPrecio() {
        return precio;
    }

    public void setIdAsiento(String idAsiento) {
        this.idAsiento = idAsiento;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setEstadoAsiento(EstadoAsiento estadoAsiento) {
        this.estadoAsiento = estadoAsiento;
    }

    @Override
    public String toString() {
        return idAsiento;
    }
}
