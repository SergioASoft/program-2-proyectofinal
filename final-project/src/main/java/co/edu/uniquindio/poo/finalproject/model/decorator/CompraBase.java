package co.edu.uniquindio.poo.finalproject.model.decorator;

import co.edu.uniquindio.poo.finalproject.model.Asiento;

import java.util.List;

public class CompraBase implements CompraCosto {
    private final List<Asiento> asientos;

    public CompraBase(List<Asiento> asientos) {
        this.asientos = asientos;
    }

    @Override
    public int calcularCosto() {
        return asientos.stream()
                .mapToInt(Asiento::getPrecio)
                .sum();
    }

    @Override
    public String obtenerDescripcion() {
        return "Entradas";
    }
}
