package org.firstinspires.ftc.teamcode.AutonomousSeperate;
/*
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.AutonomousSlideSetup.ArmControl;
import org.firstinspires.ftc.teamcode.AutonomousSlideSetup.SetupPos;
import org.opencv.core.Mat;


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

public class GrabSample extends LinearOpMode implements Runnable {//T = triangle Q = quadrilateral S = side
    // Declare Devices
    DcMotor leftFrontMotor = null;
    DcMotor rightFrontMotor = null;
    DcMotor leftRearMotor = null;
    DcMotor rightRearMotor = null;
    Servo claw = null;    //1 is shoot out, 0 is take in, 0.5 is neutral
    DcMotor slide = null;
    Servo armPivot = null;  //0.1 is facing down and forward, 0.4 is straight, 0.75 is facing backward and up
    DcMotor rotateMotor = null;
    DcMotor slide2 = null;

    Thread thread1;
    Thread thread2;


Thread thread;
    // drive motor position variables
    private int lfPos;
    private int rfPos;
    private int lrPos;
    private int rrPos;
    private int untilFinished;
    private int absolutePos;
    private int sPos;
    private int rPos;

    double defaultArmPos = 0.85;

    // operational constants
    private final double fast = 0.5; // Limit motor power to this value for Andymark RUN_USING_ENCODER mode
    private final double medium = 0.3; // medium speed
    private final double slow = 0.1; // slow speed
    private final double clicksPerInch = 23; // empirically measured
    private final double clicksPerDeg = 4.8; // empirically measured
    SetupPos setupPos = new SetupPos();
    ArmControl armControl = new ArmControl();
    // DcMotorEx rightRearMotor,leftRearMotor,leftFrontMotor,rightFrontMotor;

    @Override
    public void runOpMode() {
        telemetry.setAutoClear(true);
      /*  leftFrontMotor = hardwareMap.dcMotor.get("leftFront");
        rightFrontMotor = hardwareMap.dcMotor.get("rightFront");
        leftRearMotor = hardwareMap.dcMotor.get("leftRear");
        rightRearMotor = hardwareMap.dcMotor.get("rightRear");
        claw = hardwareMap.get(Servo.class, "claw");
        slide = hardwareMap.dcMotor.get("slide");
        armPivot = hardwareMap.get(Servo.class, "armPivot");
        rotateMotor = hardwareMap.dcMotor.get("rotateMotor");
        slide2 = hardwareMap.dcMotor.get("slide2");*/

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

      //  GrabSampleCode(1,leftFrontMotor,rightFrontMotor,leftRearMotor,rightRearMotor,slide,slide2,rotateMotor,armPivot,intake);

    }
    public void GrabSampleCode(int Sample, DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, DcMotor firstSlide, DcMotor secondSlide, DcMotor rotate, Servo cl){


        leftFrontMotor = frontLeft;
        rightFrontMotor = frontRight;
        leftRearMotor = backLeft;
        rightRearMotor = backRight;
        slide = firstSlide;
        //  armPivot = armP;
        claw = cl;
        rotateMotor = rotate;
        slide2 = secondSlide;

//0 = initial bar, 1 = basket, 2 = grabpos, 3 = stop
        if(Sample == 0) {//There aint no way this still works 💀
            claw.setPosition(0);
           /* moveForward(5, 0.15,0);
            setupPos.DoInitialBarArmPos();
            sleep(500);
            moveForward(10, 0.15,0);//16*/
            moveForward(24,0.025,0);
            setupPos.DoInitialBarArmPos(slide,slide2,rotateMotor,claw);
            sleep(250);
            setupPos.DoInitialBarDownArmPos(slide,slide2,rotateMotor,claw);
            claw.setPosition(1);
            sleep(250);
            moveForward(-15,medium,2);
            sleep(250);

        }
        if (Sample == 1) {
            claw.setPosition(1);
      /*      moveForward(-4,0.15,3);
        moveForward(-9,0.3, 4);
        moveForward(-4,0.075,4);*/
            moveForward(-13,0.25,3);
        /*turnClockwise(10,0.15,4);
        turnClockwise(85,0.25,4);
        turnClockwise(25,0.075,4);*/
            turnClockwise(128,0.1,4);
            claw.setPosition(0.5);
       /* moveForward(-2,0.15,4);
        moveForward(-2,0.3,4);
        moveForward(-2,0.075,4);*/
            moveForward(-3,0.1,4);
        setupPos.DoAutoBasketGrabArmPos(slide,slide2,rotateMotor,claw);
        claw.setPosition(0);
        sleep(700);
        claw.setPosition(0);
        sleep(100);
        rotateMotor.setPower(-0.8);
        sleep(250);
        rotateMotor.setPower(0);
        /*moveForward(2,0.15,1);
        moveForward(2,0.25,1);
        moveForward(2,0.75,1);*/
      /*  turnClockwise(-10,0.15,1);
        turnClockwise(-108,0.25,1);
        turnClockwise(-20,0.075,1);*/
            turnClockwise(232,0.09,1);
        /*moveForward(4,0.15,1);
        moveForward(9,0.25,1);
        moveForward(4,0.075,1);*/
            moveForward(10,0.1,1);
        setupPos.DoBasketArmPos(slide,slide2,rotateMotor,claw);
        claw.setPosition(1);
        sleep(100);
        }
        if (Sample == 2) {
            moveForward(-10,0.3, 4);
            moveForward(-4,0.075,4);
            turnClockwise(138,slow,4);
            moveForward(2,slow,4);
            setupPos.DoAutoBasketGrabArmPos(slide,slide2,rotateMotor,claw);
            claw.setPosition(0);
            setupPos.DoBasketArmPos(slide,slide2,rotateMotor,claw);
            moveForward(-2,slow,4);
            turnClockwise(-138,slow,1);
            moveForward(10,0.3,1);
            moveForward(4,0.075,1);
            claw.setPosition(1);
        }
        if (Sample == 3) {
            turnClockwise(-45, medium,3);
            //lower claw arm, grab sample
            setupPos.DoAutoGrabArmPos(slide,slide2,rotateMotor,claw);
            turnClockwise(45, medium,3);
            setupPos.DoAutoBasketArmPos(slide,slide2,rotateMotor,claw);
            //basket
        }

        if (Sample == 4) {//0 = initial bar, 1 = basket, 2 = grabpos, 3 = stop
          //   Task 1: Move to Basket Position
          /*  moveForward(2,0.15,1);
            moveForward(16, 0.25,1);
            moveForward(4,0.075,1);*/
            moveForward(18,0.15,1);
         //    Task 2: Perform Movements Sequentially
          /*  turnClockwise(-10,0.1,1);
            turnClockwise(-95, 0.2,1);
            turnClockwise(-45,0.1,1);*/
            turnClockwise(-135,0.1,1);
            sleep(250);
           /* moveForward(2,0.15,1);
            moveForward(18, 0.25,1);
            moveForward(10,0.1,1);*/
            moveForward(21,0.05,1);
            setupPos.DoBasketArmPos(slide,slide2,rotateMotor,claw);
            sleep(50);
            claw.setPosition(1);
            sleep(100);

        }



    }

  private void moveForward(int howMuch, double speed, int armPos) {
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

      absolutePos = lfPos;//end
      double startPos = leftFrontMotor.getCurrentPosition();
      double distance = absolutePos - startPos;

      // move robot to new position
      leftFrontMotor.setTargetPosition(lfPos);
      rightFrontMotor.setTargetPosition(rfPos);
      leftRearMotor.setTargetPosition(lrPos);
      rightRearMotor.setTargetPosition(rrPos);
      leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      leftRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      rightRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      leftFrontMotor.setPower(0.075);
      rightFrontMotor.setPower(0.075);
      leftRearMotor.setPower(0.075);
      rightRearMotor.setPower(0.075);

      // wait for move to complete
      while (leftFrontMotor.isBusy() && rightFrontMotor.isBusy() &&
              leftRearMotor.isBusy() && rightRearMotor.isBusy()) {//adjusting old code by copying and pasting

          if(armPos == 0){
              armControl.DoInitialBarArmPos(slide,slide2,rotateMotor,claw);
          }else if(armPos == 1){
              armControl.DoBasketArmPos(slide,slide2,rotateMotor,claw);
          }else if(armPos == 2){
              armControl.DoAutoGrabArmPos(slide,slide2,rotateMotor,claw);
          }else if(armPos == 3){
              slide.setPower(0);
              slide2.setPower(0);
              rotateMotor.setPower(0);
          }else if(armPos == 4){
              armControl.DoAutoBasketGrabArmPos(slide,slide2,rotateMotor,claw);
          }
          untilFinished = Math.abs(leftFrontMotor.getCurrentPosition());


          double percentComplete = Math.abs((leftFrontMotor.getCurrentPosition()-startPos)/distance);
          if(percentComplete < 0.05) {
              leftFrontMotor.setPower(0.075);
              rightFrontMotor.setPower(0.075);
              leftRearMotor.setPower(0.075);
              rightRearMotor.setPower(0.075);
          } else if (percentComplete >= 0.05 && percentComplete <= 0.5) {
              leftFrontMotor.setPower(0.075 + ((speed*2)*(percentComplete)));
              rightFrontMotor.setPower(0.075 + ((speed*2)*(percentComplete)));
              leftRearMotor.setPower(0.075 + ((speed*2)*(percentComplete)));
              rightRearMotor.setPower(0.075 + ((speed*2)*(percentComplete)));
          } else if (percentComplete > 0.5 && percentComplete <= 0.95) {
              leftFrontMotor.setPower((speed + 0.075)-((speed*2)*(percentComplete - 0.5)));
              rightFrontMotor.setPower((speed + 0.075)-((speed*2)*(percentComplete - 0.5)));
              leftRearMotor.setPower((speed + 0.075)-((speed*2)*(percentComplete - 0.5)));
              rightRearMotor.setPower((speed + 0.075)-((speed*2)*(percentComplete - 0.5)));
          } else if (percentComplete > 0.95) {
              leftFrontMotor.setPower(0.075);
              rightFrontMotor.setPower(0.075);
              leftRearMotor.setPower(0.075);
              rightRearMotor.setPower(0.075);
          } else if (untilFinished >= absolutePos){
              leftFrontMotor.setPower(0);
              rightFrontMotor.setPower(0);
              leftRearMotor.setPower(0);
              rightRearMotor.setPower(0);
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

          //     telemetry.update();
      }

      // Stop all motion;
      leftFrontMotor.setPower(0);
      rightFrontMotor.setPower(0);
      leftRearMotor.setPower(0);
      rightRearMotor.setPower(0);

      slide.setPower(0);
      slide2.setPower(0);
      rotateMotor.setPower(0);
  }


    private void moveRight(int howMuch, double speed, int armPos) {
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

        absolutePos = lfPos;//end
        double startPos = leftFrontMotor.getCurrentPosition();
        double distance = absolutePos - startPos;

        // move robot to new position
        leftFrontMotor.setTargetPosition(lfPos);
        rightFrontMotor.setTargetPosition(rfPos);
        leftRearMotor.setTargetPosition(lrPos);
        rightRearMotor.setTargetPosition(rrPos);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFrontMotor.setPower(0.075);
        rightFrontMotor.setPower(0.075);
        leftRearMotor.setPower(0.075);
        rightRearMotor.setPower(0.075);

        // wait for move to complete
        while (leftFrontMotor.isBusy() && rightFrontMotor.isBusy() &&
                leftRearMotor.isBusy() && rightRearMotor.isBusy()) {

            if(armPos == 0){
                armControl.DoInitialBarArmPos(slide,slide2,rotateMotor,claw);
            }else if(armPos == 1){
                armControl.DoBasketArmPos(slide,slide2,rotateMotor,claw);
            }else if(armPos == 2){
                armControl.DoAutoGrabArmPos(slide,slide2,rotateMotor,claw);
            }else if(armPos == 3){
                slide.setPower(0);
                slide2.setPower(0);
                rotateMotor.setPower(0);
            }else if(armPos == 4){
                armControl.DoAutoBasketGrabArmPos(slide,slide2,rotateMotor,claw);
            }

            untilFinished = Math.abs(leftFrontMotor.getCurrentPosition());


            double percentComplete = Math.abs((leftFrontMotor.getCurrentPosition()-startPos)/distance);
            if(percentComplete < 0.05) {
                leftFrontMotor.setPower(0.075);
                rightFrontMotor.setPower(0.075);
                leftRearMotor.setPower(0.075);
                rightRearMotor.setPower(0.075);
            } else if (percentComplete >= 0.05 && percentComplete <= 0.5) {
                leftFrontMotor.setPower(0.075 + ((speed*2)*(percentComplete)));
                rightFrontMotor.setPower(0.075 + ((speed*2)*(percentComplete)));
                leftRearMotor.setPower(0.075 + ((speed*2)*(percentComplete)));
                rightRearMotor.setPower(0.075 + ((speed*2)*(percentComplete)));
            } else if (percentComplete > 0.5 && percentComplete <= 0.95) {
                leftFrontMotor.setPower((speed + 0.075)-((speed*2)*(percentComplete - 0.5)));
                rightFrontMotor.setPower((speed + 0.075)-((speed*2)*(percentComplete - 0.5)));
                leftRearMotor.setPower((speed + 0.075)-((speed*2)*(percentComplete - 0.5)));
                rightRearMotor.setPower((speed + 0.075)-((speed*2)*(percentComplete - 0.5)));
            } else if (percentComplete > 0.95) {
                leftFrontMotor.setPower(0.075);
                rightFrontMotor.setPower(0.075);
                leftRearMotor.setPower(0.075);
                rightRearMotor.setPower(0.075);
            } else if (untilFinished >= absolutePos){
                leftFrontMotor.setPower(0);
                rightFrontMotor.setPower(0);
                leftRearMotor.setPower(0);
                rightRearMotor.setPower(0);
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
            //   telemetry.update();
        }

        // Stop all motion;
        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftRearMotor.setPower(0);
        rightRearMotor.setPower(0);

        slide.setPower(0);
        slide2.setPower(0);
        rotateMotor.setPower(0);

    }


    private void turnClockwise(int whatAngle, double speed, int armPos) {
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

        absolutePos = lfPos;//end
        double startPos = leftFrontMotor.getCurrentPosition();
        double distance = absolutePos - startPos;

        // move robot to new position
        leftFrontMotor.setTargetPosition(lfPos);
        rightFrontMotor.setTargetPosition(rfPos);
        leftRearMotor.setTargetPosition(lrPos);
        rightRearMotor.setTargetPosition(rrPos);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFrontMotor.setPower(0.075);
        rightFrontMotor.setPower(0.075);
        leftRearMotor.setPower(0.075);
        rightRearMotor.setPower(0.075);

        // wait for move to complete
        while (leftFrontMotor.isBusy() && rightFrontMotor.isBusy() &&
                leftRearMotor.isBusy() && rightRearMotor.isBusy()) {//create a program that slowly increases and decreases.

            if(armPos == 0){
                armControl.DoInitialBarArmPos(slide,slide2,rotateMotor,claw);
            }else if(armPos == 1){
                armControl.DoBasketArmPos(slide,slide2,rotateMotor,claw);
            }else if(armPos == 2){
                armControl.DoAutoGrabArmPos(slide,slide2,rotateMotor,claw);
            }else if(armPos == 3){
                slide.setPower(0);
                slide2.setPower(0);
                rotateMotor.setPower(0);
            }else if(armPos == 4){
                armControl.DoAutoBasketGrabArmPos(slide,slide2,rotateMotor,claw);
            }

            untilFinished = Math.abs(leftFrontMotor.getCurrentPosition());


            double percentComplete = Math.abs((leftFrontMotor.getCurrentPosition()-startPos)/distance);
            if(percentComplete < 0.05) {
                leftFrontMotor.setPower(0.075);
                rightFrontMotor.setPower(0.075);
                leftRearMotor.setPower(0.075);
                rightRearMotor.setPower(0.075);
            } else if (percentComplete >= 0.05 && percentComplete <= 0.5) {
                leftFrontMotor.setPower(0.075 + ((speed*2)*(percentComplete)));
                rightFrontMotor.setPower(0.075 + ((speed*2)*(percentComplete)));
                leftRearMotor.setPower(0.075 + ((speed*2)*(percentComplete)));
                rightRearMotor.setPower(0.075 + ((speed*2)*(percentComplete)));
            } else if (percentComplete > 0.5 && percentComplete <= 0.85) {
                leftFrontMotor.setPower((speed + 0.075)-((speed*2)*(percentComplete - 0.5)));
                rightFrontMotor.setPower((speed + 0.075)-((speed*2)*(percentComplete - 0.5)));
                leftRearMotor.setPower((speed + 0.075)-((speed*2)*(percentComplete - 0.5)));
                rightRearMotor.setPower((speed + 0.075)-((speed*2)*(percentComplete - 0.5)));
            } else if (percentComplete > 0.85) {
                leftFrontMotor.setPower(0.075);
                rightFrontMotor.setPower(0.075);
                leftRearMotor.setPower(0.075);
                rightRearMotor.setPower(0.075);
            } else if (untilFinished >= absolutePos){
                leftFrontMotor.setPower(0);
                rightFrontMotor.setPower(0);
                leftRearMotor.setPower(0);
                rightRearMotor.setPower(0);
            }

            // Display it for the driver.
            telemetry.addLine("Turn Clockwise");
            telemetry.addData("Target", "%7d :%7d", lfPos, rfPos, lrPos, rrPos);
            telemetry.addData("Actual", "%7d :%7d", leftFrontMotor.getCurrentPosition(),
                    rightFrontMotor.getCurrentPosition(), leftRearMotor.getCurrentPosition(),
                    rightRearMotor.getCurrentPosition());
            //   telemetry.update();
        }

        // Stop all motion;
        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftRearMotor.setPower(0);
        rightRearMotor.setPower(0);

        slide.setPower(0);
        slide2.setPower(0);
        rotateMotor.setPower(0);

    }
    /*  private void moveForward(int howMuch, double speed, int armPos) {
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
                leftRearMotor.isBusy() && rightRearMotor.isBusy()) {//adjusting old code by copying and pasting

            if(armPos == 0){
                armControl.DoInitialBarArmPos(slide,slide2,rotateMotor,claw);
            }else if(armPos == 1){
                armControl.DoBasketArmPos(slide,slide2,rotateMotor,claw);
            }else if(armPos == 2){
                armControl.DoAutoGrabArmPos(slide,slide2,rotateMotor,claw);
            }else if(armPos == 3){
                slide.setPower(0);
                slide2.setPower(0);
                rotateMotor.setPower(0);
            }else if(armPos == 4){
                armControl.DoAutoBasketGrabArmPos(slide,slide2,rotateMotor,claw);
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

       //     telemetry.update();
        }

        // Stop all motion;
        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftRearMotor.setPower(0);
        rightRearMotor.setPower(0);

        slide.setPower(0);
        slide2.setPower(0);
        rotateMotor.setPower(0);
    }*/
 /*   private void moveRight(int howMuch, double speed, int armPos) {
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

            if(armPos == 0){
                armControl.DoInitialBarArmPos(slide,slide2,rotateMotor,claw);
            }else if(armPos == 1){
                armControl.DoBasketArmPos(slide,slide2,rotateMotor,claw);
            }else if(armPos == 2){
                armControl.DoAutoGrabArmPos(slide,slide2,rotateMotor,claw);
            }else if(armPos == 3){
                slide.setPower(0);
                slide2.setPower(0);
                rotateMotor.setPower(0);
            }else if(armPos == 4){
                armControl.DoAutoBasketGrabArmPos(slide,slide2,rotateMotor,claw);
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
         //   telemetry.update();
        }

        // Stop all motion;
        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftRearMotor.setPower(0);
        rightRearMotor.setPower(0);

        slide.setPower(0);
        slide2.setPower(0);
        rotateMotor.setPower(0);

    }*/

  /*  private void turnClockwise(int whatAngle, double speed, int armPos) {
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

            if(armPos == 0){
                armControl.DoInitialBarArmPos(slide,slide2,rotateMotor,claw);
            }else if(armPos == 1){
                armControl.DoBasketArmPos(slide,slide2,rotateMotor,claw);
            }else if(armPos == 2){
                armControl.DoAutoGrabArmPos(slide,slide2,rotateMotor,claw);
            }else if(armPos == 3){
                slide.setPower(0);
                slide2.setPower(0);
                rotateMotor.setPower(0);
            }else if(armPos == 4){
                armControl.DoAutoBasketGrabArmPos(slide,slide2,rotateMotor,claw);
            }

            // Display it for the driver.
            telemetry.addLine("Turn Clockwise");
            telemetry.addData("Target", "%7d :%7d", lfPos, rfPos, lrPos, rrPos);
            telemetry.addData("Actual", "%7d :%7d", leftFrontMotor.getCurrentPosition(),
                    rightFrontMotor.getCurrentPosition(), leftRearMotor.getCurrentPosition(),
                    rightRearMotor.getCurrentPosition());
         //   telemetry.update();
        }

        // Stop all motion;
        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftRearMotor.setPower(0);
        rightRearMotor.setPower(0);

        slide.setPower(0);
        slide2.setPower(0);
        rotateMotor.setPower(0);

    }//funny sample thing and determines which sample to go for*/


    @Override
    public void run() {

    }
}