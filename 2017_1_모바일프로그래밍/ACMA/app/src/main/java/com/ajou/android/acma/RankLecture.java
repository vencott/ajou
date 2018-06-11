package com.ajou.android.acma;

import android.support.annotation.NonNull;

/**
 * Created by Kimjungmin on 2017. 6. 12..
 */

public class RankLecture implements Comparable<RankLecture>{
    private long count;
    private double rate;

    public RankLecture(long count, double rate) {
            this.count = count;
            this.rate = rate;
    }

    public long getCount() {
        return count;
    }

    public double getRate() {
        return rate;
    }

    @Override
    public int compareTo(@NonNull RankLecture o) {
        if(rate > o.getRate())
            return -1;
        else if(rate < o.getRate())
            return 1;
        return 0;
    }
}
