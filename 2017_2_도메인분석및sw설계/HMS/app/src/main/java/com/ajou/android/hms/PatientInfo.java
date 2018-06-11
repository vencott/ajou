package com.ajou.android.hms;

/**
 * Created by Kimjungmin on 2017. 12. 4..
 */

public class PatientInfo {
    private String name;
    private String gender;
    private int age;
    private Sickbed sickbed;
    private Chart chart;
    private VitalMachine vitalMachine;

    public PatientInfo() {
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public Sickbed getSickbed() {
        return sickbed;
    }

    public Chart getChart() {
        return chart;
    }

    public VitalMachine getVitalMachine() {
        return vitalMachine;
    }

    public void setSickbed(Sickbed sickbed) {
        this.sickbed = sickbed;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public void setVitalMachine(VitalMachine vitalMachine) {
        this.vitalMachine = vitalMachine;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public void measureVital(){
        this.vitalMachine.measure();
    }

    public boolean isNormal() {
        return this.vitalMachine.isNormal();
    }
}
