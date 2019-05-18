package com.snk.starkkrumm.service;

import com.snk.starkkrumm.exception.ExcelCreationException;
import com.snk.starkkrumm.exception.ExcelUploadException;
import com.snk.starkkrumm.exception.InvalidRoadException;
import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.repository.RoadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.snk.starkkrumm.util.RoadUtil.getMonth;
import static com.snk.starkkrumm.util.RoadUtil.getYear;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadService {
    private final ExcelCreationService excelCreationService;
    private final RoadRepository roadRepository;
    private final GoogleDriveRequestSenderService googleDriveService;
    private static final String EXCEL_CREATION_ERROR = "Could not create excel files.";
    private static final String EXCEL_UPLOAD_ERROR = "Could not upload excel files.";

    public List<Road> save(Road road) {
        Road existingRoad = findOne(road.getYear(), road.getMonth(), road.getCarNumber(), road.getRoadNumber());
        if (Objects.isNull(existingRoad)) {
            roadRepository.save(road);
            log.info("Road saved successfully.");
            return getRoads(road.getYear(), road.getMonth(), road.getCarNumber());
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
        } catch (IOException e) {
            throw new ExcelCreationException(EXCEL_CREATION_ERROR);
        }
        try {
            googleDriveService.uploadExcel(getMonth(date)
                    + "-"
                    + excelCreationService.getLicensePlateNumber(carNumber)
                    + "-SNK.xls");
        } catch (IOException e) {
            throw new ExcelUploadException(EXCEL_UPLOAD_ERROR);
        }
        return roads;
    }

    public List<Road> getRoads(String date, Integer carNumber) {
        List<Road> roads = roadRepository.findByYearMonthAndCarNumber(getYear(date), getMonth(date), carNumber);
        Collections.sort(roads);
        return roads;
    }

    private List<Road> getRoads(String year, String month, Integer carNumber) {
        List<Road> roads = roadRepository.findByYearMonthAndCarNumber(year, month, carNumber);
        Collections.sort(roads);
        return roads;
    }

    private Road findOne(String year, String month, Integer carNumber, Integer roadNumber) {
        return roadRepository.findOne(year, month, carNumber, roadNumber);
    }
}
