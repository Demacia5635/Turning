/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frc.robot.robots.data;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Udi Kislev
 */
public class K_VS {
    
    private static double _K_V = 0;
    private static double _K_S = 0;
    
    public static double K_V() {
        if(_K_V == 0) {
            init();
        }
        return _K_V;
    }
    
    public static double K_S() {
        if(_K_S == 0) {
            init();
        }
        return _K_S;
    }
    
    private static void init() {
        class P_V {
            double p;
            double v;
            P_V(double p, double v) {
                this.p = p;
                this.v = v;
            }
            
        }
        ArrayList<P_V> l = new ArrayList<>();
        for(RawData r : RawData.data) {
            if(r.lPower == r.rPower) {
                l.add(new P_V(r.lPower, (r.lVelocity + r.rVelocity)/2));
            }
        }
        l.sort(new Comparator<P_V>() {
            @Override
            public int compare(P_V o1, P_V o2) {
                return Double.compare(o1.p, o2.p);
            }
        });
        double minP = l.get(0).p;
        double minV = l.get(0).v;
        double n = 0;
        double ks = 0;
        for(int i = 1; i < l.size(); i++) {
            P_V p = l.get(i);
            ks += (p.p - minP)/(p.v - minV);
            n += 1;
        }
        _K_S = ks / n;
        double kv = 0;
        n = 0;
        for(P_V p : l) {
            kv += p.p - p.v*_K_S;
            n += 1;
        }
        _K_V = kv/n;
        // check
        double sumE = 0;
        n = 0;
        for(P_V p : l) {
            double e = p.p - power(p.v);
            sumE += e*e;
            n += 1;
        }
        System.out.printf("KV/KS = %6.5f %6.5f Error: %f\n", _K_V, _K_S, Math.sqrt(sumE)/n);
    }
    
    public static double power(double velocity) {
        return velocity * K_S() + K_V();
    }
    
    public static void main(String... arg) {
        try {
            ReadData.read("C:/Users/Demacia/Desktop/Turning/Turning/Turning/src/main/java/frc/robot/robots/data/runs.csv");
            System.out.printf("K_V = %7.6f, K_S = %7.6f\n", K_V(), K_S());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
