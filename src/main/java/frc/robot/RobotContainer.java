// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.CorralIntake;
import frc.robot.commands.CorralScoreL2;
import frc.robot.commands.CorralScoreL3;
import frc.robot.commands.CorralScoreL4;
import frc.robot.commands.StowAll;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.IntakeAlgaeSubsystem;
import frc.robot.subsystems.IntakeCoralSubsystem;
import frc.robot.subsystems.Leds;
import frc.robot.subsystems.WristSubsystem;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.util.TunerConstants;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

   private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
    private final Telemetry logger = new Telemetry(MaxSpeed);

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    //private final Telemetry logger = new Telemetry(MaxSpeed);
  // The robot's subsystems and commands are defined here...
  private final WristSubsystem wrist = new WristSubsystem();
  private final IntakeCoralSubsystem coral = new IntakeCoralSubsystem();
  private final IntakeAlgaeSubsystem algae = new IntakeAlgaeSubsystem();
  private final Leds leds = new Leds();
  private final ElevatorSubsystem elevator = new ElevatorSubsystem();
  private final ClimbSubsystem climb = new ClimbSubsystem();
  public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);
  private final CommandXboxController m_operatorController = 
      new CommandXboxController(OperatorConstants.kOperatorControllerPort);

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


    //DEFAULT COMMANDS


    drivetrain.setDefaultCommand(
    //         // Drivetrain will execute this command periodically
             drivetrain.applyRequest(() ->
                 drive.withVelocityX(-m_driverController.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                     .withVelocityY(-m_driverController.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                     .withRotationalRate((m_driverController.getLeftTriggerAxis()-m_driverController.getRightTriggerAxis()) * MaxAngularRate) // Drive counterclockwise with negative X (left)
             )
         );

    elevator.setDefaultCommand(
        new RunCommand(
          () -> elevator.motionMagicSetPosition(), elevator));
          //.manualDrive(-m_driverController.getRightY()*.25), elevator));

    climb.setDefaultCommand(
      new RunCommand(
        ()-> climb.manualClimbMove(0.0), climb));

    wrist.setDefaultCommand(
            new RunCommand(() -> wrist.closedLoopWrist(), wrist));
    
    coral.setDefaultCommand(new RunCommand(() -> coral.intakeStop(), coral));
    algae.setDefaultCommand(new RunCommand(() -> algae.intakeStop(), algae));
    leds.setDefaultCommand(new RunCommand(() -> leds.rainbow(), leds));



    //************  DRIVER CONTROLLER  ****************

    m_driverController.leftBumper().whileTrue(
        new RunCommand(
            () -> climb.manualClimbMove(-MathUtil.applyDeadband(m_driverController.getRightY(), OperatorConstants.kDriveDeadband)),
            climb));
    

    m_driverController.a().whileTrue(drivetrain.applyRequest(() -> brake));
    
    //PROBABLY REMOVE THIS ONE
    m_driverController.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-m_driverController.getLeftY(), -m_driverController.getLeftX()))
    ));

    // Run SysId routines when holding back/start and X/Y.
    // Note that each routine should be run exactly once in a single log.
    m_driverController.back().and(m_driverController.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
    m_driverController.back().and(m_driverController.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
    m_driverController.start().and(m_driverController.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
    m_driverController.start().and(m_driverController.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

    // reset the field-centric heading on start press
    m_driverController.start().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

    drivetrain.registerTelemetry(logger::telemeterize);

    //************   OPERATOR CONTROLLER  *******************

    m_operatorController.povDown().onTrue(new CorralIntake(wrist, elevator));  //Intake/Stow
    
    m_operatorController.povLeft().onTrue(new CorralScoreL2(wrist, elevator));

    m_operatorController.povUp().onTrue(new CorralScoreL3(wrist, elevator));

    m_operatorController.povRight().onTrue(new CorralScoreL4(wrist, elevator));

    m_operatorController.leftBumper().whileTrue(new RunCommand(() -> coral.intake(), coral));

    m_operatorController.rightBumper().whileTrue(new RunCommand(() -> coral.outtake(), coral));

    m_operatorController.a().whileTrue(new RunCommand(()-> algae.intake(), algae));
    
    m_operatorController.x().whileTrue(new RunCommand(()-> algae.outtake(), algae));

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
