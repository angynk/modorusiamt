package polla.mt.controller.procesor;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import polla.mt.controller.servicios.ListaPosicionesServicio;
import polla.mt.model.entity.Dependencia;
import polla.mt.model.entity.DependenciaJSON;
import polla.mt.model.entity.DependenciaPos;
import polla.mt.model.entity.Pollero;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Component
public class CalcularSegundaFecha {


    public static String rutaArchivoBase = "C:\\temp\\SegundaFecha.xls";

    @Autowired
    private ListaPosicionesServicio listaPosicionesServicio;
    private HashMap<String,List<Integer>> resultados;

    public List<String> calcularPuntajeSegundaFecha() {

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
                    System.out.println(posicion);
                    if(posicion==1){
                        resultados = obtenerDatosPartidos(row);
                    }else{
                        Integer codigo = (int) row.getCell(SegundaFechaDEF.CODIGO).getNumericCellValue();
                        String correo =  row.getCell(SegundaFechaDEF.CORREO).getStringCellValue();
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
           puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_1,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_2,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_3,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_4,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_5,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_6,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_7,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_8,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_9,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_10,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_11,predicciones);
        //     puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_12,predicciones);
        //    puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_13,predicciones);
        //    puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_14,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_15,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(SegundaFechaDEF.PARTIDO_16,predicciones);
        return puntuacion;
    }

    private Integer resultadoPrediccion(String partido, HashMap<String, List<Integer>> predicciones) {
        List<Integer> prediccion = predicciones.get(partido);
        List<Integer> resultado = resultados.get(partido);
        int puntaje = 0;
        if(marcadorExacto(prediccion,resultado)){
            puntaje = puntaje + SegundaFechaDEF.PUNTOS_MARCADOR;
        }

        if(resultadoExacto(prediccion,resultado)){
            puntaje = puntaje + SegundaFechaDEF.PUNTOS_RESULTADO;
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
         predicciones.put(SegundaFechaDEF.PARTIDO_1,obtenerValores(SegundaFechaDEF.RUSIA,SegundaFechaDEF.EGIPTO,row));
//         predicciones.put(SegundaFechaDEF.PARTIDO_2,obtenerValores(SegundaFechaDEF.URUGUAY,SegundaFechaDEF.ARABIA,row));
//         predicciones.put(SegundaFechaDEF.PARTIDO_3,obtenerValores(SegundaFechaDEF.PORTUGAL,SegundaFechaDEF.MARRUECOS,row));
//         predicciones.put(SegundaFechaDEF.PARTIDO_4,obtenerValores(SegundaFechaDEF.IRAN,SegundaFechaDEF.ESPANA,row));
//          predicciones.put(SegundaFechaDEF.PARTIDO_5,obtenerValores(SegundaFechaDEF.DINAMARCA,SegundaFechaDEF.AUSTRALIA,row));
//        predicciones.put(SegundaFechaDEF.PARTIDO_6,obtenerValores(SegundaFechaDEF.FRANCIA,SegundaFechaDEF.PERU,row));
//        predicciones.put(SegundaFechaDEF.PARTIDO_7,obtenerValores(SegundaFechaDEF.ARGENTINA,SegundaFechaDEF.CROACIA,row));
//        predicciones.put(SegundaFechaDEF.PARTIDO_8,obtenerValores(SegundaFechaDEF.NIGERIA,SegundaFechaDEF.ISLANDIA,row));
//        predicciones.put(SegundaFechaDEF.PARTIDO_9,obtenerValores(SegundaFechaDEF.BRASIL,SegundaFechaDEF.COSTARICA,row));
//        predicciones.put(SegundaFechaDEF.PARTIDO_10,obtenerValores(SegundaFechaDEF.SERBIA,SegundaFechaDEF.SUIZA,row));
//        predicciones.put(SegundaFechaDEF.PARTIDO_11,obtenerValores(SegundaFechaDEF.COREA,SegundaFechaDEF.MEXICO,row));
//        predicciones.put(SegundaFechaDEF.PARTIDO_12,obtenerValores(SegundaFechaDEF.ALEMANIA,SegundaFechaDEF.SUECIA,row));
//        predicciones.put(SegundaFechaDEF.PARTIDO_13,obtenerValores(SegundaFechaDEF.BELGICA,SegundaFechaDEF.TUNEZ,row));
//        predicciones.put(SegundaFechaDEF.PARTIDO_14,obtenerValores(SegundaFechaDEF.INGLATERRA,SegundaFechaDEF.PANAMA,row));
//        predicciones.put(SegundaFechaDEF.PARTIDO_15,obtenerValores(SegundaFechaDEF.JAPON,SegundaFechaDEF.SENEGAL,row));
//        predicciones.put(SegundaFechaDEF.PARTIDO_16,obtenerValores(SegundaFechaDEF.POLONIA,SegundaFechaDEF.COLOMBIA,row));
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

    public DependenciaPos cargarDependencias() {
        calcularPuntajeDependencias();
        reorganizarDependencias();
        List<Dependencia> dependencias = listaPosicionesServicio.cargarDependencias();

        DependenciaPos dependenciaPos = new DependenciaPos();
        List<DependenciaJSON> json = new ArrayList<>();
        for(Dependencia dependencia:dependencias){
            DependenciaJSON dep = new DependenciaJSON();
            dep.setDependencia(dependencia.getNombre());
            dep.setPosicion(dependencia.getPosicion());
            dep.setPuntuacion(dependencia.getPuntuacion());
            json.add(dep);
        }

        dependenciaPos.setPosiciones(json);

        return dependenciaPos;

    }

    private void reorganizarDependencias() {
        List<Dependencia> dependencias = listaPosicionesServicio.cargarDependencias();
        int posicion = 1;
        int band = 0;
        Dependencia anterior = null;
        for(Dependencia dependencia:dependencias){
            if(band==0){
                dependencia.setPosicion(posicion);
                listaPosicionesServicio.actualizarDependencia(dependencia);
            }else{
                if(anterior.getPuntuacion()!= dependencia.getPuntuacion()){
                    posicion++;
                }
                dependencia.setPosicion(posicion);
                listaPosicionesServicio.actualizarDependencia(dependencia);
            }
            anterior = dependencia;
            band++;
        }
    }

    private void calcularPuntajeDependencias() {
        List<Dependencia> dependencias = listaPosicionesServicio.cargarDependencias();
        for(Dependencia dependencia:dependencias){
            listaPosicionesServicio.calcularPuntuacion(dependencia);
        }
    }

    public DependenciaPos obtenerDependencias() {
        List<Dependencia> dependencias = listaPosicionesServicio.cargarDependencias();

        DependenciaPos dependenciaPos = new DependenciaPos();
        List<DependenciaJSON> json = new ArrayList<>();
        for(Dependencia dependencia:dependencias){
            DependenciaJSON dep = new DependenciaJSON();
            dep.setDependencia(dependencia.getNombre());
            dep.setPosicion(dependencia.getPosicion());
            dep.setPuntuacion(dependencia.getPuntuacion());
            json.add(dep);
        }

        dependenciaPos.setPosiciones(json);

        return dependenciaPos;
    }
}
