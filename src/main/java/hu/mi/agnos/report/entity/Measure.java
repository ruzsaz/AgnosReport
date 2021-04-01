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
public class Measure {

    private String measureUniqueName;
    private ArrayList<String> units;
    private ArrayList<String> unitPlurals;
    private boolean hidden;
    private int sign;

    public Measure(int languageNumber) {
        this.units = new ArrayList<>();
        this.unitPlurals = new ArrayList<>();
        for (int i = 0; i < languageNumber; i++) {
            units.add("");
            unitPlurals.add("");
        }
    }

    public Measure(int languageNumber, String measureUniqueName) {
        this(languageNumber);
        this.measureUniqueName = measureUniqueName;
    }

    public Measure(int languageNumber, String measureUniqueName, int sign, boolean hide) {
        this(languageNumber, measureUniqueName);
        this.sign = sign;
        this.hidden = hide;
    }

    public String getMeasureUniqueName() {
        return measureUniqueName;
    }

    public void setMeasureUniqueName(String measureUniqueName) {
        this.measureUniqueName = measureUniqueName;
    }

    public ArrayList<String> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<String> units) {
        this.units = units;
    }

    public ArrayList<String> getUnitPlurals() {
        return unitPlurals;
    }

    public void setUnitPlurals(ArrayList<String> unitPlurals) {
        this.unitPlurals = unitPlurals;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public void addLanguage() {
        this.units.add("");
        this.unitPlurals.add("");
    }

    public void removeLanguage(int index) {
        this.units.remove(index);
        this.unitPlurals.remove(index);
    }

    public Measure deepCopy() {
        Measure result = new Measure(units.size(), measureUniqueName, sign, hidden);
        result.setUnits(new ArrayList<>(units));
        result.setUnitPlurals(new ArrayList<>(unitPlurals));
        return result;
    }
}
