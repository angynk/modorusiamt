package polla.mt.controller.procesor;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import polla.mt.controller.servicios.ListaPosicionesServicio;
import polla.mt.model.entity.Pollero;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Component
public class CalcularPrimeraFecha {

    public static String rutaArchivoBase = "C:\\temp\\PrimeraFecha.xls";

    @Autowired
    private ListaPosicionesServicio listaPosicionesServicio;
    private  HashMap<String,List<Integer>> resultados;

    public List<String> calcularPuntajePrimeraFecha() {

        return readExcelAndSaveData();
    }

    public List<String> readExcelAndSaveData(){
        List<String> errores = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(rutaArchivoBase);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = worksheet.iterator();
            rowIterator.next();
            int posicion = 1;
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                if( row.getCell(0) != null ){
                    if(posicion==1){
                        resultados = obtenerDatosPartidos(row);
                    }else{
                        Integer codigo = (int) row.getCell(PrimeraFechaDEF.CODIGO).getNumericCellValue();
                        String correo =  row.getCell(PrimeraFechaDEF.CORREO).getStringCellValue();
                        Pollero pollero = listaPosicionesServicio.obtenerPollero(codigo,correo);
                        if(pollero!=null){
                            listaPosicionesServicio.crearLogRegistro(pollero);
                            pollero.setPuntuacion(calcularNuevoPuntaje(pollero.getPuntuacion(),row));
                            pollero.setFecha(new Date());
                            listaPosicionesServicio.actualizarPollero(pollero);
                        }else {
                            errores.add("El Pollero "+codigo+" No fue encontrado");
                        }
                    }

                }else{
                    break;
                }
                posicion++;
            }
            fileInputStream.close();
            reorganizarPosiciones();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        return errores;
    }

    private Integer calcularNuevoPuntaje(Integer puntuacion, Row row) {
        HashMap<String,List<Integer>> predicciones = obtenerDatosPartidos(row);
     //   puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_1,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_2,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_3,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_4,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_5,predicciones);
        puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_6,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_7,predicciones);
        puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_8,predicciones);
        puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_9,predicciones);
        puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_10,predicciones);
        puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_11,predicciones);
      //  puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_12,predicciones);
      //  puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_13,predicciones);
      //  puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_14,predicciones);
      //  puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_15,predicciones);
      //  puntuacion = puntuacion + resultadoPrediccion(PrimeraFechaDEF.PARTIDO_16,predicciones);
        return puntuacion;
    }

    private Integer resultadoPrediccion(String partido, HashMap<String, List<Integer>> predicciones) {
        List<Integer> prediccion = predicciones.get(partido);
        List<Integer> resultado = resultados.get(partido);
        int puntaje = 0;
        if(marcadorExacto(prediccion,resultado)){
            puntaje = puntaje + PrimeraFechaDEF.PUNTOS_MARCADOR;
        }

        if(resultadoExacto(prediccion,resultado)){
            puntaje = puntaje + PrimeraFechaDEF.PUNTOS_RESULTADO;
        }

        return puntaje;
    }

    private boolean resultadoExacto(List<Integer> prediccion, List<Integer> resultado) {
        //Gano el primer Equipo
        if(resultado.get(0)>resultado.get(1)){
            if(prediccion.get(0)>prediccion.get(1)){
                return true;
            }else{
                return false;
            }
        }
        //Gano el segundo Equipo
        if(resultado.get(0)<resultado.get(1)){
            if(prediccion.get(0)<prediccion.get(1)){
                return true;
            }else{
                return false;
            }
        }
        //Fue un empate
        if(resultado.get(0)==resultado.get(1)){
            if(prediccion.get(0)==prediccion.get(1)){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    private boolean marcadorExacto(List<Integer> prediccion, List<Integer> resultado) {
        if(prediccion.get(0) == resultado.get(0)){
            if(prediccion.get(1) == resultado.get(1)){
                return true;
            }
        }
        return false;
    }

    private HashMap<String, List<Integer>> obtenerDatosPartidos(Row row) {
        HashMap<String,List<Integer>> predicciones = new HashMap<String,List<Integer>>();
       // predicciones.put(PrimeraFechaDEF.PARTIDO_1,obtenerValores(PrimeraFechaDEF.RUSIA,PrimeraFechaDEF.ARABIA,row));
       // predicciones.put(PrimeraFechaDEF.PARTIDO_2,obtenerValores(PrimeraFechaDEF.EGIPTO,PrimeraFechaDEF.URUGUAY,row));
       // predicciones.put(PrimeraFechaDEF.PARTIDO_3,obtenerValores(PrimeraFechaDEF.MARRUECOS,PrimeraFechaDEF.IRAN,row));
       // predicciones.put(PrimeraFechaDEF.PARTIDO_4,obtenerValores(PrimeraFechaDEF.PORTUGAL,PrimeraFechaDEF.ESPANA,row));
      //  predicciones.put(PrimeraFechaDEF.PARTIDO_5,obtenerValores(PrimeraFechaDEF.FRANCIA,PrimeraFechaDEF.AUSTRALIA,row));
        predicciones.put(PrimeraFechaDEF.PARTIDO_6,obtenerValores(PrimeraFechaDEF.PERU,PrimeraFechaDEF.DINAMARCA,row));
       // predicciones.put(PrimeraFechaDEF.PARTIDO_7,obtenerValores(PrimeraFechaDEF.ARGENTINA,PrimeraFechaDEF.ISLANDIA,row));
        predicciones.put(PrimeraFechaDEF.PARTIDO_8,obtenerValores(PrimeraFechaDEF.CROACIA,PrimeraFechaDEF.NIGERIA,row));
        predicciones.put(PrimeraFechaDEF.PARTIDO_9,obtenerValores(PrimeraFechaDEF.COSTARICA,PrimeraFechaDEF.SERBIA,row));
        predicciones.put(PrimeraFechaDEF.PARTIDO_10,obtenerValores(PrimeraFechaDEF.BRASIL,PrimeraFechaDEF.SUIZA,row));
        predicciones.put(PrimeraFechaDEF.PARTIDO_11,obtenerValores(PrimeraFechaDEF.ALEMANIA,PrimeraFechaDEF.MEXICO,row));
      //  predicciones.put(PrimeraFechaDEF.PARTIDO_12,obtenerValores(PrimeraFechaDEF.SUECIA,PrimeraFechaDEF.COREA,row));
        //predicciones.put(PrimeraFechaDEF.PARTIDO_13,obtenerValores(PrimeraFechaDEF.BELGICA,PrimeraFechaDEF.PANAMA,row));
        //predicciones.put(PrimeraFechaDEF.PARTIDO_14,obtenerValores(PrimeraFechaDEF.TUNEZ,PrimeraFechaDEF.INGLATERRA,row));
        //predicciones.put(PrimeraFechaDEF.PARTIDO_15,obtenerValores(PrimeraFechaDEF.COLOMBIA,PrimeraFechaDEF.JAPON,row));
        //predicciones.put(PrimeraFechaDEF.PARTIDO_16,obtenerValores(PrimeraFechaDEF.POLONIA,PrimeraFechaDEF.SENEGAL,row));
        return predicciones;
    }

    private List<Integer> obtenerValores(int A, int B, Row row) {
        List<Integer> valor = new ArrayList<>();
        valor.add((int) row.getCell(A).getNumericCellValue());
        valor.add((int) row.getCell(B).getNumericCellValue());
        return valor;
    }

    private void reorganizarPosiciones() {
        List<Pollero> obtenerPollerosOrdenados = listaPosicionesServicio.obtenerPollerosPorPuntaje();
        int posicion = 1;
        int band = 0;
        Pollero anterior = null;
        for(Pollero pollero:obtenerPollerosOrdenados){
            if(band==0){
                pollero.setPosicion(posicion);
                listaPosicionesServicio.actualizarPollero(pollero);
            }else{
                if(anterior.getPuntuacion()!= pollero.getPuntuacion()){
                    posicion++;
                }
                pollero.setPosicion(posicion);
                listaPosicionesServicio.actualizarPollero(pollero);
            }
            anterior = pollero;
            band++;
        }
    }

    public boolean cargarPosiciones() {
        reorganizarPosiciones();
        return true;
    }
}
