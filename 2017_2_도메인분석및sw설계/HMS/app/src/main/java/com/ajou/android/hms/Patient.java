package com.ajou.android.hms;

/**
 * Created by Kimjungmin on 2017. 12. 4..
 */

public class Patient {
    private String ID;
    private PatientInfo patientInfo;

    public Patient(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public PatientInfo getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfo patientInfo) {
        this.patientInfo = patientInfo;
    }
}
