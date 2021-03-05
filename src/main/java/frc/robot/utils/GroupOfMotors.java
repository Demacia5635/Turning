/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.utils;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.robot.Constants;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class GroupOfMotors {
    private TalonFX lead;
    private TalonFX[] followers;
    
    public GroupOfMotors(int...talons){
        lead = new TalonFX(talons[0]);
        lead.setNeutralMode(NeutralMode.Brake);
        followers = new TalonFX[talons.length -1];
        for(int i = 0; i <followers.length; i++)
        {
            followers[i] = new TalonFX(talons[i + 1]);
            followers[i].setNeutralMode(NeutralMode.Brake);
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

    public void invert(boolean isInverted){
        lead.setInverted(isInverted);
        for (TalonFX talonFX : followers) {
            talonFX.setInverted(isInverted);
        }
    }
}
