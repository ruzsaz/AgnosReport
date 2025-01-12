package hu.agnos.report.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Keyword {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Icon")
    private String icon;

    @JacksonXmlProperty(localName = "Labels")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<KeywordLabels> labels;

    public Keyword() {
        this.labels = new ArrayList<>(2);
    }

    public Keyword(String name) {
        this();
        this.name = name;
    }

    public Keyword(String name, String icon) {
        this(name);
        this.icon = icon;
    }

    @JsonIgnore
    public int getLanguageIdx(String language) {
        int result = -1;
        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i).getLang().equals(language)) {
                result = i;
                break;
            }
        }
        return result;
    }

    @JsonIgnore
    public int getLanguageCount() {
        return labels.size();
    }

    public void addLanguage(String lang) {
        labels.add(new KeywordLabels(lang));
    }

    public void removeLanguage(int index) {
        labels.remove(index);
    }

    public String asJson() {
        try {
            return (new ObjectMapper()).writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
