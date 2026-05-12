package co.edu.uniquindio.poo.finalproject.controller.facade;

import co.edu.uniquindio.poo.finalproject.model.EstadoAsiento;
import co.edu.uniquindio.poo.finalproject.model.TipoServicioAdicional;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.builder.Zona;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControladorMetricas {
    private static ControladorMetricas instance;

    private ControladorMetricas() {
    }

    public static ControladorMetricas getInstance() {
        if (instance == null) {
            instance = new ControladorMetricas();
        }
        return instance;
    }

    public Map<LocalDate, Integer> ventasPorPeriodo(List<ContextoCompra> compras) {
        return compras.stream()
                .filter(this::esCompraConIngreso)
                .collect(Collectors.groupingBy(
                        compra -> compra.getFechaPago() == null ? compra.getFechaCreacion() : compra.getFechaPago(),
                        LinkedHashMap::new,
                        Collectors.summingInt(ContextoCompra::calcularTotal)
                ));
    }

    public Map<String, Double> ocupacionPorZona(List<Evento> eventos) {
        Map<String, Double> resultado = new LinkedHashMap<>();
        for (Evento evento : eventos) {
            if (evento.getRecinto() == null) {
                continue;
            }
            for (Zona zona : evento.getRecinto().getZonas()) {
                long ocupados = zona.getAsientos().stream()
                        .filter(asiento -> asiento.getEstadoAsiento() == EstadoAsiento.RESERVADO
                                || asiento.getEstadoAsiento() == EstadoAsiento.VENDIDO
                                || asiento.getEstadoAsiento() == EstadoAsiento.BLOQUEADO)
                        .count();
                double porcentaje = zona.getCapacidad() == 0 ? 0 : (ocupados * 100.0) / zona.getCapacidad();
                resultado.put(evento.getNombre() + " - " + zona.getIdZona(), porcentaje);
            }
        }
        return resultado;
    }

    public Map<TipoServicioAdicional, Integer> ingresosPorServicios(List<ContextoCompra> compras) {
        Map<TipoServicioAdicional, Integer> resultado = new LinkedHashMap<>();
        for (TipoServicioAdicional servicio : TipoServicioAdicional.values()) {
            int total = compras.stream()
                    .filter(this::esCompraConIngreso)
                    .filter(compra -> compra.getServiciosAdicionales().contains(servicio))
                    .mapToInt(compra -> servicio.getPrecio())
                    .sum();
            resultado.put(servicio, total);
        }
        return resultado;
    }

    public double tasaCancelacion(List<ContextoCompra> compras) {
        if (compras.isEmpty()) {
            return 0;
        }
        long canceladas = compras.stream()
                .filter(compra -> "CANCELADA".equals(compra.obtenerNombreEstado())
                        || "REEMBOLSADA".equals(compra.obtenerNombreEstado()))
                .count();
        return (canceladas * 100.0) / compras.size();
    }

    public Map<String, Long> topEventos(List<ContextoCompra> compras) {
        return compras.stream()
                .collect(Collectors.groupingBy(
                        compra -> compra.getEvento().getNombre(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
    }

    private boolean esCompraConIngreso(ContextoCompra compra) {
        return "PAGADA".equals(compra.obtenerNombreEstado())
                || "CONFIRMADA".equals(compra.obtenerNombreEstado())
                || "INCIDENCIA".equals(compra.obtenerNombreEstado());
    }
}
