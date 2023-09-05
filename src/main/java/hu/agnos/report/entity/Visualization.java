/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.agnos.report.entity;
//OK

/**
 *
 * @author parisek
 */
public class Visualization {

    private String initString;
    private int order;

    public Visualization() {
    }

    public Visualization(String initString) {
        this.initString = initString;
    }

    public Visualization(String initString, int order) {
        this.initString = initString;
        this.order = order;
    }

    public String getInitString() {
        return initString;
    }

    public void setInitString(String initString) {
        this.initString = initString;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Visualization deepCopy() {
        Visualization result = new Visualization(initString, order);
        return result;
    }

   @Override
    public String toString() {
        return initString;
    }
}
