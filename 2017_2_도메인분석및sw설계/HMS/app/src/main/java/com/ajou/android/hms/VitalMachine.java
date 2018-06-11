package com.ajou.android.hms;

/**
 * Created by Kimjungmin on 2017. 12. 4..
 */

public class VitalMachine {
    private int temparature;
    private int pulse;
    private int respiration;
    private int blood_pressure;

    public VitalMachine() {
        this.temparature = 0;
        this.pulse = 0;
        this.respiration = 0;
        this.blood_pressure = 0;
    }

    public void measure() {
        this.temparature = randomRange(36, 37);
        this.pulse = randomRange(60, 100);
        this.respiration = randomRange(10, 20);
        this.blood_pressure = randomRange(80, 120);
    }

    public boolean isNormal() {
        return ((temparature >= 36) && (temparature <= 37)) &&
                ((pulse >= 60) && (pulse <= 100)) &&
                ((respiration >= 12) && (respiration <= 20)) &&
                ((blood_pressure >= 80) && (blood_pressure <= 120));
    }

    public static int randomRange(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public int getTemparature() {
        return temparature;
    }

    public int getPulse() {
        return pulse;
    }

    public int getRespiration() {
        return respiration;
    }

    public int getBlood_pressure() {
        return blood_pressure;
    }
}
