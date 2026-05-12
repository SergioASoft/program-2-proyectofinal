package co.edu.uniquindio.poo.finalproject.model.strategy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReporteOperativoCsvStrategy implements ReporteOperativoStrategy {
    @Override
    public void exportar(List<String[]> filas, File destino) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destino))) {
            for (String[] fila : filas) {
                writer.write(escapar(fila[0]) + "," + escapar(fila[1]));
                writer.newLine();
            }
        }
    }

    private String escapar(String valor) {
        String seguro = valor == null ? "" : valor.replace("\"", "\"\"");
        return "\"" + seguro + "\"";
    }
}
