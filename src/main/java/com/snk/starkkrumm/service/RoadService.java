package com.snk.starkkrumm.service;

import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.repository.RoadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadService {

    private final RoadRepository roadRepository;

    public void save(Road road) {
        roadRepository.save(road);
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

    /*//
    HSSFWorkbook sheets = new HSSFWorkbook();
    HSSFSheet sheet = sheets.createSheet("title of sheet");

    public void instertDataToExcel(Map<Integer, List<Road>> integerListMap) throws IOException {
        for (Map.Entry<Integer, List<Road>> entry : integerListMap.entrySet()) {
            insertData(entry);
        }
    }

    private void insertData(Map.Entry<Integer, List<Road>> entry) {

    }

    public void saveDataToExcel() throws IOException {
        File file = new File("FAZ.xls");
        FileOutputStream outputStream = new FileOutputStream("FAZ.xls");
        log.info("FILEEXISTS: " + file.exists());
        //sheets.write(outputStream);
    }


    public void instertDataToExcel() throws FileNotFoundException {
        File file = new File("FAZ.xls");
        //FileOutputStream outputStream = new FileOutputStream("FAZ.xls");
        log.info("FILEEXISTS: " + file.exists());

    }*/
}
