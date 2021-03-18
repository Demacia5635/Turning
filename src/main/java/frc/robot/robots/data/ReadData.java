/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frc.robot.robots.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Udi Kislev
 */
public class ReadData {
    
    public static final int minLine = 0;
    
    public static void read(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();
        if(line != null) {
            line = reader.readLine();
        }
        while(line != null) {
            String[] d = line.split(",");
            double lineNum = Double.parseDouble(d[0]);
            if(lineNum > minLine) {
                double lPower = Double.parseDouble(d[1]);
                double rPower = Double.parseDouble(d[2]);
                double lVolt = Double.parseDouble(d[3]);
                double rVolt = Double.parseDouble(d[4]);
                double lAmp = Double.parseDouble(d[5]);
                double rAmp = Double.parseDouble(d[6]);
                double lVel = Double.parseDouble(d[7]);
                double rVel = Double.parseDouble(d[8]);
                RawData r = new RawData(lPower, rPower, rVel, lVel, lVolt, rVolt, lAmp, rAmp);
            }
            line = reader.readLine();
        }
        reader.close();
        RawData.addStrights();
    }
    
    
    public static void main(String... arg) {
        try {
            read("C:/Users/Demacia/Desktop/Turning/Turning/Turning/src/main/java/frc/robot/robots/data/runs.csv");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
