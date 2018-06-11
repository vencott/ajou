package com.ajou.android.Assignment_2_201321062;

import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int white = Color.rgb(255, 255, 255);
    public static final int red = Color.rgb(255, 0, 0);
    public static final int green = Color.rgb(0, 255, 0);
    public static final int blue = Color.rgb(0, 0, 255);

    private Toolbar mToolbar;
    private EditText mDateEditText;
    private EditText mTimeEditText;
    private EditText mPeopleNumEditText;
    private CalendarView mCalendarView;
    private TimePicker mTimePicker;
    private ImageButton mPlusButton;
    private ImageButton mMinusButton;
    private Button mReservationButton;

    private int peopleNum   =   0;
    private int year        =   0;
    private int month       =   0;
    private int day         =   0;
    private int hourOfDay   =   0;
    private int minute      =   0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);

        mDateEditText = (EditText) findViewById(R.id.editText1);
        mTimeEditText = (EditText) findViewById(R.id.editText2);
        mPeopleNumEditText = (EditText) findViewById(R.id.editText3);

        mCalendarView = (CalendarView) findViewById(R.id.calenderView1);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dateUpdate(year, month + 1, dayOfMonth);
            }
        });

        mTimePicker = (TimePicker) findViewById(R.id.timePicker1);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timeUpdate(hourOfDay, minute);
            }
        });

        mPlusButton = (ImageButton) findViewById(R.id.plusButton);
        mPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peopleNum++;
                peopleNumUpdate();
            }
        });

        mMinusButton = (ImageButton) findViewById(R.id.minusButton);
        mMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (peopleNum > 0)
                    peopleNum--;
                else
                    peopleNum = 0;

                peopleNumUpdate();
            }
        });

        mReservationButton = (Button) findViewById(R.id.reservationButton);
        mReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationAlertDialogShow();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("clear");
        menu.add("background");
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().equals("clear"))
            clear();
        else if(item.getTitle().equals("background"))
            backgroundAlertDialogShow();

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void clear() {
        Calendar calendar = Calendar.getInstance();

        mCalendarView.setDate(calendar.getTimeInMillis());
        dateUpdate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));

        mTimePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setMinute(calendar.get(Calendar.MINUTE));
        // TimePicker의 값이 변하면서 자동으로 onTimeChanged가 호출되어 따로 setText 해 줄 필요 없음

        this.peopleNum = 0;
        peopleNumUpdate();
    }

    public void dateUpdate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        mDateEditText.setText(String.format("%d:%d:%d", year, month, day));
    }

    public void timeUpdate(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        mTimeEditText.setText(String.format("%d:%d", hourOfDay, minute));
    }

    public void peopleNumUpdate(){
        mPeopleNumEditText.setText(Integer.toString(peopleNum));
    }

    public void reservationAlertDialogShow() {
        if(year == 0) {
            if(hourOfDay == 0 && minute == 0)
                Toast.makeText(this, "예약 날짜와 시간이 선택되지 않았습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "예약 날짜가 선택되지 않았습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(peopleNum == 0) {
            Toast.makeText(this, "예약 인원이 선택되지 않았습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(hourOfDay == 0 && minute == 0)
            Toast.makeText(this, "00시 00분이 맞는지 다시 확인하시고 예약해주세요.", Toast.LENGTH_SHORT).show();

        final String message = String.format("%d년 %d월 %d일 시간 %d:%d에 %d명으로 ", year, month, day, hourOfDay, minute, peopleNum);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("예약 확인");
        builder.setMessage(message + "예약하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("예약", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), message+"예약되었습니다.", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void backgroundAlertDialogShow() {
        final CharSequence[] items = {"White", "Red", "Green", "Blue"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("색상을 선택하세요");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int index){
                switch(index) {
                    case 0 :
                        mCalendarView.setBackgroundColor(white);
                        dialog.cancel();
                        break;
                    case 1 :
                        mCalendarView.setBackgroundColor(red);
                        dialog.cancel();
                        break;
                    case 2 :
                        mCalendarView.setBackgroundColor(green);
                        dialog.cancel();
                        break;
                    case 3 :
                        mCalendarView.setBackgroundColor(blue);
                        dialog.cancel();
                        break;
                }
            }
        });
        AlertDialog radioDialog = builder.create();
        radioDialog.show();
    }
}