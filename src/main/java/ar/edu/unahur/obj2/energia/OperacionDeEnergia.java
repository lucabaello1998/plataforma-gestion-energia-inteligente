package ar.edu.unahur.obj2.energia;

import ar.edu.unahur.obj2.energia.excepciones.LimiteDeReservaException;

public interface OperacionDeEnergia {
    void ejecutar() throws LimiteDeReservaException;

    void deshacer();
}
