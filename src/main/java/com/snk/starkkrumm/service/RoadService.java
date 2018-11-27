package com.snk.starkkrumm.service;

import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.repository.RoadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.format.DateTimeFormatter.ofPattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadService {

    private final RoadRepository roadRepository;

    public void save(Road road) {
        roadRepository.save(road);
    }

    public void populateExcelFiles(LocalDateTime departure) {
        List<Road> roads = roadRepository.findByDeparture(departure);
        Map<Integer, List<Road>> integerListMap = categorizeRoadsByCarNumber(roads);
        try {
            createExcels(integerListMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, List<Road>> categorizeRoadsByCarNumber(List<Road> roads) {
        Map<Integer, List<Road>> categorizedRoads = new HashMap<>();
        for (Road road : roads) {
            int carNumber = road.getCarNumber();
            if (!categorizedRoads.containsKey(carNumber)) {
                categorizedRoads.put(carNumber, new ArrayList<>(Arrays.asList(road)));
            } else {
                List<Road> roadsByCarNumber = categorizedRoads.get(carNumber);
                roadsByCarNumber.add(road);
                categorizedRoads.put(carNumber, roadsByCarNumber);
            }
        }
        return categorizedRoads;
    }

    public void createExcels(Map<Integer, List<Road>> map) throws IOException {
        for (Map.Entry<Integer, List<Road>> entry : map.entrySet()) {
            FileInputStream in = new FileInputStream("FAZ.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(in);
            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            HSSFSheet sheet = workbook.getSheetAt(0);
            List<Road> roads = entry.getValue();
            int i = 11;
            for (Road road : roads) {
                HSSFRow row = sheet.createRow(i++);
                row.setRowStyle(style);
                HSSFCell cell = row.createCell(1);
                cell.setCellValue(road.getRoadNumber());
                cell.setCellStyle(style);
                cell = row.createCell(2);
                cell.setCellValue(road.getCarNumber());
                cell.setCellStyle(style);
                cell = row.createCell(3);
                cell.setCellValue(road.getDriverName());
                cell.setCellStyle(style);
                cell = row.createCell(4);
                cell.setCellValue(road.getDeparture().toString());
                cell.setCellStyle(style);
                cell = row.createCell(5);
                cell.setCellValue(road.getArrival().toString());
                cell.setCellStyle(style);
                cell = row.createCell(6);
                cell.setCellValue(road.getDistance());
                cell.setCellStyle(style);
                cell = row.createCell(7);
                cell.setCellValue(road.getConsumption());
                cell.setCellStyle(style);
            }
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(new File(roads.get(0).getDeparture().format(ofPattern("yyyy-MM"))
                        + "-SNK-"
                        + entry.getKey().toString()
                        + ".xls"));
                workbook.write(out);
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
