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

import com.fasterxml.jackson.core.io.JsonEOFException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

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
    double[] distances = m_chassis.calculateDistances(leftPower, rightPower);
    radius = distances[0];
    leftDistance = distances[1];
    rightDistance = distances[2];
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_chassis.setPower(leftPower, rightPower);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    double leftSpeed = m_chassis.getLeftSpeed();
    double rightSpeed = m_chassis.getRightSpeed();
    double leftVoltage = m_chassis.getLeftVoltage();
    double rightVoltage = m_chassis.getRightVoltage();
    double leftCurrent = m_chassis.getLeftCurrent();
    double rightCurrent = m_chassis.getRightCurrent();

    try {
      String text = new String(Files.readAllBytes(Paths.get("Statistics.json")), StandardCharsets.UTF_8);
      JSONArray data = new JSONArray(text);
      JSONObject run = new JSONObject();
      JSONObject statistics = new JSONObject();
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss");
      LocalDateTime now = LocalDateTime.now();
      statistics.put("L Current", leftCurrent);
      statistics.put("R Current", rightCurrent);
      statistics.put("L Voltage", leftVoltage);
      statistics.put("R Voltage", rightVoltage);
      statistics.put("L Speed", leftSpeed);
      statistics.put("R Speed", rightSpeed);
      run.put(dtf.format(now), statistics);
      data.put(run);
      FileWriter writer = new FileWriter("Statistics.json");
      writer.write(data.toString(4));
      writer.close();
    } catch (Exception e1) {
      System.out.println(e1);
      System.out.println("");
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return leftDistance <= m_chassis.getLeftEncoderPosition() / Constants.pulsePerMeter &&
    rightDistance <= m_chassis.getRightEncoderPosition() / Constants.pulsePerMeter;
  }
}