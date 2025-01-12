package hu.agnos.report.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

/**
 * @author parisek
 */
public class KeywordsRepository implements CrudRepository<Keyword, String> {

    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";
    private static final String KEYWORDSFILENAME = "keywords.xml";
    private static final Logger log = LoggerFactory.getLogger(KeywordsRepository.class);
    private final String keywordsDirectoryURI;

    public KeywordsRepository() {
        this.keywordsDirectoryURI = System.getenv("AGNOS_REPORTS_DIR");
    }

    public Optional<Keyword> findByName(String keywordName) {
        Assert.notNull(keywordName, ID_MUST_NOT_BE_NULL);
        return findById(keywordName);
    }

    @Override
    public Optional<Keyword> findById(String keywordName) {
        Assert.notNull(keywordName, ID_MUST_NOT_BE_NULL);
        List<Keyword> all = findAll();
        return all.stream().filter(k -> k.getName().equals(keywordName)).findFirst();
    }

    @Override
    public List<Keyword> findAllById(Iterable<String> keywordNames) {
        Assert.notNull(keywordNames, ID_MUST_NOT_BE_NULL);
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
                log.error(ex.getMessage());
                return new ArrayList<>(10);
            }
        }
        return new ArrayList<>(10);
    }

    @Override
    public boolean existsById(String keywordName) {
        Assert.notNull(keywordName, ID_MUST_NOT_BE_NULL);
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
        Assert.notNull(s, ID_MUST_NOT_BE_NULL);
        log.error("Delete by id not implemented");
        // TODO: finish
    }

    @Override
    public void delete(Keyword keyword) {
        Assert.notNull(keyword, ID_MUST_NOT_BE_NULL);
        log.error("Delete not implemented");
        // TODO: finish
    }

    @Override
    public void deleteAllById(Iterable<? extends String> ids) {
        log.error("Delete all by ids not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends Keyword> keywords) {
        log.error("Delete all keywords not implemented");
    }

    @Override
    public void deleteAll() {
        log.error("Delete all not implemented");
    }

    @Override
    public <S extends Keyword> S save(S entity) {
        Assert.notNull(entity, "Entity must not be null.");
        log.error("Saving not implemented");
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
            log.error(e.getMessage());
            return false;
        }
    }

}
