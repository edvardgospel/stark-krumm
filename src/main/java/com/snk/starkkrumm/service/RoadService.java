package com.snk.starkkrumm.service;

import com.snk.starkkrumm.exception.ExcelCreationException;
import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.repository.RoadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.snk.starkkrumm.util.RoadUtil.getMonthFromDate;

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

    public List<Road> getRoadsByDateAndCarNumber(String date, Integer carNumber) {
        return roadRepository.findByMonthAndCarNumber(getMonthFromDate(date), carNumber);
    }

    public void uploadRoad(String date, Integer carNumber) {
        List<Road> roads = roadRepository.findByMonthAndCarNumber(getMonthFromDate(date), carNumber);
        try {
            excelCreationService.createExcelFile(roads);
        } catch (IOException e) {
            throw new ExcelCreationException(EXCEL_ERROR);
        }
    }

}
