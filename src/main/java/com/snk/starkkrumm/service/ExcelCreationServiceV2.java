package com.snk.starkkrumm.service;

import com.snk.starkkrumm.exception.ExcelCreationException;
import com.snk.starkkrumm.model.RoadV2;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.snk.starkkrumm.model.Month.values;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;

@Service
public class ExcelCreationServiceV2 {
    private static final String INPUT_XLS = "\\Desktop\\work\\apache-tomcat-8.5.30\\webapps\\starkkrumm\\WEB-INF\\classes\\FAZ.xls";
    private static final String YYYY_MM = "yyyy-MM";
    private static final String DD_HH_MM = "dd.HH.mm";
    private static final String SNK1 = ".SNK";
    private static final String SNK2 = "-SNK-";
    private static final String XLS_EXTENSION = ".xls";
    private static final String CIRCULATIE = "NR. CIRCULAȚIE: SM.";
    private static final String MM = "MM";
    private static final String YYYY = "yyyy";
    private static final String ZERO = "0";
    private static final String LUNA = "Luna:";
    private static final String AN = ".Anul: ";
    private static final String EXCEL_ERROR = "Could not create excel files";

    void createExcelFiles(Map<Integer, List<RoadV2>> map) {
        for (Map.Entry<Integer, List<RoadV2>> entry : map.entrySet()) {
            try {
                createExcelFile(entry);
            } catch (IOException e) {
                throw new ExcelCreationException(EXCEL_ERROR);
            }

        }
    }

    private void createExcelFile(Map.Entry<Integer, List<RoadV2>> entry) throws IOException {
        final FileInputStream in = new FileInputStream(System.getProperty("user.home") + INPUT_XLS);
        final HSSFWorkbook workbook = new HSSFWorkbook(in);
        final HSSFSheet sheet = workbook.getSheetAt(0);
        final List<RoadV2> roads = entry.getValue();
        final int carNumber = entry.getKey();
        final String month = roads.get(0).getMonth();
        final String year = roads.get(0).getYear();
        final FileOutputStream out = new FileOutputStream(createFile(carNumber, month));
        int rowNumber = 11;
        int allDistance = 0;
        double allConsumption = 0.0;
        double shortDistance;
        for (RoadV2 road : roads) {
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

    private void insertRowToExcel(HSSFSheet sheet, int rowNumber, RoadV2 road) {
        sheet.getRow(rowNumber).getCell(0).setCellValue(road.getDeparture());
        sheet.getRow(rowNumber).getCell(1).setCellValue(road.getArrival());
        sheet.getRow(rowNumber).getCell(2).setCellValue(road.getRoadNumber());
        sheet.getRow(rowNumber).getCell(3).setCellValue(road.getDriverName());
        sheet.getRow(rowNumber).getCell(5).setCellValue(road.getDistance());
        sheet.getRow(rowNumber).getCell(10).setCellValue(road.getConsumption());

    }

    private void insertAdditionalDataToExcel(HSSFSheet sheet, int carNumber, String month, String year, double allConsumption, double shortDistance) {
        sheet.getRow(0).getCell(19).setCellValue(LUNA + month + AN + year);
        sheet.getRow(3).getCell(11).setCellValue(CIRCULATIE + getLicensePlateNumber(carNumber) + SNK1);
        sheet.getRow(28).getCell(17).setCellValue(allConsumption);
        sheet.getRow(29).getCell(17).setCellValue(allConsumption);
        sheet.getRow(30).getCell(14).setCellValue(allConsumption);
        sheet.getRow(30).getCell(17).setCellValue(shortDistance);
        sheet.getRow(30).getCell(20).setCellValue(allConsumption / shortDistance);
    }

    private File createFile(int carNumber, String month) {
        return new File(System.getProperty("user.home") + "\\Desktop\\work\\apache-tomcat-8.5.30\\webapps\\starkkrumm\\WEB-INF\\classes\\" + month
                + SNK2
                + getLicensePlateNumber(carNumber)
                + XLS_EXTENSION);
    }

    public Object getLicensePlateNumber(int carNumber) {
        return carNumber < 10 ? ZERO + carNumber : carNumber;
    }

    private void closeResources(FileInputStream in, FileOutputStream out) throws IOException {
        in.close();
        out.close();
    }

}
