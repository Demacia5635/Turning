/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.subsystems.Chassis;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * An example command that uses an example subsystem.
 */
public class PowerDrive extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final Chassis m_subsystem;
  private final XboxController controller;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public PowerDrive(Chassis subsystem, XboxController controller) {
    m_subsystem = subsystem;
    this.controller = controller;
    if(true){
      System.out.println("Meow");
    }
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double left = controller.getY(Hand.kLeft);
    double right = controller.getY(Hand.kRight);
    SmartDashboard.putNumber("right speed", right);
    SmartDashboard.putNumber("left speed", left);
    m_subsystem.setPower(SmartDashboard.getNumber("left pw", 0), SmartDashboard.getNumber("right pw", 0));
    m_subsystem.displayPower(SmartDashboard.getNumber("left pw", 0), SmartDashboard.getNumber("right pw", 0));

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
