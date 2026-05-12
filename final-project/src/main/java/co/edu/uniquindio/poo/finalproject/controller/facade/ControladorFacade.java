package co.edu.uniquindio.poo.finalproject.controller.facade;

import co.edu.uniquindio.poo.finalproject.model.EstadoAsiento;
import co.edu.uniquindio.poo.finalproject.model.EstadoEvento;
import co.edu.uniquindio.poo.finalproject.model.Incidencia;
import co.edu.uniquindio.poo.finalproject.model.Recinto;
import co.edu.uniquindio.poo.finalproject.model.TipoIncidencia;
import co.edu.uniquindio.poo.finalproject.model.TipoPago;
import co.edu.uniquindio.poo.finalproject.model.TipoServicioAdicional;
import co.edu.uniquindio.poo.finalproject.model.Asiento;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.builder.Zona;
import co.edu.uniquindio.poo.finalproject.model.factory.Cliente;
import co.edu.uniquindio.poo.finalproject.model.factory.Usuario;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;
import co.edu.uniquindio.poo.finalproject.model.strategy.ExportadorReporteCompras;
import co.edu.uniquindio.poo.finalproject.model.strategy.ExportadorReporteOperativo;
import co.edu.uniquindio.poo.finalproject.model.strategy.ReporteCompraStrategy;
import co.edu.uniquindio.poo.finalproject.model.strategy.ReporteOperativoStrategy;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class ControladorFacade {
    private static ControladorFacade instance;
    ControladorEventos controladorEventos;
    ControladorUsuario controladorUsuario;
    ControladorCompras controladorCompras;
    ControladorIncidencias controladorIncidencias;
    ControladorMetricas controladorMetricas;

    private ControladorFacade() {
        this.controladorEventos = ControladorEventos.getInstance();
        this.controladorUsuario = ControladorUsuario.getInstance();
        this.controladorCompras = ControladorCompras.getInstance();
        this.controladorIncidencias = ControladorIncidencias.getInstance();
        this.controladorMetricas = ControladorMetricas.getInstance();
    }

    public static ControladorFacade getInstance() {
        if (instance == null) {
            instance = new ControladorFacade();
        }
        return instance;
    }

    public void registrarUsuario(Usuario usuario) {
        controladorUsuario.registrarUsuario(usuario);
    }

    public Usuario iniciarSesion(String id, String contrasena) {
        Usuario usuario = controladorUsuario.obtenerUsuario(id, contrasena);
        if (usuario != null) {
            controladorUsuario.setUsuarioActual(usuario);
        }
        return usuario;
    }

    public boolean existeUsuario(String id) {
        return controladorUsuario.encontrarUsuario(id);
    }

    public boolean actualizarCredenciales(String idActual, String nuevoId, String nuevaContrasena) {
        return controladorUsuario.actualizarUsuario(idActual, nuevoId, nuevaContrasena);
    }

    public boolean actualizarUsuario(String idActual, String nuevoId, String nombre, String correo, String numero, String nuevaContrasena) {
        return controladorUsuario.actualizarUsuario(idActual, nuevoId, nombre, correo, numero, nuevaContrasena);
    }

    public boolean eliminarUsuario(Usuario usuario) {
        return controladorUsuario.eliminarUsuario(usuario);
    }

    public ObservableList<Usuario> getTodosLosUsuarios() {
        return controladorUsuario.getListaUsuarios();
    }

    public Usuario getUsuarioActual() {
        return controladorUsuario.getUsuarioActual();
    }

    public void setUsuarioActual(Usuario usuario) {
        controladorUsuario.setUsuarioActual(usuario);
    }

    public void registrarEvento(Evento evento) {
        controladorEventos.registrarEvento(evento);
    }

    public void registrarRecinto(Recinto recinto) {
        controladorEventos.registrarRecinto(recinto);
    }

    public Evento obtenerEvento(String id) {
        return controladorEventos.obtenerEvento(id);
    }

    public ObservableList<Evento> getEventosPublicados() {
        return controladorEventos.getEventosPublicados();
    }

    public ObservableList<Evento> getTodosLosEventos() {
        return controladorEventos.getListaEventos();
    }

    public ObservableList<Evento> filtrarEventosAdmin(String texto, co.edu.uniquindio.poo.finalproject.model.TipoEvento tipo, EstadoEvento estado, java.time.LocalDate fecha) {
        return controladorEventos.filtrarEventos(texto, tipo, estado, fecha);
    }

    public ObservableList<Recinto> getTodosLosRecintos() {
        return controladorEventos.getListaRecintos();
    }

    public void cambiarEstadoEvento(Evento evento, EstadoEvento nuevoEstado) {
        if (evento != null) {
            evento.setEstadoEvento(nuevoEstado);
        }
    }
    public boolean eliminarEvento(Evento evento) {
        return controladorEventos.eliminarEvento(evento);
    }
    public boolean eliminarRecinto(Recinto recinto) {
        return controladorEventos.eliminarRecinto(recinto);
    }
    public void registrarCompraFinalizada(ContextoCompra compra) {
        Cliente cliente = (Cliente) getUsuarioActual();
        if (!cliente.getHistorialCompras().contains(compra)) {
            cliente.getHistorialCompras().add(compra);
        }
    }

    public ContextoCompra crearCompra(Evento evento, List<Asiento> asientos, List<TipoServicioAdicional> servicios) {
        return controladorCompras.crearCompra(obtenerClienteActual(), evento, asientos, servicios);
    }

    public void actualizarCompra(ContextoCompra compra, List<Asiento> asientos, List<TipoServicioAdicional> servicios) {
        controladorCompras.actualizarCompra(compra, asientos, servicios);
    }

    public void reasignarAsientosCompra(ContextoCompra compra, List<Asiento> asientos) {
        controladorCompras.reasignarAsientos(compra, asientos);
    }

    public void cancelarCompra(ContextoCompra compra) {
        controladorCompras.cancelarCompra(compra);
    }

    public void pagarCompra(ContextoCompra compra, TipoPago metodoPago) {
        controladorCompras.pagarCompra(compra, metodoPago);
    }

    public void confirmarCompra(ContextoCompra compra) {
        controladorCompras.confirmarCompra(compra);
    }

    public void reembolsarCompra(ContextoCompra compra) {
        controladorCompras.reembolsarCompra(compra);
    }

    public void reportarIncidencia(ContextoCompra compra) {
        reportarIncidencia(compra, TipoIncidencia.OPERATIVA, "Incidencia reportada sobre la compra.");
    }

    public Incidencia reportarIncidencia(ContextoCompra compra, TipoIncidencia tipo, String descripcion) {
        controladorCompras.reportarIncidencia(compra);
        return controladorIncidencias.registrar(tipo, descripcion, compra.getEvento(), compra, compra.getUsuarioAsociado());
    }

    public Incidencia registrarIncidenciaEvento(Evento evento, TipoIncidencia tipo, String descripcion) {
        if (tipo == TipoIncidencia.CANCELACION_EVENTO || tipo == TipoIncidencia.CANCELACION_MASIVA) {
            cambiarEstadoEvento(evento, EstadoEvento.CANCELADO);
        }
        return controladorIncidencias.registrar(tipo, descripcion, evento, null, getUsuarioActual());
    }

    public ObservableList<Incidencia> getIncidencias() {
        return controladorIncidencias.getIncidencias();
    }

    public ObservableList<Incidencia> filtrarIncidencias(LocalDate desde, LocalDate hasta, TipoIncidencia tipo) {
        return controladorIncidencias.filtrar(desde, hasta, tipo);
    }

    public ObservableList<ContextoCompra> getHistorialComprasCliente() {
        return controladorCompras.obtenerHistorial(obtenerClienteActual());
    }

    public ObservableList<ContextoCompra> filtrarComprasCliente(LocalDate fecha, String evento, String estado) {
        return controladorCompras.filtrarCompras(obtenerClienteActual(), fecha, evento, estado);
    }

    public ObservableList<ContextoCompra> getTodasLasCompras() {
        return controladorCompras.obtenerTodasLasCompras(controladorUsuario.getListaUsuarios());
    }

    public ObservableList<ContextoCompra> filtrarTodasLasCompras(LocalDate fecha, String evento, String estado) {
        return controladorCompras.filtrarTodasLasCompras(controladorUsuario.getListaUsuarios(), fecha, evento, estado);
    }

    public void exportarCompras(List<ContextoCompra> compras, File destino, ReporteCompraStrategy estrategia) throws IOException {
        new ExportadorReporteCompras(estrategia).exportar(compras, destino);
    }

    public void exportarReporteOperativo(List<String[]> filas, File destino, ReporteOperativoStrategy estrategia) throws IOException {
        new ExportadorReporteOperativo(estrategia).exportar(filas, destino);
    }

    public void cambiarEstadoAsiento(Asiento asiento, EstadoAsiento estado) {
        if (asiento != null && estado != null) {
            asiento.setEstadoAsiento(estado);
        }
    }

    public void agregarAsiento(Zona zona, Asiento asiento) {
        if (zona != null && asiento != null) {
            zona.getAsientos().add(asiento);
        }
    }

    public void actualizarAsiento(Asiento asiento, String id, int fila, int numero, int precio, EstadoAsiento estado) {
        if (asiento != null) {
            asiento.setIdAsiento(id);
            asiento.setFila(fila);
            asiento.setNumero(numero);
            asiento.setPrecio(precio);
            asiento.setEstadoAsiento(estado);
        }
    }

    public boolean eliminarAsiento(Zona zona, Asiento asiento) {
        return zona != null && asiento != null && zona.getAsientos().remove(asiento);
    }

    public Map<LocalDate, Integer> ventasPorPeriodo() {
        return controladorMetricas.ventasPorPeriodo(getTodasLasCompras());
    }

    public Map<String, Double> ocupacionPorZona() {
        return controladorMetricas.ocupacionPorZona(getTodosLosEventos());
    }

    public Map<TipoServicioAdicional, Integer> ingresosPorServicios() {
        return controladorMetricas.ingresosPorServicios(getTodasLasCompras());
    }

    public double tasaCancelacion() {
        return controladorMetricas.tasaCancelacion(getTodasLasCompras());
    }

    public Map<String, Long> topEventos() {
        return controladorMetricas.topEventos(getTodasLasCompras());
    }

    private Cliente obtenerClienteActual() {
        Usuario usuario = getUsuarioActual();
        if (usuario instanceof Cliente cliente) {
            return cliente;
        }
        throw new IllegalStateException("La operacion requiere un cliente autenticado.");
    }
}
