package com.tool.struts;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

public class DownloadExampleAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private InputStream inputStream;

	public InputStream getInputStream() {
		return inputStream;
	}

	public String execute() throws IOException {
		ServletContext context = ServletActionContext.getServletContext();

		String exampleFileName = "Example.xlsx"; //  tuo file di esempio
		String resourcePath = "/WEB-INF/excelFile/" + exampleFileName; // Percorso del file di esempio nella specifica directory
																		

		InputStream inputStream = context.getResourceAsStream(resourcePath);
		if (inputStream == null) {
			addActionError("File non trovato");
			return ERROR;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, bytesRead);
		}
		inputStream.close();
		this.inputStream = new ByteArrayInputStream(baos.toByteArray());

		return SUCCESS;
	}
}
