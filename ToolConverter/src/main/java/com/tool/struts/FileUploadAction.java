package com.tool.struts;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.tool.converter.XmlWriter;
import com.tool.model.ExcelRecord;
import com.tool.util.ExcelReader;

public class FileUploadAction extends ActionSupport {
    private static final long serialVersionUID = 1L;
    private File file;
    private String fileContentType;
    private String fileFileName;
    private InputStream inputStream;

    public String execute() throws Exception {
        if (file != null) {
            // Processa il file qui
            ExcelReader reader = new ExcelReader();
            List<ExcelRecord> records = reader.readBooksFromExcelFile(new FileInputStream(file));

            // Elabora i record qui, ad esempio convertendoli in un file XML
            XmlWriter writer = new XmlWriter();
            String outputFilePath = "C://Users//Tornaboni-Dk//Documents//output.xml";
            writer.writeXml(records, outputFilePath);

            // Invia il file XML al client
            inputStream = new FileInputStream(new File(outputFilePath));

            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getFileFileName() {
        return fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }
    
    public InputStream getInputStream() {
        return inputStream;
    }
}
