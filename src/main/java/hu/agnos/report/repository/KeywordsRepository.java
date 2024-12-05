package hu.agnos.report.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import javax.xml.XMLConstants;
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

import hu.agnos.report.entity.Keyword;
import hu.agnos.report.entity.Keywords;
import hu.agnos.report.entity.Report;

/**
 * @author parisek
 */
public class KeywordsRepository implements CrudRepository<Keyword, String> {

    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";
    private static final String KEYWORDSFILENAME = "keywords.xml";
    private static final Logger logger = LoggerFactory.getLogger(KeywordsRepository.class);
    private final String keywordsDirectoryURI;

    public KeywordsRepository() {
        this.keywordsDirectoryURI = System.getenv("AGNOS_REPORTS_DIR");
    }

    public Optional<Keyword> findByName(String keywordName) {
        Assert.notNull(keywordName, KeywordsRepository.ID_MUST_NOT_BE_NULL);
        return findById(keywordName);
    }

    @Override
    public Optional<Keyword> findById(String keywordName) {
        Assert.notNull(keywordName, KeywordsRepository.ID_MUST_NOT_BE_NULL);
        List<Keyword> all = findAll();
        return all.stream().filter(k -> k.getName().equals(keywordName)).findFirst();
    }

    @Override
    public List<Keyword> findAllById(Iterable<String> keywordNames) {
        Assert.notNull(keywordNames, KeywordsRepository.ID_MUST_NOT_BE_NULL);
        List<Keyword> all = findAll();
        return all.stream().filter(k -> StreamSupport.stream(keywordNames.spliterator(), false).anyMatch(k.getName()::equals)).toList();
    }

    @Override
    public List<Keyword> findAll() {
        Path file = Path.of(keywordsDirectoryURI, KEYWORDSFILENAME);
        if (validateXML(file.toString())) {
            try {
                XmlMapper xmlMapper = new XmlMapper();
                Keywords result = xmlMapper.readValue(file.toFile(), Keywords.class);
                return result.getKeywords();
            } catch (IOException ex) {
                KeywordsRepository.logger.error(ex.getMessage());
                return new ArrayList<>(10);
            }
        }
        return new ArrayList<>(10);
    }

    /*
    private static void storeCubeReports(Map<String, Report> storage, Report report) {
        String reportKey = report.getName().toUpperCase(Locale.ROOT);
        storage.remove(reportKey);
        storage.put(reportKey, report);
    }
     */

    @Override
    public boolean existsById(String keywordName) {
        Assert.notNull(keywordName, KeywordsRepository.ID_MUST_NOT_BE_NULL);
        Optional<Keyword> optKeyword = findById(keywordName);
        return optKeyword.isPresent();
    }

    @Override
    public long count() {
        List<Keyword> keywords = findAll();
        return (keywords == null ? 0 : keywords.size());
    }

    @Override
    public void deleteById(String s) {
        Assert.notNull(s, KeywordsRepository.ID_MUST_NOT_BE_NULL);
        KeywordsRepository.logger.error("Not implemented");
        // TODO: finish
    }

    @Override
    public void delete(Keyword keyword) {
        Assert.notNull(keyword, KeywordsRepository.ID_MUST_NOT_BE_NULL);
        KeywordsRepository.logger.error("Not implemented");
        // TODO: finish
    }

    @Override
    public void deleteAllById(Iterable<? extends String> ids) {
        KeywordsRepository.logger.error("Not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends Keyword> keywords) {
        KeywordsRepository.logger.error("Not implemented");
    }

    @Override
    public void deleteAll() {
        KeywordsRepository.logger.error("Not implemented");
    }

    @Override
    public <S extends Keyword> S save(S entity) {
        Assert.notNull(entity, "Entity must not be null.");
        KeywordsRepository.logger.error("Not implemented");
        /*
        String reportFullName = entity.getName() + ".report.xml";
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.writeValue(new File(keywordsDirectoryURI, reportFullName), entity);
        } catch (IOException ex) {
            KeywordsRepository.logger.error(ex.getMessage());
            return null;
        }
        */
        return entity;
    }

    @Override
    public <S extends Keyword> List<S> saveAll(Iterable<S> keywords) {
        List<S> result = new ArrayList<>(10);
        for (S keyword : keywords) {
            S tempKeyword = save(keyword);
            if (tempKeyword != null) {
                result.add(tempKeyword);
            }
        }
        return result;
    }

    private boolean validateXML(String fileName) {
        try {
            Source xmlFile = new StreamSource(new File(fileName));
            SchemaFactory schemaFactory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory
                    .newSchema(getClass().getResource("/keywords.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
            return true;
        } catch (SAXException | IOException e) {
            KeywordsRepository.logger.error(e.getMessage());
            return false;
        }
    }

}
