// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.WristSubsystem;
import frc.robot.util.Position;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class WristCmd extends Command {
  /** Creates a new ElevatorCmd. */
  Position position;
  WristSubsystem wrist;
  boolean finishes;


  public WristCmd(Position position, WristSubsystem wrist) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.position = position;
    this.wrist = wrist;
    this.finishes = true;
    addRequirements(wrist);
  }

  public WristCmd(Position position, WristSubsystem wrist, boolean finishes) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.position = position;
    this.wrist = wrist;
    this.finishes = finishes;
    addRequirements(wrist);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    switch (this.position) {
      case STOW:
        this.wrist.setWristStow();
        break;
      case CORAL_INTAKE:
        this.wrist.setWristCoralIntake();
        break;
      case CORAL_L1_DUMP:
        this.wrist.setWristCoralL1Dump();
        break;
      case CORAL_L1:
        this.wrist.setWristCoralL1();
        break;
      case CORAL_L2:
        this.wrist.setWristCoralL2();
        break;
      case CORAL_L3:
        this.wrist.setWristCoralL3();
        break;
      case CORAL_L4:
        this.wrist.setWristCoralL4();
        break;
      case ALGAE_L2:
        this.wrist.setWristAlgaeL2();
        break;
      case ALGAE_L3:
        this.wrist.setWristAlgaeL3();
        break;
      case HOLD:
        this.wrist.closedLoopWrist();
        break;
    }

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    this.wrist.closedLoopWrist();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    this.wrist.closedLoopWrist();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
      if (finishes) {
      return this.wrist.isAtAngle();
    } else {
      return false;
    }
  }
}
