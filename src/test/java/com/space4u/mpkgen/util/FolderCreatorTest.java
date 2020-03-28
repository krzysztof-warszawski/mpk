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

    @org.junit.Test
    public void createOfferFolders() throws IOException {
        folder = new FolderCreator();
        folder.setDate("202002");
        folder.setBuildingNum(34);
        folder.setBuildingName("Nemo");
        folder.setFloor("1");
        folder.setTenant("Coca Cola");

        folder.createOfferFolders();

        String location = folder.getCorePath() + folder.getDate() + "_" + folder.getBuildingNum()
                + "_" + folder.getBuildingName() + "_" + folder.getFloor() + "_" + folder.getTenant();

        assertTrue(Files.exists(Paths.get(location)));
    }

    @org.junit.Test
    public void createFolders() throws IOException {
        folder = new FolderCreator();
        folder.setMpk("340042");
        folder.setDate("202002");
        folder.setBuildingName("Nemo");
        folder.setFloor("1");
        folder.setTenant("Coca Cola");

        folder.createFolders();

        String location = folder.getCorePath() + folder.getMpk() + "_" + folder.getDate() + "_"
                + folder.getBuildingName() + "_" + folder.getFloor() + "_" + folder.getTenant();

        assertTrue(Files.exists(Paths.get(location)));
    }
}