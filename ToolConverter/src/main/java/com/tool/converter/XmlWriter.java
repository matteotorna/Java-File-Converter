package com.tool.converter;

import com.tool.model.ExcelRecord;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class XmlWriter {

    public String writeXml(List<ExcelRecord> records) {
        try {
            Document mainDoc = DocumentHelper.createDocument();
            Element rootElement = mainDoc.addElement("root"); // Elemento radice generico

            for (ExcelRecord record : records) {
                Element recordElement = rootElement.addElement("record"); // Elemento radice per il record

                Map<String, Object> data = record.getData();
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    String columnName = entry.getKey();
                    Object value = entry.getValue();

                    // Crea elementi XML basati sulle colonne e i valori dell'ExcelRecord
                    Element columnElement = recordElement.addElement(getValidXmlName(columnName));
                    if (value != null) {
                        columnElement.setText(value.toString()); // Usa setText per impostare il contenuto dell'elemento
                    } else {
                        // Tratta valori nulli come vuoti o gestiscili come preferisci
                        columnElement.setText("");
                    }
                }
            }

            // Formatta il documento XML in modo che sia ben leggibile
            OutputFormat format = OutputFormat.createPrettyPrint();
            StringWriter stringWriter = new StringWriter();
            XMLWriter writer = new XMLWriter(stringWriter, format);
            writer.write(mainDoc);
            writer.flush();
            writer.close();

            return stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Tratta l'errore in qualche modo
        }
    }

    private String getValidXmlName(String columnName) {
        // Rimuovi i caratteri non validi sostituendoli con vuoti
        return columnName.replaceAll("[^a-zA-Z0-9]", "");
    }
}
