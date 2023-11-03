package hu.agnos.report.repository;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import hu.agnos.report.entity.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.Assert;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author parisek
 */
public class ReportRepository implements CrudRepository<Report, String> {

    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";
    private static final Logger logger = LoggerFactory.getLogger(ReportRepository.class);
    private final String reportDirectoryURI;

    public ReportRepository() {
        this.reportDirectoryURI = System.getenv("AGNOS_REPORTS_DIR");
    }

    public Optional<Report> findByName(String reportName) {
        Assert.notNull(reportName, ID_MUST_NOT_BE_NULL);
        return findById(reportName + ".report.xml");
    }

    @Override
    public Optional<Report> findById(String reportFileName) {
        Assert.notNull(reportFileName, ID_MUST_NOT_BE_NULL);
        Report result = null;
        if (reportFileName.toLowerCase(Locale.ROOT).endsWith(".report.xml")) {
            if (validateXML(reportFileName)) {
                try {
                    XmlMapper xmlMapper = new XmlMapper();
                    result = xmlMapper.readValue(new File(reportDirectoryURI, reportFileName), Report.class);
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                    return Optional.empty();
                }
            }
        }
        return Optional.ofNullable(result);
    }

    @Override
    public List<Report> findAllById(Iterable<String> reportFileNames) {
        Assert.notNull(reportFileNames, ID_MUST_NOT_BE_NULL);
        List<Report> result = new ArrayList<>(10);
        for (String reportFileName : reportFileNames) {
            Optional<Report> optReport = findById(reportFileName);
            optReport.ifPresent(result::add);
        }
        return result;
    }

    @Override
    public List<Report> findAll() {
        Map<String, Report> tempReportStore = new HashMap<>(10);
        File folder = new File(reportDirectoryURI);
        for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            String fileName = fileEntry.getName();
            if (fileName.toLowerCase(Locale.ROOT).endsWith(".report.xml")) {
                Optional<Report> optReport = findById(fileName);
                optReport.ifPresent(report -> storeCubeReports(tempReportStore, report));
            }
        }
        return new ArrayList<>(tempReportStore.values());
    }

    private static void storeCubeReports(Map<String, Report> storage, Report report) {
        String reportKey = report.getName().toUpperCase(Locale.ROOT);
        storage.remove(reportKey);
        storage.put(reportKey, report);
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
        String reportFullName = report.getName() + ".report.xml";
        File file = new File(reportDirectoryURI, reportFullName);
        if (file.exists()) {
            file.delete();
        }
    }

    public void deleteById(String reportName) {
        Assert.notNull(reportName, ID_MUST_NOT_BE_NULL);
        String reportFullName = reportName + ".report.xml";
        File file = new File(reportDirectoryURI, reportFullName);
        if (file.exists()) {
            file.delete();
        }

    }

    @Override
    public void deleteAllById(Iterable<? extends String> ids) {
        logger.error("Not implemented");
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
    public <S extends Report> S save(S entity) {
        Assert.notNull(entity, "Entity must not be null.");
        String reportFullName = entity.getName() + ".report.xml";
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.writeValue(new File(reportDirectoryURI, reportFullName), entity);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            return null;
        }
        return entity;
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

    private boolean validateXML(String reportFileName) {
        try {
            Source xmlFile = new StreamSource(new File(reportDirectoryURI, reportFileName));
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
