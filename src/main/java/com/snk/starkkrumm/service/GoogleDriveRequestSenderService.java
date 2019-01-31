package com.snk.starkkrumm.service;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleDriveRequestSenderService {

    private final Drive drive;

    void uploadExcel(String fileName) throws IOException {
        File fileMetadata = new File().setName(fileName);//e.g.:"IAN-08-SNK.xls"
        java.io.File filePath = new java.io.File(System.getProperty("user.home")
                + "\\Desktop\\work\\"
                + fileName);
        FileContent mediaContent = new FileContent("application/vnd.ms-excel", filePath);
        drive.files()
                .create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        log.info("File uploaded to Google Drive successfully: " + fileName);
    }
}
