package com.example.practice.util;

import com.example.practice.model.BookDuty;
import com.example.practice.model.Log;
import com.example.practice.service.LogService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExcelFileWriter {

    public static void writeCountryListToFile(List<Log> logs) throws Exception {

        var workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Долги");


        Row row = sheet.createRow(0);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("ФИО");

        Cell cell1 = row.createCell(1);
        cell1.setCellValue("Название и автор книги");

        Cell cell2 = row.createCell(2);
        cell2.setCellValue("выдана");

        Cell cell3 = row.createCell(3);
        cell3.setCellValue("долг");
        int i = 1;

        Cell nameCell;
        Cell titleCell;
        Cell issueCell;
        Cell isDutyCell;
        for (var log : logs) {
            Row newRow = sheet.createRow(i);
            nameCell = newRow.createCell(0);
            nameCell.setCellValue(log.getReader().getFirstname() + " " + log.getReader().getLastname());

            titleCell = newRow.createCell(1);
            titleCell.setCellValue(log.getBook().getTitle() + " " + log.getBook().getAuthorName());


            issueCell = newRow.createCell(2);
            issueCell.setCellValue(log.getIssueDate().toString());


            isDutyCell = newRow.createCell(3);
            isDutyCell.setCellValue(LogService.checkIsDuty(log));
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        var now = LocalDateTime.now();
        String filename="отчет" + now.getYear() + now.getMonth().toString() + now.getDayOfYear() + now.getHour() + now.getMinute();
        FileOutputStream fos = new FileOutputStream("files/" + filename +".xlsx");
        workbook.write(fos);
        fos.close();
    }

}
