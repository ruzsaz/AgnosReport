/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.agnos.report;

import hu.agnos.report.repository.ReportRepository;

/**
 *
 * @author parisek
 */
public class AgnosReport {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ReportRepository rr = new ReportRepository();
        rr.findAll();
    }
}
