package ar.edu.unahur.obj2.energia;

import ar.edu.unahur.obj2.energia.excepciones.LimiteDeReservaException;

import java.util.ArrayList;
import java.util.List;

public class Rutina implements OperacionDeEnergia {

    private final List<OperacionDeEnergia> pendientes = new ArrayList<>();
    private final List<OperacionDeEnergia> ejecutadas = new ArrayList<>();

    public void agregarOperacion(OperacionDeEnergia operacion) {
        pendientes.add(operacion);
    }

    public List<OperacionDeEnergia> getPendientes() {
        return new ArrayList<>(pendientes);
    }

    @Override
    public void ejecutar() throws LimiteDeReservaException {
        try {
            for (OperacionDeEnergia operacion : pendientes) {
                operacion.ejecutar();
                ejecutadas.add(operacion);
            }
        } catch (LimiteDeReservaException e) {
            deshacer();
            throw e;
        } finally {
            pendientes.clear();
        }
    }

    @Override
    public void deshacer() {
        for (int i = ejecutadas.size() - 1; i >= 0; i--) {
            ejecutadas.get(i).deshacer();
        }
        ejecutadas.clear();
    }
}
