package com.consilium.excel.models.caseproctimeavg;

public class CaseProcTimeAvgSubDataObject {
    /*
     *
     * */
    protected String unitCode = "";

    /*
     *
     * */
    protected String unitName = "";

    /*
     *
     * */
    protected int counter = 0;

    /*
     *
     * */
    protected int score = 0;

    /*
     *
     * */
    protected String avgTime = "";

    protected int counterCase = 0;
    protected String avgTimeCase = "";

    protected int scoreCase = 0;

    /*
     *
     * */
    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    /*
     *
     * */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    /*
     *
     * */
    public void setCounter(int count) {
        this.counter = count;
    }

    /*
     *
     * */
    public void setAvgTime(String avgTime) {
        this.avgTime = avgTime;
    }

    /*
     *
     * */
    public void setScore(int score) {
        this.score = score;
    }

    /*
     *
     * */
    public String getUnitCode() {
        return this.unitCode;
    }

    /*
     *
     * */
    public String getUnitName() {
        return this.unitName;
    }

    /*
     *
     * */
    public int getCounter() {
        return this.counter;
    }

    /*
     *
     * */
    public String getAvgTime() {
        return this.avgTime;
    }

    /*
     *
     * */
    public int getScore() {
        return this.score;
    }

    public int getCounterCase() {
        return counterCase;
    }

    public void setCounterCase(int counterCase) {
        this.counterCase = counterCase;
    }

    public String getAvgTimeCase() {
        return avgTimeCase;
    }

    public void setAvgTimeCase(String avgTimeCase) {
        this.avgTimeCase = avgTimeCase;
    }


    public int getScoreCase() {
        return scoreCase;
    }

    public void setScoreCase(int scoreCase) {
        this.scoreCase = scoreCase;
    }
}
