package polla.mt.controller.procesor;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import polla.mt.controller.servicios.ListaPosicionesServicio;
import polla.mt.model.entity.Listado;
import polla.mt.model.entity.Pollero;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class CargarPolleros {

    public static String rutaArchivoBase = "C:\\temp\\Polleros.xls";

    @Autowired
    private ListaPosicionesServicio listaPosicionesServicio;

    public List<String> cargarPolleros() {

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
                    Pollero pollero = new Pollero();
                    pollero.setFecha(new Date());
                    pollero.setPuntuacion(0);
                    pollero.setPosicion(posicion);
                    pollero.setNombre(row.getCell(PolleroDEF.nombre).getStringCellValue());
                    pollero.setApodo(row.getCell(PolleroDEF.apodo).getStringCellValue());
                    pollero.setDependencia(row.getCell(PolleroDEF.area).getStringCellValue());
                    pollero.setEmail(row.getCell(PolleroDEF.correo).getStringCellValue());
                    pollero.setCodigo((int) row.getCell(PolleroDEF.codigo).getNumericCellValue());
                    String resultado = listaPosicionesServicio.guardarPollero(pollero);
                    if(!resultado.equals("OK")) errores.add(resultado);
                }else{
                    break;
                }

                posicion++;
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        return errores;
    }

    private Integer convertirANumero(String stringCellValue) {
        return Integer.parseInt(stringCellValue);
    }
}
