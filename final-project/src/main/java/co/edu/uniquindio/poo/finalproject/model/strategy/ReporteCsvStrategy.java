package co.edu.uniquindio.poo.finalproject.model.strategy;

import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReporteCsvStrategy implements ReporteCompraStrategy {
    @Override
    public void exportar(List<ContextoCompra> compras, File destino) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destino))) {
            writer.write("id,fecha,event_id,evento,estado,asientos,servicios,total,comprobante");
            writer.newLine();
            for (ContextoCompra compra : compras) {
                writer.write(String.join(",",
                        escapar(compra.getIdCompra()),
                        escapar(compra.getFechaCreacion().toString()),
                        escapar(compra.getEvento().getIdEvento()),
                        escapar(compra.getEvento().getNombre()),
                        escapar(compra.obtenerNombreEstado()),
                        escapar(compra.getResumenAsientos()),
                        escapar(compra.getResumenServicios()),
                        escapar(String.valueOf(compra.calcularTotal())),
                        escapar(compra.getComprobante() == null ? "" : compra.getComprobante())
                ));
                writer.newLine();
            }
        }
    }

    private String escapar(String valor) {
        String seguro = valor == null ? "" : valor.replace("\"", "\"\"");
        return "\"" + seguro + "\"";
    }
}
