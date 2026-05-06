package co.edu.uniquindio.poo.finalproject.controller;
import co.edu.uniquindio.poo.finalproject.model.TipoUsuario;
import co.edu.uniquindio.poo.finalproject.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class ControladorUsuario {
    private static ControladorUsuario instance;
    private final ObservableList<Usuario> listaClientes = FXCollections.observableArrayList();
    private final ObservableList<Usuario> listaAdmins = FXCollections.observableArrayList();
    private Usuario usuarioActual;

    public static ControladorUsuario getInstance(){
        if (instance == null){
            instance = new ControladorUsuario();
        }
        return instance;
    }
    public void registrarUsuario(Usuario usuario){
        if(usuario.getTipoUsuario() == TipoUsuario.ADMIN){
            listaAdmins.add(usuario);
            usuarioActual = usuario;
            return;
        }
        usuarioActual = usuario;
        listaClientes.add(usuario);
    }

    public Usuario obtenerUsuario(String id, String contrasena) {
        return listaClientes.stream()
                .filter(u -> u.getIdUsuario().equals(id) && u.getContrasena().equals(contrasena))
                .findFirst()
                .orElse(listaAdmins.stream()
                        .filter(u -> u.getIdUsuario().equals(id) && u.getContrasena().equals(contrasena))
                        .findFirst()
                        .orElse(null));
    }
    public boolean encontrarUsuario(String id){
        return listaClientes.stream()
                .anyMatch(u -> u.getIdUsuario().equals(id));
    }
    public boolean actualizarUsuario(String idActual, String nuevoId, String nuevaContrasena) {
        Usuario u = listaClientes.stream()
                .filter(user -> user.getIdUsuario().equals(idActual))
                .findFirst()
                .orElse(listaAdmins.stream()
                        .filter(user -> user.getIdUsuario().equals(idActual))
                        .findFirst()
                        .orElse(null));
        if (u != null) {
            u.setIdUsuario(nuevoId);
            u.setContrasena(nuevaContrasena);
            return true;
        }
        return false;
    }

    public Usuario getUsuarioActual() {

        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {

        this.usuarioActual = usuarioActual;
    }
}