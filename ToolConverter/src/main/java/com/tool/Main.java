package com.tool;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tool.converter.XmlWriter;
import com.tool.model.ExcelRecord;
import com.tool.util.ExcelReader;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("Start!");

        // Leggi il percorso del file Excel dall'utente
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il percorso del file Excel da leggere: ");
        String excelFilePath = scanner.nextLine();

        List<ExcelRecord> records = null;
        String xmlData = null; // Dichiarazione della variabile xmlData

        logger.info("Reading xlsx file..");
        ExcelReader reader = new ExcelReader();

        try (InputStream inputStream = Main.class.getResourceAsStream(excelFilePath)) {
            if (inputStream == null) {
                logger.error("File non trovato: " + excelFilePath);
                return;
            }

            records = reader.readDataFromExcel(inputStream);

            // Ora invece di scrivere su file, converti in stringhe XML e JSON
            logger.info("Generating XML data..");
            XmlWriter xmlWriter = new XmlWriter();
            xmlData = xmlWriter.writeXml(records); // Assegnamento del valore restituito

            // Scrivi la stringa XML su un file (ad esempio, "output.xml")
            writeXmlToFile(xmlData, "output.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeXmlToFile(String xmlData, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(xmlData);
            logger.info("XML data written to file: " + filePath);
        } catch (IOException e) {
            logger.error("Error writing XML data to file: " + filePath);
            e.printStackTrace();
        }
    }
}
