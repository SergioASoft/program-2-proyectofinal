package co.edu.uniquindio.poo.finalproject.model.decorator;

import co.edu.uniquindio.poo.finalproject.model.TipoServicioAdicional;

public class ServicioAdicionalDecorator implements CompraCosto {
    private final CompraCosto compra;
    private final TipoServicioAdicional servicio;

    public ServicioAdicionalDecorator(CompraCosto compra, TipoServicioAdicional servicio) {
        this.compra = compra;
        this.servicio = servicio;
    }

    @Override
    public int calcularCosto() {
        return compra.calcularCosto() + servicio.getPrecio();
    }

    @Override
    public String obtenerDescripcion() {
        return compra.obtenerDescripcion() + " + " + servicio.getNombre();
    }
}
