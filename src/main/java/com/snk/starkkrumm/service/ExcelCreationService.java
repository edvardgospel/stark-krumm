package com.snk.starkkrumm.service;

import com.snk.starkkrumm.model.Road;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static java.lang.System.getProperty;

@Service
@RequiredArgsConstructor
public class ExcelCreationService {
    private final String INPUT_XLS;
    private final String OUTPUT_PATH;
    private static final String USER_HOME = "user.home";


    void createExcelFile(List<Road> roads) throws IOException {
        final FileInputStream in = new FileInputStream(getProperty(USER_HOME) + INPUT_XLS);
        final HSSFWorkbook workbook = new HSSFWorkbook(in);
        final HSSFSheet sheet = workbook.getSheetAt(0);
        final int carNumber = roads.get(0).getCarNumber();
        final String month = roads.get(0).getMonth();
        final String year = roads.get(0).getYear();
        final FileOutputStream out = new FileOutputStream(createFile(carNumber, year, month));
        int rowNumber = 11;
        int allDistance = 0;
        double allConsumption = 0.0;
        double shortDistance;
        for (Road road : roads) {
            insertRowToExcel(sheet, rowNumber, road);
            allConsumption += road.getConsumption();
            allDistance += road.getDistance();
            rowNumber++;
        }
        shortDistance = allDistance * 1.0 / 100;
        insertAdditionalDataToExcel(sheet, carNumber, month, year, allConsumption, shortDistance);
        workbook.write(out);
        closeResources(in, out);
    }

    private void insertRowToExcel(HSSFSheet sheet, int rowNumber, Road road) {
        sheet.getRow(rowNumber).getCell(0).setCellValue(road.getDeparture());
        sheet.getRow(rowNumber).getCell(1).setCellValue(road.getArrival());
        sheet.getRow(rowNumber).getCell(2).setCellValue(road.getRoadNumber());
        sheet.getRow(rowNumber).getCell(3).setCellValue(road.getDriverName());
        sheet.getRow(rowNumber).getCell(5).setCellValue(road.getDistance());
        sheet.getRow(rowNumber).getCell(10).setCellValue(road.getConsumption());

    }

    private void insertAdditionalDataToExcel(HSSFSheet sheet, int carNumber, String month, String year, double allConsumption, double shortDistance) {
        sheet.getRow(0).getCell(19).setCellValue("Luna:" + month + ".Anul: " + year);
        sheet.getRow(3).getCell(11).setCellValue("NR. CIRCULAȚIE: SM." + getLicensePlateNumber(carNumber) + ".SNK");
        sheet.getRow(28).getCell(17).setCellValue(allConsumption);
        sheet.getRow(29).getCell(17).setCellValue(allConsumption);
        sheet.getRow(30).getCell(14).setCellValue(allConsumption);
        sheet.getRow(30).getCell(17).setCellValue(shortDistance);
        sheet.getRow(30).getCell(20).setCellValue(allConsumption / shortDistance);
    }

    private File createFile(int carNumber, String year, String month) {
        return new File(getProperty(USER_HOME) + OUTPUT_PATH + year + "-" + month
                + "-SNK-" + getLicensePlateNumber(carNumber) + ".xls");
    }

    private Object getLicensePlateNumber(int carNumber) {
        return carNumber < 10 ? "0" + carNumber : carNumber;
    }

    private void closeResources(FileInputStream in, FileOutputStream out) throws IOException {
        in.close();
        out.close();
    }
}
