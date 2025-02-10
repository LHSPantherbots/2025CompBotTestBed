package frc.robot.subsystems;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeAlgaeConstants;

public class IntakeAlgaeSubsystem extends SubsystemBase{
  private final SparkMax m_IntakeAlgae;
  private final SparkMaxConfig c_IntakeAlgae;

    public IntakeAlgaeSubsystem() {  
      m_IntakeAlgae = new SparkMax(IntakeAlgaeConstants.kIntakeAlgae, MotorType.kBrushless);
      c_IntakeAlgae = new SparkMaxConfig();  

      c_IntakeAlgae
            .inverted(false)
            .idleMode(IdleMode.kBrake)
            .smartCurrentLimit(30);

      m_IntakeAlgae.configure(c_IntakeAlgae, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);


    }
    @Override
    public void periodic() {
      
    }
  
     

     public void intake() {
      m_IntakeAlgae.set(-.7); //test this
    }

    public void outtake() {
      m_IntakeAlgae.set(.7); //test this
    }

    public void intakeStop() {
      m_IntakeAlgae.set(0);
    }
  
    public double getCurrent() {
      return m_IntakeAlgae.getOutputCurrent();
    }

  
   
  
 }
