package co.edu.uniquindio.poo.finalproject.model;

public class Plataforma implements IPlataforma{

    private static Plataforma instance;
    private Usuario usuarioActual;
    private boolean superUsuario;

    public static Plataforma getInstance(){
        if(instance == null){
            instance = new Plataforma();
        }
        return instance;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public boolean isSuperUsuario() {
        return superUsuario;
    }

    @Override
    public void concederPermisos() {
        superUsuario = true;
    }
    public void negarPermisos(){
        superUsuario = false;
    }
}
