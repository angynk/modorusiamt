package polla.mt.controller.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import polla.mt.controller.procesor.CalcularMarcadores;


@RestController
@RequestMapping("/data")
public class ConfigRestController {

    @Autowired
    public CalcularMarcadores calcularMarcadores;



    @RequestMapping(value = "/test/", method = RequestMethod.GET)
    public ResponseEntity<String> test() {
        return new ResponseEntity<String>("Hola Mundo", HttpStatus.OK);
    }


}
