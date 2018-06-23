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
public class CalcularTerceraFecha {


    public static String rutaArchivoBase = "C:\\temp\\TerceraFecha.xls";

    @Autowired
    private ListaPosicionesServicio listaPosicionesServicio;
    private HashMap<String,List<Integer>> resultados;

    public List<String> CalcularTerceraFechaMarcadores() {

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
                        Integer codigo = (int) row.getCell(TerceraFechaDEF.CODIGO).getNumericCellValue();
                        String correo =  row.getCell(TerceraFechaDEF.CORREO).getStringCellValue();
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
//           puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_1,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_2,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_3,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_4,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_5,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_6,predicciones);
//        puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_7,predicciones);
      //  puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_8,predicciones);
       // puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_9,predicciones);
       // puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_10,predicciones);
     //    puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_11,predicciones);
       // puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_12,predicciones);
         //   puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_13,predicciones);
         //   puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_14,predicciones);
    //    puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_15,predicciones);
    //    puntuacion = puntuacion + resultadoPrediccion(TerceraFechaDEF.PARTIDO_16,predicciones);
        return puntuacion;
    }

    private Integer resultadoPrediccion(String partido, HashMap<String, List<Integer>> predicciones) {
        List<Integer> prediccion = predicciones.get(partido);
        List<Integer> resultado = resultados.get(partido);
        int puntaje = 0;
        if(marcadorExacto(prediccion,resultado)){
            puntaje = puntaje + TerceraFechaDEF.PUNTOS_MARCADOR;
        }

        if(resultadoExacto(prediccion,resultado)){
            puntaje = puntaje + TerceraFechaDEF.PUNTOS_RESULTADO;
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
         predicciones.put(TerceraFechaDEF.PARTIDO_1,obtenerValores(TerceraFechaDEF.URUGUAY,TerceraFechaDEF.RUSIA,row));
         predicciones.put(TerceraFechaDEF.PARTIDO_2,obtenerValores(TerceraFechaDEF.ARABIA,TerceraFechaDEF.EGIPTO,row));
         predicciones.put(TerceraFechaDEF.PARTIDO_3,obtenerValores(TerceraFechaDEF.ESPANA,TerceraFechaDEF.MARRUECOS,row));
         predicciones.put(TerceraFechaDEF.PARTIDO_4,obtenerValores(TerceraFechaDEF.IRAN,TerceraFechaDEF.PORTUGAL,row));
          predicciones.put(TerceraFechaDEF.PARTIDO_5,obtenerValores(TerceraFechaDEF.AUSTRALIA,TerceraFechaDEF.PERU,row));
        predicciones.put(TerceraFechaDEF.PARTIDO_6,obtenerValores(TerceraFechaDEF.DINAMARCA,TerceraFechaDEF.FRANCIA,row));
        predicciones.put(TerceraFechaDEF.PARTIDO_7,obtenerValores(TerceraFechaDEF.NIGERIA,TerceraFechaDEF.ARGENTINA,row));
        predicciones.put(TerceraFechaDEF.PARTIDO_8,obtenerValores(TerceraFechaDEF.ISLANDIA,TerceraFechaDEF.CROACIA,row));
        predicciones.put(TerceraFechaDEF.PARTIDO_9,obtenerValores(TerceraFechaDEF.SERBIA,TerceraFechaDEF.BRASIL,row));
       predicciones.put(TerceraFechaDEF.PARTIDO_10,obtenerValores(TerceraFechaDEF.SUIZA,TerceraFechaDEF.COSTARICA,row));
        predicciones.put(TerceraFechaDEF.PARTIDO_11,obtenerValores(TerceraFechaDEF.COREA,TerceraFechaDEF.ALEMANIA,row));
        predicciones.put(TerceraFechaDEF.PARTIDO_12,obtenerValores(TerceraFechaDEF.MEXICO,TerceraFechaDEF.SUECIA,row));
        predicciones.put(TerceraFechaDEF.PARTIDO_13,obtenerValores(TerceraFechaDEF.PANAMA,TerceraFechaDEF.TUNEZ,row));
        predicciones.put(TerceraFechaDEF.PARTIDO_14,obtenerValores(TerceraFechaDEF.INGLATERRA,TerceraFechaDEF.BELGICA,row));
        predicciones.put(TerceraFechaDEF.PARTIDO_15,obtenerValores(TerceraFechaDEF.JAPON,TerceraFechaDEF.POLONIA,row));
        predicciones.put(TerceraFechaDEF.PARTIDO_16,obtenerValores(TerceraFechaDEF.SENEGAL,TerceraFechaDEF.COLOMBIA,row));
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
