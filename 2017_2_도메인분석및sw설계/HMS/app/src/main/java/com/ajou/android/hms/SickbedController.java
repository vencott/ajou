package com.ajou.android.hms;

import java.util.List;

/**
 * Created by Kimjungmin on 2017. 12. 4..
 */

public class SickbedController {
    private static SickbedController sickbedController;

    private SickbedController() {}

    public static SickbedController getInstance() {
        if(sickbedController == null)
            sickbedController = new SickbedController();

        return sickbedController;
    }

    public List<Sickbed> getSickbedList() {
        return Database.getInstance().getSickbedList();
    }

    public int allocateSickbedtoPatient(Patient patient) {
        for (int i = 0; i < getSickbedList().size(); i++) {
            if (getSickbedList().get(i).isAllocated() == false) {
                getSickbedList().get(i).setAllocated(true);
                getSickbedList().get(i).setPatientID(patient.getID());
                patient.getPatientInfo().setSickbed(getSickbedList().get(i));
                return i;
            }
        }
        return -1;
    }
}
