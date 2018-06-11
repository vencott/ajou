    package com.ajou.android.hms;

    import android.annotation.SuppressLint;
    import android.content.Intent;
    import android.graphics.Color;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;

    import java.util.ArrayList;
    import java.util.List;

    public class MainActivity extends AppCompatActivity {

        private Button receptionist_bt;
        private Button caregiver_bt;
        private Button medicalteam_bt;

        private ArrayList<Button> sickbeds;
        private Button sickbed_bt_0;
        private Button sickbed_bt_1;
        private Button sickbed_bt_2;
        private Button sickbed_bt_3;
        private Button sickbed_bt_4;
        private Button sickbed_bt_5;
        private Button sickbed_bt_6;
        private Button sickbed_bt_7;
        private Button sickbed_bt_8;
        private Button sickbed_bt_9;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            sickbeds = new ArrayList<>();

            initiateDatabase();

            receptionist_bt = findViewById(R.id.receptionist_bt);
            receptionist_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, HospitalizeActivity.class);
                    startActivity(intent);
                }
            });

            caregiver_bt = findViewById(R.id.caregiver_bt);
            caregiver_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, StateActivity.class);
                    intent.putExtra("mode", 1);
                    startActivity(intent);
                }
            });

            medicalteam_bt = findViewById(R.id.medicalteam_bt);
            medicalteam_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, StateActivity.class);
                    intent.putExtra("mode", 2);
                    startActivity(intent);
                }
            });

            sickbed_bt_0 = findViewById(R.id.sickbed_0);
            sickbeds.add(sickbed_bt_0);
            sickbed_bt_1 = findViewById(R.id.sickbed_1);
            sickbeds.add(sickbed_bt_1);
            sickbed_bt_2 = findViewById(R.id.sickbed_2);
            sickbeds.add(sickbed_bt_2);
            sickbed_bt_3 = findViewById(R.id.sickbed_3);
            sickbeds.add(sickbed_bt_3);
            sickbed_bt_4 = findViewById(R.id.sickbed_4);
            sickbeds.add(sickbed_bt_4);
            sickbed_bt_5 = findViewById(R.id.sickbed_5);
            sickbeds.add(sickbed_bt_5);
            sickbed_bt_6 = findViewById(R.id.sickbed_6);
            sickbeds.add(sickbed_bt_6);
            sickbed_bt_7 = findViewById(R.id.sickbed_7);
            sickbeds.add(sickbed_bt_7);
            sickbed_bt_8 = findViewById(R.id.sickbed_8);
            sickbeds.add(sickbed_bt_8);
            sickbed_bt_9 = findViewById(R.id.sickbed_9);
            sickbeds.add(sickbed_bt_9);
        }

        @Override
        protected void onResume() {
            super.onResume();
            update();
        }

        private void initiateDatabase() {
            for (int i = 0; i < 10; i++)
                Database.getInstance().getSickbedList().add(new Sickbed("Sickbed" + i));
        }

        private void update() {
            Database.getInstance().getSickbedList();
            for (int i = 0; i < Database.getInstance().getSickbedList().size(); i++) {
                if (Database.getInstance().getSickbedList().get(i).isAllocated() == true) {
                    sickbeds.get(i).setBackgroundColor(Color.rgb(125, 40, 40));
                    sickbeds.get(i).setText(Database.getInstance().getSickbedList().get(i).getPatientID());
                }
            }
        }
    }