package com.ajou.android.acma;

import android.content.Context;
import android.widget.TextView;

public class Schedule {
    private String monday[] = new String[8];
    private String tuesday[] = new String[8];
    private String wednesday[] = new String[8];
    private String thursday[] = new String[8];
    private String friday[] = new String[8];

    public Schedule(){
        for (int i = 0; i<8; i++){
            monday[i] = "";
            tuesday[i] = "";
            wednesday[i] = "";
            thursday[i] = "";
            friday[i] = "";
        }
    }

    public void addSchedule(String scheduleText, String courseTitle, String courseProfessor){
        String professor;
        if (courseProfessor.equals("")){
            professor = "";
        }
        else {
            professor = "(" + courseProfessor + ")";
        }
        int temp;
        // 월[1][2][3], 화[4][5][6]
        if ((temp = scheduleText.indexOf("월")) > -1){
            temp += 2;
            int StartPoint = temp;
            int EndPoint = temp;
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++){
                if (scheduleText.charAt(i) == '['){
                    StartPoint = i;
                }
                if (scheduleText.charAt(i) == ']'){
                    EndPoint = i;
                    monday[Integer.parseInt(scheduleText.substring(StartPoint + 1, EndPoint))] = courseTitle + professor;
                }
            }
        }

        if ((temp = scheduleText.indexOf("화")) > -1){
            temp += 2;
            int StartPoint = temp;
            int EndPoint = temp;
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++){
                if (scheduleText.charAt(i) == '['){
                    StartPoint = i;
                }
                if (scheduleText.charAt(i) == ']'){
                    EndPoint = i;
                    tuesday[Integer.parseInt(scheduleText.substring(StartPoint + 1, EndPoint))] = courseTitle + professor;
                }
            }
        }

        if ((temp = scheduleText.indexOf("수")) > -1){
            temp += 2;
            int StartPoint = temp;
            int EndPoint = temp;
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++){
                if (scheduleText.charAt(i) == '['){
                    StartPoint = i;
                }
                if (scheduleText.charAt(i) == ']'){
                    EndPoint = i;
                    wednesday[Integer.parseInt(scheduleText.substring(StartPoint + 1, EndPoint))] = courseTitle + professor;
                }
            }
        }

        if ((temp = scheduleText.indexOf("목")) > -1){
            temp += 2;
            int StartPoint = temp;
            int EndPoint = temp;
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++){
                if (scheduleText.charAt(i) == '['){
                    StartPoint = i;
                }
                if (scheduleText.charAt(i) == ']'){
                    EndPoint = i;
                    thursday[Integer.parseInt(scheduleText.substring(StartPoint + 1, EndPoint))] = courseTitle + professor;
                }
            }
        }

        if ((temp = scheduleText.indexOf("금")) > -1){
            temp += 2;
            int StartPoint = temp;
            int EndPoint = temp;
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++){
                if (scheduleText.charAt(i) == '['){
                    StartPoint = i;
                }
                if (scheduleText.charAt(i) == ']'){
                    EndPoint = i;
                    friday[Integer.parseInt(scheduleText.substring(StartPoint + 1, EndPoint))] = courseTitle + professor;
                }
            }
        }
    }

    public void setting(TextView[] monday, TextView[] tuesday, TextView[] wednesday, TextView[] thursday, TextView[] friday, Context context){
        for (int i=0; i<8; i++){
            if (!this.monday[i].equals("")){
                monday[i].setText(this.monday[i]);
                monday[i].setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
            if (!this.tuesday[i].equals("")){
                tuesday[i].setText(this.tuesday[i]);
                tuesday[i].setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
            if (!this.wednesday[i].equals("")){
                wednesday[i].setText(this.wednesday[i]);
                wednesday[i].setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
            if (!this.thursday[i].equals("")){
                thursday[i].setText(this.thursday[i]);
                thursday[i].setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
            if (!this.friday[i].equals("")){
                friday[i].setText(this.friday[i]);
                friday[i].setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }
    }

}