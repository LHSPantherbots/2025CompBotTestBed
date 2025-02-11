// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ElevatorSubsystem extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */
  private final TalonFX talon;
  private final TalonFX follower;
  private final DutyCycleOut talonOut = new DutyCycleOut(0);
  private final MotionMagicVoltage m_mmReq = new MotionMagicVoltage(0);
  private double elevatorSetpoint = 0.0;
  private double allowableError = 1.0;

  public ElevatorSubsystem() {

    talon = new TalonFX(40, "drive"); 
    
    follower = new TalonFX(41, "drive");
    
    

    TalonFXConfiguration cfg = new TalonFXConfiguration();

    cfg.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    cfg.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    cfg.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 69.0;
    cfg.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
    cfg.SoftwareLimitSwitch.ReverseSoftLimitThreshold = 0.0;
    cfg.CurrentLimits.StatorCurrentLimitEnable = true;
    cfg.CurrentLimits.StatorCurrentLimit = 60; //Output Current Limit
    //cfg.CurrentLimits.SupplyTimeThreshold = 5; //Amont of time to allow current over supply limit
    cfg.CurrentLimits.SupplyCurrentLimitEnable = true;
    cfg.CurrentLimits.SupplyCurrentLimit = 60; //Supply Current Limit
    cfg.MotionMagic.MotionMagicCruiseVelocity = 50; // 5 rotations per second cruise
    cfg.MotionMagic.MotionMagicAcceleration = 100; // Take approximately 0.5 seconds to reach max vel
    cfg.MotionMagic.MotionMagicJerk = 1000;// Take approximately 0.2 seconds to reach max accel 
    cfg.TorqueCurrent.PeakForwardTorqueCurrent = 50; //Current Limit value used in FOC Torque Mode
    cfg.TorqueCurrent.PeakReverseTorqueCurrent = 50; //Current Limit value used in FOC Torque Mode

    m_mmReq.EnableFOC = true;



    Slot0Configs slot0 = cfg.Slot0;
    slot0.kP = 60/12.8;
    slot0.kI = 0;
    slot0.kD = 0.1;
    slot0.kV = 0.12;
    slot0.kS = 0.25; // Approximately 0.25V to get the mechanism moving

    FeedbackConfigs fdb = cfg.Feedback;
    fdb.SensorToMechanismRatio = 1.0;

    

    StatusCode status = StatusCode.StatusCodeNotInitialized;
    for(int i = 0; i < 5; ++i) {
      status = talon.getConfigurator().apply(cfg);
      if (status.isOK()) break;
    }
    if (!status.isOK()) {
      System.out.println("Could not configure device. Error: " + status.toString());
    }

    follower.setControl(new Follower(talon.getDeviceID(), true));
    talon.setPosition(0);

  }

  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Elevator Position", talon.getRotorPosition().getValueAsDouble());
    SmartDashboard.putNumber("Elevator Setpoint", elevatorSetpoint);
    SmartDashboard.putNumber("Elevator Velocity ",talon.getRotorVelocity().getValueAsDouble());
    SmartDashboard.putNumber("Elevator Supply Current",talon.getSupplyCurrent().getValueAsDouble());
    SmartDashboard.putNumber("Elevator Stator Current",talon.getStatorCurrent().getValueAsDouble());
    SmartDashboard.putNumber("Eleveator Leader Motor Voltage", talon.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putNumber("Elevator Follower Motor Voltage", follower.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putNumber("Elevator Leader Supply Voltage", talon.getSupplyVoltage().getValueAsDouble());
    SmartDashboard.putNumber("Elevator Follower Supply Volgate", follower.getSupplyVoltage().getValueAsDouble());
;

  
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }


  public void manualDrive(double value){
    talonOut.Output = value;
    talonOut.EnableFOC = true;
    talon.setControl(talonOut);

  }

  public void motionMagicSetPosition(){
    talon.setControl(m_mmReq.withPosition(elevatorSetpoint).withSlot(0));
  }

  public void setZero(){
    talon.setPosition(0.0);
  }

  public double getPosition(){
    double pos = talon.getPosition().getValueAsDouble();
    return pos;
  }

  public void setElevatorSetpoint(double pos){
    elevatorSetpoint = pos;
  }

  public boolean isAtHeight() {
    double error = getPosition() - elevatorSetpoint;
    return (Math.abs(error) < allowableError);
  }

  


  public void setElevatorStow(){
    setElevatorSetpoint(0.0);
  }

  public void setElevatorCoralIntake(){
    setElevatorSetpoint(0.0);
  }

  public void setElevatorCoralL1Dump(){
    setElevatorSetpoint(0.0);
  }

  public void setElevatorCoralL1(){
    setElevatorSetpoint(0.0);
  }

  public void setElevatorCoralL2(){
    setElevatorSetpoint(0.0); // other option .53
  }

  public void setElevatorCoralL3(){
    setElevatorSetpoint(31.5);
  }

  public void setElevatorCoralL4(){
    setElevatorSetpoint(69.0);
  }

  public void setElevatorAlgaeL2(){
    setElevatorSetpoint(23.8);
  }

  public void setElevatorAlgaeL3(){
    setElevatorSetpoint(43.4);
  }

}
