package co.edu.uniquindio.poo.finalproject.model.strategy;

import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ReportePdfStrategy implements ReporteCompraStrategy {
    @Override
    public void exportar(List<ContextoCompra> compras, File destino) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add("Reporte de compras");
        lineas.add("Total registros: " + compras.size());
        lineas.add("");
        for (ContextoCompra compra : compras) {
            lineas.add(compra.getIdCompra() + " | " + compra.getFechaCreacion() + " | "
                    + compra.getEvento().getNombre() + " | " + compra.obtenerNombreEstado()
                    + " | Total $" + compra.calcularTotal());
            lineas.add("Asientos: " + compra.getResumenAsientos());
            lineas.add("Servicios: " + compra.getResumenServicios());
            lineas.add("Comprobante: " + (compra.getComprobante() == null ? "Pendiente" : compra.getComprobante()));
            lineas.add("");
        }
        escribirPdfSimple(lineas, destino);
    }

    private void escribirPdfSimple(List<String> lineas, File destino) throws IOException {
        List<String> objetos = new ArrayList<>();
        String contenido = construirContenido(lineas);
        objetos.add("1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");
        objetos.add("2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");
        objetos.add("3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\nendobj\n");
        objetos.add("4 0 obj\n<< /Length " + contenido.getBytes(StandardCharsets.ISO_8859_1).length + " >>\nstream\n" + contenido + "endstream\nendobj\n");
        objetos.add("5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n");

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
        StringBuilder builder = new StringBuilder();
        builder.append("BT\n/F1 11 Tf\n40 760 Td\n");
        int lineasEscritas = 0;
        for (String linea : lineas) {
            if (lineasEscritas >= 45) {
                builder.append("(").append(escaparPdf("Reporte truncado por espacio de pagina.")).append(") Tj\n");
                break;
            }
            builder.append("(").append(escaparPdf(linea)).append(") Tj\n");
            builder.append("0 -15 Td\n");
            lineasEscritas++;
        }
        builder.append("ET\n");
        return builder.toString();
    }

    private String escaparPdf(String texto) {
        return texto.replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)");
    }
}
