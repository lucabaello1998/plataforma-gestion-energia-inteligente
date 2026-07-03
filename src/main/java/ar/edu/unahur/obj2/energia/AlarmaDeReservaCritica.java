package ar.edu.unahur.obj2.energia;

import java.util.ArrayList;
import java.util.List;

public class AlarmaDeReservaCritica implements SistemaInteresado {

    private final List<String> alertas = new ArrayList<>();

    @Override
    public void reaccionarACarga(Bateria bateria, Double kwh) {
        verificarReserva(bateria);
    }

    @Override
    public void reaccionarAConsumo(Bateria bateria, Double kwh) {
        verificarReserva(bateria);
    }

    private void verificarReserva(Bateria bateria) {
        if (bateria.getNivelEnergia() < 0) {
            alertas.add("¡Alerta! La batería " + bateria.getId()
                    + " está usando reserva. Nivel actual: " + bateria.getNivelEnergia() + " kWh.");
        }
    }

    public List<String> getAlertas() {
        return new ArrayList<>(alertas);
    }
}
