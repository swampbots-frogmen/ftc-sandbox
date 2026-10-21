/*
Oversimplified by ChatGPT
 */
package org.firstinspires.ftc.teamcode.AutonomousSlideSetup;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class SetupPosChatGpt extends LinearOpMode {
    private DcMotor slide = null;
    private DcMotor rotateMotor = null;
    private DcMotor slide2 = null;
    private Servo armPivot = null;
    private static boolean FinishedBasketArmPos = false;

    // Constants
    private static final double ROTATE_LIMIT = -12500;
    private static final double SLIDE_LIMIT = 12000;
    private static final double ARM_POSITION = 0.37;

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
    }

    public static boolean ArmBasketPosIsDone() {
        return FinishedBasketArmPos;
    }

    private void configureMotors(DcMotor slide, DcMotor slide2) {
        slide.setDirection(DcMotorSimple.Direction.REVERSE);
        slide2.setDirection(DcMotorSimple.Direction.FORWARD);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private void setMotorPower(DcMotor motor, double power) {
        motor.setPower(power);
    }

    private boolean isWithinLimits(int currentPos, double lowerLimit, double upperLimit) {
        return currentPos >= lowerLimit && currentPos <= upperLimit;
    }

    public void moveArmToPosition(double rotateLimitFactor, double slideLimitFactor, double minRotateFactor, double minSlideFactor) {
        FinishedBasketArmPos = false;

        configureMotors(slide, slide2);

        double rotateLimit = ROTATE_LIMIT * rotateLimitFactor;
        double slideLimit = SLIDE_LIMIT * slideLimitFactor;
        double minRotateLimit = ROTATE_LIMIT * minRotateFactor;
        double minSlideLimit = SLIDE_LIMIT * minSlideFactor;

        while (!(isWithinLimits(slide.getCurrentPosition(), minSlideLimit, slideLimit) &&
                isWithinLimits(rotateMotor.getCurrentPosition(), rotateLimit, minRotateLimit))) {

            if (rotateMotor.getCurrentPosition() > minRotateLimit) {
                setMotorPower(rotateMotor, -1);
            } else if (rotateMotor.getCurrentPosition() < rotateLimit) {
                setMotorPower(rotateMotor, 1);
            } else {
                setMotorPower(rotateMotor, 0);
            }

            if (slide.getCurrentPosition() < minSlideLimit) {
                setMotorPower(slide, 1);
                setMotorPower(slide2, 1);
            } else if (slide.getCurrentPosition() > slideLimit) {
                setMotorPower(slide, -1);
                setMotorPower(slide2, -1);
            } else {
                setMotorPower(slide, 0);
                setMotorPower(slide2, 0);
            }
        }

        FinishedBasketArmPos = true;
        setMotorPower(slide, 0);
        setMotorPower(slide2, 0);
        setMotorPower(rotateMotor, 0);
    }

    public void DoBasketArmPos() {
        moveArmToPosition(8.0 / 9, 1.0, 6.0 / 7, 40.0 / 41);
    }

    public void DoFirstLevelAscentArmPos() {
        moveArmToPosition(4.0 / 5, 22.0 / 41, 5.0 / 7, 20.0 / 41);
    }

    public void DoAutoBasketArmPos() {
        moveArmToPosition(4.0 / 5, 22.0 / 41, 5.0 / 7, 20.0 / 41);
    }

    public void DoAutoGrabArmPos() {
        moveArmToPosition(-10.0 / 125, 4.0 / 5, -15.0 / 125, 3.0 / 4);
    }

    public void DoInitialBarArmPos() {
        moveArmToPosition(4.0 / 5, 22.0 / 41, 5.0 / 7, 20.0 / 41);
    }
  public void DoInitialBarDownArmPos(){
      moveArmToPosition(3.0 / 5, 36.0 / 120, 1.0 / 3, 35.0 / 120);
  }
}
