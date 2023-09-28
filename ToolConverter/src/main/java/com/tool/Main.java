package com.tool;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tool.converter.JsonWriter;
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
            String xmlData = xmlWriter.writeXml(records);

            logger.info("Generating JSON data..");
            JsonWriter jsonWriter = new JsonWriter();
            String jsonData = jsonWriter.writeJson(records); // Ottieni la stringa JSON
            logger.info(jsonData); // Stampa la rappresentazione JSON

            // Puoi fare quello che vuoi con xmlData e jsonData, ad esempio inviarli su una rete o elaborarli ulteriormente
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
