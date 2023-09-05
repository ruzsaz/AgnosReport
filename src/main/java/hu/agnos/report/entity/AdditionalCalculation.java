/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.agnos.report.entity;

/**
 *
 * @author parisek
 */
public class AdditionalCalculation {
    private String function;
    private String args;

    public AdditionalCalculation(String function, String args) {
        this.function = function;
        this.args = args;
    }

    public String getFunction() {
        return function;
    }

    public String getArgs() {
        return args;
    }
    
}
