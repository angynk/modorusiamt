package polla.mt.model.entity;

public class PolleroJSON {

    private String apodo;
    private Integer puntuacion;
    private Integer posicion;

    public PolleroJSON() {
    }

    public PolleroJSON(String apodo, Integer puntuacion, Integer posicion) {
        this.apodo = apodo;
        this.puntuacion = puntuacion;
        this.posicion = posicion;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }
}
