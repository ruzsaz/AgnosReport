/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.agnos.report.util;

import hu.agnos.report.entity.Indicator;
import hu.agnos.report.entity.AdditionalCalculation;
import hu.agnos.report.entity.Hierarchy;
import hu.agnos.report.entity.Level;
import hu.agnos.report.entity.Measure;
import hu.agnos.report.entity.Report;
import hu.agnos.report.entity.Visualization;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author parisek
 */
public class XmlParser extends DefaultHandler {

    //List to hold Employees object
//    private List<Employee> empList = null;
//    private Cube cube = null;

    private Report report = null;
    private Indicator indicator = null;

    private Hierarchy hierarchy = null;

    private boolean bReportHelp;
    private String reportHelpLang;

    ;
    
    public Report getReport() {
        return report;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        if (qName.equalsIgnoreCase(
                "Report")) {
            String cubeUniqueName = attributes.getValue("cubeUniqueName");
//            cube = new Cube(cubeUniqueName);
            String reportUniqueName = attributes.getValue("reportUniqueName");
          
            String dataUpdatedBy = attributes.getValue("dataUpdatedBy");
            String databaseType = attributes.getValue("databaseType");
            String roleToAccess = attributes.getValue("roleToAccess");
            report = new Report(reportUniqueName, cubeUniqueName, dataUpdatedBy, databaseType, roleToAccess);
            report.setRoleToAccess(roleToAccess);
//            cube.put(reportUniqueName, report);

        }

        else if (qName.equalsIgnoreCase(
                "ReportLabels") && report != null) {
            String lang = attributes.getValue("lang");
            String caption = attributes.getValue("caption");
            String description = attributes.getValue("description");
            String datasource = attributes.getValue("datasource");
            int langIdx = report.getLanguageIdx(lang);
            if (langIdx < 0) {
                report.addLanguage(lang);
                langIdx = report.getLanguageIdx(lang);
            }
            report.getCaptions().set(langIdx, caption);
            report.getDescriptions().set(langIdx, description);
            report.getDatasource().set(langIdx, datasource);

        } else if (qName.equalsIgnoreCase(
                "ReportHelp")) {
            reportHelpLang = attributes.getValue("lang");
            bReportHelp = true;
        } else if (qName.equalsIgnoreCase(
                "Indicator")) {
            int id = Integer.parseInt(attributes.getValue("id"));

            String valueUniqueName = attributes.getValue("valueUniqueName");

            int valueSign = Integer.parseInt(attributes.getValue("valueSign"));
            boolean valueIsHidden = Boolean.parseBoolean(attributes.getValue("valueIsHidden"));
            String denominatorUniqueName = attributes.getValue("denominatorUniqueName");

            int denominatorSign = Integer.parseInt(attributes.getValue("valueSign"));
            boolean denominatorIsHidden = Boolean.parseBoolean(attributes.getValue("denominatorIsHidden"));
            double denominatorMultiplier = Double.parseDouble(attributes.getValue("denominatorMultiplier"));

            int languageCnt = report.getLanguageCount();

            Measure value = new Measure(languageCnt, valueUniqueName, valueSign, valueIsHidden);
            Measure denominator = new Measure(languageCnt, denominatorUniqueName, denominatorSign, denominatorIsHidden);
            indicator = new Indicator(languageCnt, id, value, denominator, denominatorMultiplier);
            report.addIndicator(indicator);
        } else if (qName.equalsIgnoreCase(
                "IndicatorLabels")) {
            String lang = attributes.getValue("lang");
            String caption = attributes.getValue("caption");
            String description = attributes.getValue("description");
            String valueUnit = attributes.getValue("valueUnit");
            String valueUnitPlural = attributes.getValue("valueUnitPlural");
            if (valueUnitPlural == null) {
                valueUnitPlural = valueUnit;
            }

            String denominatorUnit = attributes.getValue("denominatorUnit");
            String denominatorUnitPlural = attributes.getValue("denominatorUnitPlural");
            if (denominatorUnitPlural == null) {
                denominatorUnitPlural = denominatorUnit;
            }

            int langIdx = report.getLanguageIdx(lang);
            if (langIdx < 0) {
                report.addLanguage(lang);
                langIdx = report.getLanguageIdx(lang);
            }

            this.indicator.getCaptions().set(langIdx, caption);
            this.indicator.getDescriptions().set(langIdx, description);

            this.indicator.getValue().getUnits().set(langIdx, valueUnit);
            this.indicator.getValue().getUnitPlurals().set(langIdx, valueUnitPlural);

            this.indicator.getDenominator().getUnits().set(langIdx, denominatorUnit);
            this.indicator.getDenominator().getUnitPlurals().set(langIdx, denominatorUnitPlural);

        } else if (qName.equalsIgnoreCase(
                "Visualization")) {
            String initString = attributes.getValue("initString");
            int order = Integer.parseInt(attributes.getValue("order"));
            Visualization visualization = new Visualization(initString, order);
            report.addVisualization(visualization);

        } else if (qName.equalsIgnoreCase(
                "Hierarchy")) {
            int id = Integer.parseInt(attributes.getValue("id"));
            String uniqueName = attributes.getValue("uniqueName");
            String type = attributes.getValue("type");
            int allowedDepth = Integer.parseInt(attributes.getValue("allowedDepth"));

            int languageNumber = report.getLanguageCount();

            hierarchy = new Hierarchy(languageNumber, id, uniqueName, type, allowedDepth);
            report.addHierarchy(hierarchy);
        } else if (qName.equalsIgnoreCase(
                "HierarchyNames")) {
            String lang = attributes.getValue("lang");

            String caption = attributes.getValue("caption");
            String description = attributes.getValue("description");
            String topLevelString = attributes.getValue("topLevelString");

            int langIdx = report.getLanguageIdx(lang);
            if (langIdx < 0) {
                report.addLanguage(lang);
                langIdx = report.getLanguageIdx(lang);
            }

            hierarchy.getCaptions().set(langIdx, caption);
            hierarchy.getDescriptions().set(langIdx, description);
            hierarchy.getToplevelStrings().set(langIdx, topLevelString);

        } else if (qName.equalsIgnoreCase(
                "Level")) {
            int depth = Integer.parseInt(attributes.getValue("depth"));
            String idColumnName = attributes.getValue("idColumnName");
            String codeColumnName = attributes.getValue("codeColumnName");
            String nameColumnName = attributes.getValue("nameColumnName");
            Level level = new Level(depth, idColumnName, codeColumnName, nameColumnName);
            hierarchy.addLevel(level);

        } else if (qName.equalsIgnoreCase(
                "AdditionalCalculation")) {
            String function = attributes.getValue("function");
            String args = attributes.getValue("args");
            AdditionalCalculation ac = new AdditionalCalculation(function, args);
            report.setAdditionalCalculation(ac);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        if (bReportHelp) {
            //age element, set Employee age
            String reportHelp = new String(ch, start, length);

            int langIdx = report.getLanguageIdx(this.reportHelpLang);
            if (langIdx < 0) {
                report.addLanguage(this.reportHelpLang);
                langIdx = report.getLanguageIdx(this.reportHelpLang);
            }
            this.report.getHelps().set(langIdx, reportHelp);
            bReportHelp = false;
        }
    }
   

}
