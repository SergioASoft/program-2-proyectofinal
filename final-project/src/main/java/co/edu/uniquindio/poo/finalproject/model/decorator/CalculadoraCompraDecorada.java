package co.edu.uniquindio.poo.finalproject.model.decorator;

import co.edu.uniquindio.poo.finalproject.model.Asiento;
import co.edu.uniquindio.poo.finalproject.model.TipoServicioAdicional;

import java.util.List;

public final class CalculadoraCompraDecorada {
    private CalculadoraCompraDecorada() {
    }

    public static CompraCosto construir(List<Asiento> asientos, List<TipoServicioAdicional> servicios) {
        CompraCosto compra = new CompraBase(asientos);
        for (TipoServicioAdicional servicio : servicios) {
            compra = new ServicioAdicionalDecorator(compra, servicio);
        }
        return compra;
    }

    public static int calcularTotal(List<Asiento> asientos, List<TipoServicioAdicional> servicios) {
        return construir(asientos, servicios).calcularCosto();
    }
}
