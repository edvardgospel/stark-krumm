package com.snk.starkkrumm.service;

import static com.snk.starkkrumm.util.RoadUtil.getMonth;
import static com.snk.starkkrumm.util.RoadUtil.getYear;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.snk.starkkrumm.exception.ExcelCreationOrUploadException;
import com.snk.starkkrumm.exception.InvalidRoadException;
import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.repository.RoadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadService {
    private final ExcelCreationService excelCreationService;
    private final RoadRepository roadRepository;
    private final GoogleDriveRequestSenderService googleDriveService;
    private static final String EXCEL_ERROR = "Could not create or upload excel files";

    public void save(Road road) {
        Road existingRoad = findOne(road.getYear(), road.getMonth(), road.getCarNumber(), road.getRoadNumber());
        if (Objects.isNull(existingRoad)) {
            roadRepository.save(road);
            log.info("Road saved successfully.");
        } else {
            log.warn("Road already exists!");
            throw new InvalidRoadException("Invalid road.");
        }
    }

    public List<Road> deleteRoad(String date, Integer carNumber, Integer roadNumber) {
        Road existingRoad = findOne(getYear(date), getMonth(date), carNumber, roadNumber);
        if (Objects.nonNull(existingRoad)) {
            roadRepository.delete(existingRoad);
            log.info("Road deleted successfully.");
        } else {
            log.warn("Road not exists, can't be deleted!");
            throw new InvalidRoadException("Invalid road.");
        }
        return getRoads(date, carNumber);
    }

    public List<Road> uploadRoad(String date, Integer carNumber) {
        List<Road> roads = getRoads(date, carNumber);
        if (CollectionUtils.isEmpty(roads)) {
            log.warn("Null or empty road list");
            throw new InvalidRoadException("Invalid road.");
        }
        try {
            excelCreationService.createExcelFile(roads);
            googleDriveService.uploadExcel(getMonth(date)
                    + "-"
                    + excelCreationService.getLicensePlateNumber(carNumber)
                    + "-SNK.xls");
        } catch (IOException e) {
            throw new ExcelCreationOrUploadException(EXCEL_ERROR);
        }
        return roads;
    }

    public List<Road> getRoads(String date, Integer carNumber) {
        return roadRepository.findByYearMonthAndCarNumber(getYear(date), getMonth(date), carNumber);
    }

    private Road findOne(String year, String month, Integer carNumber, Integer roadNumber) {
        return roadRepository.findOne(year, month, carNumber, roadNumber);
    }
}
