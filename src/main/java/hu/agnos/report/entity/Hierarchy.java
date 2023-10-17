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

    /**
     * Zsoltikaokos: A metódus által visszaadott string része lesz az SGL query
     * WHERE részénak Ha a level tagja a hierarhiának, akkor az egy szintel
     * alatta lévő levelID != null és az az alatt lévőnek nullnak egyébként a
     * legfelső levelnek null-nal kell lennie
     *
     * @param levelId
     * @return
     */
//    public String getHierarchyFilterString(String levelId) {
//        String result = "";
//        if (levelId == null || (!isMember(levelId))) {
//            result += " AND " + this.levels.get(1).getIdColumnName() + " is null";
//        } else {
//            int hierarchySize = this.levels.size();
//            for (int i = 0; i < hierarchySize; i++) {
//                if (this.levels.get(i).getIdColumnName().equals(levelId)) {
//                    //ha a gyereknek is van gyereke
//                    if (i < (hierarchySize - 2)) {
//                        result += " AND " + this.levels.get(i + 1).getIdColumnName() + " is not null AND "
//                                + this.levels.get(i + 2).getIdColumnName() + " is null";
//                        break;
//                    } // ha a gyereknek már nincs gyereke
//                    else if (i < (hierarchySize - 1)) {
//                        result += " AND " + this.levels.get(i + 1).getIdColumnName() + " is not null";
//                        break;
//                    }
//                }
//            }
//
//        }
//        return result;
//    }
    /**
     * Ez visszaadja az új elem beszúrási indexét. Ezt arra használjuk, hogy a
     * hierarchiában a szintek rendezetten tárolódhassanak.
     *
     * @param entity a beszúrandó Level
     * @return az új elem beszúrásának indexe
     */
//    private int getInsertIdxOfNewLevel(Level entity) {
//        int result = -1;
//        for (int i = 0; i < this.levels.size(); i++) {
//            if (this.levels.get(i).getDepth() > entity.getDepth()) {
//                result = i;
//                break;
//            }
//        }
//        if (result == -1) {
//            result = this.levels.size();
//        }
//        return result;
//    }
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
