package polla.mt.model.entity;

import java.util.Date;
import java.util.List;

public class Listado {

    private List<PolleroJSON> posiciones;
    private String fechaActualizacion;

    public Listado() {
    }

    public List<PolleroJSON> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(List<PolleroJSON> posiciones) {
        this.posiciones = posiciones;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
