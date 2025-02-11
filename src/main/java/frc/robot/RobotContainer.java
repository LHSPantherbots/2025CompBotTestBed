// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.CorralScoreL2;
import frc.robot.commands.StowAll;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.IntakeAlgaeSubsystem;
import frc.robot.subsystems.IntakeCoralSubsystem;
import frc.robot.subsystems.Leds;
import frc.robot.subsystems.WristSubsystem;
import frc.robot.subsystems.
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final WristSubsystem wrist = new WristSubsystem();
  private final IntakeCoralSubsystem coral = new IntakeCoralSubsystem();
  private final IntakeAlgaeSubsystem algae = new IntakeAlgaeSubsystem();
  private final Leds leds = new Leds();
  private final ElevatorSubsystem elevator = new ElevatorSubsystem();
  private final ClimbSubsystem climb = new ClimbSubsystem();
  private final CommandSwerveDriveTrain drivetrain = new Command

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();


  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {

    .setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-joystick.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-joystick.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate((joystick.getLeftTriggerAxis()-joystick.getRightTriggerAxis()) * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );


    elevator.setDefaultCommand(
        new RunCommand(
          () -> elevator.motionMagicSetPosition(), elevator));
          //.manualDrive(-m_driverController.getRightY()*.25), elevator));

    climb.setDefaultCommand(
      new RunCommand(
        ()-> climb.manualClimbMove(-m_driverController.getRightY()), climb));

    wrist.setDefaultCommand(
            // The left stick controls translation of the robot.
            // Turning is controlled by the X axis of the right stick.
            //new RunCommand(
            //    () -> wrist.manualWristMove(-m_driverController.getRightY()*.25), wrist));
                
            new RunCommand(() -> wrist.closedLoopWrist(), wrist));
    
    
    coral.setDefaultCommand(new RunCommand(() -> coral.intakeStop(), coral));
    algae.setDefaultCommand(new RunCommand(() -> algae.intakeStop(), algae));
    leds.setDefaultCommand(new RunCommand(() -> leds.rainbow(), leds));


    m_driverController.b().whileTrue(new RunCommand(() -> coral.intake(), coral));
    m_driverController.a().whileTrue(new RunCommand(() -> coral.outtake(), coral));

    m_driverController.y().whileTrue(new RunCommand(()-> algae.intake(), algae));
    m_driverController.x().whileTrue(new RunCommand(()-> algae.outtake(), algae));


    m_driverController.pov(180).onTrue(new InstantCommand(() -> wrist.lowWrist(), wrist));
    m_driverController.pov(90).onTrue(new InstantCommand(() -> wrist.midWrist(), wrist));


    m_driverController.pov(0).onTrue(new InstantCommand(() -> wrist.upWrist(), wrist));

    m_driverController.rightBumper().onTrue(new InstantCommand(() ->elevator.setElevatorCoralL2(), elevator));
    m_driverController.leftBumper().onTrue(new InstantCommand(() ->elevator.setElevatorCoralL1(), elevator));

    m_driverController.back().onTrue(new StowAll(wrist, elevator));
    m_driverController.start().onFalse(new CorralScoreL2(wrist, elevator));

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return new RunCommand(() -> coral.intakeStop(), coral);
  }
}
