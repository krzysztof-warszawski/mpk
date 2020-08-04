package com.space4u.mpkgen.util;

import com.space4u.mpkgen.util.mappings.MpkMappings;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Getter
@Setter
public class FolderCreator {

    private final String corePath = MpkMappings.CORE_PATH;
    private String projectNum;
    private String buildingName;
    private Integer buildingNum;
    private String mpk;
    private String date;
    private String floor;
    private String tenant;


    public void createOfferFolders() throws IOException {
        String location = getCorePath() + date + "_" + buildingNum + "_" + buildingName
                + "_" + floor + "_" + tenant;
        Files.createDirectories(Paths.get(location));
        String subPath = location + File.separator + date + "_" + buildingNum + "_" + tenant + "_";
        String subFolderLocation = subPath + "Dostawcy";
        Files.createDirectory(Paths.get(subFolderLocation));
    }


    public void createFolders() throws IOException {
        String location = getCorePath() + mpk + "_" + date + "_" + buildingName + "_" + floor + "_" + tenant;
        Files.createDirectories(Paths.get(location));
        String subPath = location + File.separator + mpk + "_";
        String subFolderLocation = subPath + "DokumentacjaPowykonawcza";
        Files.createDirectory(Paths.get(subFolderLocation));
    }

}

