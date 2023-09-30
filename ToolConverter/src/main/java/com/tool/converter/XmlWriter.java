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
            Element rootElement = mainDoc.addElement("data");
            rootElement.addNamespace("xmlns", "http://www.example.com/xmlns");
            rootElement.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            rootElement.addAttribute("xsi:schemaLocation", "http://www.example.com/xmlns schema.xsd");

            for (ExcelRecord record : records) {
                Element recordElement = rootElement.addElement("record");

                Map<String, Object> data = record.getData();
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    String columnName = entry.getKey();
                    Object value = entry.getValue();

                    Element columnElement = recordElement.addElement(getValidXmlName(columnName));
                    if (value != null) {
                        columnElement.setText(value.toString());
                    } else {
                        columnElement.setText("");
                    }
                }
            }

            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setNewlines(true);
            format.setTrimText(false);
            StringWriter stringWriter = new StringWriter();
            XMLWriter writer = new XMLWriter(stringWriter, format);
            writer.write(mainDoc);
            writer.flush();
            writer.close();

            return stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getValidXmlName(String columnName) {
        return columnName.replaceAll("[^a-zA-Z0-9]", "");
    }
}
