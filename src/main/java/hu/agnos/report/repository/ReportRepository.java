/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.agnos.report.repository;

import hu.agnos.report.util.XmlMarshaller;
import hu.agnos.report.util.XmlParser;
import hu.agnos.report.entity.Report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.Assert;
import org.xml.sax.SAXException;

/**
 *
 * @author parisek
 */
public class ReportRepository implements CrudRepository<Report, String> {

    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";
    private final Logger logger;
    private final String reportDirectoryURI;

    public ReportRepository() {
        logger = LoggerFactory.getLogger(ReportRepository.class);
        this.reportDirectoryURI = System.getenv("AGNOS_REPORTS_DIR");
    }

    @Override
    public Optional<Report> findById(String reportFileName) {
        Assert.notNull(reportFileName, ID_MUST_NOT_BE_NULL);
        Report result = null;
        if (reportFileName.toLowerCase().endsWith(".report.xml")) {
            if (validateXML(reportFileName)) {
                try {
                    String huntedReportFullName = reportFileName
                            .replaceAll(".report.xml", "")
                            .replaceAll(".REPORT.XML", "");

                    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                    SAXParser saxParser = saxParserFactory.newSAXParser();
                    XmlParser handler = new XmlParser();
                    saxParser.parse(new File(reportDirectoryURI, reportFileName), handler);
                    Report report = handler.getReport();
                    String reportFullName = new StringBuilder()
                            .append(report.getCubeName())
                            .append(".")
                            .append(report.getName())
                            .toString();
                    if (reportFullName.equals(huntedReportFullName)) {
                        result = report;
                    }
                } catch (ParserConfigurationException | SAXException | IOException e) {
                    logger.error(e.getMessage());
                    return Optional.empty();
                }
            }
        }
        return Optional.ofNullable(result);
    }

    public Optional<Report> findById(String cubeName, String reportName) {
        String reportFileName = new StringBuilder()
                .append(cubeName)
                .append(".")
                .append(reportName)
                .append(".report.xml")
                .toString();
        return findById(reportFileName);
    }

    @Override
    public List<Report> findAllById(Iterable<String> reportFileNames) {
        Assert.notNull(reportFileNames, ID_MUST_NOT_BE_NULL);
        List<Report> result = new ArrayList<>();
        for (String reportFileName : reportFileNames) {
            Optional<Report> optReport = findById(reportFileName);
            optReport.ifPresent(result::add);
        }
        return result;
    }

    @Override
    public List<Report> findAll() {
        Map<String, Report> tempReportStore = new HashMap<>();
        final File folder = new File(reportDirectoryURI);
        for (final File fileEntry : folder.listFiles()) {
            String fileName = fileEntry.getName();

            if (fileName.toLowerCase().endsWith(".report.xml")) {
                if (validateXML(fileName)) {
                    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                    try {
                        SAXParser saxParser = saxParserFactory.newSAXParser();
                        XmlParser handler = new XmlParser();
                        saxParser.parse(new File(reportDirectoryURI, fileName), handler);
                        storeCubeReports(tempReportStore, handler.getReport());
                    } catch (ParserConfigurationException | SAXException | IOException e) {
                        logger.error(e.getMessage());
                    }
                }

            }

        }
        return new ArrayList<>(tempReportStore.values());
    }

    @Override
    public boolean existsById(String reportFileName) {
        Assert.notNull(reportFileName, ID_MUST_NOT_BE_NULL);
        Optional<Report> optReport = findById(reportFileName);
        return optReport.isPresent();
    }

    @Override
    public long count() {
        List<Report> reports = findAll();
        return reports == null ? 0 : reports.size();
    }

    @Override
    public void delete(Report report) {
        Assert.notNull(report, ID_MUST_NOT_BE_NULL);
        String reportFullName = new StringBuilder()
                .append(report.getCubeName())
                .append(".")
                .append(report.getName())
                .append(".report.xml")
                .toString();
        File file = new File(this.reportDirectoryURI, reportFullName);
        if (file.exists()) {
            file.delete();
        }
    }

    public void deleteById(String cubeName, String reportName) {
        Assert.notNull(cubeName, ID_MUST_NOT_BE_NULL);
        Assert.notNull(reportName, ID_MUST_NOT_BE_NULL);
        String reportFullName = new StringBuilder()
                .append(cubeName)
                .append(".")
                .append(reportName)
                .append(".report.xml")
                .toString();
        File file = new File(this.reportDirectoryURI, reportFullName);
        if (file.exists()) {
            file.delete();
        }

    }

    @Override
    public void deleteById(String reportFileName) {
        Assert.notNull(reportFileName, ID_MUST_NOT_BE_NULL);
        Optional<Report> optReport = findById(reportFileName);
        if (optReport.isPresent()) {
            delete(optReport.get());
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Report> reports) {
        for (Report report : reports) {
            delete(report);
        }
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <S extends Report> S save(S s) {
        Assert.notNull(s, "Entity must not be null.");
        String reportFullName = new StringBuilder()
                .append(s.getCubeName())
                .append(".")
                .append(s.getName())
                .append(".report.xml")
                .toString();
        if (XmlMarshaller.marshal(s, this.reportDirectoryURI + "/" + reportFullName)) {
            return s;
        } else {
            return null;
        }
    }

    @Override
    public <S extends Report> List<S> saveAll(Iterable<S> reports) {
        List<S> result = new ArrayList<>();
        for (S report : reports) {
            S tempReport = save(report);
            if (tempReport != null) {
                result.add(tempReport);
            }
        }
        return result;
    }

    public List<String> getReportNamesByCubeName(String cubeName) {
        List<String> result = new ArrayList<>();
        for (Report r : findAll()) {
            if (r.getCubeName().equals(cubeName)) {
                result.add(r.getName());
            }
        }
        return result;
    }

    public List<String> getCubeNames() {
        List<String> result = new ArrayList<>();
        for (Report r : findAll()) {
            String cubeName = r.getCubeName();
            if (!result.contains(cubeName)) {
                result.add(cubeName);
            }
        }
        return result;
    }

    private void storeCubeReports(Map<String, Report> storage, Report report) {
        String reportKey = new StringBuilder()
                .append(report.getCubeName().toUpperCase())
                .append(".")
                .append(report.getName().toUpperCase())
                .toString();

        storage.remove(reportKey);
        storage.put(reportKey, report);
    }

    private boolean validateXML(String reportFileName) {
        try {
            Source xmlFile = new StreamSource(new File(this.reportDirectoryURI, reportFileName));
            SchemaFactory schemaFactory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory
                    .newSchema(getClass().getResource("/report.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
            return true;
        } catch (SAXException | IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

}
