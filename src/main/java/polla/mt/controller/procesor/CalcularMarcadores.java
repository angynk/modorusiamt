package polla.mt.controller.procesor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import polla.mt.controller.servicios.ListaPosicionesServicio;
import polla.mt.model.entity.Listado;
import polla.mt.model.entity.Pollero;
import polla.mt.model.entity.PolleroJSON;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CalcularMarcadores {

    @Autowired
    private ListaPosicionesServicio listaPosicionesServicio;

    private Listado listado;
    private Date fecha;

    public CalcularMarcadores() {
        this.listado = new Listado();
    }

    public List<PolleroJSON> obtenerListadoPosiciones() {
        List<Pollero> polleros = listaPosicionesServicio.obtenerListadoPosiciones();
        List<PolleroJSON> polleroJSONS = new ArrayList<>();
        fecha = new Date();
        for(Pollero pollero:polleros){
            polleroJSONS.add(new PolleroJSON(pollero.getApodo(),pollero.getPuntuacion(),pollero.getPosicion()));
            fecha = pollero.getFecha();
        }
        return polleroJSONS;
    }

    public Boolean cargarListado() {
        listado = new Listado();
        listado.setPosiciones(obtenerListadoPosiciones());
        listado.setFechaActualizacion(transformDate(fecha));

        return true;
    }

    public Listado getListado() {
        return listado;
    }

    public void setListado(Listado listado) {
        this.listado = listado;
    }

    public String transformDate(Date date){
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        return sdfDate.format(date);
    }
}
