package com.ajou.android.assignment3_201321062;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private RotatingGearView mRotatingGearView;
    private Button mButton;
    private ToggleButton mToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRotatingGearView = (RotatingGearView) findViewById(R.id.RotatingGearView);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRotatingGearView.startAnimation();
            }
        });

        mToggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToggleButton.isChecked())
                    mRotatingGearView.setCCW(true);
                else
                    mRotatingGearView.setCCW(false);
            }
        });
    }
}