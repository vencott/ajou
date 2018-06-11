package com.ajou.android.acma;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class RankAdapter extends BaseAdapter{

    private Context context;
    private List<Lecture> rankList;
    private RankLecture[] rankLectures;

    public RankAdapter(Context context, List<Lecture> courseList, RankLecture[] rankLectures) {
        this.context = context;
        this.rankList = courseList;
        this.rankLectures = rankLectures;
    }

    @Override
    public int getCount() {
        return rankList.size();
    }

    @Override
    public Object getItem(int i) {
        return rankList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.rank,null);
        TextView courseGrade = (TextView)v.findViewById(R.id.courseGrade);
        TextView courseName = (TextView)v.findViewById(R.id.courseName);
        TextView courseProfessor = (TextView)v.findViewById(R.id.courseProfessor);
        TextView courseCredit = (TextView)v.findViewById(R.id.courseCredit);
        TextView courseID = (TextView)v.findViewById(R.id.courseID);
        TextView courseLimit = (TextView)v.findViewById(R.id.courseLimit);
        TextView courseRegister = (TextView)v.findViewById(R.id.courseRegister);
        TextView courseRate = (TextView)v.findViewById(R.id.courseRate);
        Button courseRank = (Button)v.findViewById(R.id.courseRank);

        courseGrade.setText(rankList.get(i).getCourseGrade() + "학년");
        courseName.setText(rankList.get(i).getCourseName());
        courseProfessor.setText(rankList.get(i).getCourseProfessor());
        courseCredit.setText(rankList.get(i).getCourseCredit() + "학점");
        courseID.setText(rankList.get(i).getCourseID());
        courseLimit.setText("제한: " + rankList.get(i).getCourseLimit());
        courseRegister.setText("신청: " + rankList.get(i).getCourseRegister());
        courseRate.setText("경쟁률 : " + String.format("%.2f", rankLectures[i].getRate()) + " : 1");
        courseRank.setText((i+1) + " 위"); // 경쟁률 제일 높은 강의가 1위 강의

        v.setTag(rankList.get(i).getCount());
        return v;
    }
}