package com.ajou.android.hms;

/**
 * Created by Kimjungmin on 2017. 12. 4..
 */

public class Chart {
    private String chart;

    public Chart(String chart) {
        this.chart = chart;
    }

    public String getChart() {
        return chart;
    }

    public void changeChart(String memo) {
        this.chart = memo;
    }
}
