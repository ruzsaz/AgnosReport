/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.report.entity;

import hu.mi.agnos.report.util.XmlEscaper;
import java.util.ArrayList;

/**
 *
 * @author parisek
 */
public class Indicator {

    private int id;
    private ArrayList<String> captions;
    private ArrayList<String> descriptions;
    private Measure denominator;
    private Measure value;
    private Double multiplier;

    public Indicator(int languageNumber) {
        this.captions = new ArrayList<>();
        this.descriptions = new ArrayList<>();
        for (int i = 0; i < languageNumber; i++) {
            captions.add("");
            descriptions.add("");
        }
    }

    public Indicator(int languageNumber, Double multiplier) {
        this(languageNumber);
        this.multiplier = multiplier;
    }

    public Indicator(int languageNumber, int id, Measure value, Measure denominator, Double multiplier) {
        this(languageNumber, multiplier);
        this.id = id;
        this.denominator = denominator;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Measure getDenominator() {
        return denominator;
    }

    public void setDenominator(Measure denominator) {
        this.denominator = denominator;
    }

    public Measure getValue() {
        return value;
    }

    public void setValue(Measure value) {
        this.value = value;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public void addLanguage() {
        this.captions.add("");
        this.descriptions.add("");
        this.value.addLanguage();
        this.denominator.addLanguage();
    }

    public void removeLanguage(int index) {
        this.captions.remove(index);
        this.descriptions.remove(index);
        this.value.removeLanguage(index);
        this.denominator.removeLanguage(index);
    }

    public Indicator deepCopy() {
        Indicator result = new Indicator(captions.size(), id, value.deepCopy(), denominator.deepCopy(), multiplier);
        result.setCaptions(new ArrayList<>(captions));
        result.setDescriptions(new ArrayList<>(descriptions));
        return result;
    }

}
