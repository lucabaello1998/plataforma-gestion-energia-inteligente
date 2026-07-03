package ar.edu.unahur.obj2.energia;

import ar.edu.unahur.obj2.energia.excepciones.LimiteDeReservaException;

import java.util.HashMap;
import java.util.Map;

public class ControladorDeOperaciones {

    private final Map<String, Rutina> rutinas = new HashMap<>();

    public void ejecutarInmediata(OperacionDeEnergia operacion) throws LimiteDeReservaException {
        operacion.ejecutar();
    }

    public void registrarEnRutina(String nombreRutina, OperacionDeEnergia operacion) {
        obtenerORegistrarRutina(nombreRutina).agregarOperacion(operacion);
    }

    public void ejecutarRutina(String nombreRutina) throws LimiteDeReservaException {
        obtenerORegistrarRutina(nombreRutina).ejecutar();
    }

    private Rutina obtenerORegistrarRutina(String nombreRutina) {
        return rutinas.computeIfAbsent(nombreRutina, nombre -> new Rutina());
    }
}
