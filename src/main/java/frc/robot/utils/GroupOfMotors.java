/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.utils;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.Constants;


public class GroupOfMotors {
    private TalonSRX lead;
    private TalonSRX[] followers;
    
    public GroupOfMotors(int...talons){
        lead = new TalonSRX(talons[0]);
        followers = new TalonSRX[talons.length -1];
        for(int i = 0; i <followers.length; i++)
        {
            followers[i] = new TalonSRX(talons[i + 1]);
            followers[i].follow(lead);
        }
    }

    public void setPower(double power){ // -1 <= power <= 1
        lead.set(ControlMode.PercentOutput, power);
    }

    public double getEncoderPosition(){
        return lead.getSelectedSensorPosition() / Constants.pulsePerMeter;
    }

    public void resetEncoderPosition(){
        lead.setSelectedSensorPosition(0);
    }

    //Returns the speed in Meter/Second
    public double getSpeed(){
        return lead.getSelectedSensorVelocity() / Constants.pulsePerMeter * 10;
    }

    public double getVoltage(){
        return lead.getMotorOutputVoltage();
    }

    public double getCurrent(){
        return lead.getSupplyCurrent();
    }
}
