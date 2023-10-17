/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.agnos.report.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @JacksonXmlElementWrapper(localName = "Hierarchies")
    @JacksonXmlProperty(localName = "Hierarchy")
    private ArrayList<Hierarchy> hierarchies;

    @JacksonXmlElementWrapper(localName = "Visualizations")
    @JacksonXmlProperty(localName = "Visualization")
    private ArrayList<Visualization> visualizations;

    public Report() {
        this.roleToAccess = "";
        this.cubes = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.helps = new ArrayList<>();
        this.indicators = new ArrayList<>();
        this.hierarchies = new ArrayList<>();
        this.visualizations = new ArrayList<>();
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
        for (Hierarchy hierarchy : hierarchies) {
            hierarchy.addLanguage(lang);
        }
    }

    public void removeLanguage(int index) {
        this.labels.remove(index);
        this.helps.remove(index);
        for (Indicator indicator : indicators) {
            indicator.removeLanguage(index);
        }
        for (Hierarchy hierarchy : hierarchies) {
            hierarchy.removeLanguage(index);
        }
    }

    @JsonIgnore
    public int getHierarchyIdxByName(String name) {
        for (int i = 0; i < this.hierarchies.size(); i++) {
            if (this.hierarchies.get(i).getName().toUpperCase().equals(name.toUpperCase())) {
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

    public Report deepCopy() {
        Report result = new Report(name);
        result.setRoleToAccess(roleToAccess);

        result.setLabels(new ArrayList<>(labels));
        result.setHelps(new ArrayList<>(helps));
        for (Indicator indicator : indicators) {
            result.indicators.add(indicator.deepCopy());
        }
        for (Hierarchy hierarchy : hierarchies) {
            result.hierarchies.add(hierarchy.deepCopy());
        }
        for (Visualization visualization : visualizations) {
            result.visualizations.add(visualization.deepCopy());
        }
        return result;
    }

}
