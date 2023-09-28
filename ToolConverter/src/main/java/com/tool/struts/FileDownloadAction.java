package com.tool.struts;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.opensymphony.xwork2.ActionSupport;

public class FileDownloadAction extends ActionSupport {
    private static final long serialVersionUID = 1L;
    private String filename;
    private InputStream inputStream;

    public String execute() throws IOException {
        String filePath = "C://Users//Tornaboni-Dk//Documents//" + filename;
        Path file = Paths.get(filePath);

        if (Files.exists(file)) {
            try {
                inputStream = new FileInputStream(file.toFile());
            } catch (FileNotFoundException e) {
                return ERROR;
            }
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
