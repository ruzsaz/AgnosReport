/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.report.repository;

//import hu.mi.agnos.report.entity.Cube;
import hu.mi.agnos.report.entity.Report;
import hu.mi.agnos.report.util.XmlMarshaller;
import hu.mi.agnos.report.util.XmlParser;
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
    private final String reportDirectoriURI;
    private final String xsdURI;

    public ReportRepository(String reportDirectoriURI, String pathOfReportXSD) {
        logger = LoggerFactory.getLogger(ReportRepository.class);
        this.reportDirectoriURI
                = reportDirectoriURI.endsWith("/")
                ? reportDirectoriURI.substring(0, reportDirectoriURI.length() - 1)
                : reportDirectoriURI;
        this.xsdURI = new StringBuilder(pathOfReportXSD)
                .append(pathOfReportXSD.endsWith("/") ? "report.xsd" : "/report.xsd")
                .toString();
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
                    saxParser.parse(new File(reportDirectoriURI + "/" + reportFileName), handler);
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
            if (optReport.isPresent()) {
                result.add(optReport.get());
            }
        }
        return result;
    }

    @Override
    public List<Report> findAll() {
        Map<String, Report> tempReportStore = new HashMap<>();
        final File folder = new File(reportDirectoriURI);
        for (final File fileEntry : folder.listFiles()) {
            String fileName = fileEntry.getName();

            if (fileName.toLowerCase().endsWith(".report.xml")) {;
                if (validateXML(fileName)) {
                    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                    try {
                        SAXParser saxParser = saxParserFactory.newSAXParser();
                        XmlParser handler = new XmlParser();
                        saxParser.parse(new File(reportDirectoriURI + "/" + fileName), handler);
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
        File file = new File(this.reportDirectoriURI + "/" + reportFullName);
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
        Report report = (Report) s;

        String reportFullName = new StringBuilder()
                .append(report.getCubeName())
                .append(".")
                .append(report.getName())
                .append(".report.xml")
                .toString();
        if (XmlMarshaller.marshal(report, this.reportDirectoriURI + "/" + reportFullName)) {
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

        if (storage.containsKey(reportKey)) {
            storage.remove(reportKey);
        }
        storage.put(reportKey, report);
    }

    private boolean validateXML(String reportFileName) {
        try {
            Source xmlFile = new StreamSource(new File(this.reportDirectoriURI + "/" + reportFileName));
            SchemaFactory schemaFactory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory
                    .newSchema(new File(this.xsdURI));
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
            return true;
        } catch (SAXException | IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

}
