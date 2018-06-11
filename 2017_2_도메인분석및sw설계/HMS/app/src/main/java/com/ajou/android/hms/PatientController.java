package com.ajou.android.hms;

import java.util.List;

/**
 * Created by Kimjungmin on 2017. 12. 5..
 */

public class PatientController {
    private static PatientController patientController;

    private PatientController() {}

    public static PatientController getInstance() {
        if(patientController == null)
            patientController = new PatientController();

        return patientController;
    }

    public List<Patient> getPatientList() {
        return Database.getInstance().getPatientList();
    }

    public void allocateCharttoPatient(Patient patient, String memo) {
        Chart chart = new Chart(memo);
        patient.getPatientInfo().setChart(chart);
    }

    public void hospitalize(Patient patient, String name, String gender, int age) {
        patient.getPatientInfo().setName(name);
        patient.getPatientInfo().setGender(gender);
        patient.getPatientInfo().setAge(age);
        patient.getPatientInfo().setVitalMachine(new VitalMachine());
        getPatientList().add(patient);
    }

    public PatientInfo requestPatientInfo(String patientID) {
        if (getPatientbyID(patientID) != null)
            return getPatientbyID(patientID).getPatientInfo();
        return null;
    }

    public Patient getPatientbyID(String patientId) {
        for(int i = 0; i < getPatientList().size(); i++)
            if (getPatientList().get(i).getID().equals(patientId))
                return getPatientList().get(i);
        return null;
    }


}
