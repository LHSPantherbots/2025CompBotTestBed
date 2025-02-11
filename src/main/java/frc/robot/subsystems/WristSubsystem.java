package frc.robot.subsystems;

import com.revrobotics.spark.config.AbsoluteEncoderConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import java.util.Map;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.WristConstants;

public class WristSubsystem extends SubsystemBase{
    private  SparkMax m_Wrist; 
    private  SparkMaxConfig c_Wrist = new SparkMaxConfig();
    AbsoluteEncoder e_WristEncoder;
    private AbsoluteEncoderConfig c_EncoderConfig = new AbsoluteEncoderConfig();
    private final SparkClosedLoopController wristController;


    private double wristSetpoint = 0.8;
    private double allowableError = 0.1;

    private ShuffleboardTab tab = Shuffleboard.getTab("Tuning");  //angles used for shuffleboard; taken from 2024 fulcrum code
    private GenericEntry sbAngle = tab.add("Wrist Angle", 1)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 90))
            .getEntry();

    public WristSubsystem() {      
        m_Wrist = new SparkMax(WristConstants.kWrist, MotorType.kBrushless);
        wristController = m_Wrist.getClosedLoopController();
        e_WristEncoder = m_Wrist.getAbsoluteEncoder();




        c_Wrist
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(40)
                .inverted(false);
        c_Wrist.softLimit
            .forwardSoftLimit(0.9)
            .forwardSoftLimitEnabled(true)
            .reverseSoftLimit(0.5)
            .reverseSoftLimitEnabled(true);
                
                
        c_Wrist.encoder
                
                .positionConversionFactor(1.0) // meters
                .velocityConversionFactor(1.0);//meters/sec
        c_EncoderConfig.zeroOffset(0.25);
                
                
//                  .inverted(false);
        c_Wrist.closedLoop
                  .feedbackSensor(FeedbackSensor.kAbsoluteEncoder)
                  .p(3.0)//need to tune
                  .i(0)
                  .d(0.0)
                  .velocityFF(0.0)
                  .maxOutput(1.00)
                  .minOutput(-1.0)
                  .maxMotion
                    .maxAcceleration(1.0)
                    .maxVelocity(1.0)
                    .allowedClosedLoopError(.1) //degrees
                    ;
        
        m_Wrist.configure(c_Wrist, ResetMode.kResetSafeParameters,
                    PersistMode.kPersistParameters);
        
        
    }
    @Override
    public void periodic() {
      SmartDashboard.putNumber("Wrist Velocity", e_WristEncoder.getVelocity());
      SmartDashboard.putNumber("Wrist Output", m_Wrist.getAppliedOutput());
      SmartDashboard.putNumber("Wrist Setpoint", wristSetpoint);
      SmartDashboard.putNumber("Wrist Pos", e_WristEncoder.getPosition());
      SmartDashboard.putNumber("Wrist FF", getPositionFeedForward());
    
    }

     public void stop() {
        m_Wrist.set(0.0);
    }

     public double getPosition() {
        return e_WristEncoder.getPosition()*2*3.14159*34.0/36.0+.35319;
    }

    public double getPositionFeedForward(){
        double kF = 0.5;
        return kF*-1*Math.sin(getPosition());
    }



    public double getSbAngle() {  //reference lines 26-30
        return sbAngle.getDouble(1.0);
    }

     public void manualWristMove(double move) {
        m_Wrist.set(move);
    }

    public void setWristSetpoint(double setpoint){
        wristSetpoint = setpoint;
    }

    public void maxMotionPosition(){
    wristController.setReference(wristSetpoint, ControlType.kMAXMotionPositionControl);

    }

    public double getWristSetPoint(){
        return wristSetpoint;
    }


    public void closedLoopWrist() {
        // m_FulcrumRight.set(m_Controller.calculate(e_FulcrumEncoder.getPosition(),
        // setPoint));
        
        double ff = getPositionFeedForward();
        double sp = getWristSetPoint();
        wristController.setReference(sp, ControlType.kPosition, ClosedLoopSlot.kSlot0, ff);
    }


    public boolean isAtAngle() {
        double error = getPosition() - wristSetpoint;
        return (Math.abs(error) < allowableError);
      }


    public void setWristStow(){
        setWristSetpoint(0.88);
      }
    
      public void setWristCoralIntake(){
        setWristSetpoint(0.88);
      }
    
      public void setWristCoralL1Dump(){
        setWristSetpoint(0.69);
      }
    
      public void setWristCoralL1(){
        setWristSetpoint(0.69);
      }
    
      public void setWristCoralL2(){
        setWristSetpoint(0.79); // other option .72
      }
    
      public void setWristCoralL3(){
        setWristSetpoint(0.73);
      }
    
      public void setWristCoralL4(){
        setWristSetpoint(0.69);
      }
    
      public void setWristAlgaeL2(){
        setWristSetpoint(0.75);
      }
    
      public void setWristAlgaeL3(){
        setWristSetpoint(0.74);
      }


 }

