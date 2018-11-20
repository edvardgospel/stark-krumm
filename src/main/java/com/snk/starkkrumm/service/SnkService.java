package com.snk.starkkrumm.service;

import com.snk.starkkrumm.model.Road;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class SnkService {
    HSSFWorkbook sheets = new HSSFWorkbook();
    HSSFSheet sheet = sheets.createSheet("title of sheet");

    //

    public void instertData(Road road) throws IOException {
       // dataValidationService.validate(data);
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("ezaz");
        //row.createCell(1).setCellValue(data);
        saveDataToExcel();
    }

    public void saveDataToExcel() throws IOException {
        FileOutputStream outputStream = new FileOutputStream("D:\\testout.xls");
        sheets.write(outputStream);
    }
}
