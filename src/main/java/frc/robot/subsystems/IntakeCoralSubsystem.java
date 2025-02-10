package frc.robot.subsystems;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeCoralConstants;

public class IntakeCoralSubsystem extends SubsystemBase{
  private final SparkMax m_IntakeCoral;
  private final SparkMaxConfig c_IntakeCoral;

    public IntakeCoralSubsystem() {  
      m_IntakeCoral = new SparkMax(IntakeCoralConstants.kIntakeCoral, MotorType.kBrushless);
      c_IntakeCoral = new SparkMaxConfig();  

      c_IntakeCoral
            .inverted(false)
            .idleMode(IdleMode.kBrake)
            .smartCurrentLimit(30);

      m_IntakeCoral.configure(c_IntakeCoral, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);


    }
    @Override
    public void periodic() {
      
    }
  

     public void intake() {
      m_IntakeCoral.set(-.7); //test this
    }

    public void outtake() {
      m_IntakeCoral.set(.7); //test this
    }

    public void intakeStop() {
      m_IntakeCoral.set(0);
    }
  
    public double getCurrent() {
      return m_IntakeCoral.getOutputCurrent();
    }

  
   
  
 }
