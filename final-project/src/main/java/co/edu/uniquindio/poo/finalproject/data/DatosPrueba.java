package co.edu.uniquindio.poo.finalproject.data;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.Asiento;
import co.edu.uniquindio.poo.finalproject.model.EstadoAsiento;
import co.edu.uniquindio.poo.finalproject.model.EstadoEvento;
import co.edu.uniquindio.poo.finalproject.model.Recinto;
import co.edu.uniquindio.poo.finalproject.model.TipoEvento;
import co.edu.uniquindio.poo.finalproject.model.TipoPago;
import co.edu.uniquindio.poo.finalproject.model.TipoPolitica;
import co.edu.uniquindio.poo.finalproject.model.TipoServicioAdicional;
import co.edu.uniquindio.poo.finalproject.model.TipoUsuario;
import co.edu.uniquindio.poo.finalproject.model.TipoZona;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.builder.Zona;
import co.edu.uniquindio.poo.finalproject.model.factory.Cliente;
import co.edu.uniquindio.poo.finalproject.model.factory.Usuario;
import co.edu.uniquindio.poo.finalproject.model.factory.UsuarioFactory;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class DatosPrueba {
    private static boolean inicializado;

    private DatosPrueba() {
    }

    public static void inicializar() {
        if (inicializado) {
            return;
        }
        inicializado = true;

        ControladorFacade facade = ControladorFacade.getInstance();
        UsuarioFactory usuarioFactory = new UsuarioFactory();

        Usuario admin = usuarioFactory.GetUsuario("cristian", "cristian", "123",
                "cristian@gmail.com", "123", TipoUsuario.ADMIN);
        Cliente cliente = (Cliente) usuarioFactory.GetUsuario("sergio", "sergio", "123",
                "sergio@gmail.com", "123", TipoUsuario.CLIENTE);
        Cliente clienteDos = (Cliente) usuarioFactory.GetUsuario("mafe", "mafe", "123",
                "mafe@gmail.com", "123", TipoUsuario.CLIENTE);
        cliente.setTipoPago(TipoPago.VISA);
        clienteDos.setTipoPago(TipoPago.PSE);

        facade.registrarUsuario(admin);
        facade.registrarUsuario(cliente);
        facade.registrarUsuario(clienteDos);

        Recinto estadio = new Recinto("REC-001", "Estadio Centenario", "Armenia", "Av. Bolivar",
                List.of(
                        crearZona("General Norte", TipoZona.GENERAL, 12, 45000, "G"),
                        crearZona("VIP Central", TipoZona.VIP, 8, 120000, "V"),
                        crearZona("Preferencial Occidente", TipoZona.PREFERENCIAL, 10, 80000, "P")
                ));
        Recinto auditorio = new Recinto("REC-002", "Auditorio Principal", "Cali", "Cra 12 #8-40",
                List.of(
                        crearZona("General", TipoZona.GENERAL, 10, 30000, "AG"),
                        crearZona("VIP", TipoZona.VIP, 6, 90000, "AV"),
                        crearZona("Preferencial", TipoZona.PREFERENCIAL, 8, 60000, "AP")
                ));
        facade.registrarRecinto(estadio);
        facade.registrarRecinto(auditorio);

        Evento concierto = new Evento.Builder("EV-001", "Concierto Ferxxo",
                "Ferxxo tour armenia.", "Armenia", LocalDate.now().plusDays(15), estadio)
                .tipoEvento(TipoEvento.CONCIERTO)
                .estadoEvento(EstadoEvento.PUBLICADO)
                .tipoPolitica(TipoPolitica.REMBOLSO)
                .hora("20:00")
                .build();
        Evento conferencia = new Evento.Builder("EV-002", "Colombia 5.0",
                "Conferencia sobre innovacion y software.", "Pereira", LocalDate.now().plusDays(25), auditorio)
                .tipoEvento(TipoEvento.CONFERENCIA)
                .estadoEvento(EstadoEvento.PUBLICADO)
                .tipoPolitica(TipoPolitica.CANCELACION)
                .hora("09:00")
                .build();
        facade.registrarEvento(concierto);
        facade.registrarEvento(conferencia);

        /*facade.setUsuarioActual(cliente);
        ContextoCompra compraPagada = facade.crearCompra(concierto,
                List.of(estadio.getZonas().get(0).getAsientos().get(0), estadio.getZonas().get(1).getAsientos().get(0)),
                List.of(TipoServicioAdicional.SEGURO, TipoServicioAdicional.PARQUEADERO));
        facade.pagarCompra(compraPagada, TipoPago.VISA);
        facade.confirmarCompra(compraPagada);

        facade.crearCompra(conferencia,
                List.of(auditorio.getZonas().get(2).getAsientos().get(0)),
                List.of(TipoServicioAdicional.MERCHANDISING));

        facade.setUsuarioActual(clienteDos);
        ContextoCompra compraIncidencia = facade.crearCompra(concierto,
                List.of(estadio.getZonas().get(2).getAsientos().get(1)),
                List.of(TipoServicioAdicional.ACCESO_PREFERENCIAL));
        facade.pagarCompra(compraIncidencia, TipoPago.PSE);
        facade.reportarIncidencia(compraIncidencia);

        facade.setUsuarioActual(null);*/
    }

    private static Zona crearZona(String nombre, TipoZona tipo, int capacidad, int precio, String prefijo) {
        List<Asiento> asientos = new ArrayList<>();
        for (int i = 1; i <= capacidad; i++) {
            asientos.add(new Asiento(prefijo + i, 1, i, EstadoAsiento.DISPONIBLE, precio));
        }
        return new Zona.Builder(nombre, tipo, capacidad, asientos)
                .reglas(tipo == TipoZona.VIP ? "Acceso exclusivo VIP" : "Reglas generales")
                .build();
    }
}
