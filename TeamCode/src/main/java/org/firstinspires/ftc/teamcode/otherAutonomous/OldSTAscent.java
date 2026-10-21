package org.firstinspires.ftc.teamcode.otherAutonomous;
/*
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.AutonomousSeperate.AutoPark;
import org.firstinspires.ftc.teamcode.AutonomousSeperate.GrabSample;
import org.firstinspires.ftc.teamcode.AutonomousSlideSetup.SetupPos;


/**
 * Team 9960 Revision 170119.0
 * This program is the scaffold for autonomous operation.
 * Designed to push the correct button on both beacons
 *
 * This robot uses four VEX Mecanum wheels, each direct driven by Neverest 40 motors.
 * It is designed as a linear op mode, and uses RUN_TO_POSITION motor operation.
 *
 */
@Autonomous()  // @TeleOp(...) is the other common choice

public class OldSTAscent extends LinearOpMode {//T = triangle Q = quadrilateral S = side
    // Declare Devices
    DcMotor leftFrontMotor = null;
    DcMotor rightFrontMotor = null;
    DcMotor leftRearMotor = null;
    DcMotor rightRearMotor = null;
    Servo claw = null;    //1 is shoot out, 0 is take in, 0.5 is neutral
    DcMotor slide = null;
    //Servo push = null;  //0.1 is facing down and forward, 0.4 is straight, 0.75 is facing backward and up
    DcMotor rotateMotor = null;
    DcMotor slide2 = null;

    // drive motor position variables
    private int lfPos;
    private int rfPos;
    private int lrPos;
    private int rrPos;
    private int sPos;
    private int rPos;

    // operational constants
    private double fast = 0.5; // Limit motor power to this value for Andymark RUN_USING_ENCODER mode
    private double medium = 0.3; // medium speed
    private double slow = 0.1; // slow speed
    private double clicksPerInch = 67.3; // empirically measured
    private double clicksPerDeg = 4.9027777777777777777777777777778; // empirically measured
    SetupPos setupPos = new SetupPos();
    AutoPark autoPark = new AutoPark();
    GrabSample grabSample = new GrabSample();
    // DcMotorEx rightRearMotor,leftRearMotor,leftFrontMotor,rightFrontMotor;


    @Override
    public void runOpMode() {
        telemetry.setAutoClear(true);


        // Initialize the hardware variables.
        leftFrontMotor = hardwareMap.dcMotor.get("leftFront");
        rightFrontMotor = hardwareMap.dcMotor.get("rightFront");
        leftRearMotor = hardwareMap.dcMotor.get("leftRear");
        rightRearMotor = hardwareMap.dcMotor.get("rightRear");
        claw = hardwareMap.get(Servo.class, "claw");
        slide = hardwareMap.dcMotor.get("slide");
        // push = hardwareMap.get(Servo.class, "push");
        rotateMotor = hardwareMap.dcMotor.get("rotateMotor");
        slide2 = hardwareMap.dcMotor.get("slide2");

        // The right motors need reversing
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        rightRearMotor.setDirection(DcMotor.Direction.FORWARD);
        leftRearMotor.setDirection(DcMotor.Direction.REVERSE);
        slide.setDirection(DcMotorSimple.Direction.REVERSE);
        slide2.setDirection(DcMotorSimple.Direction.FORWARD);



        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        // Set the drive motor run modes:
        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRearMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRearMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        double defaultArmPos = 0.85;
        double armPivotPosForBasket = 0.25;
        double intakeTakeInValue = 0;
        double intakePushOutValue = 0.6;
        double intakeNeutralValue = 0.5;
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        if (!opModeIsActive()) return;
        // *****************Dead reckoning list*************
        // Distances in inches, angles in deg, speed 0.0 to 0.6
        //each tile is 24x24
        grabSample.GrabSampleCode(4,leftFrontMotor,rightFrontMotor,leftRearMotor,rightRearMotor,slide,slide2,rotateMotor,claw);
       sleep(50);
        claw.setPosition(1);
        sleep(100);
        grabSample.GrabSampleCode(1,leftFrontMotor,rightFrontMotor,leftRearMotor,rightRearMotor,slide,slide2,rotateMotor,claw);
       sleep(100);
        claw.setPosition(1);
        sleep(100);
     //   grabSample.GrabSampleCode(2,leftFrontMotor,rightFrontMotor,leftRearMotor,rightRearMotor,slide,slide2,rotateMotor,claw);
        sleep(100);
        claw.setPosition(1);
        sleep(100);
     //    autoPark.AutoParkCode(true,leftFrontMotor,rightFrontMotor,leftRearMotor,rightRearMotor,slide,slide2,rotateMotor,claw);


        // grabSample.GrabSampleCode(2,leftFrontMotor,rightFrontMotor,leftRearMotor,rightRearMotor,slide,slide2,rotateMotor,claw);
        //  grabSample.GrabSampleCode(2,leftFrontMotor,rightFrontMotor,leftRearMotor,rightRearMotor,slide,slide2,rotateMotor,claw);
        //  grabSample.GrabSampleCode(3,leftFrontMotor,rightFrontMotor,leftRearMotor,rightRearMotor,slide,slide2,rotateMotor,claw);

        //autoPark.AutoParkCode(true,leftFrontMotor,rightFrontMotor,leftRearMotor,rightRearMotor,slide,slide2,rotateMotor, claw);

    }

    private void moveForward(int howMuch, double speed, boolean moveArmUp) {
        // howMuch is in inches. A negative howMuch moves backward.

        // fetch motor positions
        lfPos = leftFrontMotor.getCurrentPosition();
        rfPos = rightFrontMotor.getCurrentPosition();
        lrPos = leftRearMotor.getCurrentPosition();
        rrPos = rightRearMotor.getCurrentPosition();

        // calculate new targets
        lfPos += howMuch * clicksPerInch;
        rfPos += howMuch * clicksPerInch;
        lrPos += howMuch * clicksPerInch;
        rrPos += howMuch * clicksPerInch;

        // move robot to new position
        leftFrontMotor.setTargetPosition(lfPos);
        rightFrontMotor.setTargetPosition(rfPos);
        leftRearMotor.setTargetPosition(lrPos);
        rightRearMotor.setTargetPosition(rrPos);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFrontMotor.setPower(speed);
        rightFrontMotor.setPower(speed);
        leftRearMotor.setPower(speed);
        rightRearMotor.setPower(speed);

        // wait for move to complete
        while (leftFrontMotor.isBusy() && rightFrontMotor.isBusy() &&
                leftRearMotor.isBusy() && rightRearMotor.isBusy()) {
            if (!opModeIsActive()) return;

            if(moveArmUp == true){
                if(rotateMotor.getCurrentPosition() < 17000){
                    rotateMotor.setPower(1);
                }
                if (rotateMotor.getCurrentPosition() >= 17000){
                    rotateMotor.setPower(0);
                }
                if(rotateMotor.getCurrentPosition() > 5000 && slide.getCurrentPosition() < 25000) {
                    slide.setPower(1);
                    slide2.setPower(1);
                }
                if(slide.getCurrentPosition() >= 28000){
                    slide.setPower(0);
                    slide2.setPower(0);
                }
            }
            // Display it for the driver.
            telemetry.addLine("Move Forward");
            telemetry.addData("Target", "%7d :%7d", lfPos, rfPos, lrPos, rrPos);
            telemetry.addData("LF",leftFrontMotor.getCurrentPosition());
            telemetry.addData("RF",rightFrontMotor.getCurrentPosition());
            telemetry.addData("LR",leftRearMotor.getCurrentPosition());
            telemetry.addData("RR",rightRearMotor.getCurrentPosition());
            telemetry.addData("LFSpeed:", leftFrontMotor.getPower());
            telemetry.addData("LRSpeed:", leftRearMotor.getPower());
            telemetry.addData("RFSpeed:", rightFrontMotor.getPower());
            telemetry.addData("RRSpeed:", rightRearMotor.getPower());

            telemetry.update();
        }

        // Stop all motion;
        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftRearMotor.setPower(0);
        rightRearMotor.setPower(0);
        if(moveArmUp == true){
            rotateMotor.setPower(0);
            slide.setPower(0);
        }
    }

    private void moveRight(int howMuch, double speed, boolean moveArmUp) {
        // howMuch is in inches. A negative howMuch moves backward.

        // fetch motor positions
        lfPos = leftFrontMotor.getCurrentPosition();
        rfPos = rightFrontMotor.getCurrentPosition();
        lrPos = leftRearMotor.getCurrentPosition();
        rrPos = rightRearMotor.getCurrentPosition();

        // calculate new targets
        lfPos += howMuch * clicksPerInch;
        rfPos -= howMuch * clicksPerInch;
        lrPos -= howMuch * clicksPerInch;
        rrPos += howMuch * clicksPerInch;

        // move robot to new position
        leftFrontMotor.setTargetPosition(lfPos);
        rightFrontMotor.setTargetPosition(rfPos);
        leftRearMotor.setTargetPosition(lrPos);
        rightRearMotor.setTargetPosition(rrPos);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFrontMotor.setPower(speed);
        rightFrontMotor.setPower(speed);
        leftRearMotor.setPower(speed);
        rightRearMotor.setPower(speed);

        // wait for move to complete
        while (leftFrontMotor.isBusy() && rightFrontMotor.isBusy() &&
                leftRearMotor.isBusy() && rightRearMotor.isBusy()) {
            if (!opModeIsActive()) return;

            if(moveArmUp == true){
                if(rotateMotor.getCurrentPosition() < 17000){
                    rotateMotor.setPower(1);
                }
                if (rotateMotor.getCurrentPosition() >= 17000){
                    rotateMotor.setPower(0);
                }
                if(rotateMotor.getCurrentPosition() > 5000 && slide.getCurrentPosition() < 25000) {
                    slide.setPower(1);
                    slide2.setPower(1);
                }
                if(slide.getCurrentPosition() >= 28000){
                    slide.setPower(0);
                    slide2.setPower(0);
                }
            }
            // Display it for the driver.
            telemetry.addLine("Strafe Right");
            telemetry.addData("Target", "%7d :%7d", lfPos, rfPos, lrPos, rrPos);
            telemetry.addData("Actual", "%7d :%7d", leftFrontMotor.getCurrentPosition(),
                    rightFrontMotor.getCurrentPosition(), leftRearMotor.getCurrentPosition(),
                    rightRearMotor.getCurrentPosition());
            telemetry.addData("LFSpeed:", leftFrontMotor.getPower());
            telemetry.addData("LRSpeed:", leftRearMotor.getPower());
            telemetry.addData("RFSpeed:", rightFrontMotor.getPower());
            telemetry.addData("RRSpeed:", rightRearMotor.getPower());
            telemetry.update();
        }

        // Stop all motion;
        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftRearMotor.setPower(0);
        rightRearMotor.setPower(0);
        if(moveArmUp == true){
            rotateMotor.setPower(0);
            slide.setPower(0);
        }

    }

    private void turnClockwise(int whatAngle, double speed) {
        // whatAngle is in degrees. A negative whatAngle turns counterclockwise.

        // fetch motor positions
        lfPos = leftFrontMotor.getCurrentPosition();
        rfPos = rightFrontMotor.getCurrentPosition();
        lrPos = leftRearMotor.getCurrentPosition();
        rrPos = rightRearMotor.getCurrentPosition();

        // calculate new targets
        lfPos += whatAngle * clicksPerDeg;
        rfPos -= whatAngle * clicksPerDeg;
        lrPos += whatAngle * clicksPerDeg;
        rrPos -= whatAngle * clicksPerDeg;

        // move robot to new position
        leftFrontMotor.setTargetPosition(lfPos);
        rightFrontMotor.setTargetPosition(rfPos);
        leftRearMotor.setTargetPosition(lrPos);
        rightRearMotor.setTargetPosition(rrPos);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFrontMotor.setPower(speed);
        rightFrontMotor.setPower(speed);
        leftRearMotor.setPower(speed);
        rightRearMotor.setPower(speed);

        // wait for move to complete
        while (leftFrontMotor.isBusy() && rightFrontMotor.isBusy() &&
                leftRearMotor.isBusy() && rightRearMotor.isBusy()) {
            if (!opModeIsActive()) return;

            // Display it for the driver.
            telemetry.addLine("Turn Clockwise");
            telemetry.addData("Target", "%7d :%7d", lfPos, rfPos, lrPos, rrPos);
            telemetry.addData("Actual", "%7d :%7d", leftFrontMotor.getCurrentPosition(),
                    rightFrontMotor.getCurrentPosition(), leftRearMotor.getCurrentPosition(),
                    rightRearMotor.getCurrentPosition());
            telemetry.update();
        }

        // Stop all motion;
        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftRearMotor.setPower(0);
        rightRearMotor.setPower(0);



    }/*
    private void ninetyDegreeRotate(boolean goingUp){
        if (goingUp == true) {

            if (rotateMotor.getCurrentPosition() < 17000) {
                rotateMotor.setPower(1);
            }

        }
        if (goingUp == false) {
            if (rotateMotor.getCurrentPosition() > 1) {
                rotateMotor.setPower(-1);
            }
        }
        while (rotateMotor.getPower() > 0.1 || rotateMotor.getPower() < -0.1){
            telemetry.addData("Rotate motor pos",rotateMotor.getCurrentPosition());
            telemetry.update();
            if(rotateMotor.getCurrentPosition() >= 17000 && goingUp == true){
                rotateMotor.setPower(0);
            }else if(rotateMotor.getCurrentPosition() <= 0 && goingUp == false){
                rotateMotor.setPower(0);
            }
        }

    }


    private void extendSlide(boolean extend){
        if(extend == true){
            if(slide.getCurrentPosition() < 25000){
                slide.setPower(1);
                slide2.setPower(1);
            }
        }else if(extend == false){
            if(slide.getCurrentPosition() > 100){
                slide.setPower(-1);
                slide2.setPower(-1);
            }
        }
        while (slide.getPower() > 0.1 || slide.getPower() < -0.1){
            telemetry.addData("Slide Pos:", slide.getCurrentPosition());
            telemetry.update();
            if(slide.getCurrentPosition() >= 28000 && extend == true){
                slide.setPower(0);
                slide2.setPower(0);
            }else if(slide.getCurrentPosition() <= 100 && extend == false){
                slide.setPower(0);
                slide2.setPower(0);
            }
        }

    }*/
}