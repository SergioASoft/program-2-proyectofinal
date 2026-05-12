package co.edu.uniquindio.poo.finalproject.model.state;

import co.edu.uniquindio.poo.finalproject.model.Asiento;
import co.edu.uniquindio.poo.finalproject.model.TipoPago;
import co.edu.uniquindio.poo.finalproject.model.TipoServicioAdicional;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.decorator.CalculadoraCompraDecorada;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ContextoCompra {
    private final String idCompra;
    private EstadoCompra estadoActual;
    private final Evento evento;
    private final List<Asiento> asientos;
    private final List<TipoServicioAdicional> serviciosAdicionales;
    private final LocalDate fechaCreacion;
    private LocalDate fechaPago;
    private TipoPago metodoPago;
    private String comprobante;

    public ContextoCompra(Evento evento, List<Asiento> asientos) {
        this("CMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(), evento, asientos, List.of());
    }

    public ContextoCompra(String idCompra, Evento evento, List<Asiento> asientos, List<TipoServicioAdicional> serviciosAdicionales) {
        this.idCompra = idCompra;
        this.estadoActual = new EstadoCreada();
        this.evento = evento;
        this.asientos = new ArrayList<>(asientos);
        this.serviciosAdicionales = new ArrayList<>(serviciosAdicionales);
        this.fechaCreacion = LocalDate.now();
        this.metodoPago = TipoPago.NINGUNO;
    }

    public void setEstado(EstadoCompra nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }

    public void actualizarDetalle(List<Asiento> nuevosAsientos, List<TipoServicioAdicional> nuevosServicios) {
        if (!permiteModificacion()) {
            throw new IllegalStateException("Solo se puede modificar una compra creada antes del pago.");
        }
        asientos.clear();
        asientos.addAll(nuevosAsientos);
        serviciosAdicionales.clear();
        serviciosAdicionales.addAll(nuevosServicios);
    }

    public void realizarPago(TipoPago metodoPago) {
        estadoActual.pagar(this);
        if ("PAGADA".equals(obtenerNombreEstado())) {
            this.metodoPago = metodoPago;
            this.fechaPago = LocalDate.now();
            this.comprobante = generarComprobante();
        }
    }

    public void realizarCancelacion() {
        estadoActual.cancelar(this);
    }

    public void confirmar() {
        estadoActual.confirmar(this);
    }

    public void reembolsar() {
        estadoActual.reembolsar(this);
    }

    public void reportarIncidencia() {
        estadoActual.reportarIncidencia(this);
    }

    public boolean permiteModificacion() {
        return estadoActual.permiteModificacion();
    }

    public String obtenerNombreEstado() {
        return estadoActual.getNombre();
    }

    public int calcularSubtotalEntradas() {
        return asientos.stream()
                .mapToInt(Asiento::getPrecio)
                .sum();
    }

    public int calcularTotalServicios() {
        return serviciosAdicionales.stream()
                .mapToInt(TipoServicioAdicional::getPrecio)
                .sum();
    }

    public int calcularTotal() {
        return CalculadoraCompraDecorada.calcularTotal(asientos, serviciosAdicionales);
    }

    public String getResumenAsientos() {
        if (asientos.isEmpty()) {
            return "Sin asientos";
        }
        return asientos.stream()
                .map(Asiento::getIdAsiento)
                .collect(Collectors.joining(", "));
    }

    public String getResumenServicios() {
        if (serviciosAdicionales.isEmpty()) {
            return "Sin servicios";
        }
        return serviciosAdicionales.stream()
                .map(TipoServicioAdicional::getNombre)
                .collect(Collectors.joining(", "));
    }

    private String generarComprobante() {
        return "CB-" + idCompra.replace("CMP-", "") + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    public String getIdCompra() {
        return idCompra;
    }

    public Evento getEvento() {
        return evento;
    }

    public List<Asiento> getAsientos() {
        return Collections.unmodifiableList(asientos);
    }

    public List<TipoServicioAdicional> getServiciosAdicionales() {
        return Collections.unmodifiableList(serviciosAdicionales);
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public TipoPago getMetodoPago() {
        return metodoPago;
    }

    public String getComprobante() {
        return comprobante;
    }
}
