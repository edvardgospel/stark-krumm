package com.snk.starkkrumm.service;

import com.snk.starkkrumm.exception.ExcelCreationException;
import com.snk.starkkrumm.model.Month;
import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.repository.RoadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.asList;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadService {
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

    private final RoadRepository roadRepository;

    public void save(Road road) {
        roadRepository.save(road);
    }

    public void populateExcelFiles(LocalDateTime departure) {
        List<Road> roads = roadRepository.findByDeparture(departure);
        Map<Integer, List<Road>> integerListMap = categorizeRoadsByCarNumber(roads);
        createExcelFiles(integerListMap);
    }

    private Map<Integer, List<Road>> categorizeRoadsByCarNumber(List<Road> roads) {
        Map<Integer, List<Road>> categorizedRoads = new HashMap<>();
        for (Road road : roads) {
            int carNumber = road.getCarNumber();
            if (!categorizedRoads.containsKey(carNumber)) {
                categorizedRoads.put(carNumber, new ArrayList<>(asList(road)));
            } else {
                List<Road> roadsByCarNumber = categorizedRoads.get(carNumber);
                roadsByCarNumber.add(road);
                categorizedRoads.put(carNumber, roadsByCarNumber);
            }
        }
        return categorizedRoads;
    }

    private void createExcelFiles(Map<Integer, List<Road>> map) {
        for (Map.Entry<Integer, List<Road>> entry : map.entrySet()) {
            try {
                FileInputStream in = new FileInputStream(INPUT_XLS_FILE);
                HSSFWorkbook workbook = new HSSFWorkbook(in);
                HSSFSheet sheet = workbook.getSheetAt(0);
                List<Road> roads = entry.getValue();
                int i = 11;
                double allConsumption = 0.0;
                int allDistance = 0;
                for (Road road : roads) {
                    HSSFRow row = sheet.getRow(i++);
                    row.getCell(0).setCellValue(road.getDeparture().format(ofPattern(DD_HH_MM)));
                    row.getCell(1).setCellValue(road.getArrival().format(ofPattern(DD_HH_MM)));
                    row.getCell(2).setCellValue(road.getRoadNumber());
                    row.getCell(3).setCellValue(road.getDriverName());
                    row.getCell(5).setCellValue(road.getDistance());
                    row.getCell(10).setCellValue(road.getConsumption());
                    allConsumption += road.getConsumption();
                    allDistance += road.getDistance();
                }

                double shortDistance = allDistance * 1.0 / 100;
                sheet.getRow(3).getCell(11).setCellValue(NR_CIRCULATIE_SM + (entry.getKey() < 10 ? ZERO + entry.getKey() : entry.getKey()) + SNK1);
                sheet.getRow(0).getCell(19).setCellValue(LUNA + Arrays.stream(Month.values()).filter(month -> month.number.equals(roads.get(0).getDeparture().format(ofPattern(MM))))
                        .map(month -> month.text).findFirst().get() + AN + roads.get(0).getDeparture().format(ofPattern(YYYY)));
                sheet.getRow(28).getCell(17).setCellValue(allConsumption);
                sheet.getRow(29).getCell(17).setCellValue(allConsumption);
                sheet.getRow(30).getCell(14).setCellValue(allConsumption);
                sheet.getRow(30).getCell(17).setCellValue(shortDistance);
                sheet.getRow(30).getCell(20).setCellValue(allConsumption / shortDistance);
                FileOutputStream out = new FileOutputStream(new File(roads.get(0).getDeparture().format(ofPattern(YYYY_MM))
                        + SNK2 + (entry.getKey() < 10 ? ZERO + entry.getKey() : entry.getKey()) + XLS_EXTENSION));
                workbook.write(out);
                in.close();
                out.close();
            } catch (IOException e) {
                throw new ExcelCreationException("Could not create excel files!");
            }

        }
    }
}
