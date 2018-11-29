package com.snk.starkkrumm.service;

import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.repository.RoadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public void save(Road road) {
        roadRepository.save(road);
    }

    public void populateExcelFiles(LocalDateTime departure) {
        List<Road> roads = roadRepository.findByDeparture(departure);
        excelCreationService.createExcelFiles(categorizeRoadsByCarNumber(roads));
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
}
