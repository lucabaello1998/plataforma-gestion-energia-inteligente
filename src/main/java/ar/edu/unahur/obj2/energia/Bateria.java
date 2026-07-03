package ar.edu.unahur.obj2.energia;

import ar.edu.unahur.obj2.energia.excepciones.LimiteDeReservaException;

public class Bateria {

    public static final Double LIMITE_RESERVA = -5000.0;

    private final String id;
    private Double nivelEnergia;

    public Bateria(String id, Double nivelEnergiaInicial) {
        this.id = id;
        this.nivelEnergia = nivelEnergiaInicial;
    }

    public String getId() {
        return id;
    }

    public Double getNivelEnergia() {
        return nivelEnergia;
    }

    public void cargar(Double kwh) {
        nivelEnergia += kwh;
    }

    public void consumir(Double kwh) throws LimiteDeReservaException {
        if (nivelEnergia - kwh < LIMITE_RESERVA) {
            throw new LimiteDeReservaException(id, kwh,
                    "La batería " + id + " no puede consumir " + kwh
                            + " kWh: se superaría el límite de reserva de " + LIMITE_RESERVA + " kWh.");
        }
        nivelEnergia -= kwh;
    }

    void revertirCarga(Double kwh) {
        nivelEnergia -= kwh;
    }

    void revertirConsumo(Double kwh) {
        nivelEnergia += kwh;
    }
}
