package com.example.practice.util;

import com.example.practice.model.BookDuty;
import com.example.practice.model.Log;
import com.example.practice.service.LogService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExcelFileWriter {

    public static void generateSchedulerFile(Set<Log> issuedAndNotReturnedBooks) throws IOException {
        var workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Отчет");
        Row row = sheet.createRow(0);
        Cell cell= row.createCell(0);
        cell.setCellValue("Количество выданных книг:");
        row.createCell(1).setCellValue(issuedAndNotReturnedBooks.size());

        var duties = issuedAndNotReturnedBooks.stream().filter(LogService::checkIsDuty).collect(Collectors.toSet());
        Row row1= sheet.createRow(1);
        row1.createCell(0).setCellValue("Количство должников:");
        row1.createCell(1).setCellValue(duties.size());
        sheet.createRow(2).createCell(0).setCellValue("Должники:");

        int rowCount=3;
        for (var item:duties){
            var reader=item.getReader();
            var dutyRow=sheet.createRow(rowCount);
            dutyRow.createCell(0).setCellValue(reader.getLastname());
            dutyRow.createCell(1).setCellValue(reader.getFirstname());
            dutyRow.createCell(2).setCellValue(reader.getSurname());

            rowCount++;
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        String filename="отчет по таймеру";
        FileOutputStream fos = new FileOutputStream("files/" + filename +".xlsx");
        workbook.write(fos);
        fos.close();

    }

    public static double getNumericCellValue(int row, int cell, String file) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(file));
        Sheet myExcelSheet = myExcelBook.getSheet("Отчет");
        Row sheetRow = myExcelSheet.getRow(row);
        var v=sheetRow.getCell(cell).getNumericCellValue();
        return v;

    }

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
