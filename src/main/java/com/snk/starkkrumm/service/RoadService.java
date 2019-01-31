package com.snk.starkkrumm.service;

import com.snk.starkkrumm.exception.ExcelCreationOrUploadException;
import com.snk.starkkrumm.model.Road;
import com.snk.starkkrumm.repository.RoadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

import static com.snk.starkkrumm.util.RoadUtil.getMonthFromDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadService {
    private final ExcelCreationService excelCreationService;
    private final RoadRepository roadRepository;
    private final GoogleDriveRequestSenderService googleDriveService;
    private static final String EXCEL_ERROR = "Could not create or upload excel files";

    public void save(Road road) {
        roadRepository.save(road);
    }

    public List<Road> getRoadsByDateAndCarNumber(String date, Integer carNumber) {
        return roadRepository.findByMonthAndCarNumber(getMonthFromDate(date), carNumber);
    }

    public void uploadRoad(String date, Integer carNumber) {
        List<Road> roads = roadRepository.findByMonthAndCarNumber(getMonthFromDate(date), carNumber);
        if (CollectionUtils.isEmpty(roads)) {
            log.warn("Null or empty road list"); //TODO exception handling
            return;
        }
        try {
            excelCreationService.createExcelFile(roads);
            googleDriveService.uploadExcel(getMonthFromDate(date)
                    + "-"
                    + getLicensePlateNumber(carNumber)
                    + "-SNK.xls");
        } catch (IOException e) {
            throw new ExcelCreationOrUploadException(EXCEL_ERROR);
        }
    }

    private Object getLicensePlateNumber(int carNumber) {
        return carNumber < 10 ? "0" + carNumber : carNumber;
    }

}
