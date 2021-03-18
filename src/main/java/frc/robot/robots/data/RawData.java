package frc.robot.robots.data;

import java.util.ArrayList;
import java.util.Comparator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Udi Kislev
 */
public class RawData {
    
    public static final double MAX_VELOCITY = 4;
    public static final double DELTA = 0.1;

    public static final double STEP = 0.05;
    public static final double MAX = 0.55;
    public static final int N = (int)(MAX/STEP) + 1;
    public static final double MAX_DIF = 0.2;
    public static final double MIN_V = 0.1;
    public static final double WHEEL_BASE = 0.6;
    
    public static ArrayList<RawData> data = new ArrayList<>();
    public static RawData[][] nData = new RawData[N][N];
    
    
    public double lPower;
    public double rPower;
    public double rVelocity;
    public double lVelocity;
    public double lVolt;
    public double rVolt;
    public double lAmp;
    public double rAmp;
    
    public static double round(double v, double delta) {
        return Math.round(v/delta) * delta;
    }

    public RawData(double lPower, double rPower, double rVelocity, double lVelocity, double lVolt, double rVolt, double lAmp, double rAmp) {
        this.lPower = round(lPower,0.01);
        this.rPower = round(rPower,0.01);
        this.rVelocity = -rVelocity;
        this.lVelocity = lVelocity;
        this.lVolt = lVolt;
        this.rVolt = rVolt;
        this.lAmp = lAmp;
        this.rAmp = rAmp;
        if(this.lPower * this.rPower > 0) { // if negative directions - ignored for now
            if(this.lPower < 0) { // reverse
                this.lPower = -this.lPower;
                this.rPower = -this.rPower;
                this.lVelocity = -this.lVelocity;
                this.rVelocity = -this.rVelocity;
            }
        }
        if(this.lPower < this.rPower) {
            // reverse all
            double t = this.lPower;
            this.lPower = this.rPower;
            this.rPower = t;
            t = this.lVelocity;
            this.lVelocity = this.rVelocity;
            this.rVelocity = t;
        }
        if(this.lVelocity > MIN_V || this.rVelocity > MIN_V) {
            RawData d = get(this.lPower, this.rPower);
            if(d == null) {
                data.add(this);
            } else if(Math.abs(d.lVelocity - this.lVelocity) < MAX_DIF && Math.abs(d.rVelocity - this.rVelocity) < MAX_DIF) {
                d.lVelocity = (d.lVelocity + this.lVelocity)/2;
                d.rVelocity = (d.rVelocity + this.rVelocity)/2;
            } else {
                System.out.printf("DIFF too big - %s, %s\n", d, this);
                // we use the MAX values
                if(this.lVelocity > d.rVelocity) {
                    d.lVelocity = this.lVelocity;
                    d.rVelocity = this.rVelocity;
                }
            }
        }   
    }
    
    public static void addStrights() {
        for(double v = DELTA; v < MAX_VELOCITY; v += DELTA) {
            double p = K_VS.K_V() + v*K_VS.K_S();
            RawData r = new RawData(p, p, -v, v, 0, 0, 0, 0);
        }
    }
    
    public RawData get(double lPower, double rPower) {
        for(RawData d : data) {
            if(d.lPower == lPower && d.rPower == rPower) {
                return d;
            }
        }
        return null;
    }
    
    public RawData() {
        this(0,0,0,0,0,0,0,0);
    }

    @Override
    public String toString() {
        return String.format("Power=%+4.2f/%+4.2f   Vel=%+4.2f/%+4.2f", 
                lPower, rPower, lVelocity, rVelocity);
    }

    
    public static double maxVelocity() {
        double max = 0;
        for(RawData r : data) {
            max = Math.max(max, r.lVelocity);
        }
        return max;
    }
    public static double minVelocity() {
        double min = 0;
        for(RawData r : data) {
            min = Math.min(min, r.rVelocity);
        }
        return min;
    }
}
