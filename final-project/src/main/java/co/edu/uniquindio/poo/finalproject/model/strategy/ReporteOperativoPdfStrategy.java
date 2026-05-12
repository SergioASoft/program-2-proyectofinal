package co.edu.uniquindio.poo.finalproject.model.strategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ReporteOperativoPdfStrategy implements ReporteOperativoStrategy {
    @Override
    public void exportar(List<String[]> filas, File destino) throws IOException {
        List<String> lineas = new ArrayList<>();
        for (String[] fila : filas) {
            lineas.add(fila[0] + ": " + fila[1]);
        }
        escribirPdfSimple(lineas, destino);
    }

    private void escribirPdfSimple(List<String> lineas, File destino) throws IOException {
        String contenido = construirContenido(lineas);
        List<String> objetos = List.of(
                "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n",
                "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n",
                "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\nendobj\n",
                "4 0 obj\n<< /Length " + contenido.getBytes(StandardCharsets.ISO_8859_1).length + " >>\nstream\n" + contenido + "endstream\nendobj\n",
                "5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n"
        );

        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        salida.write("%PDF-1.4\n".getBytes(StandardCharsets.ISO_8859_1));
        List<Integer> offsets = new ArrayList<>();
        for (String objeto : objetos) {
            offsets.add(salida.size());
            salida.write(objeto.getBytes(StandardCharsets.ISO_8859_1));
        }
        int inicioXref = salida.size();
        salida.write(("xref\n0 " + (objetos.size() + 1) + "\n").getBytes(StandardCharsets.ISO_8859_1));
        salida.write("0000000000 65535 f \n".getBytes(StandardCharsets.ISO_8859_1));
        for (int offset : offsets) {
            salida.write(String.format("%010d 00000 n \n", offset).getBytes(StandardCharsets.ISO_8859_1));
        }
        salida.write(("trailer\n<< /Size " + (objetos.size() + 1) + " /Root 1 0 R >>\nstartxref\n" + inicioXref + "\n%%EOF").getBytes(StandardCharsets.ISO_8859_1));

        try (FileOutputStream fos = new FileOutputStream(destino)) {
            salida.writeTo(fos);
        }
    }

    private String construirContenido(List<String> lineas) {
        StringBuilder builder = new StringBuilder("BT\n/F1 11 Tf\n40 760 Td\n");
        int count = 0;
        for (String linea : lineas) {
            if (count++ >= 45) {
                break;
            }
            builder.append("(").append(escaparPdf(linea)).append(") Tj\n0 -15 Td\n");
        }
        builder.append("ET\n");
        return builder.toString();
    }

    private String escaparPdf(String texto) {
        return texto.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
    }
}
