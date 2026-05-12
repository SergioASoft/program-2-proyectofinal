package co.edu.uniquindio.poo.finalproject.controller.facade;

import co.edu.uniquindio.poo.finalproject.model.EstadoAsiento;
import co.edu.uniquindio.poo.finalproject.model.EstadoEvento;
import co.edu.uniquindio.poo.finalproject.model.Recinto;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.builder.Zona;
import co.edu.uniquindio.poo.finalproject.model.factory.Cliente;
import co.edu.uniquindio.poo.finalproject.model.factory.Usuario;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;
import javafx.collections.ObservableList;


public class ControladorFacade {
    private static ControladorFacade instance;
    ControladorEventos controladorEventos;
    ControladorUsuario controladorUsuario;

    private ControladorFacade() {
        this.controladorEventos = ControladorEventos.getInstance();
        this.controladorUsuario = ControladorUsuario.getInstance();
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
        cliente.getHistorialCompras().add(compra);
    }
}
