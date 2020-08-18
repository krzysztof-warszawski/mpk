package com.space4u.mpkgen.util;

import com.space4u.mpkgen.entity.Project;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ExcelView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> map,
                                      Workbook workbook,
                                      HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse) throws Exception {

        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"projekty.xls\"");
        @SuppressWarnings({"unchecked"})
        final List<Project> projects = (List<Project>) map.get("projects");
        final Sheet sheet = workbook.createSheet();

        addHeaderRow(sheet);

        projects.forEach(project -> createRow(sheet, project));
        for (int i = 0; i<=7; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void addHeaderRow(Sheet sheet) {
        Row header = sheet.createRow(0);
        header.createCell((short)0).setCellValue("MPK");
        header.createCell((short)1).setCellValue("Nazwa budynku");
        header.createCell((short)2).setCellValue("Data");
        header.createCell((short)3).setCellValue("Piętro");
        header.createCell((short)4).setCellValue("Numer projektu");
        header.createCell((short)5).setCellValue("Krótki opis");
        header.createCell((short)6).setCellValue("Najemca");
        header.createCell((short)7).setCellValue("Usługa");
    }

    private void createRow(Sheet sheet, Project project) {
        Row row = sheet.createRow(sheet.getLastRowNum()+1);
        row.createCell((short) 0).setCellValue(project.getMpk());
        row.createCell((short) 1).setCellValue(project.getBuilding().getName());
        row.createCell((short) 2).setCellValue(project.getDate());
        row.createCell((short) 3).setCellValue(project.getFloor());
        row.createCell((short) 4).setCellValue(project.getProjectNum());
        row.createCell((short) 5).setCellValue(project.getShortDescription());
        row.createCell((short) 6).setCellValue(project.getTenant());
        row.createCell((short) 7).setCellValue(project.getServiceType().getName());
    }
}
