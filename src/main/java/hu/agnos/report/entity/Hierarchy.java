/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.agnos.report.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.ArrayList;
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
public class Hierarchy {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private int allowedDepth;

    @JacksonXmlProperty(localName = "Labels")
    @JacksonXmlElementWrapper(useWrapping = false)
    private ArrayList<HierarchyLabels> multilingualization;

    public Hierarchy() {
        this.name = "";
        this.multilingualization = new ArrayList<>();
    }

    public Hierarchy(int languageNumber) {
        this();
        for (int i = 0; i < languageNumber; i++) {
            this.multilingualization.add(new HierarchyLabels());
        }
    }

    public Hierarchy(int languageNumber, String hierarchyUniqueName, String type, int allowedDepth) {
        this(languageNumber);
        this.name = hierarchyUniqueName;
        this.type = type;
        this.allowedDepth = allowedDepth;
    }

    public void addLanguage(String lang) {
        this.multilingualization.add(new HierarchyLabels(lang));
    }

    public void removeLanguage(int index) {
        this.multilingualization.remove(index);
    }

    public Hierarchy deepCopy() {
        Hierarchy result = new Hierarchy(multilingualization.size(), name, type, allowedDepth);
        result.setMultilingualization(new ArrayList<>(multilingualization));
        return result;
    }
}
