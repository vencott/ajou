package com.ajou.android.hms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HospitalizeActivity extends AppCompatActivity {

    private Receptionist receptionist;

    private TextView receptionist_tv;
    private EditText ID_et;
    private Button ID_bt;
    private EditText name_et;
    private EditText gender_et;
    private EditText age_et;
    private TextView sickbed_tv;
    private Button sickbed_bt;
    private Button hospitalize_bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitalize);

        receptionist = new Receptionist("Receptionist_1");

        receptionist_tv = findViewById(R.id.receptionist_tv);
        receptionist_tv.setText("Receptionist ID : " + receptionist.getID());

        ID_et = findViewById(R.id.ID_et);

        ID_bt = findViewById(R.id.ID_bt);
        ID_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receptionist.register(ID_et.getText().toString());
                Toast.makeText(getApplicationContext(), "checking done!", Toast.LENGTH_SHORT).show();
                ID_bt.setVisibility(View.INVISIBLE);
            }
        });

        name_et = findViewById(R.id.name_et);
        gender_et = findViewById(R.id.gender_et);
        age_et = findViewById(R.id.age_et);

        sickbed_tv = findViewById(R.id.sickbed_tv);

        sickbed_bt = findViewById(R.id.sickbed_bt);
        sickbed_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SickbedController.getInstance().getSickbedList() != null) {
                    sickbed_tv.setText("Allocated sickbed ID : " + receptionist.allocateSickbedtoPatient());
                    sickbed_bt.setVisibility(View.INVISIBLE);
                }
            }
        });

        hospitalize_bt = findViewById(R.id.hospitalize_bt);
        hospitalize_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receptionist.allocateCharttoPatient(initiateChart());

                String name = name_et.getText().toString();
                String gender = gender_et.getText().toString();
                int age = Integer.parseInt(age_et.getText().toString());
                receptionist.hospitalize(name, gender, age);
                Toast.makeText(getApplicationContext(), name_et.getText().toString() + "님 입원수속 완료"
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private String initiateChart() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(date);
        return time + " : 입원수속 완료\n";
    }
}
