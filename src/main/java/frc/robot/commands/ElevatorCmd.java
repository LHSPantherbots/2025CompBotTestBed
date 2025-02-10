// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.util.Position;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ElevatorCmd extends Command {
  /** Creates a new ElevatorCmd. */
  Position position;
  ElevatorSubsystem elevator;
  boolean finishes;


  public ElevatorCmd(Position position, ElevatorSubsystem elevator) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.position = position;
    this.elevator = elevator;
    this.finishes = true;
    addRequirements(elevator);
  }

  public ElevatorCmd(Position position, ElevatorSubsystem elevator, boolean finishes) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.position = position;
    this.elevator = elevator;
    this.finishes = finishes;
    addRequirements(elevator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    switch (this.position) {
      case STOW:
        this.elevator.setElevatorStow();
        break;
      case CORAL_INTAKE:
        this.elevator.setElevatorCoralIntake();
        break;
      case CORAL_L1_DUMP:
        this.elevator.setElevatorCoralL1Dump();
        break;
      case CORAL_L1:
        this.elevator.setElevatorCoralL1();
        break;
      case CORAL_L2:
        this.elevator.setElevatorCoralL2();
        break;
      case CORAL_L3:
        this.elevator.setElevatorCoralL3();
        break;
      case CORAL_L4:
        this.elevator.setElevatorCoralL4();
        break;
      case ALGAE_L2:
        this.elevator.setElevatorAlgaeL2();
        break;
      case ALGAE_L3:
        this.elevator.setElevatorAlgaeL3();
        break;
      case HOLD:
        this.elevator.motionMagicSetPosition();
        break;
    }

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    this.elevator.motionMagicSetPosition();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    this.elevator.motionMagicSetPosition();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
      if (finishes) {
      return this.elevator.isAtHeight();
    } else {
      return false;
    }
  }
}
