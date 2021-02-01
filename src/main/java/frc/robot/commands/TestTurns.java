/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.Chassis;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.lang.Math; 
import com.fasterxml.jackson.core.io.JsonEOFException;

/**
 * An example command that uses an example subsystem.
 */
public class TestTurns extends CommandBase {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final Chassis m_chassis;
  private double leftPower = 0;
  private double rightPower = 0;
  private double radius = 0;
  private double leftDistance = 0;
  private double rightDistance = 0;
  private double count = 0;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public TestTurns(Chassis subsystem) {
    m_chassis = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_chassis.resetLeftEncoderPosition();
    m_chassis.resetRightEncoderPosition();
    leftPower = SmartDashboard.getNumber("Left Power", 0);
    rightPower = SmartDashboard.getNumber("Right Power", 0);
    //double[] distances = m_chassis.calculateDistances(leftPower, rightPower);
    //radius = distances[0];
    leftDistance = Constants.distance; //distances[1];
    rightDistance = Constants.distance;//distances[2];
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_chassis.setPower(leftPower, rightPower);
    double leftSpeed = m_chassis.getLeftSpeed();
    double rightSpeed = m_chassis.getRightSpeed();
    if(leftSpeed == 0 && rightSpeed == 0){
      count += 1;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_chassis.setPower(0, 0);
    double leftSpeed = m_chassis.getLeftSpeed();
    double rightSpeed = m_chassis.getRightSpeed();
    double leftVoltage = m_chassis.getLeftVoltage();
    double rightVoltage = m_chassis.getRightVoltage();
    double leftCurrent = m_chassis.getLeftCurrent();
    double rightCurrent = m_chassis.getRightCurrent();
    SmartDashboard.putNumber("L Current", leftCurrent);
    SmartDashboard.putNumber("R Current", rightCurrent);
    SmartDashboard.putNumber("L Voltage", leftVoltage);
    SmartDashboard.putNumber("R Voltage", rightVoltage);
    SmartDashboard.putNumber("L Speed", leftSpeed);
    SmartDashboard.putNumber("R Speed", rightSpeed);
    //wait(1000);
    // try {
    //   String text = new String(Files.readAllBytes(Paths.get("Statistics.json")), StandardCharsets.UTF_8);
    //   JSONArray data = new JSONArray(text);
    //   JSONObject run = new JSONObject();
    //   JSONObject statistics = new JSONObject();
    //   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss");
    //   LocalDateTime now = LocalDateTime.now();
    //   statistics.put("Left Power", leftPower);
    //   statistics.put("Right Power", rightPower);
    //   statistics.put("L Current", leftCurrent);
    //   statistics.put("R Current", rightCurrent);
    //   statistics.put("L Voltage", leftVoltage);
    //   statistics.put("R Voltage", rightVoltage);
    //   statistics.put("L Speed", leftSpeed);
    //   statistics.put("R Speed", rightSpeed);
    //   run.put(dtf.format(now), statistics);
    //   data.put(run);
    //   FileWriter writer = new FileWriter("Statistics.json");
    //   writer.write(data.toString(4));
    //   writer.close();
    // } catch (Exception e1) {
    //   System.out.println(e1);
    //   System.out.println("");
    // }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // / Constants.pulsePerMeter
    return (leftDistance <= Math.abs(m_chassis.getLeftEncoderPosition())  ||
    rightDistance <= Math.abs(m_chassis.getRightEncoderPosition())) || (count >= 400);
  }
  public static void wait(int ms)
  {
    try
    {
        Thread.sleep(ms);
    }
    catch(InterruptedException ex)
    {
        Thread.currentThread().interrupt();
    }
  }
}
