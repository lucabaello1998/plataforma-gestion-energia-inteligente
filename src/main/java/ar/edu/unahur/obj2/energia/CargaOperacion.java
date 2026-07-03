package ar.edu.unahur.obj2.energia;

import ar.edu.unahur.obj2.energia.excepciones.CantidadInvalidaException;

public class CargaOperacion implements OperacionDeEnergia {

    private final Bateria bateria;
    private final Double cantidad;

    public CargaOperacion(Bateria bateria, Double cantidad) {
        if (cantidad <= 0) {
            throw new CantidadInvalidaException("La cantidad a cargar debe ser mayor a 0 kWh.");
        }
        this.bateria = bateria;
        this.cantidad = cantidad;
    }

    @Override
    public void ejecutar() {
        bateria.cargar(cantidad);
    }

    @Override
    public void deshacer() {
        bateria.revertirCarga(cantidad);
    }
}
