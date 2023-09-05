/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.agnos.report.entity;

import java.util.ArrayList;

/**
 *
 * @author parisek
 */
public class Hierarchy {

    private int id;
    private String hierarchyUniqueName;
    private ArrayList<String> captions;
    private ArrayList<String> toplevelStrings;
    private ArrayList<String> descriptions;
    private String type;
    private ArrayList<Level> levels;
    private int allowedDepth;

    public Hierarchy() {
        this.hierarchyUniqueName = "";
    
    }

    
    public Hierarchy(int languageNumber) {
        this();
        captions = new ArrayList<>();
        toplevelStrings = new ArrayList<>();
        descriptions = new ArrayList<>();
        for (int i = 0; i < languageNumber; i++) {
            captions.add("");
            toplevelStrings.add("");
            descriptions.add("");
        }
        levels = new ArrayList<>();
    }

    public Hierarchy(int languageNumber, int id, String hierarchyUniqueName, String type, int allowedDepth) {
        this(languageNumber);
        this.id = id;
        this.hierarchyUniqueName = hierarchyUniqueName;
        this.type = type;
        this.allowedDepth = allowedDepth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHierarchyUniqueName() {
        return hierarchyUniqueName;
    }

    public void setHierarchyUniqueName(String hierarchyUniqueName) {
        this.hierarchyUniqueName = hierarchyUniqueName;
    }

    public ArrayList<String> getCaptions() {
        return captions;
    }

    public void setCaptions(ArrayList<String> captions) {
        this.captions = captions;
    }

    public ArrayList<String> getToplevelStrings() {
        return toplevelStrings;
    }

    public void setToplevelStrings(ArrayList<String> toplevelStrings) {
        this.toplevelStrings = toplevelStrings;
    }

    public ArrayList<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(ArrayList<String> descriptions) {
        this.descriptions = descriptions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public void setLevels(ArrayList<Level> levels) {
        this.levels = levels;
    }

    public int getAllowedDepth() {
        return allowedDepth;
    }

    public void setAllowedDepth(int allowedDepth) {
        this.allowedDepth = allowedDepth;
    }

    public boolean isMember(String levelId) {
        boolean result = false;
        for (Level l : this.levels) {
            if (l.getIdColumnName().equals(levelId)) {
                result = true;
                break;
            }
        }
        return result;
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
    public String getHierarchyFilterString(String levelId) {
        String result = "";
        if (levelId == null || (!isMember(levelId))) {
            result += " AND " + this.levels.get(1).getIdColumnName() + " is null";
        } else {
            int hierarchySize = this.levels.size();
            for (int i = 0; i < hierarchySize; i++) {
                if (this.levels.get(i).getIdColumnName().equals(levelId)) {
                    //ha a gyereknek is van gyereke
                    if (i < (hierarchySize - 2)) {
                        result += " AND " + this.levels.get(i + 1).getIdColumnName() + " is not null AND "
                                + this.levels.get(i + 2).getIdColumnName() + " is null";
                        break;
                    } // ha a gyereknek már nincs gyereke
                    else if (i < (hierarchySize - 1)) {
                        result += " AND " + this.levels.get(i + 1).getIdColumnName() + " is not null";
                        break;
                    }
                }
            }

        }
        return result;
    }
    
   
    public void addLevel(Level entity) {
        int idx = getInsertIdxOfNewLevel(entity);
        if (idx == this.levels.size()) {
            this.levels.add(entity);
        } else {
            this.levels.add(idx, entity);
        }
    }

    /**
     * Ez visszaadja az új elem beszúrási indexét. Ezt arra használjuk, hogy a
     * hierarchiában a szintek rendezetten tárolódhassanak.
     *
     * @param entity a beszúrandó Level
     * @return az új elem beszúrásának indexe
     */
    private int getInsertIdxOfNewLevel(Level entity) {
        int result = -1;
        for (int i = 0; i < this.levels.size(); i++) {
            if (this.levels.get(i).getDepth() > entity.getDepth()) {
                result = i;
                break;
            }
        }
        if (result == -1) {
            result = this.levels.size();
        }
        return result;
    }

    public void addLanguage() {
        this.captions.add("");
        this.descriptions.add("");
        this.toplevelStrings.add("");
    }

    public void removeLanguage(int index) {
        this.captions.remove(index);
        this.descriptions.remove(index);
        this.toplevelStrings.remove(index);
    }

    public Hierarchy deepCopy() {
        Hierarchy result = new Hierarchy(captions.size(), id, hierarchyUniqueName, type, allowedDepth);
        result.setCaptions(new ArrayList<>(captions));
        result.setDescriptions(new ArrayList<>(descriptions));
        result.setToplevelStrings(new ArrayList<>(toplevelStrings));
        for (Level level : levels) {
            result.levels.add(level.deepCopy());
        }
        return result;
    }

}
