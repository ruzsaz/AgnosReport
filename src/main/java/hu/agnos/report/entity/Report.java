package hu.agnos.report.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import hu.agnos.report.jacksonIgnoreUtils.JsonIgnoreWhenPersist;
import hu.agnos.report.jacksonIgnoreUtils.DisableIgnoreWhenPersistInspector;
import hu.agnos.report.jacksonIgnoreUtils.JsonIgnoreWhenSend;
import hu.agnos.report.repository.KeywordsRepository;

@Getter
@Setter
@ToString
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "Report")
public class Report {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String roleToAccess;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean saveAllowed;

    @JacksonXmlElementWrapper(localName = "Cubes")
    @JacksonXmlProperty(localName = "Cube")
    private List<Cube> cubes;

    @JacksonXmlProperty(localName = "Labels")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ReportLabels> labels;

    @JacksonXmlProperty(localName = "Help")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ReportHelp> helps;

    @JacksonXmlElementWrapper(localName = "Dimensions")
    @JacksonXmlProperty(localName = "Dimension")
    private List<Dimension> dimensions;

    @JacksonXmlElementWrapper(localName = "Indicators")
    @JacksonXmlProperty(localName = "Indicator")
    private List<Indicator> indicators;

    @JacksonXmlElementWrapper(localName = "Visualizations")
    @JacksonXmlProperty(localName = "Visualization")
    private List<Visualization> visualizations;

    @JsonIgnoreWhenPersist
    private List<Keyword> keywords;

    @JacksonXmlElementWrapper(localName = "Keywords")
    @JacksonXmlProperty(localName = "Keyword")
    @JsonIgnoreWhenSend
    private List<String> keywordStrings;

    @JacksonXmlProperty(localName = "Kpi")
    private Kpi kpi;

    @JsonIgnoreWhenPersist
    @JsonRawValue
    private String topLevelValues;

    @JsonIgnore
    private boolean broken;

    public Report() {
        this.roleToAccess = "";
        this.saveAllowed = true;
        this.cubes = new ArrayList<>(3);
        this.labels = new ArrayList<>(2);
        this.helps = new ArrayList<>(2);
        this.dimensions = new ArrayList<>(6);
        this.indicators = new ArrayList<>(6);
        this.visualizations = new ArrayList<>(4);
        this.keywords = new ArrayList<>(5);
        this.keywordStrings = new ArrayList<>(5);
        this.broken = false;
        this.kpi = new Kpi();
        this.topLevelValues = "";
    }

    public Report(String name) {
        this();
        this.name = name;
    }

    public Report(String name, String roleToAccess) {
        this(name);
        this.roleToAccess = roleToAccess;
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
    public List<String> getCubeNames() {
        return cubes.stream().map(Cube::getName).toList();
    }

    @JsonIgnore
    public int getLanguageCount() {
        return labels.size();
    }

    public void addLanguage(String lang) {
        labels.add(new ReportLabels(lang));
        helps.add(new ReportHelp(lang));
        for (Indicator indicator : indicators) {
            indicator.addLanguage(lang);
        }
        for (Dimension dimension : dimensions) {
            dimension.addLanguage(lang);
        }
        kpi.addLanguage(lang);
    }

    public void removeLanguage(int index) {
        labels.remove(index);
        helps.remove(index);
        for (Indicator indicator : indicators) {
            indicator.removeLanguage(index);
        }
        for (Dimension dimension : dimensions) {
            dimension.removeLanguage(index);
        }
        kpi.removeLanguage(index);
    }

    @JsonIgnore
    public Dimension getDimensionByName(String nameToFind) {
        for (Dimension dimension : dimensions) {
            if (dimension.getName().equalsIgnoreCase(nameToFind)) {
                return dimension;
            }
        }
        return null;
    }

    public void setUnitedVisualization(String initString) {
        if (initString == null || initString.isEmpty()) {
            visualizations = new ArrayList<>();
            return;
        }
        visualizations = Arrays.stream(initString.split(";")).map(Visualization::new).toList();
    }

    @JsonIgnore
    public String getUnitedVisualization() {
        return visualizations.stream().map(Visualization::getInitString).collect(Collectors.joining(";"));
    }

    public void addIndicator(Indicator entity) {
        indicators.add(entity);
    }

    public String asJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setAnnotationIntrospector(new DisableIgnoreWhenPersistInspector());
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void fillKeywordsFromKeywordStrings() {
        List<Keyword> allKeywords = new KeywordsRepository().findAll();
        this.keywords = new ArrayList<>(keywordStrings.size());
        for (String kString : keywordStrings) {
            Keyword keyword = fromKeywordString(allKeywords, kString);
            if (keyword != null) {
                keywords.add(keyword);
            }
        }
    }

    private Keyword fromKeywordString(List<Keyword> all, String kString) {
        return all.stream().filter(k -> k.getName().equals(kString)).findFirst().orElse(null);
    }

}
