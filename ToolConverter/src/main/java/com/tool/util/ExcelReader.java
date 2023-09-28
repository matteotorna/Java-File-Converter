package com.tool.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.tool.model.ExcelRecord;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReader {

    public List<ExcelRecord> readDataFromExcel(InputStream inputStream) {
        List<ExcelRecord> records = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            System.out.println("Inizio lettura dati da Excel");

            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next();
                List<String> columnNames = new ArrayList<>();
                for (Cell cell : headerRow) {
                    columnNames.add(cell.getStringCellValue());
                }

                while (rowIterator.hasNext()) {
                    Row dataRow = rowIterator.next();
                    ExcelRecord record = new ExcelRecord("UniqueName");
                    for (int i = 0; i < columnNames.size(); i++) {
                        Cell cell = dataRow.getCell(i);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case STRING:
                                    record.setValue(columnNames.get(i), cell.getStringCellValue());
                                    break;
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        record.setValue(columnNames.get(i), cell.getDateCellValue());
                                    } else {
                                        record.setValue(columnNames.get(i), cell.getNumericCellValue());
                                    }
                                    break;
                                case BOOLEAN:
                                    record.setValue(columnNames.get(i), cell.getBooleanCellValue());
                                    break;
                                case BLANK:
                                    record.setValue(columnNames.get(i), null);
                                    break;
                                case FORMULA:
                                    record.setValue(columnNames.get(i), cell.getCellFormula());
                                    break;
                                default:
                                    // Tratta altri tipi di dati come necessario
                                    record.setValue(columnNames.get(i), cell.toString());
                                    break;
                            }
                        } else {
                            // Tratta celle vuote come necessario
                            record.setValue(columnNames.get(i), null);
                        }
                    }
                    records.add(record);
                }
            }

            System.out.println("Fine lettura dati da Excel");

        } catch (IOException e) {
            e.printStackTrace();
            // Aggiungi la tua gestione delle eccezioni per IOException qui
        } catch (Exception e) {
            e.printStackTrace();
            // Aggiungi la tua gestione delle eccezioni generiche qui
        }

        return records;
    }
}
