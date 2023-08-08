/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.report.entity;

import java.util.ArrayList;

/**
 *
 * @author parisek
 */
public class Report {
    private String name;
    private String cubeName;
    private String updatedBy;
    private String databaseType;
    private String roleToAccess;

    private ArrayList<String> captions;
    private ArrayList<String> descriptions;
    private ArrayList<String> helps;
    private ArrayList<String> datasources;
    private ArrayList<String> languages;

    private ArrayList<Indicator> indicators;
    private ArrayList<Hierarchy> hierarchies;
    private ArrayList<Visualization> visualizations;

    private AdditionalCalculation additionalCalculation;

    public Report() {
        this.roleToAccess = "";
        this.captions = new ArrayList<>();
        this.descriptions = new ArrayList<>();
        this.helps = new ArrayList<>();
        this.datasources = new ArrayList<>();
        this.languages = new ArrayList<>();
        this.indicators = new ArrayList<>();
        this.hierarchies = new ArrayList<>();
        this.visualizations = new ArrayList<>();
        this.additionalCalculation = null;
        this.updatedBy = "SYSTEM_USER";
    }

    public Report(String name) {
        this();
        this.name = name;
    }

    public Report(String name, String updated, String databaseType) {
        this(name);
        this.updatedBy = updated;
        this.databaseType = databaseType;
    }

    public Report(String name, String cubeName, String updated, String databaseType, String roleToAccess) {
        this(name, updated, databaseType);
        this.cubeName = cubeName;
        this.roleToAccess = roleToAccess;
    }

    public ArrayList<String> getDatasource() {
        return datasources;
    }

    public void setDatasources(ArrayList<String> datasources) {
        this.datasources = datasources;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCubeName() {
        return cubeName;
    }

    public void setCubeName(String cubeName) {
        this.cubeName = cubeName;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getRoleToAccess() {
        return roleToAccess;
    }

    public void setRoleToAccess(String roleToAccess) {
        this.roleToAccess = roleToAccess;
    }

    public ArrayList<String> getCaptions() {
        return captions;
    }

    public void setCaptions(ArrayList<String> captions) {
        this.captions = captions;
    }

    public ArrayList<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(ArrayList<String> descriptions) {
        this.descriptions = descriptions;
    }

    public ArrayList<String> getHelps() {
        return helps;
    }

    public void setHelps(ArrayList<String> helps) {
        this.helps = new ArrayList<>(helps);
        for (String h : helps) {
            h = h.replaceAll("<!\\[CDATA\\[", "").replaceAll("\\]\\]>", "");
        }
    }

    public ArrayList<String> getEscapedHelps() {
        ArrayList<String> result = new ArrayList<>();
        for (String h : helps) {
            result.add("<![CDATA[" + h.replaceAll("<!\\[CDATA\\[", "").replaceAll("\\]\\]>", "") + "]]>");
        }
        return result;
    }

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public int getLanguageIdx(String language) {
        int result = -1;
        for (int i = 0; i < languages.size(); i++) {
            if (languages.get(i).equals(language)) {
                result = i;
                break;
            }
        }
        return result;
    }

    public int getLanguageCount() {
        return this.languages.size();
    }

    public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }

    public ArrayList<Indicator> getIndicators() {
        return indicators;
    }

    public void addLanguage(String lang) {
        this.captions.add("");
        this.descriptions.add("");
        this.helps.add("");
        this.datasources.add("");
        this.languages.add(lang);
        for (Indicator indicator : indicators) {
            indicator.addLanguage();
        }
        for (Hierarchy hierarchy : hierarchies) {
            hierarchy.addLanguage();
        }
    }

    public void removeLanguage(int index) {
        this.captions.remove(index);
        this.descriptions.remove(index);
        this.helps.remove(index);
        this.datasources.remove(index);
        this.languages.remove(index);
        for (Indicator indicator : indicators) {
            indicator.removeLanguage(index);
        }
        for (Hierarchy hierarchy : hierarchies) {
            hierarchy.removeLanguage(index);
        }
    }

    public void setIndicators(ArrayList<Indicator> indicators) {
        this.indicators = indicators;
    }

    public ArrayList<Hierarchy> getHierarchies() {
        return hierarchies;
    }

    public void setHierarchies(ArrayList<Hierarchy> hierarchies) {
        this.hierarchies = hierarchies;
    }

    public ArrayList<Visualization> getVisualizations() {
        return visualizations;
    }

    public int getHierarchyIdxByUniqueName(String uniqueName) {
        for (int i = 0; i < this.hierarchies.size(); i++) {
            if (this.hierarchies.get(i).getHierarchyUniqueName().toUpperCase().equals(uniqueName.toUpperCase())) {
                return i;
            }
        }
        return -1;
    }

    public void setVisualizations(ArrayList<Visualization> visualizations) {
        this.visualizations = visualizations;
        for (int i = 0; i < visualizations.size(); i++) {
            Visualization v = this.visualizations.get(i);
            if (v.getOrder() < 0) {
                v.setOrder(i + 1);
            }
        }
    }

    public void addVisualization(Visualization entity) {
        int idx = getInsertIdxOfNewVisualization(entity);
        if (idx == this.visualizations.size()) {
            this.visualizations.add(entity);
        } else {
            this.visualizations.add(idx, entity);
        }
    }

    /**
     * Ez visszaadja az új elem beszúrási indexét. Ezt arra használjuk, hogy a
     * hierarchiában a szintek rendezetten tárolódhassanak.
     *
     * @param entity a beszúrandó Level
     * @return az új elem beszúrásának indexe
     */
    private int getInsertIdxOfNewVisualization(Visualization entity) {
        int result = -1;
        for (int i = 0; i < this.visualizations.size(); i++) {
            if (this.visualizations.get(i).getOrder() > entity.getOrder()) {
                result = i;
                break;
            }
        }
        if (result == -1) {
            result = this.visualizations.size();
        }
        return result;
    }

    public void addIndicator(Indicator entity) {
        int idx = getInsertIdxOfNewIndicator(entity);
        if (idx == this.indicators.size()) {
            this.indicators.add(entity);
        } else {
            this.indicators.add(idx, entity);
        }
    }

    /**
     * Ez visszaadja az új elem beszúrási indexét. Ezt arra használjuk, hogy a
     * hierarchiában a szintek rendezetten tárolódhassanak.
     *
     * @param entity a beszúrandó Level
     * @return az új elem beszúrásának indexe
     */
    private int getInsertIdxOfNewIndicator(Indicator entity) {
        int result = -1;
        for (int i = 0; i < this.indicators.size(); i++) {
            if (this.indicators.get(i).getId() > entity.getId()) {
                result = i;
                break;
            }
        }
        if (result == -1) {
            result = this.indicators.size();
        }
        return result;
    }

    public void addHierarchy(Hierarchy entity) {
        int idx = getInsertIdxOfNewHierarchy(entity);
        if (idx == this.hierarchies.size()) {
            this.hierarchies.add(entity);
        } else {
            this.hierarchies.add(idx, entity);
        }
    }

    public AdditionalCalculation getAdditionalCalculation() {
        return additionalCalculation;
    }

    public void setAdditionalCalculation(AdditionalCalculation additionalCalculation) {
        this.additionalCalculation = additionalCalculation;
    }

    public boolean isAdditionalCalculation() {
        return this.additionalCalculation != null;
    }

    /**
     * Ez visszaadja az új elem beszúrási indexét. Ezt arra használjuk, hogy a
     * hierarchiában a szintek rendezetten tárolódhassanak.
     *
     * @param entity a beszúrandó Level
     * @return az új elem beszúrásának indexe
     */

    private int getInsertIdxOfNewHierarchy(Hierarchy entity) {
        int result = -1;
        for (int i = 0; i < this.hierarchies.size(); i++) {
            if (this.hierarchies.get(i).getId() > entity.getId()) {
                result = i;
                break;
            }
        }
        if (result == -1) {
            result = this.hierarchies.size();
        }
        return result;
    }

    public Report deepCopy() {
        Report result = new Report(name, updatedBy, databaseType);
        result.setCubeName(cubeName);
        result.setRoleToAccess(roleToAccess);
        result.setCaptions(new ArrayList<>(captions));
        result.setDescriptions(new ArrayList<>(descriptions));
        result.setHelps(new ArrayList<>(helps));
        result.setLanguages(new ArrayList<>(languages));
        result.setDatasources(new ArrayList<>(datasources));
        for (Indicator indicator : indicators) {
            result.indicators.add(indicator.deepCopy());
        }
        for (Hierarchy hierarchy : hierarchies) {
            result.hierarchies.add(hierarchy.deepCopy());
        }
        for (Visualization visualization : visualizations) {
            result.visualizations.add(visualization.deepCopy());
        }

        result.additionalCalculation = additionalCalculation;

        return result;
    }

    
    @Override
    public String toString() {
        return "Report{" + "name=" + name + ", cubeName=" + cubeName + ", updatedBy=" + updatedBy + ", databaseType=" + databaseType + ", roleToAccess=" + roleToAccess + ", captions=" + captions + ", descriptions=" + descriptions + ", helps=" + helps + ", datasources=" + datasources + ", languages=" + languages + ", indicators=" + indicators + ", hierarchies=" + hierarchies + ", visualizations=" + visualizations + ", additionalCalculation=" + additionalCalculation + '}';
    }

}
