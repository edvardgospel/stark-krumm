package com.snk.starkkrumm.service;

import com.snk.starkkrumm.exception.ExcelCreationException;
import com.snk.starkkrumm.model.Road;
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
import java.util.Map.Entry;

import static com.snk.starkkrumm.model.Month.values;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;

@Service
public class ExcelCreationService {
    private static final String INPUT_XLS = "FAZ.xls";
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

    void createExcelFiles(Map<Integer, List<Road>> map) {
        for (Entry<Integer, List<Road>> entry : map.entrySet()) {
            try {
                createExcelFile(entry);
            } catch (IOException e) {
                throw new ExcelCreationException(EXCEL_ERROR);
            }

        }
    }

    private void createExcelFile(Entry<Integer, List<Road>> entry) throws IOException {
        final FileInputStream in = new FileInputStream(INPUT_XLS);
        final HSSFWorkbook workbook = new HSSFWorkbook(in);
        final HSSFSheet sheet = workbook.getSheetAt(0);
        final List<Road> roads = entry.getValue();
        final int carNumber = entry.getKey();
        final LocalDateTime departure = roads.get(0).getDeparture();
        final FileOutputStream out = new FileOutputStream(createFile(carNumber, departure));
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
        insertAdditionalDataToExcel(sheet, carNumber, departure, allConsumption, shortDistance);
        workbook.write(out);
        closeResources(in, out);
    }

    private void insertRowToExcel(HSSFSheet sheet, int rowNumber, Road road) {
        sheet.getRow(rowNumber).getCell(0).setCellValue(road.getDeparture().format(ofPattern(DD_HH_MM)));
        sheet.getRow(rowNumber).getCell(1).setCellValue(road.getArrival().format(ofPattern(DD_HH_MM)));
        sheet.getRow(rowNumber).getCell(2).setCellValue(road.getRoadNumber());
        sheet.getRow(rowNumber).getCell(3).setCellValue(road.getDriverName());
        sheet.getRow(rowNumber).getCell(5).setCellValue(road.getDistance());
        sheet.getRow(rowNumber).getCell(10).setCellValue(road.getConsumption());

    }

    private void insertAdditionalDataToExcel(HSSFSheet sheet, int carNumber, LocalDateTime departure, double allConsumption, double shortDistance) {
        sheet.getRow(0).getCell(19).setCellValue(LUNA + getMonthInTextFormat(departure) + AN + departure.format(ofPattern(YYYY)));
        sheet.getRow(3).getCell(11).setCellValue(CIRCULATIE + getLicensePlateNumber(carNumber) + SNK1);
        sheet.getRow(28).getCell(17).setCellValue(allConsumption);
        sheet.getRow(29).getCell(17).setCellValue(allConsumption);
        sheet.getRow(30).getCell(14).setCellValue(allConsumption);
        sheet.getRow(30).getCell(17).setCellValue(shortDistance);
        sheet.getRow(30).getCell(20).setCellValue(allConsumption / shortDistance);
    }

    private File createFile(int carNumber, LocalDateTime departure) {
        return new File(departure.format(ofPattern(YYYY_MM))
                + SNK2
                + getLicensePlateNumber(carNumber)
                + XLS_EXTENSION);
    }

    private String getMonthInTextFormat(LocalDateTime departure) {
        return stream(values())
                .filter(month -> month.number.equals(departure.format(ofPattern(MM))))
                .map(month -> month.text).findFirst().get();
    }

    public Object getLicensePlateNumber(int carNumber) {
        return carNumber < 10 ? ZERO + carNumber : carNumber;
    }

    private void closeResources(FileInputStream in, FileOutputStream out) throws IOException {
        in.close();
        out.close();
    }
}
