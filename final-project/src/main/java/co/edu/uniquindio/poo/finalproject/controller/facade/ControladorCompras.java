package co.edu.uniquindio.poo.finalproject.controller.facade;

import co.edu.uniquindio.poo.finalproject.model.Asiento;
import co.edu.uniquindio.poo.finalproject.model.EstadoAsiento;
import co.edu.uniquindio.poo.finalproject.model.Entrada;
import co.edu.uniquindio.poo.finalproject.model.TipoPago;
import co.edu.uniquindio.poo.finalproject.model.TipoServicioAdicional;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.builder.Zona;
import co.edu.uniquindio.poo.finalproject.model.factory.Cliente;
import co.edu.uniquindio.poo.finalproject.model.factory.CompraFactory;
import co.edu.uniquindio.poo.finalproject.model.factory.EntradaFactory;
import co.edu.uniquindio.poo.finalproject.model.factory.Usuario;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorCompras {
    private static ControladorCompras instance;
    private final CompraFactory compraFactory = new CompraFactory();
    private final EntradaFactory entradaFactory = new EntradaFactory();

    private ControladorCompras() {
    }

    public static ControladorCompras getInstance() {
        if (instance == null) {
            instance = new ControladorCompras();
        }
        return instance;
    }

    public ContextoCompra crearCompra(Cliente cliente, Evento evento, List<Asiento> asientos, List<TipoServicioAdicional> servicios) {
        validarCliente(cliente);
        validarAsientos(asientos);
        validarDisponibilidad(asientos, null);

        ContextoCompra compra = compraFactory.crear(evento, asientos, servicios);
        compra.setUsuarioAsociado(cliente);
        asientos.forEach(asiento -> asiento.setEstadoAsiento(EstadoAsiento.RESERVADO));
        cliente.getHistorialCompras().add(compra);
        return compra;
    }

    public void actualizarCompra(ContextoCompra compra, List<Asiento> nuevosAsientos, List<TipoServicioAdicional> servicios) {
        validarCompra(compra);
        validarAsientos(nuevosAsientos);
        if (!compra.permiteModificacion()) {
            throw new IllegalStateException("La compra ya no se puede modificar.");
        }
        validarDisponibilidad(nuevosAsientos, compra);

        List<Asiento> asientosAnteriores = new ArrayList<>(compra.getAsientos());
        for (Asiento asiento : asientosAnteriores) {
            if (!nuevosAsientos.contains(asiento) && asiento.getEstadoAsiento() == EstadoAsiento.RESERVADO) {
                asiento.setEstadoAsiento(EstadoAsiento.DISPONIBLE);
            }
        }
        nuevosAsientos.forEach(asiento -> asiento.setEstadoAsiento(EstadoAsiento.RESERVADO));
        compra.actualizarDetalle(nuevosAsientos, servicios);
    }

    public void reasignarAsientos(ContextoCompra compra, List<Asiento> nuevosAsientos) {
        validarCompra(compra);
        validarAsientos(nuevosAsientos);
        if ("CANCELADA".equals(compra.obtenerNombreEstado()) || "REEMBOLSADA".equals(compra.obtenerNombreEstado())) {
            throw new IllegalStateException("No se pueden reasignar asientos de una compra cerrada.");
        }
        validarDisponibilidad(nuevosAsientos, compra);

        List<Asiento> anteriores = new ArrayList<>(compra.getAsientos());
        anteriores.stream()
                .filter(asiento -> !nuevosAsientos.contains(asiento))
                .forEach(asiento -> asiento.setEstadoAsiento(EstadoAsiento.DISPONIBLE));

        EstadoAsiento estadoDestino = compra.permiteModificacion() ? EstadoAsiento.RESERVADO : EstadoAsiento.VENDIDO;
        nuevosAsientos.forEach(asiento -> asiento.setEstadoAsiento(estadoDestino));
        compra.reasignarAsientos(nuevosAsientos);

        if (!compra.permiteModificacion()) {
            compra.registrarEntradas(generarEntradas(compra));
        }
    }

    public void cancelarCompra(ContextoCompra compra) {
        validarCompra(compra);
        if (!compra.permiteModificacion()) {
            throw new IllegalStateException("Solo se puede cancelar antes de confirmar el pago.");
        }
        liberarAsientos(compra);
        compra.realizarCancelacion();
    }

    public void pagarCompra(ContextoCompra compra, TipoPago metodoPago) {
        validarCompra(compra);
        if (metodoPago == null || metodoPago == TipoPago.NINGUNO) {
            throw new IllegalArgumentException("Selecciona un metodo de pago valido.");
        }
        if (!compra.permiteModificacion()) {
            throw new IllegalStateException("La compra no esta lista para pago.");
        }
        compra.realizarPago(metodoPago);
        compra.getAsientos().forEach(asiento -> asiento.setEstadoAsiento(EstadoAsiento.VENDIDO));
        compra.registrarEntradas(generarEntradas(compra));
    }

    public void confirmarCompra(ContextoCompra compra) {
        validarCompra(compra);
        if (!"PAGADA".equals(compra.obtenerNombreEstado()) && !"INCIDENCIA".equals(compra.obtenerNombreEstado())) {
            throw new IllegalStateException("Solo se puede confirmar una compra pagada o con incidencia resuelta.");
        }
        compra.confirmar();
    }

    public void reembolsarCompra(ContextoCompra compra) {
        validarCompra(compra);
        if (!"PAGADA".equals(compra.obtenerNombreEstado())
                && !"CONFIRMADA".equals(compra.obtenerNombreEstado())
                && !"INCIDENCIA".equals(compra.obtenerNombreEstado())) {
            throw new IllegalStateException("Solo se puede reembolsar una compra pagada, confirmada o con incidencia.");
        }
        compra.reembolsar();
        if ("REEMBOLSADA".equals(compra.obtenerNombreEstado())) {
            liberarAsientos(compra);
            compra.anularEntradas();
        }
    }

    public void reportarIncidencia(ContextoCompra compra) {
        validarCompra(compra);
        if ("CANCELADA".equals(compra.obtenerNombreEstado()) || "REEMBOLSADA".equals(compra.obtenerNombreEstado())) {
            throw new IllegalStateException("No se puede reportar incidencia sobre una compra cerrada.");
        }
        compra.reportarIncidencia();
    }

    public ObservableList<ContextoCompra> obtenerHistorial(Cliente cliente) {
        validarCliente(cliente);
        return FXCollections.observableArrayList(cliente.getHistorialCompras());
    }

    public ObservableList<ContextoCompra> obtenerTodasLasCompras(List<Usuario> usuarios) {
        return usuarios.stream()
                .filter(Cliente.class::isInstance)
                .map(Cliente.class::cast)
                .flatMap(cliente -> cliente.getHistorialCompras().stream())
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        FXCollections::observableArrayList
                ));
    }

    public ObservableList<ContextoCompra> filtrarTodasLasCompras(List<Usuario> usuarios, LocalDate fecha, String evento, String estado) {
        String busquedaEvento = evento == null ? "" : evento.toLowerCase();
        String estadoBuscado = estado == null ? "" : estado;
        return obtenerTodasLasCompras(usuarios).stream()
                .filter(compra -> fecha == null || compra.getFechaCreacion().equals(fecha))
                .filter(compra -> busquedaEvento.isBlank()
                        || compra.getEvento().getNombre().toLowerCase().contains(busquedaEvento)
                        || compra.getEvento().getIdEvento().toLowerCase().contains(busquedaEvento)
                        || compra.getIdCompra().toLowerCase().contains(busquedaEvento))
                .filter(compra -> estadoBuscado.isBlank() || compra.obtenerNombreEstado().equals(estadoBuscado))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        FXCollections::observableArrayList
                ));
    }

    public ObservableList<ContextoCompra> filtrarCompras(Cliente cliente, LocalDate fecha, String evento, String estado) {
        validarCliente(cliente);
        String busquedaEvento = evento == null ? "" : evento.toLowerCase();
        String estadoBuscado = estado == null ? "" : estado;
        return cliente.getHistorialCompras().stream()
                .filter(compra -> fecha == null || compra.getFechaCreacion().equals(fecha))
                .filter(compra -> busquedaEvento.isBlank()
                        || compra.getEvento().getNombre().toLowerCase().contains(busquedaEvento)
                        || compra.getEvento().getIdEvento().toLowerCase().contains(busquedaEvento))
                .filter(compra -> estadoBuscado.isBlank() || compra.obtenerNombreEstado().equals(estadoBuscado))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        FXCollections::observableArrayList
                ));
    }

    private void validarDisponibilidad(List<Asiento> asientos, ContextoCompra compraActual) {
        for (Asiento asiento : asientos) {
            boolean reservadoPorCompraActual = compraActual != null && compraActual.getAsientos().contains(asiento);
            boolean disponible = asiento.getEstadoAsiento() == EstadoAsiento.DISPONIBLE || reservadoPorCompraActual;
            if (!disponible) {
                throw new IllegalStateException("El asiento " + asiento.getIdAsiento() + " ya no esta disponible.");
            }
        }
    }

    private void liberarAsientos(ContextoCompra compra) {
        compra.getAsientos().forEach(asiento -> {
            if (asiento.getEstadoAsiento() == EstadoAsiento.RESERVADO || asiento.getEstadoAsiento() == EstadoAsiento.VENDIDO) {
                asiento.setEstadoAsiento(EstadoAsiento.DISPONIBLE);
            }
        });
    }

    private List<Entrada> generarEntradas(ContextoCompra compra) {
        List<Entrada> entradas = new ArrayList<>();
        for (Asiento asiento : compra.getAsientos()) {
            Zona zona = buscarZona(compra.getEvento(), asiento);
            if (zona != null) {
                entradas.add(entradaFactory.crear(compra, zona, asiento));
            }
        }
        return entradas;
    }

    private Zona buscarZona(Evento evento, Asiento asiento) {
        if (evento == null || evento.getRecinto() == null) {
            return null;
        }
        return evento.getRecinto().getZonas().stream()
                .filter(zona -> zona.getAsientos().contains(asiento))
                .findFirst()
                .orElse(null);
    }

    private void validarAsientos(List<Asiento> asientos) {
        if (asientos == null || asientos.isEmpty()) {
            throw new IllegalArgumentException("Selecciona al menos un asiento.");
        }
    }

    private void validarCompra(ContextoCompra compra) {
        if (compra == null) {
            throw new IllegalArgumentException("No hay una compra seleccionada.");
        }
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalStateException("La operacion requiere un cliente autenticado.");
        }
    }
}
