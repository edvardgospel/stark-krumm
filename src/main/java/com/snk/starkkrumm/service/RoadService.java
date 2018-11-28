package com.snk.starkkrumm.service;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.asList;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadService {

    private static final int CELL_NUM_0 = 0;
    private static final int CELL_NUM_1 = 1;
    private static final int CELL_NUM_2 = 2;
    private static final int CELL_NUM_3 = 3;
    private static final int CELL_NUM_5 = 5;
    private static final int CELL_NUM_10 = 10;
    private static final int ROW_INDEX_3 = 3;
    private static final String INPUT_XLS_FILE = "FAZ.xls";
    private static final String YYYY_MM = "yyyy-MM";
    private static final String DD_HH_MM = "dd.HH.mm";
    private static final String SNK1 = ".SNK";
    private static final String SNK2 = "-SNK-";
    private static final String XLS_EXTENSION = ".xls";
    private static final String NR_CIRCULATIE_SM = "NR. CIRCULAȚIE: SM.";

    private final RoadRepository roadRepository;

    public void save(Road road) {
        roadRepository.save(road);
    }

    public void populateExcelFiles(LocalDateTime departure) {
        List<Road> roads = roadRepository.findByDeparture(departure);
        Map<Integer, List<Road>> integerListMap = categorizeRoadsByCarNumber(roads);
        createExcels(integerListMap);
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

    private void createExcels(Map<Integer, List<Road>> map) {
        for (Map.Entry<Integer, List<Road>> entry : map.entrySet()) {
            try {
                FileInputStream in = new FileInputStream(INPUT_XLS_FILE);
                HSSFWorkbook workbook = new HSSFWorkbook(in);
                HSSFSheet sheet = workbook.getSheetAt(0);
                sheet.getRow(ROW_INDEX_3).getCell(11).setCellValue(NR_CIRCULATIE_SM + entry.getKey() + SNK1);
                List<Road> roads = entry.getValue();
                int i = 11;
                for (Road road : roads) {
                    HSSFRow row = sheet.getRow(i++);
                    row.getCell(CELL_NUM_0).setCellValue(road.getDeparture().format(ofPattern(DD_HH_MM)));
                    row.getCell(CELL_NUM_1).setCellValue(road.getArrival().format(ofPattern(DD_HH_MM)));
                    row.getCell(CELL_NUM_2).setCellValue(road.getRoadNumber());
                    row.getCell(CELL_NUM_3).setCellValue(road.getDriverName());
                    row.getCell(CELL_NUM_5).setCellValue(road.getDistance());
                    row.getCell(CELL_NUM_10).setCellValue(road.getConsumption());//08
                }
                FileOutputStream out = new FileOutputStream(new File(roads.get(0).getDeparture().format(ofPattern(YYYY_MM))
                        + SNK2 + entry.getKey().toString() + XLS_EXTENSION));
                workbook.write(out);
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
