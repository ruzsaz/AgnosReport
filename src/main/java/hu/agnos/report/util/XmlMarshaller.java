/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.agnos.report.util;

import hu.agnos.report.entity.Hierarchy;
import hu.agnos.report.entity.Indicator;
import hu.agnos.report.entity.Level;
import hu.agnos.report.entity.Report;
import hu.agnos.report.entity.Visualization;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author parisek
 */
public class XmlMarshaller {

    private final static Logger logger = LoggerFactory.getLogger(XmlMarshaller.class);

    public static boolean marshal(Report report, String filePath) {
        boolean result = true;
        try {
            reportWriter(report, filePath);
            xmlFormatter(filePath);
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
            logger.error(ex.getMessage());
            result = false;
        }
        return result;
    }

    private static void xmlFormatter(String filePath) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new InputStreamReader(new FileInputStream(filePath))));
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        Source source = new DOMSource(document);
        Result result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }

    private static void reportWriter(Report r, String filePath) {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        XMLStreamWriter xsw = null;
        try {
            xsw = xof.createXMLStreamWriter(new FileWriter(filePath));
            xsw.writeStartDocument();
            xsw.writeStartElement("Report");

            xsw.writeAttribute("cubeUniqueName", XmlEscaper.escape(r.getCubeName()));
            xsw.writeAttribute("reportUniqueName", XmlEscaper.escape(r.getName()));
            xsw.writeAttribute("dataUpdatedBy", XmlEscaper.escape(r.getUpdatedBy()));
            xsw.writeAttribute("databaseType", XmlEscaper.escape(r.getDatabaseType()));
            xsw.writeAttribute("roleToAccess", XmlEscaper.escape(r.getRoleToAccess()));
            for (int i = 0; i < r.getLanguageCount(); i++) {
                xsw.writeStartElement("ReportLabels");
                xsw.writeAttribute("lang", XmlEscaper.escape(r.getLanguages().get(i)));
                xsw.writeAttribute("caption", XmlEscaper.escape(r.getCaptions().get(i)));
                xsw.writeAttribute("description", XmlEscaper.escape(r.getDescriptions().get(i)));
                xsw.writeAttribute("datasource", XmlEscaper.escape(r.getDatasource().get(i)));
                xsw.writeEndElement();
            }

            for (int i = 0; i < r.getLanguageCount(); i++) {
                xsw.writeStartElement("ReportHelp");
                xsw.writeAttribute("lang", XmlEscaper.escape(r.getLanguages().get(i)));
                xsw.writeCData(r.getHelps().get(i));
                xsw.writeEndElement();

            }
            xsw.writeStartElement("Indicators");

            for (Indicator indicator : r.getIndicators()) {
                indicatorWriter(xsw, indicator, r.getLanguages());
            }
            xsw.writeEndElement();

            xsw.writeStartElement("Visualizations");
            for (Visualization v : r.getVisualizations()) {
                visualizationWriter(xsw, v);
            }
            xsw.writeEndElement();

            xsw.writeStartElement("Hierarchies");
            for (Hierarchy h : r.getHierarchies()) {
                hierarchyWriter(xsw, h, r.getLanguages());
            }
            xsw.writeEndElement();
            xsw.writeEndElement();
            xsw.writeEndDocument();
            xsw.flush();

        } catch (IOException | XMLStreamException e) {
            System.err.println("Unable to write the file: " + e.getMessage());
        } finally {
            try {
                if (xsw != null) {
                    xsw.close();
                }
            } catch (XMLStreamException e) {
                System.err.println("Unable to close the file: " + e.getMessage());
            }
        }
    }

    private static void hierarchyWriter(XMLStreamWriter xsw, Hierarchy hierarchy, ArrayList<String> languages) throws XMLStreamException {
        if (hierarchy.getHierarchyUniqueName() != null && !hierarchy.getHierarchyUniqueName().isEmpty()) {
            xsw.writeStartElement("Hierarchy");
            xsw.writeAttribute("id", Integer.toString(hierarchy.getId()));
            xsw.writeAttribute("uniqueName", XmlEscaper.escape(hierarchy.getHierarchyUniqueName()));

            if (hierarchy.getType() != null && !hierarchy.getType().isEmpty()) {
                xsw.writeAttribute("type", XmlEscaper.escape(hierarchy.getType()));
            }
            xsw.writeAttribute("allowedDepth", Integer.toString(hierarchy.getAllowedDepth()));

            for (int i = 0; i < languages.size(); i++) {
                xsw.writeStartElement("HierarchyNames");
                xsw.writeAttribute("lang", XmlEscaper.escape(languages.get(i)));
                xsw.writeAttribute("caption", XmlEscaper.escape(hierarchy.getCaptions().get(i)));
                xsw.writeAttribute("description", XmlEscaper.escape(hierarchy.getDescriptions().get(i)));
                xsw.writeAttribute("topLevelString", XmlEscaper.escape(hierarchy.getToplevelStrings().get(i)));
                xsw.writeEndElement();

            }

            xsw.writeStartElement("Levels");
            for (Level level : hierarchy.getLevels()) {
                levelWriter(xsw, level);
            }
            xsw.writeEndElement();

            xsw.writeEndElement();
        }
    }

    private static void levelWriter(XMLStreamWriter xsw, Level level) throws XMLStreamException {
        xsw.writeStartElement("Level");
        xsw.writeAttribute("depth", Integer.toString(level.getDepth()));
        xsw.writeAttribute("idColumnName", XmlEscaper.escape(level.getIdColumnName()));
        xsw.writeAttribute("codeColumnName", XmlEscaper.escape(level.getCodeColumnName()));
        xsw.writeAttribute("nameColumnName", XmlEscaper.escape(level.getNameColumnName()));
        xsw.writeEndElement();
    }

    private static void visualizationWriter(XMLStreamWriter xsw, Visualization visualization) throws XMLStreamException {
        if (visualization.getInitString() != null && !visualization.getInitString().isEmpty()) {
            xsw.writeStartElement("Visualization");
            xsw.writeAttribute("initString", XmlEscaper.escape(visualization.getInitString()));
            xsw.writeAttribute("order", Integer.toString(visualization.getOrder()));
            xsw.writeEndElement();
        }
    }

    private static void indicatorWriter(XMLStreamWriter xsw, Indicator indicator, ArrayList<String> languages) throws XMLStreamException {
        if (indicator.getValue().getMeasureUniqueName() != null && !indicator.getValue().getMeasureUniqueName().isEmpty()) {
            xsw.writeStartElement("Indicator");
            xsw.writeAttribute("id", Integer.toString(indicator.getId()));
            xsw.writeAttribute("valueUniqueName", XmlEscaper.escape(indicator.getValue().getMeasureUniqueName()));
            xsw.writeAttribute("valueIsHidden", Boolean.toString(indicator.getValue().isHidden()));
            xsw.writeAttribute("valueSign", Integer.toString(indicator.getValue().getSign()));
            xsw.writeAttribute("denominatorUniqueName", XmlEscaper.escape(indicator.getDenominator().getMeasureUniqueName()));
            xsw.writeAttribute("denominatorIsHidden", Boolean.toString(indicator.getDenominator().isHidden()));
            xsw.writeAttribute("denominatorMultiplier", Double.toString(indicator.getMultiplier()));
            xsw.writeAttribute("denominatorSign", Integer.toString(indicator.getDenominator().getSign()));

            if (indicator.hasExtraCalculation()) {
                xsw.writeStartElement("ExtraCalculation");
                xsw.writeAttribute("function", XmlEscaper.escape(indicator.getExtraCalculation().getFunction()));
                xsw.writeAttribute("args", XmlEscaper.escape(indicator.getExtraCalculation().getArgs()));
                xsw.writeEndElement();
            }

            for (int i = 0; i < languages.size(); i++) {
                xsw.writeStartElement("IndicatorLabels");
                xsw.writeAttribute("lang", languages.get(i));
                xsw.writeAttribute("caption", XmlEscaper.escape(indicator.getCaptions().get(i)));
                xsw.writeAttribute("description", XmlEscaper.escape(indicator.getDescriptions().get(i)));
                xsw.writeAttribute("valueUnit", XmlEscaper.escape(indicator.getValue().getUnits().get(i)));
                xsw.writeAttribute("valueUnitPlural", XmlEscaper.escape(indicator.getValue().getUnitPlurals().get(i)));
                xsw.writeAttribute("denominatorUnit", XmlEscaper.escape(indicator.getDenominator().getUnits().get(i)));
                xsw.writeAttribute("denominatorUnitPlural", XmlEscaper.escape(indicator.getDenominator().getUnitPlurals().get(i)));
                xsw.writeEndElement();
            }
            xsw.writeEndElement();
        }
    }

}
