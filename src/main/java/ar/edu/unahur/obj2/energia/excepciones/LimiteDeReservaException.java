package ar.edu.unahur.obj2.energia.excepciones;

public class LimiteDeReservaException extends Exception {

    private final String bateriaId;
    private final Double cantidadSolicitada;

    public LimiteDeReservaException(String bateriaId, Double cantidadSolicitada, String message) {
        super(message);
        this.bateriaId = bateriaId;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public String getBateriaId() {
        return bateriaId;
    }

    public Double getCantidadSolicitada() {
        return cantidadSolicitada;
    }
}
