package com.tool.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tool.model.ExcelRecord;

public class ExcelReader {

	private static final Logger logger = LogManager.getLogger(ExcelReader.class);
	final DateTimeFormatter x = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public List<ExcelRecord> readBooksFromExcelFile(String excelFilePath) throws IOException, InvalidFormatException {
		try (InputStream inputStream = new FileInputStream(excelFilePath)) {
			return readBooksFromExcelFile(inputStream);
		}
	}

	public List<ExcelRecord> readBooksFromExcelFile(InputStream inputStream)
			throws IOException, InvalidFormatException {

		List<ExcelRecord> records = new ArrayList<ExcelRecord>();

		XSSFWorkbook wb = new XSSFWorkbook(inputStream);
		XSSFSheet firstSheet = wb.getSheetAt(0);

		final int rowNum = firstSheet.getPhysicalNumberOfRows();
		
		logger.info("rowNum" + rowNum);
		
		int rowCount = 0; // Conteggio delle righe piene
		
		for (int i = 1; i < rowNum + 1; i++) {

			XSSFRow row = firstSheet.getRow(i);

			// Verifica riga vuota e interruzione for
		    if (row == null || isRowEmpty(row)) {
		        break;
		    }

			ExcelRecord aRow = new ExcelRecord();

			for (int j = 0; j < 16; j++) {

				XSSFCell nextCell = row.getCell(j);
				

				switch (j) {
				case 0:
					aRow.setAction(nextCell.getStringCellValue());
					break;

				case 1:
					aRow.setDataOraInizio(nextCell.getStringCellValue());
					break;

				case 2:
					aRow.setDataOraFine(nextCell.getStringCellValue());
					break;

				case 3:
					aRow.setCodiceEtso(nextCell.getStringCellValue());
					break;

				case 4:
					aRow.setIdMotivazione(nextCell.getStringCellValue());
					break;

				case 5:
					double psMinFromXls = 0.0;
					try {
						psMinFromXls = nextCell.getNumericCellValue();
						double a = (double) psMinFromXls;
						aRow.setPsMin(a);
					} catch (Exception e) {
						logger.info("Error parsing psMinFromXls: " + psMinFromXls);
						e.printStackTrace();
					}
					break;

				case 6:
					double psMaxFromXls = 0.0;
					try {
						psMaxFromXls = nextCell.getNumericCellValue();
						double b = (double) psMaxFromXls;
						aRow.setPsMax(b);
					} catch (Exception e) {
						logger.info("Error parsing psMaxFromXls: " + psMaxFromXls);
						e.printStackTrace();
					}
					break;

				case 7:
					aRow.setIdAssetto(nextCell.getStringCellValue());
					break;

				case 8:
					double ptMinFromXls = 0.0;
					try {
						ptMinFromXls = nextCell.getNumericCellValue();
						double c = (double) ptMinFromXls;
						aRow.setPtMin(c);
					} catch (Exception e) {
						logger.info("Error parsing ptMinFromXls: " + ptMinFromXls);
						e.printStackTrace();
					}
					break;

				case 9:
					double ptMaxFromXls = 0.0;
					try {
						ptMaxFromXls = nextCell.getNumericCellValue();
						double d = (double) ptMaxFromXls;
						aRow.setPtMax(d);
					} catch (Exception e) {
						logger.info("Error parsing ptMaxFromXls: " + ptMaxFromXls);
						e.printStackTrace();
					}
					break;

				case 10:
					double tRispFromXls = 0.0;
					try {
						tRispFromXls = nextCell.getNumericCellValue();
						int f = (int) tRispFromXls;
						aRow.setTRrisp(f);
					} catch (Exception e) {
						logger.info("Error parsing tRispFromXls: " + tRispFromXls);
						e.printStackTrace();
					}
					break;

				case 11:
					double gpaFromXls = 0.0;
					try {
						gpaFromXls = nextCell.getNumericCellValue();
						double g = (double) gpaFromXls;
						aRow.setGpa(g);
					} catch (Exception e) {
						logger.info("Error parsing gpaFromXls: " + gpaFromXls);
						e.printStackTrace();
					}
					break;

				case 12:
					double gpdFromXls = 0.0;
					try {
						gpdFromXls = nextCell.getNumericCellValue();
						double h = (double) gpdFromXls;
						aRow.setGpd(h);
					} catch (Exception e) {
						logger.info("Error parsing gpdFromXls: " + gpdFromXls);
						e.printStackTrace();
					}
					break;

				case 13:
					double tavaFromXls = 0.0;
					try {
						tavaFromXls = nextCell.getNumericCellValue();
						int l = (int) tavaFromXls;
						aRow.setTava(l);
					} catch (Exception e) {
						logger.info("Error parsing tavaFromXls: " + tavaFromXls);
						e.printStackTrace();
					}
					break;

				case 14:
					double taraFromXls = 0.0;
					try {
						taraFromXls = nextCell.getNumericCellValue();
						int m = (int) taraFromXls;
						aRow.setTara(m);
					} catch (Exception e) {
						logger.info("Error parsing taraFromXls: " + taraFromXls);
						e.printStackTrace();
					}
					break;

				case 15:
					double brsFromXls = 0.0;
					try {
						brsFromXls = nextCell.getNumericCellValue();
						int n = (int) brsFromXls;
						aRow.setBrs(n);
					} catch (Exception e) {
						logger.info("Error parsing brsFromXls: " + brsFromXls);
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}
			logger.info("Adding: " + aRow.toString());
			records.add(aRow);
			rowCount++;

		}
		wb.close();
		inputStream.close();
		
		// Verifica righe vuote
				if (rowCount == 0) {
					logger.info("Nessuna riga piena trovata");
				}

		return records;
	}
	
	// Metodo per verificare se una cella è vuota
	private boolean isRowEmpty(XSSFRow row) {
	    int lastCellNum = row.getLastCellNum();
	    for (int i = 0; i < lastCellNum; i++) {
	        XSSFCell cell = row.getCell(i);
	        if (cell != null && cell.getCellType() != CellType.BLANK) {
	            return false;
	        }
	    }
	    return true;
	}

}
