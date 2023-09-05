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
public class Level {

    private int depth;
    private String idColumnName;
    private String codeColumnName;
    private String nameColumnName;

    public Level() {
    }

    public Level(int depth, String levelId, String levelKnownId, String levelName) {
        this.depth = depth;
        this.idColumnName = levelId;
        this.codeColumnName = levelKnownId;
        this.nameColumnName = levelName;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    public void setIdColumnName(String idColumnName) {
        this.idColumnName = idColumnName;
    }

    public String getCodeColumnName() {
        return codeColumnName;
    }

    public void setCodeColumnName(String codeColumnName) {
        this.codeColumnName = codeColumnName;
    }

    public String getNameColumnName() {
        return nameColumnName;
    }

    public void setNameColumnName(String nameColumnName) {
        this.nameColumnName = nameColumnName;
    }
        
    public Level deepCopy() {
        Level result = new Level(depth, idColumnName, codeColumnName, nameColumnName);
        return result;
    }

}
