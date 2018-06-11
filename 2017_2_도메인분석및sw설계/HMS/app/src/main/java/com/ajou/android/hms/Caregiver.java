package com.ajou.android.hms;

/**
 * Created by Kimjungmin on 2017. 12. 4..
 */

public class Caregiver extends User{
    private String relatedPatientID;

    public Caregiver(String ID) {
        super(ID);
    }

    public void setRelatedPatientID(String relatedPatientID) {
        this.relatedPatientID = relatedPatientID;
    }

    public String getRelatedPatientID() {
        return relatedPatientID;
    }
}
