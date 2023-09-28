package com.tool.converter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tool.model.ExcelRecord;

public class XmlWriter {

    public void writeXml(List<ExcelRecord> vdt, String filePath) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Root creation
            String namespace = "http://www.w3.org/2001/XMLSchema-instance";
            Element rootElement = doc.createElement("FLUSSO");
            rootElement.setAttribute("xmlns:xsi", namespace);
            doc.appendChild(rootElement);

            Element inserisciElement = doc.createElement("INSERISCI");
            rootElement.appendChild(inserisciElement);

            Map<String, Element> vdtElementsMap = new HashMap<>();

            for (ExcelRecord record : vdt) {
                String dataOraInizio = record.getDataOraInizio();
                String dataOraFine = record.getDataOraFine();

                if (!vdtElementsMap.containsKey(dataOraInizio)) {
                    Element vdtElement = doc.createElement("VDT");
                    vdtElement.setAttribute("DATAORAINIZIO", dataOraInizio);
                    vdtElement.setAttribute("DATAORAFINE", dataOraFine);

                    Element codiceEtsoElement = doc.createElement("CODICEETSO");
                    codiceEtsoElement.appendChild(doc.createTextNode(record.getCodiceEtso()));
                    vdtElement.appendChild(codiceEtsoElement);

                    Element IdMotivazioneElement = doc.createElement("IDMOTIVAZIONE");
                    IdMotivazioneElement.appendChild(doc.createTextNode(record.getIdMotivazione()));
                    vdtElement.appendChild(IdMotivazioneElement);

                    Element noteElement = doc.createElement("NOTE");
                    noteElement.appendChild(doc.createTextNode("Caricamento VDT"));
                    vdtElement.appendChild(noteElement);

                    vdtElementsMap.put(dataOraInizio, vdtElement);
                    inserisciElement.appendChild(vdtElement);
                }

                Element vdtElement = vdtElementsMap.get(dataOraInizio);

                Element fasciaElement = doc.createElement("FASCIA");
                vdtElement.appendChild(fasciaElement);

                Element psMinElement = doc.createElement("PSMIN");
                psMinElement.appendChild(doc.createTextNode(String.valueOf(record.getPsMin())));
                fasciaElement.appendChild(psMinElement);

                Element psMaxElement = doc.createElement("PSMAX");
                psMaxElement.appendChild(doc.createTextNode(String.valueOf(record.getPsMax())));
                fasciaElement.appendChild(psMaxElement);

                Element assettoElement = doc.createElement("ASSETTO");
                fasciaElement.appendChild(assettoElement);

                Element idAssettoElement = doc.createElement("IDASSETTO");
                idAssettoElement.appendChild(doc.createTextNode(record.getIdAssetto()));
                assettoElement.appendChild(idAssettoElement);

                Element ptMinElement = doc.createElement("PTMIN");
                ptMinElement.appendChild(doc.createTextNode(String.valueOf(record.getPtMin())));
                assettoElement.appendChild(ptMinElement);

                Element ptMaxElement = doc.createElement("PTMAX");
                ptMaxElement.appendChild(doc.createTextNode(String.valueOf(record.getPtMax())));
                assettoElement.appendChild(ptMaxElement);

                Element trispElement = doc.createElement("TRISP");
                trispElement.appendChild(doc.createTextNode(String.valueOf(record.getTRrisp())));
                assettoElement.appendChild(trispElement);

                Element gpaElement = doc.createElement("GPA");
                gpaElement.appendChild(doc.createTextNode(String.valueOf(record.getGpa())));
                assettoElement.appendChild(gpaElement);

                Element gpdElement = doc.createElement("GPD");
                gpdElement.appendChild(doc.createTextNode(String.valueOf(record.getGpd())));
                assettoElement.appendChild(gpdElement);

                Element tavaElement = doc.createElement("TAVA");
                tavaElement.appendChild(doc.createTextNode(String.valueOf(record.getTava())));
                assettoElement.appendChild(tavaElement);

                Element taraElement = doc.createElement("TARA");
                taraElement.appendChild(doc.createTextNode(String.valueOf(record.getTara())));
                assettoElement.appendChild(taraElement);

                Element brsElement = doc.createElement("BRS");
                brsElement.appendChild(doc.createTextNode(String.valueOf(record.getBrs())));
                assettoElement.appendChild(brsElement);

            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);

            try (FileOutputStream fos = new FileOutputStream(filePath);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 BufferedWriter writer = new BufferedWriter(osw)) {

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                writer.newLine();
                transformer.transform(source, new StreamResult(writer));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
