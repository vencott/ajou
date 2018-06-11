package com.ajou.android.lab5;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final int PINK = Color.rgb(255, 286, 210);
    public static final int BLACK = Color.BLACK;
    public static final int GREEN = Color.GREEN;

    private CanvasView customCanvas;
    private Button mClearButton;
    private Button mPinkButton;
    private Button mBlackButton;
    private Button mGreenButton;
    private Button mUndoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);

        mUndoButton = (Button) findViewById(R.id.undoButton);
        mUndoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCanvas.undo();
            }
        });

        mClearButton = (Button) findViewById(R.id.clearButton);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCanvas(v);
            }
        });

        mPinkButton = (Button) findViewById(R.id.pinkButton);
        mPinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCanvas.setPenColor(PINK);
            }
        });

        mBlackButton = (Button) findViewById(R.id.blackButton);
        mBlackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCanvas.setPenColor(BLACK);
            }
        });

        mGreenButton = (Button) findViewById(R.id.greenButton);
        mGreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCanvas.setPenColor(GREEN);
            }
        });

    }

    public void clearCanvas(View v) {
        customCanvas.clearCanvas();
    }
}
