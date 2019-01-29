package com.snk.starkkrumm.service;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GoogleDriveRequestSenderService {
    private final Drive drive;

    public void uploadExcel() throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName("My Report");
        fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet");

        java.io.File filePath = new java.io.File(System.getProperty("user.home") + "\\Desktop\\work\\2019-IAN-SNK-08.xls");
        FileContent mediaContent = new FileContent("text/xls", filePath);
        File file = drive.files()
                .create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        System.out.println("File ID: " + file.getId());
    }
}
