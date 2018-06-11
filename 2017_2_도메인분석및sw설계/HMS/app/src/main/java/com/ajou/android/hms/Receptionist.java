package com.ajou.android.hms;

import java.util.List;

/**
 * Created by Kimjungmin on 2017. 12. 4..
 */

public class Receptionist {
    private String ID;
    private Patient newPatient;

    public Receptionist(String ID) {
        this.ID = ID;
    }

    public void register(String ID) {
        newPatient = new Patient(ID);
        newPatient.setPatientInfo(new PatientInfo());
    }

    public List<Sickbed> getSickbedList() {
        return SickbedController.getInstance().getSickbedList();
    }

    public int allocateSickbedtoPatient() {
        if(newPatient != null)
            return SickbedController.getInstance().allocateSickbedtoPatient(newPatient);
        else
            return -1;
    }

    public void allocateCharttoPatient(String memo) {
        PatientController.getInstance().allocateCharttoPatient(newPatient, memo);
    }

    public void hospitalize(String name, String gender, int age) {
        PatientController.getInstance().hospitalize(newPatient, name, gender, age);
    }

    public String getID() {
        return ID;
    }
}
