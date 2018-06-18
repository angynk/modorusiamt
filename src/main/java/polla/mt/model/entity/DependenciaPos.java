package polla.mt.model.entity;

import java.util.List;

public class DependenciaPos {

    private List<DependenciaJSON> posiciones;

    public DependenciaPos() {
    }

    public List<DependenciaJSON> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(List<DependenciaJSON> posiciones) {
        this.posiciones = posiciones;
    }
}
