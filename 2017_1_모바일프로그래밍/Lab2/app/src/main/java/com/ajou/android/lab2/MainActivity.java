package com.ajou.android.lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ShipItem shipItem;
    private EditText weightET;
    private TextView baseCostTV;
    private TextView addedCostTV;
    private TextView totalCostTV;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shipItem = new ShipItem();

        weightET = (EditText) findViewById(R.id.TextEditor);
        baseCostTV = (TextView) findViewById(R.id.textBaseCost);
        addedCostTV = (TextView) findViewById(R.id.textAddedCost);
        totalCostTV= (TextView) findViewById(R.id.textTotalCost);
        weightET.addTextChangedListener(weightTextWatcher);
    }

    private void displayShipping() {

        String baseCost = String.format("%.2f", shipItem.getBaseCost());
        String addedCost = String.format("%.2f", shipItem.getAddedCost());
        String totalCost = String.format("%.2f", shipItem.getTotalCost());

        baseCostTV.setText("$" + baseCost);
        addedCostTV.setText("$" + addedCost);
        totalCostTV.setText("$" + totalCost);
    }
}