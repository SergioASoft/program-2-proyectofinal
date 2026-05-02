package co.edu.uniquindio.poo.finalproject.model;

public abstract class Usuario {
    private String idUsuario;
    private String contrasena;
    private String nombre;
    private String correo;
    private String numero;
    private TipoUsuario tipoUsuario;

    public Usuario(String idUsuario, String contrasena , String nombre, String correo, String numero, TipoUsuario tipoUsuario) {
        this.idUsuario = idUsuario;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.correo = correo;
        this.numero = numero;
        this.tipoUsuario = tipoUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }
}
