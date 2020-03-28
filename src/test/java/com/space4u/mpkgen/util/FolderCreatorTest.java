package com.space4u.mpkgen.util;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class FolderCreatorTest {

    private FolderCreator folder;
    private String projectNum;
    private String buildingName;
    private String buildingNum;
    private String mpk;
    private String date;
    private String floor;
    private String tenant;

    @org.junit.Test
    public void createFolders() throws IOException {
        folder = new FolderCreator();
        folder.setDate("202003");
        folder.setBuildingNum("24");
        folder.setBuildingName("BlueSky");
        folder.setTenant("DELL");

        folder.createFolders(folder.getDate(),folder.getBuildingNum(),folder.getBuildingName(),folder.getTenant());

        String location = folder.getCorePath() + folder.getDate() + "_" + folder.getBuildingNum()
                + "_" + folder.getBuildingName() + "_" + folder.getTenant();

        assertTrue(Files.exists(Paths.get(location)));
    }
}