/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frc.robot.robots;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import frc.robot.robots.data.RawData;
import frc.robot.robots.data.ReadData;
import frc.robot.robots.data.VelTable;

/**
 *
 * @author Udi Kislev
 */
public class Robots {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            long sTime = System.currentTimeMillis();
            ReadData.read("runs.csv");
//            for(RawData r : RawData.data) {
//                System.out.printf("%s\n", r);
//            }
            VelTable.init();
            long eTime = System.currentTimeMillis();
            System.out.printf("Load and calc in %dms\n", eTime - sTime);
            for(RawData r : RawData.data) {
                double h = VelTable.getLeftPower(r.lVelocity,r.rVelocity);
                double l = VelTable.getRightPower(r.lVelocity,r.rVelocity);
                double error = Math.abs(r.lPower - h) + Math.abs(r.rPower - l);
                if(error > 0.01) {
                    System.out.printf("%s - %4.3f/%4.3f  error = %4.3f/%4.3f\n", r, h, l, r.lPower-h, r.rPower - l);
                    VelTable v = VelTable.get(r.lVelocity, r.rVelocity);
                    System.out.printf("   Using %s %s\n", v.best1, v.best2);
                }
            }
            
            for(double h = VelTable.DELTA; h <= VelTable.MAX_VELOCITY; h += VelTable.DELTA) {
                double lp1 = VelTable.getLeftPower(h, 0);
                double rp1 =  VelTable.getRightPower(h, 0);
                for(double l = VelTable.DELTA; l <= h; l += VelTable.DELTA) {
                    double lp = VelTable.getLeftPower(h, l);
                    double rp = VelTable.getRightPower(h, l);
                    if(lp > lp1) {
                        System.out.printf("%3.1f / %3.1f - %4.3f/%4.3f - prev %4.3f/%4.3f\n", h, l, lp, rp, lp1, rp1);
                        System.out.printf("    %s\n", VelTable.getString(h, l - VelTable.DELTA));
                        System.out.printf("    %s\n", VelTable.getString(h, l));
                    }
                    lp1 = lp;
                    rp1 = rp;
                }
            }
            /*
            for(double h = 0.1; h < VelTable.MAX_VELOCITY; h+=0.1) {
                for(double l = 0; l <= h; l+=0.1) {
                    VelTable v = VelTable.get(h, l);
                    System.out.printf("%3.1f/%3.1f - %4.3f/%4.3f  - using %s %s\n", h, l, VelTableData.getHigh(h, l), VelTableData.getLow(h, l),
                            v.best1, v.best2);
                }
            } */
       } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
