package ar.edu.unahur.obj2.energia;

public interface SistemaInteresado {
    void reaccionarACarga(Bateria bateria, Double kwh);

    void reaccionarAConsumo(Bateria bateria, Double kwh);
}
