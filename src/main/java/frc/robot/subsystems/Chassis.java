/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.GroupOfMotors;

public class Chassis extends SubsystemBase {
  GroupOfMotors left = new GroupOfMotors(3,4);
  GroupOfMotors right = new GroupOfMotors(1,2);
  /**
   * Creates a new ExampleSubsystem.
   */
  public Chassis() {
    right.invert(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setPower(double left, double right){
    this.left.setPower(left);
    this.right.setPower(right);
  }

  public void displayPower(double left, double right){
    SmartDashboard.putNumber("Left Power", left);
    SmartDashboard.putNumber("Right Power", right);
  }

  public double getLeftEncoderPosition(){
    return left.getEncoderPosition();
  }

  public double getRightEncoderPosition(){
    return right.getEncoderPosition();
  }

  public void resetLeftEncoderPosition(){
    left.resetEncoderPosition();
  }

  public void resetRightEncoderPosition(){
    right.resetEncoderPosition();
  }

  public double getLeftSpeed(){
    return left.getSpeed();
  }

  public double getRightSpeed(){
    return -right.getSpeed();
  }

  public double getLeftVoltage(){
    return left.getVoltage();
  }

  public double getRightVoltage(){
    return right.getVoltage();
  }

  public double getLeftCurrent(){
    return left.getCurrent();
  }

  public double getRightCurrent(){
    return right.getCurrent();
  }

  public double[] calculateDistances(double leftPower, double rightPower){
    double space = leftPower > rightPower ? Constants.robotWidth : -Constants.robotWidth;
    double r = (space * (leftPower + rightPower)) / (leftPower - rightPower);
    double[] arr = {r, ((r + space) * Math.PI) / 2, ((r - space) * Math.PI) / 2};
    return arr;
  }
}
