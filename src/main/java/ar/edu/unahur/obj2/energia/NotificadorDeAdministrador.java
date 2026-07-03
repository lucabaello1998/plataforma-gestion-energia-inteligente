package ar.edu.unahur.obj2.energia;

import java.util.ArrayList;
import java.util.List;

public class NotificadorDeAdministrador implements SistemaInteresado {

    private final String administrador;
    private final List<String> mensajesEnviados = new ArrayList<>();

    public NotificadorDeAdministrador(String administrador) {
        this.administrador = administrador;
    }

    @Override
    public void reaccionarACarga(Bateria bateria, Double kwh) {
        enviar("Se han cargado " + kwh + " kWh en su batería " + bateria.getId() + ".");
    }

    @Override
    public void reaccionarAConsumo(Bateria bateria, Double kwh) {
        enviar("Se han consumido " + kwh + " kWh en su batería " + bateria.getId() + ".");
    }

    private void enviar(String mensaje) {
        mensajesEnviados.add(mensaje);
    }

    public String getAdministrador() {
        return administrador;
    }

    public List<String> getMensajesEnviados() {
        return new ArrayList<>(mensajesEnviados);
    }
}
