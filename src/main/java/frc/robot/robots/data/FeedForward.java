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
public class FeedForward {
    
    public static  double K_HA = 8;
    public static  double K_LA = 0.29;
    public static double MAX_BRAKEMODE = 0.05;
    public static double MIN_BRAKEMODE = 0.1;
    
    static boolean debug = false;
    
    public static double feedForwardPower(double velocity) {
        return K_VS.power(velocity);
    }
    
    public static double feedForwardLeftPower(double leftVelocity, double rightVelocity) {
        // if zeros - return 0
        if(leftVelocity == 0 && rightVelocity == 0) {
            return 0;
        }
        // if not in same direction - use normal power
        if(leftVelocity * rightVelocity < 0) {
            return feedForwardPower(leftVelocity);
        }
        // if negative - return minus of the positive values
        if(leftVelocity < 0) {
            return -feedForwardLeftPower(-leftVelocity, -rightVelocity);
        }
        // if very small velocity and other side is higher - retuen 0 for brake mode
        if(leftVelocity < MAX_BRAKEMODE && rightVelocity > MIN_BRAKEMODE) {
            return 0;
        }
        if(leftVelocity > rightVelocity) {
            return highPower(leftVelocity, rightVelocity);
        } else {
            return lowPower(rightVelocity, leftVelocity);
        }
    }

    public static double feedForwardRightPower(double leftVelocity, double rightVelocity) {
        return feedForwardLeftPower(rightVelocity, leftVelocity);
    }
    
    private static double power(double highVelocity, double lowVelocity, boolean high) {
        double v = (highVelocity - lowVelocity + highVelocity*K_HA - lowVelocity*K_LA) / (K_HA - K_LA);
        double p = feedForwardPower(v);
        if(high) {
            return p;
        } else {
            return p - (v-lowVelocity) * K_LA;
        }
    }
    private static double highPower(double highVelocity, double lowVelocity) {
        return power(highVelocity, lowVelocity, true);
    }
    private static double lowPower(double highVelocity, double lowVelocity) {
        return power(highVelocity, lowVelocity, false);
    }

    private static double calcError(double KH, double KL) { 
        K_HA = KH;
        K_LA = KL;
        double sumE = 0;
        double n = 0;
        double maxE = 0;
        for(RawData r : RawData.data) {
           double rV = r.rVelocity > 0? r.rVelocity:0;
           double lp = feedForwardLeftPower(r.lVelocity,rV);
           double rp = feedForwardRightPower(r.lVelocity,rV);
           double error = Math.max(Math.abs(lp - r.lPower), Math.abs(rp - r.rPower));
           sumE += error;
           n += 1;
           if(error > maxE) {
            maxE = error;
           }
        }
        return maxE*9 + sumE/n;
    }
    
    private static void optimize() {
        optimize(0.5, 20, 0.5, 0.05, 1.5, 0.05);
        double mh = K_HA - 2;
        if(mh <= 0) {
            mh = 0.1;
        }
        double ml = K_LA - 0.2;
        if(ml <= 0) {
            ml = 0.01;
        }
        optimize(mh, mh + 4, 0.1, ml, ml + 0.4, 0.005);
        
    }
    
    private static void optimize(double minH, double maxH, double stepH, double minL, double maxL, double stepL) {
        double bestH = 0;
        double bestL = 0;
        double bestError = Double.MAX_VALUE;
        for(double h = minH; h <= maxH; h += stepH) {
            for(double l = minL; l <= maxL; l += stepL) {
                double e = calcError(h, l);
                if(e < bestError) {
                    bestH = h;
                    bestL = l;
                    bestError = e;
                }
            }
        }
        K_HA = bestH;
        K_LA = bestL;
        System.out.printf("Best - %4.3f / %4.3f - error - %4.3f\n", K_HA, K_LA, bestError);
    }
    
    public static void main(String... arg) {
        try {
            ReadData.read("C:/Users/Demacia/Desktop/Turning/Turning/Turning/src/main/java/frc/robot/robots/data/runs.csv");
            optimize();
            double sumE = 0;
            double n = 0;
            double maxE = 0;
            double maxEratio = 0;
            for(RawData r : RawData.data) {
                double rV = r.rVelocity > 0? r.rVelocity:0;
                double lp = feedForwardLeftPower(r.lVelocity,rV);
                double rp = feedForwardRightPower(r.lVelocity,rV);
                double maxError = Math.max(Math.abs(lp - r.lPower), Math.abs(rp - r.rPower));
                double eratio = Math.max(Math.abs(lp - r.lPower)/K_VS.K_S()/r.lVelocity, Math.abs(rp - r.rPower)/K_VS.K_S()/r.rVelocity);
                sumE += maxError;
                n += 1;
                if(maxError > maxE) {
                    maxE = maxError;
                    maxEratio = eratio;
                }
                System.out.printf("%s - %4.3f, %4.3f   - error = %4.3f / %4.3f  %s\n",
                        r, lp, rp, r.lPower - lp, r.rPower - rp, maxError > 0.05?"*****": "");
            }
            System.out.printf("Max Error: %4.3f   %4.3fV    %4.3fm/s\n", maxE, maxE * 12, maxE/K_VS.K_S());
            System.out.printf("Avg Error: %4.3f   %4.3fV    %4.3fm/s\n", sumE/n/2, sumE*6/n, sumE/n/2/K_VS.K_S());
            System.out.printf("Max R Error: %3.1f%%\n", maxEratio*100);
            System.out.printf("Low A = %f,  High A = %f\n", K_LA, K_HA);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
