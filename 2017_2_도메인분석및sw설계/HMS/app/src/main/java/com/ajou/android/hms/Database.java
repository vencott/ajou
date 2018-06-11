package com.ajou.android.hms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kimjungmin on 2017. 12. 4..
 */

public class Database {
    private static Database database;
    private List<Sickbed> sickbedList;
    private List<Patient> patientList;

    private Database() {
        sickbedList = new ArrayList<>();
        patientList = new ArrayList<>();
    }

    public static Database getInstance() {
        if(database == null)
            database = new Database();

        return database;
    }

    public List<Sickbed> getSickbedList() {
        return sickbedList;
    }

    public List<Patient> getPatientList() {
        return patientList;
    }

    public void setSickbedList(List<Sickbed> sickbedList) {
        this.sickbedList = sickbedList;
    }

    public void setPatientList(List<Patient> patientList) {
        this.patientList = patientList;
    }
}
