/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;

/**
 * An example command that uses an example subsystem.
 */
public class TestTurnsHandler extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // private double runCount = 0;
  private double minLeft;
  private double minRight;
  private double maxLeft;
  private double maxRight;
  private double skips;
  private double currentLeft;
  private double currentRight;
  private boolean isReversed = false;
  public boolean paused = false;

  private final TestTurns command;
  private final MoveSlow slowCommand;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public TestTurnsHandler(TestTurns command, MoveSlow slowcommand) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.command = command;
    this.slowCommand = slowcommand;
    
  }
   public void reset() {
    command.leftPower = 0;
    command.rightPower = 0;
    // SmartDashboard.putNumber("Left Power", 0);
    // SmartDashboard.putNumber("Right Power", 0);
    this.command.cancel();
    this.cancel();
  }

  public void pause() {
    paused = !paused;
    this.command.cancel();
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    minLeft = SmartDashboard.getNumber("Left Min Range", 0);
    minRight = SmartDashboard.getNumber("Right Min Range", 0);
    maxLeft = SmartDashboard.getNumber("Left Max Range", 1);
    maxRight = SmartDashboard.getNumber("Right Max Range", 1);
    skips = SmartDashboard.getNumber("Skips", 0.01);
    currentLeft = minLeft;
    currentRight = minRight;
    // SmartDashboard.putNumber("Run Count", runCount);
    SmartDashboard.putBoolean("isRunning", true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(!paused){
      if(!command.isScheduled()){
        if (!slowCommand.isScheduled()) {
          if (!MoveSlow.end) {
            slowCommand.lPower = command.leftPower;
            slowCommand.rPower = command.rightPower;
            slowCommand.schedule(); 
          } else {
            // runCount += 1;
          // SmartDashboard.putNumber("Run Count", runCount);
          // SmartDashboard.putNumber("Left Power", currentLeft * (isReversed ? -1 : 1));
          // SmartDashboard.putNumber("Right Power", currentRight * (isReversed ? -1 : 1));
          if(currentRight > maxRight && isReversed){
            currentRight = minRight;
            currentLeft += skips;
          } else if(isReversed){
            currentRight += skips;
          }
          isReversed = !isReversed;

          command.leftPower = currentLeft * (isReversed ? -1 : 1);
          command.rightPower = currentRight * (isReversed ? -1 : 1);
          command.runCount++;
          command.schedule();
          }
        }
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean a = SmartDashboard.getBoolean("isRunning", true);
    return currentLeft > maxLeft || !a;
  }
}
