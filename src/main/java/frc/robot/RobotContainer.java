/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.PowerDrive;
import frc.robot.commands.TestTurns;
import frc.robot.commands.TestTurnsHandler;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  private final XboxController controller = new XboxController(0);

  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final Chassis m_chassis = new Chassis();

  private final PowerDrive m_teleopCommand = new PowerDrive(m_chassis, controller);
  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);
  private final TestTurns turnCommand = new TestTurns(m_chassis);
  private final TestTurnsHandler turnHandlerCommand = new TestTurnsHandler(turnCommand);





  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    configureSmartDashboard();

  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
  }

  private void configureSmartDashboard() {
    SmartDashboard.setDefaultNumber("Left Power", 0);
    SmartDashboard.setDefaultNumber("Right Power", 0);
    SmartDashboard.setDefaultNumber("Left Min Range", 0);
    SmartDashboard.setDefaultNumber("Right Min Range", 0);
    SmartDashboard.setDefaultNumber("Left Max Range", 1);
    SmartDashboard.setDefaultNumber("Right Max Range", 1);
    SmartDashboard.setDefaultNumber("Skips", 0.01);

    SmartDashboard.putData("Activate", new InstantCommand(() ->{
      turnHandlerCommand.schedule();
    }));
    SmartDashboard.putData("Reset", new InstantCommand(() ->{
      turnHandlerCommand.reset();
    }));
    SmartDashboard.putData("Pause", new InstantCommand(() ->{
      turnHandlerCommand.pause();
    }));
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }

  public Command getTeleopCommand(){
    return m_teleopCommand;
  }
}
