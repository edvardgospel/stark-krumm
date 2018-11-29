package com.snk.starkkrumm.service;

import com.snk.starkkrumm.exception.ExcelCreationException;
import com.snk.starkkrumm.model.Month;
import com.snk.starkkrumm.model.Road;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ofPattern;

@Service
public class ExcelCreationService {
    private static final String INPUT_XLS_FILE = "FAZ.xls";
    private static final String YYYY_MM = "yyyy-MM";
    private static final String DD_HH_MM = "dd.HH.mm";
    private static final String SNK1 = ".SNK";
    private static final String SNK2 = "-SNK-";
    private static final String XLS_EXTENSION = ".xls";
    private static final String NR_CIRCULATIE_SM = "NR. CIRCULAȚIE: SM.";
    private static final String MM = "MM";
    private static final String YYYY = "yyyy";
    private static final String ZERO = "0";
    private static final String LUNA = "Luna:";
    private static final String AN = ".Anul: ";
    private static final String COULD_NOT_CREATE_EXCEL_FILES = "Could not create excel files";

    void createExcelFiles(Map<Integer, List<Road>> map) {
        for (Map.Entry<Integer, List<Road>> entry : map.entrySet()) {
            try {
                final FileInputStream in = new FileInputStream(INPUT_XLS_FILE);
                final HSSFWorkbook workbook = new HSSFWorkbook(in);
                final HSSFSheet sheet = workbook.getSheetAt(0);
                final List<Road> roads = entry.getValue();
                final FileOutputStream out = new FileOutputStream(new File(roads.get(0).getDeparture().format(ofPattern(YYYY_MM))
                        + SNK2 + (entry.getKey() < 10 ? ZERO + entry.getKey() : entry.getKey()) + XLS_EXTENSION));
                int rowNumber = 11;
                int allDistance = 0;
                double allConsumption = 0.0;
                double shortDistance;
                for (Road road : roads) {
                    sheet.getRow(rowNumber).getCell(0).setCellValue(road.getDeparture().format(ofPattern(DD_HH_MM)));
                    sheet.getRow(rowNumber).getCell(1).setCellValue(road.getArrival().format(ofPattern(DD_HH_MM)));
                    sheet.getRow(rowNumber).getCell(2).setCellValue(road.getRoadNumber());
                    sheet.getRow(rowNumber).getCell(3).setCellValue(road.getDriverName());
                    sheet.getRow(rowNumber).getCell(5).setCellValue(road.getDistance());
                    sheet.getRow(rowNumber).getCell(10).setCellValue(road.getConsumption());
                    allConsumption += road.getConsumption();
                    allDistance += road.getDistance();
                    rowNumber++;
                }

                shortDistance = allDistance * 1.0 / 100;
                sheet.getRow(3).getCell(11).setCellValue(NR_CIRCULATIE_SM + (entry.getKey() < 10 ? ZERO + entry.getKey() : entry.getKey()) + SNK1);
                sheet.getRow(0).getCell(19).setCellValue(LUNA + Arrays.stream(Month.values()).filter(month -> month.number.equals(roads.get(0).getDeparture().format(ofPattern(MM))))
                        .map(month -> month.text).findFirst().get() + AN + roads.get(0).getDeparture().format(ofPattern(YYYY)));
                sheet.getRow(28).getCell(17).setCellValue(allConsumption);
                sheet.getRow(29).getCell(17).setCellValue(allConsumption);
                sheet.getRow(30).getCell(14).setCellValue(allConsumption);
                sheet.getRow(30).getCell(17).setCellValue(shortDistance);
                sheet.getRow(30).getCell(20).setCellValue(allConsumption / shortDistance);
                workbook.write(out);
                in.close();
                out.close();
            } catch (IOException e) {
                throw new ExcelCreationException(COULD_NOT_CREATE_EXCEL_FILES);
            }

        }
    }
}
