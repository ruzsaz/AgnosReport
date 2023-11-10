package hu.agnos.report.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author parisek
 */
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
    private String databaseType;

    @JacksonXmlProperty(isAttribute = true)
    private String roleToAccess;

    @JacksonXmlElementWrapper(localName = "Cubes")
    @JacksonXmlProperty(localName = "Cube")
    private List<Cube> cubes;

    @JacksonXmlProperty(localName = "Labels")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ReportLabels> labels;

    @JacksonXmlProperty(localName = "Help")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ReportHelp> helps;

    @JacksonXmlElementWrapper(localName = "Indicators")
    @JacksonXmlProperty(localName = "Indicator")
    private List<Indicator> indicators;

    @JacksonXmlElementWrapper(localName = "Dimensions")
    @JacksonXmlProperty(localName = "Dimension")
    private List<Dimension> dimensions;

    @JacksonXmlElementWrapper(localName = "Visualizations")
    @JacksonXmlProperty(localName = "Visualization")
    private List<Visualization> visualizations;

    private boolean broken;

    public Report() {
        this.roleToAccess = "";
        this.cubes = new ArrayList<>(3);
        this.labels = new ArrayList<>(2);
        this.helps = new ArrayList<>(2);
        this.indicators = new ArrayList<>(6);
        this.dimensions = new ArrayList<>(6);
        this.visualizations = new ArrayList<>(4);
        this.broken = false;
    }

    public Report(String name) {
        this();
        this.name = name;
    }

    public Report(String name, String roleToAccess) {
        this(name);
        this.roleToAccess = roleToAccess;
    }

    private static String base64encoder(String origin) {
        byte[] encodedBytes = Base64.getEncoder().encode(origin.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
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
        labels.add(new ReportLabels(lang));
        helps.add(new ReportHelp(lang));
        for (Indicator indicator : indicators) {
            indicator.addLanguage(lang);
        }
        for (Dimension dimension : dimensions) {
            dimension.addLanguage(lang);
        }
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
    }

    @JsonIgnore
    public Dimension getDimensionByName(String name) {
        for (Dimension dimension : dimensions) {
            if (dimension.getName().equalsIgnoreCase(name)) {
                return dimension;
            }
        }
        return null;
    }

    public void addVisualization(Visualization entity) {
        visualizations.add(entity);
    }

    public void addIndicator(Indicator entity) {
        indicators.add(entity);
    }

    public Report deepCopy() {
        Report result = new Report(name);
        result.setRoleToAccess(roleToAccess);

        result.setLabels(new ArrayList<>(labels));
        result.setHelps(new ArrayList<>(helps));
        for (Indicator indicator : indicators) {
            result.indicators.add(indicator.deepCopy());
        }
        for (Dimension dimension : dimensions) {
            result.dimensions.add(dimension.deepCopy());
        }
        for (Visualization visualization : visualizations) {
            result.visualizations.add(visualization.deepCopy());
        }
        return result;
    }

    public String asJson() {
        try {
            return (new ObjectMapper()).writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
