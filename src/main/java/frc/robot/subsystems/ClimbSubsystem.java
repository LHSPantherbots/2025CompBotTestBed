package frc.robot.subsystems;


import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimbConstants;



public class ClimbSubsystem extends SubsystemBase{
    private  SparkMax m_Climb; 
    private  SparkMaxConfig c_Climb = new SparkMaxConfig();
    //private RelativeEncoderConfig 
    private RelativeEncoder c_Encoder = m_Climb.getEncoder();
    

    

    public ClimbSubsystem() {      
        m_Climb = new SparkMax(ClimbConstants.kClimb, MotorType.kBrushless);
        



        c_Climb
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(30)
                .inverted(false);
        c_Climb.softLimit
            .forwardSoftLimit(50)
            .forwardSoftLimitEnabled(false)
            .reverseSoftLimit(0.0)
            .reverseSoftLimitEnabled(false);
                
                
        c_Climb.encoder
                
                .positionConversionFactor(1.0) // meters
                .velocityConversionFactor(1.0);//meters/sec
        
        m_Climb.configure(c_Climb, ResetMode.kResetSafeParameters,
                    PersistMode.kPersistParameters);
        
        
    }
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Climber Position", c_Encoder.getPosition());
    
    }

     public void stop() {
        m_Climb.set(0.0);
    }

     public double getPosition() {
        return c_Encoder.getPosition();
    }

   

     public void manualClimbMove(double move) {
        m_Climb.set(move);
    }

    
 }

