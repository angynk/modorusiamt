package polla.mt.controller.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polla.mt.controller.procesor.CalcularMarcadores;
import polla.mt.controller.procesor.CalcularPrimeraFecha;
import polla.mt.controller.procesor.CargarPolleros;
import polla.mt.model.entity.Listado;

import java.util.List;


@RestController
@RequestMapping("/data")
public class ConfigRestController {

    @Autowired
    public CalcularMarcadores calcularMarcadores;

    @Autowired
    public CalcularPrimeraFecha calcularPrimeraFecha;

    @Autowired
    public CargarPolleros cargarPolleros;


    @RequestMapping(value = "/test/", method = RequestMethod.GET)
    public ResponseEntity<String> test() {
        return new ResponseEntity<String>("Hola Mundo", HttpStatus.OK);
    }

    @RequestMapping(value = "/cargar-listado/", method = RequestMethod.GET)
    public ResponseEntity<Boolean> cargarListado() {
        Boolean valor = calcularMarcadores.cargarListado();
        return new ResponseEntity<Boolean>(valor, HttpStatus.OK);
    }

    @RequestMapping(value = "/cargar-polleros/", method = RequestMethod.GET)
    public ResponseEntity<List<String>> cargarPolleros() {
        List<String> valor = cargarPolleros.cargarPolleros();
        calcularMarcadores.cargarListado();
        return new ResponseEntity<List<String>>(valor, HttpStatus.OK);
    }

    @RequestMapping(value = "/cargar-primeraFecha/", method = RequestMethod.GET)
    public ResponseEntity<List<String>> calcularPrimeraFecha() {
        List<String> valor = calcularPrimeraFecha.calcularPuntajePrimeraFecha();
        calcularMarcadores.cargarListado();
        return new ResponseEntity<List<String>>(valor, HttpStatus.OK);
    }

    @RequestMapping(value = "/cargar-posiciones/", method = RequestMethod.GET)
    public ResponseEntity<Boolean> cargarPosiciones() {
        Boolean valor = calcularPrimeraFecha.cargarPosiciones();
        calcularMarcadores.cargarListado();
        return new ResponseEntity<Boolean>(valor, HttpStatus.OK);
    }

    @RequestMapping(value = "/listado/", method = RequestMethod.GET)
    public ResponseEntity<Listado> listado() {
        Listado listado = calcularMarcadores.getListado();
        return new ResponseEntity<Listado>(listado, HttpStatus.OK);
    }



}
