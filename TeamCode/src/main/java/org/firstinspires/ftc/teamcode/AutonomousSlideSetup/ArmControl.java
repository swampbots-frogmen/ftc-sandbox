package org.firstinspires.ftc.teamcode.AutonomousSlideSetup;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class ArmControl extends LinearOpMode {
    DcMotor slide = null;
    DcMotor rotateMotor = null;
    DcMotor slide2 = null;
    Servo armPivot = null;
    static boolean FinishedBasketArmPos = false;

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
    }

    public static boolean ArmBasketPosIsDone() {
        return FinishedBasketArmPos;
    }

    public void DoArmPosition(DcMotor slide, DcMotor slide2, DcMotor rotateMotor, Servo claw, double rotateLimit, double slideLimit, double limitCodeRotate, double minCodeRotate, double limitCodeSlide, double minCodeSlide, double armPosition) {
        FinishedBasketArmPos = false;

        // Setup motor directions and behaviors
        slide.setDirection(DcMotorSimple.Direction.REVERSE);
        slide2.setDirection(DcMotorSimple.Direction.FORWARD);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Rotate motor control logic
        handleRotateMotor(rotateMotor, minCodeRotate, limitCodeRotate);

        // Slide motor control logic
        handleSlideMotor(slide, slide2, minCodeSlide, limitCodeSlide);

        // Check if basket arm position is done
        if (slide.getCurrentPosition() <= limitCodeSlide && slide.getCurrentPosition() >= minCodeSlide &&
                rotateMotor.getCurrentPosition() >= limitCodeRotate && rotateMotor.getCurrentPosition() <= minCodeRotate) {
            FinishedBasketArmPos = true;
            stopMotors(slide, slide2, rotateMotor);
        }
    }

    private void handleRotateMotor(DcMotor rotateMotor, double minCodeRotate, double limitCodeRotate) {
        if (rotateMotor.getCurrentPosition() > minCodeRotate) {
            rotateMotor.setPower(-1);
        } else if (rotateMotor.getCurrentPosition() < limitCodeRotate) {
            rotateMotor.setPower(1);
        } else {
            rotateMotor.setPower(0);
        }
    }

    private void handleSlideMotor(DcMotor slide, DcMotor slide2, double minCodeSlide, double limitCodeSlide) {
        if (slide.getCurrentPosition() < minCodeSlide) {
            slide.setPower(1);
            slide2.setPower(1);
        } else if (slide.getCurrentPosition() > limitCodeSlide) {
            slide.setPower(-1);
            slide2.setPower(-1);
        } else {
            slide.setPower(0);
            slide2.setPower(0);
        }
    }

    private void stopMotors(DcMotor slide, DcMotor slide2, DcMotor rotateMotor) {
        slide.setPower(0);
        slide2.setPower(0);
        rotateMotor.setPower(0);
    }

    public void DoBasketArmPos(DcMotor slide, DcMotor slide2, DcMotor rotateMotor, Servo claw) {
        double rotateLimit = -9700;
        double slideLimit = 11500;
        double limitCodeRotate = (rotateLimit * 8) / 9;
        double minCodeRotate = (rotateLimit * 6) / 7;
        double limitCodeSlide = slideLimit;
        double minCodeSlide = (slideLimit * 40) / 41;
        double armPosition = 0.37;

        DoArmPosition(slide, slide2, rotateMotor, claw, rotateLimit, slideLimit, limitCodeRotate, minCodeRotate, limitCodeSlide, minCodeSlide, armPosition);
    }

    public void DoFirstLevelAscentArmPos(DcMotor slide, DcMotor slide2, DcMotor rotateMotor, Servo claw) {
        double rotateLimit = -9700;
        double slideLimit = 12000;
        double limitCodeRotate = (rotateLimit * 4) / 5;
        double minCodeRotate = (rotateLimit * 5) / 7;
        double limitCodeSlide = (slideLimit * 22) / 41;
        double minCodeSlide = (slideLimit * 20) / 41;
        double armPosition = 0.37;

        DoArmPosition(slide, slide2, rotateMotor, claw, rotateLimit, slideLimit, limitCodeRotate, minCodeRotate, limitCodeSlide, minCodeSlide, armPosition);
    }

    public void DoAutoBasketArmPos(DcMotor slide, DcMotor slide2, DcMotor rotateMotor, Servo claw) {
        double rotateLimit = -9700;
        double slideLimit = 12000;
        double limitCodeRotate = (rotateLimit * 4) / 5;
        double minCodeRotate = (rotateLimit * 5) / 7;
        double limitCodeSlide = (slideLimit * 22) / 41;
        double minCodeSlide = (slideLimit * 20) / 41;
        double armPosition = 0.37;

        DoArmPosition(slide, slide2, rotateMotor, claw, rotateLimit, slideLimit, limitCodeRotate, minCodeRotate, limitCodeSlide, minCodeSlide, armPosition);
    }

    public void DoAutoGrabArmPos(DcMotor slide, DcMotor slide2, DcMotor rotateMotor, Servo claw) {
        double rotateLimit = -9700;
        double slideLimit = 12000;
        double limitCodeRotate = (rotateLimit * -6) / 125;
        double minCodeRotate = (rotateLimit * -10) / 125;
        double limitCodeSlide = (slideLimit * 4) / 5;
        double minCodeSlide = (slideLimit * 3) / 4;
        double armPosition = 0.37;

        DoArmPosition(slide, slide2, rotateMotor, claw, rotateLimit, slideLimit, limitCodeRotate, minCodeRotate, limitCodeSlide, minCodeSlide, armPosition);
    }

    public void DoAutoBasketGrabArmPos(DcMotor slide, DcMotor slide2, DcMotor rotateMotor, Servo claw) {
        double rotateLimit = -9700;
        double slideLimit = 12000;
        double limitCodeRotate = (rotateLimit*-1150)/9700;
        double minCodeRotate = (rotateLimit*-16)/97;//limit
        double limitCodeSlide = (slideLimit*1)/2;//2/3
        double minCodeSlide = (slideLimit*43)/120;//1/2
        double armPosition = 0.37;

        DoArmPosition(slide, slide2, rotateMotor, claw, rotateLimit, slideLimit, limitCodeRotate, minCodeRotate, limitCodeSlide, minCodeSlide, armPosition);
    }

    public void DoInitialBarArmPos(DcMotor slide, DcMotor slide2, DcMotor rotateMotor, Servo claw) {
        double rotateLimit = -9700;
        double slideLimit = 12000;
        double limitCodeRotate = (rotateLimit * 4) / 5;
        double minCodeRotate = (rotateLimit * 5) / 7;
        double rotateHighEnough = 250;
        double limitCodeSlide = (slideLimit * 22) / 41;
        double minCodeSlide = (slideLimit * 20) / 41;
        double armPosition = 0.37;

        DoArmPosition(slide, slide2, rotateMotor, claw, rotateLimit, slideLimit, limitCodeRotate, minCodeRotate, limitCodeSlide, minCodeSlide, armPosition);
    }

    public void DoInitialBarDownArmPos(DcMotor slide, DcMotor slide2, DcMotor rotateMotor, Servo claw) {
        double rotateLimit = -9700;
        double slideLimit = 12000;
        double limitCodeRotate = (rotateLimit * 3) / 5;
        double minCodeRotate = (rotateLimit * 1) / 3;
        double rotateHighEnough = 10;
        double limitCodeSlide = (slideLimit * 36) / 120;
        double minCodeSlide = (slideLimit * 35) / 120;
        double armPosition = 0.37;

        DoArmPosition(slide, slide2, rotateMotor, claw, rotateLimit, slideLimit, limitCodeRotate, minCodeRotate, limitCodeSlide, minCodeSlide, armPosition);
    }
}
