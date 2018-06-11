package com.ajou.android.hms;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StateActivity extends AppCompatActivity {
    private EditText search_et;
    private Button search_bt;
    private TextView ID_tv;
    private TextView name_tv;
    private TextView gender_tv;
    private TextView age_tv;
    private TextView sickbed_tv;
    private TextView temparature_tv;
    private TextView pulse_tv;
    private TextView respiration_tv;
    private TextView blood_tv;
    private TextView normal_tv;
    private EditText chart_et;
    private Button chart_bt;

    private User user;
    private int mode;
    private boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        isActive = false;

        ID_tv = findViewById(R.id.state_ID_tv);
        name_tv = findViewById(R.id.state_name_tv);
        gender_tv = findViewById(R.id.state_gender_tv);
        age_tv = findViewById(R.id.state_age_tv);
        sickbed_tv = findViewById(R.id.state_sickbed_tv);
        temparature_tv = findViewById(R.id.state_temparature_tv);
        pulse_tv = findViewById(R.id.state_pulse_tv);
        respiration_tv = findViewById(R.id.state_respiration_tv);
        blood_tv = findViewById(R.id.state_blood_tv);
        normal_tv = findViewById(R.id.state_normal_tv);
        chart_et = findViewById(R.id.state_chart_et);
        chart_bt = findViewById(R.id.state_chart_bt);

        Intent intent = getIntent();
        mode = intent.getExtras().getInt("mode");

        if (mode == 1) {
            user = new Caregiver("caregiver_1");
            ((Caregiver) user).setRelatedPatientID("relatedexample");
            chart_et.setVisibility(View.INVISIBLE);
            chart_bt.setVisibility(View.INVISIBLE);
        } else {
            user = new MedicalTeam("medicalteam_1");
        }

        search_et = findViewById(R.id.state_search_et);

        search_bt = findViewById(R.id.state_search_bt);
        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = search_et.getText().toString();

                if ((PatientController.getInstance().requestPatientInfo(ID)) == null) {
                    ID_tv.setText("There is No patient : " + ID);
                    return;
                }

                if (user instanceof Caregiver && !(((Caregiver) user).getRelatedPatientID().equals(ID))) {
                    ID_tv.setText("Access denied");
                    return;
                }

                isActive = true;
                update(ID);
            }
        });

        chart_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = search_et.getText().toString();
                Chart chart = PatientController.getInstance().getPatientbyID(ID).getPatientInfo()
                        .getChart();
                chart.changeChart(chart_et.getText().toString());
                chart_et.setText(chart.getChart());
            }
        });
    }

    public void update(String ID) {
        Patient patient = PatientController.getInstance().getPatientbyID(ID);
        PatientInfo patientInfo = PatientController.getInstance().requestPatientInfo(ID);

        ID_tv.setText("ID : " + patient.getID());
        name_tv.setText("name : " + patientInfo.getName());
        gender_tv.setText("gender : " + patientInfo.getGender());
        age_tv.setText("age : " + patientInfo.getAge());
        sickbed_tv.setText("sickbed : " + patientInfo.getSickbed().getID());
        chart_et.setText(patientInfo.getChart().getChart());
        activateVitalMachine(patientInfo);
    }

    public void activateVitalMachine(final PatientInfo patientInfo) {
        new Thread() {
            @Override
            public void run() {
                while (isActive) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                patientInfo.measureVital();

                                temparature_tv.setText("temparature : " + patientInfo.getVitalMachine().getTemparature());
                                pulse_tv.setText("pulse : " + patientInfo.getVitalMachine().getPulse());
                                respiration_tv.setText("respiration : " + patientInfo.getVitalMachine().getRespiration());
                                blood_tv.setText("blood : " + patientInfo.getVitalMachine().getBlood_pressure());
                                if (patientInfo.getVitalMachine().isNormal()) {
                                    normal_tv.setText("NORMAL");
                                    normal_tv.setBackgroundColor(Color.rgb(40, 125, 40));
                                } else {
                                    normal_tv.setText("ABNORMAL");
                                    normal_tv.setBackgroundColor(Color.rgb(125, 40, 40));
                                }
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
