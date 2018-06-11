package com.ajou.android.assignment_1_201321062;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final double OUNCES_PER_BOOK = 14;
    public static final double OUNCES_PER_PENCIL = 1.5;
    public static final double OUNCES_PER_ERASER = 3;

    private ShipItem shipItem;
    private TextView baseCostTV;
    private TextView addedCostTV;
    private TextView totalCostTV;

    private EditText weightET;

    private ImageButton bookPlusBt;
    private ImageButton bookMinusBt;
    private ImageButton pencilPlusBt;
    private ImageButton pencilMinusBt;
    private ImageButton eraserPlusBt;
    private ImageButton eraserMinusBt;

    private TextView bookNumTV;
    private TextView pencilNumTV;
    private TextView eraserNumTV;

    private int books   = 0;
    private int pencils = 0;
    private int erasers = 0;

    private double weight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shipItem = new ShipItem();

        baseCostTV = (TextView) findViewById(R.id.textBaseCost);
        addedCostTV = (TextView) findViewById(R.id.textAddedCost);
        totalCostTV= (TextView) findViewById(R.id.textTotalCost);

        weightET = (EditText) findViewById(R.id.TextEditor);
        weightET.addTextChangedListener(weightTextWatcher);

        bookPlusBt = (ImageButton) findViewById(R.id.bookPlus);
        bookPlusBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                books++;
                bookNumTV.setText(Integer.toString(books));
                weightCalculate();
            }
        });
        bookPlusBt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                books+=10;
                bookNumTV.setText(Integer.toString(books));
                weightCalculate();
                return true;
            }
        });

        bookMinusBt = (ImageButton) findViewById(R.id.bookMinus);
        bookMinusBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(books > 0)
                books--;

                bookNumTV.setText(Integer.toString(books));
                weightCalculate();
            }
        });
        bookMinusBt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(books > 10)
                    books-=10;
                else books = 0;
                bookNumTV.setText(Integer.toString(books));
                weightCalculate();
                return true;
            }
        });

        pencilPlusBt = (ImageButton) findViewById(R.id.pencilPlus);
        pencilPlusBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pencils++;
                pencilNumTV.setText(Integer.toString(pencils));
                weightCalculate();
            }
        });
        pencilPlusBt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                pencils+=10;
                pencilNumTV.setText(Integer.toString(pencils));
                weightCalculate();
                return true;
            }
        });

        pencilMinusBt = (ImageButton) findViewById(R.id.pencilMinus);
        pencilMinusBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pencils > 0)
                    pencils--;

                pencilNumTV.setText(Integer.toString(pencils));
                weightCalculate();
            }
        });
        pencilMinusBt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(pencils > 10)
                    pencils-=10;
                else pencils = 0;
                pencilNumTV.setText(Integer.toString(pencils));
                weightCalculate();
                return true;
            }
        });

        eraserPlusBt = (ImageButton) findViewById(R.id.eraserPlus);
        eraserPlusBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                erasers++;
                eraserNumTV.setText(Integer.toString(erasers));
                weightCalculate();
            }
        });
        eraserPlusBt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                erasers+=10;
                eraserNumTV.setText(Integer.toString(erasers));
                weightCalculate();
                return true;
            }
        });

        eraserMinusBt = (ImageButton) findViewById(R.id.eraserMinus);
        eraserMinusBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(erasers > 0)
                    erasers--;

                eraserNumTV.setText(Integer.toString(erasers));
                weightCalculate();
            }
        });
        eraserMinusBt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(erasers > 10)
                    erasers-=10;
                else erasers = 0;
                eraserNumTV.setText(Integer.toString(erasers));
                weightCalculate();
                return true;
            }
        });

        bookNumTV = (TextView) findViewById(R.id.bookNum);
        pencilNumTV = (TextView) findViewById(R.id.pencilNum);
        eraserNumTV = (TextView) findViewById(R.id.eraserNum);
    }

    private TextWatcher weightTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,int start, int before, int count){
            try {
                shipItem.setWeight((int) Double.parseDouble(s.toString()));
            }catch (NumberFormatException e){
                shipItem.setWeight(0);
            }
            displayShipping();
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,int start, int count, int after){}
    };

    private void displayShipping() {
        String baseCost = String.format("%.2f", shipItem.getBaseCost());
        String addedCost = String.format("%.2f", shipItem.getAddedCost());
        String totalCost = String.format("%.2f", shipItem.getTotalCost());

        baseCostTV.setText("$" + baseCost);
        addedCostTV.setText("$" + addedCost);
        totalCostTV.setText("$" + totalCost);
    }

    private void weightCalculate() {
        this.weight = (books * OUNCES_PER_BOOK)
                    + (pencils * OUNCES_PER_PENCIL)
                    + (erasers * OUNCES_PER_ERASER);

        weightET.setText(Double.toString(weight));
        shipItem.setWeight((int)weight);
    }
}