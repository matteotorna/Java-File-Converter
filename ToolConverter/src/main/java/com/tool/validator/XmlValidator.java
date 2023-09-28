package com.tool.validator;
import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;


public class XmlValidator {

	public static void main(String[] args) {
		
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File("schema.xsd")); // sostituisco con il percorso del tuo file XSD
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new File("file.xml"))); // sostituisco con il percorso del tuo file XML
			System.out.println("Il file XML è valido.");
		} catch (Exception e) {
			System.out.println("Il file XML non è valido: " + e.getMessage());
		}
	}
}
