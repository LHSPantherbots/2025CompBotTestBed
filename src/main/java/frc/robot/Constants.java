// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
    public static final int kOperatorControllerPort = 1;
    public static final double kDriveDeadband = 0.1;
  }
  public static final class IntakeCoralConstants{
    public static final int kIntakeCoral = 51;
  }

  public static final class IntakeAlgaeConstants{
    public static final int kIntakeAlgae = 52;
  }

  public static final class WristConstants{
    public static final int kWrist = 50;
  }

  public static final class ClimbConstants{
    public static final int kClimb = 53;
  }

  public static final class LEDs{
    public static final int purple_Red = 148;
    public static final int purple_Green = 0;
    public static final int purple_Blue = 211;
    public static final int purple_Hue = 282;
    public static final int purple_Sat = 100;
    public static final int purple_Val = 83;

    public static final int yellow_Red = 255;
    public static final int yellow_Green = 90;
    public static final int yellow_Blue = 0;
    public static final int yellow_Hue = 21;
    public static final int yellow_Sat = 100;
    public static final int yellow_Val = 100;

    public static final int orange_Red = 251;
    public static final int orange_Green = 51;
    public static final int orange_Blue = 0;
    public static final int orange_Hue = 12;
    public static final int orange_Sat = 100;
    public static final int orange_Val = 98;

    public static final int red_Hue = 0;
    public static final int red_Sat = 100;
    public static final int red_Val = 100;

    public static final int blue_Hue = 240;
    public static final int blue_Sat = 100;
    public static final int blue_Val = 100;

    public static final int green_Hue = 240;
    public static final int green_Sat = 100;
    public static final int green_Val = 100;

    public static final int Red = 255;
    public static final int Green = 255;
    public static final int Blue = 255;
    public static final int white_Hue = 0;
    public static final int white_Sat = 0;
    public static final int white_Val = 100;
  }

}
