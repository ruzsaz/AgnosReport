package hu.agnos.report.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

/**
 * @author parisek
 */
@Getter
@Setter
@ToString
public class Dimension {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private int allowedDepth;

    @JacksonXmlProperty(localName = "Labels")
    @JacksonXmlElementWrapper(useWrapping = false)
    private ArrayList<DimensionLabels> multilingualization;

    public Dimension() {
        this.name = "";
        this.multilingualization = new ArrayList<>(2);
    }

    public Dimension(int languageNumber) {
        this();
        for (int i = 0; i < languageNumber; i++) {
            multilingualization.add(new DimensionLabels());
        }
    }

    public Dimension(int languageNumber, String hierarchyUniqueName, String type, int allowedDepth) {
        this(languageNumber);
        this.name = hierarchyUniqueName;
        this.type = type;
        this.allowedDepth = allowedDepth;
    }

    public void addLanguage(String lang) {
        multilingualization.add(new DimensionLabels(lang));
    }

    public void removeLanguage(int index) {
        multilingualization.remove(index);
    }

    public Dimension deepCopy() {
        Dimension result = new Dimension(multilingualization.size(), name, type, allowedDepth);
        result.setMultilingualization(new ArrayList<>(multilingualization));
        return result;
    }

}
