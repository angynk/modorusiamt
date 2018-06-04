package polla.mt.controller.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polla.mt.model.dao.LogDao;
import polla.mt.model.dao.PolleroDao;
import polla.mt.model.entity.Log;
import polla.mt.model.entity.Pollero;

import java.util.Date;
import java.util.List;

@Service
public class ListaPosicionesServicio {

    @Autowired
    private PolleroDao polleroDao;

    @Autowired
    private LogDao logDao;


    public List<Pollero> obtenerListadoPosiciones() {
        return polleroDao.obtenerListadoPosiciones();
    }

    public String guardarPollero(Pollero pollero) {
        Pollero polleroExistente = polleroDao.buscarPollero(pollero.getCodigo());
        if(polleroExistente!=null){
         return "Ya existe el codigo: "+pollero.getCodigo();
        }
        polleroDao.guardarPollero(pollero);
        return "OK";
    }

    public Pollero obtenerPollero(Integer codigo) {
        return polleroDao.buscarPollero(codigo);
    }

    public void actualizarPollero(Pollero pollero) {
        polleroDao.actualizarPollero(pollero);
    }

    public void crearLogRegistro(Pollero pollero) {
        Log logPollero = new Log();
        logPollero.setFecha(new Date());
        logPollero.setPollero(pollero);
        logPollero.setPosicion(pollero.getPosicion());
        logPollero.setPuntuacion(pollero.getPuntuacion());
        logDao.addLogPollero(logPollero);
    }

    public List<Pollero> obtenerPollerosPorPuntaje() {
        return polleroDao.obtenerPollerosPorPuntaje();
    }
}
