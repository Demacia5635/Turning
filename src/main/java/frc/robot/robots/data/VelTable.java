/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frc.robot.robots.data;

/**
 *
 * @author Udi Kislev
 */
public class VelTable {
    
    public static final double MAX_VELOCITY = 4;
    public static final double DELTA = 0.1;
    public static final int N = (int)(MAX_VELOCITY/DELTA) + 1;
    private static VelTable[][] data = new VelTable[N][N];
    
    public static void init() {
        for(int i = 0; i < N; i++) {
            for(int j = 0; j <=i; j++) {
                data[i][j] = new VelTable(i, j);
            }
        }
        
    }
    
    private double i;
    private double j;
    private double hRatio; 
    private double lRatio; 
    public RawData best1;
    public RawData best2;

    private VelTable(int i, int j) {
        this.i = i;
        this.j = j;
        best1 = null;
        best2 = null;
        hRatio = 0;
        lRatio = 0;
        setBest((this.i+0.5) * DELTA,(this.j+0.5) * DELTA);
        calc();
    }
    
    private double sqr(double d1,double d2) {
        return d1*d1 + d2*d2;
    }
    private double distance(RawData r, double hVel, double lVel) {
        return sqr(r.lVelocity - hVel, r.rVelocity - lVel);
    }
    
    private void setBest(double hVel, double lVel) {
        double dis1 = Double.MAX_VALUE;
        double dis2 = dis1;
        double dis = 0;
        for(RawData r : RawData.data) {
            dis = distance(r, hVel, lVel);
            if(dis < dis1) {
                best2 = best1;
                dis2 = dis1;
                best1 = r;
                dis1 = dis;
            } else if(dis < dis2) {
                best2 = r;
                dis2 = dis;
            }
        }
    }
    
    private void calc() {
        hRatio = (best1.lPower - best2.lPower)/(best1.lVelocity - best2.lVelocity);
        lRatio = (best1.rPower - best2.rPower)/(best1.rVelocity - best2.rVelocity);
    }
    
    public double baseHVel() {
        return (i + 0.5) * DELTA;
    }
    public double baseLVel() {
        return (j + 0.5) * DELTA;
    }
    
    public static double getLeftPower(double l, double r) {
        if(l >= r) {
            return _highPower(l, r);
        } else {
            return _lowPower(r, l);
        }
    }
    public static double getRightPower(double l, double r) {
        if(l >= r) {
            return _lowPower(l, r);
        } else {
            return _highPower(r, l);
        }
    }
    
    public static VelTable get(double h, double l) {
        return data[(int)(h/DELTA)][(int)(l/DELTA)];
    }
    
    private static double _highPower(double h, double l) {
        VelTable t = get(h,l);
        return t.best1.lPower + t.hRatio*(h - t.best1.lVelocity);
    }
    private static double _lowPower(double h, double l) {
        VelTable t = get(h,l);
        return t.best1.rPower + t.lRatio*(l - t.best1.rVelocity);
    }
    
    public static String getString(double l, double r) {
        if(l >= r) {
            return get(l,r).toString();
        } else {
            return get(r,l).toString();
        }
    }

    @Override
    public String toString() {
        return String.format(" Base=%3f %3f %3.2f/%3.2f Ratio = %4.3f/%4.3f   Best1=%s Best2=%s", 
                i, j, baseHVel(), baseLVel(), hRatio, lRatio, best1, best2);
    }
}
