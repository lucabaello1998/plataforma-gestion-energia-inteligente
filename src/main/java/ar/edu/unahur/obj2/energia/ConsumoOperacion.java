package ar.edu.unahur.obj2.energia;

import ar.edu.unahur.obj2.energia.excepciones.CantidadInvalidaException;
import ar.edu.unahur.obj2.energia.excepciones.LimiteDeReservaException;

public class ConsumoOperacion implements OperacionDeEnergia {

    private final Bateria bateria;
    private final Double cantidad;

    public ConsumoOperacion(Bateria bateria, Double cantidad) {
        if (cantidad <= 0) {
            throw new CantidadInvalidaException("La cantidad a consumir debe ser mayor a 0 kWh.");
        }
        this.bateria = bateria;
        this.cantidad = cantidad;
    }

    @Override
    public void ejecutar() throws LimiteDeReservaException {
        bateria.consumir(cantidad);
    }

    @Override
    public void deshacer() {
        bateria.revertirConsumo(cantidad);
    }
}
