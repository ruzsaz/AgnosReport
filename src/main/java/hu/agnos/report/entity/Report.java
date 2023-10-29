package hu.agnos.report.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.Base64;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
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
    private ArrayList<Cube> cubes;

    @JacksonXmlProperty(localName = "Labels")
    @JacksonXmlElementWrapper(useWrapping = false)
    private ArrayList<ReportLabels> labels;

    @JacksonXmlProperty(localName = "Help")
    @JacksonXmlElementWrapper(useWrapping = false)
    private ArrayList<ReportHelp> helps;

    @JacksonXmlElementWrapper(localName = "Indicators")
    @JacksonXmlProperty(localName = "Indicator")
    private ArrayList<Indicator> indicators;

    @JacksonXmlElementWrapper(localName = "Dimensions")
    @JacksonXmlProperty(localName = "Dimension")
    private ArrayList<Dimension> dimensions;

    @JacksonXmlElementWrapper(localName = "Visualizations")
    @JacksonXmlProperty(localName = "Visualization")
    private ArrayList<Visualization> visualizations;

    private boolean broken;

    public Report() {
        this.roleToAccess = "";
        this.cubes = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.helps = new ArrayList<>();
        this.indicators = new ArrayList<>();
        this.dimensions = new ArrayList<>();
        this.visualizations = new ArrayList<>();
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
        return this.labels.size();
    }

    public void addLanguage(String lang) {
        this.labels.add(new ReportLabels(lang));
        this.helps.add(new ReportHelp(lang));
        for (Indicator indicator : indicators) {
            indicator.addLanguage(lang);
        }
        for (Dimension dimension : dimensions) {
            dimension.addLanguage(lang);
        }
    }

    public void removeLanguage(int index) {
        this.labels.remove(index);
        this.helps.remove(index);
        for (Indicator indicator : indicators) {
            indicator.removeLanguage(index);
        }
        for (Dimension dimension : dimensions) {
            dimension.removeLanguage(index);
        }
    }

    @JsonIgnore
    public int getHierarchyIdxByName(String name) {
        for (int i = 0; i < this.dimensions.size(); i++) {
            if (this.dimensions.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public void addVisualization(Visualization entity) {
        this.visualizations.add(entity);
    }

    public void addIndicator(Indicator entity) {
        this.indicators.add(entity);
    }

    public AdditionalCalculation getAdditionalCalculation() {
        for (Indicator ind : indicators) {
            if (ind.hasExtraCalculation()) {
                return new AdditionalCalculation(ind.getExtraCalculation().getFunction(), ind.getExtraCalculation().getArgs() + "," + ind.getValueName());
            }
        }
        return null;
    }

    public boolean isAdditionalCalculation() {
        for (Indicator ind : indicators) {
            if (ind.hasExtraCalculation()) {
                return true;
            }
        }
        return false;
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


    private static String base64encoder(final String origin) {
        final byte[] encodedBytes = Base64.getEncoder().encode(origin.getBytes());
        return new String(encodedBytes);
    }

}
