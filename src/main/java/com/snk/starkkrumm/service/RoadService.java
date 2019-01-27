package com.snk.starkkrumm.service;

import com.snk.starkkrumm.exception.ExcelCreationException;
import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.model.RoadV2;
import com.snk.starkkrumm.repository.RoadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadService {

    private final ExcelCreationService excelCreationService;
    private final RoadRepository roadRepository;
    private static final String EXCEL_ERROR = "Could not create excel files";

    public void save(Road road) {
        roadRepository.save(road);
    }

    /*public void populateExcelFiles(LocalDateTime departure) {
        List<Road> roads = roadRepository.findByDeparture(departure);
        excelCreationService.createExcelFiles(categorizeRoadsByCarNumber(roads));
    }*/

    public List<String> getRoadsByMonth(String month) {
        LocalDateTime departure = convertMonthToLocalDateTime(month);
        List<Road> roads = roadRepository.findByDeparture(departure);
        Map<Integer, List<Road>> map = categorizeRoadsByCarNumber(roads);
        return convertMapToStringList(map);
    }

    private LocalDateTime convertMonthToLocalDateTime(String month) {
        StringBuilder sb = new StringBuilder(month);
        if (sb.length() == 1) {
            sb = new StringBuilder("0" + month);
        }
        String str = "2019-" + sb + "-01 12:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        return dateTime;
    }

    private List<String> convertMapToStringList(Map<Integer, List<Road>> map) {
        List<String> strings = new ArrayList<>();
        for (Map.Entry<Integer, List<Road>> entry : map.entrySet()) {
            StringBuilder sb = new StringBuilder("SM-" +
                    excelCreationService.getLicensePlateNumber(entry.getKey()) + "-SNK: ");
            for (Road road : entry.getValue()) {
                sb.append(road.getRoadNumber() + " ");
            }
            sb.append("\n");
            strings.add(sb.toString());
        }
        return strings;
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

    public void save(RoadV2 roadV2) {
        log.info("save()");
        roadRepository.save(roadV2);
    }

    public List<RoadV2> getRoadsByDateAndCarNumber(String date, Integer carNumber) {
        return roadRepository.findByMonthAndCarNumber(getMonthFromDate(date), carNumber);
    }

    public void uploadRoad(String date, Integer carNumber) {
        List<RoadV2> roads = roadRepository.findByMonthAndCarNumber(getMonthFromDate(date), carNumber);
        try {
            excelCreationService.createExcelFileV2(roads);
        } catch (IOException e) {
            throw new ExcelCreationException(EXCEL_ERROR);
        }
    }

    private String getMonthFromDate(String month) {
        return month.split("-")[1];
    }
}
