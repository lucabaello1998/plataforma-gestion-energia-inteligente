package ar.edu.unahur.obj2.energia;

import java.util.ArrayList;
import java.util.List;

public class RegistroDeAuditoria implements SistemaInteresado {

    private final List<String> registros = new ArrayList<>();

    @Override
    public void reaccionarACarga(Bateria bateria, Double kwh) {
        registrar(bateria, "carga", kwh);
    }

    @Override
    public void reaccionarAConsumo(Bateria bateria, Double kwh) {
        registrar(bateria, "consumo", kwh);
    }

    private void registrar(Bateria bateria, String tipo, Double kwh) {
        registros.add("Batería " + bateria.getId() + ": " + tipo + " de " + kwh
                + " kWh. Nivel actual: " + bateria.getNivelEnergia() + " kWh.");
    }

    public List<String> getRegistros() {
        return new ArrayList<>(registros);
    }
}
