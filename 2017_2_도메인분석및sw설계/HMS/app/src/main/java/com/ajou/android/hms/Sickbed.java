package com.ajou.android.hms;

/**
 * Created by Kimjungmin on 2017. 12. 4..
 */

public class Sickbed {
    private String ID;
    private String patientID;
    private boolean allocated;

    public Sickbed(String ID) {
        this.ID = ID;
        this.allocated = false;
    }

    public String getID() {
        return ID;
    }

    public String getPatientID() {
        return patientID;
    }

    public boolean isAllocated() {
        return allocated;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }
}
