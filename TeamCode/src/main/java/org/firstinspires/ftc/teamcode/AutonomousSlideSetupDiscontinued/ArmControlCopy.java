package org.firstinspires.ftc.teamcode.AutonomousSlideSetupDiscontinued;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


public class ArmControlCopy extends LinearOpMode {
    DcMotor slide = null;
    DcMotor rotateMotor = null;
    DcMotor slide2 = null;
    Servo armPivot = null;
    static boolean FinishedBasketArmPos = false;
    @Override
    public void runOpMode() throws InterruptedException {  //creates a method that runs op mode. Does not catch the interrupted exception error
        waitForStart();
    }

    public static boolean ArmBasketPosIsDone(){
        return FinishedBasketArmPos;
    }
    //calls the package it is located in
    //Tells you what mode this extends, and allows you to call it on the driver station
    //creates the mecanum teleOp class. It is a sub class of LinearOpMode, as it extends the class

    public void DoBasketArmPos(DcMotor slide, DcMotor slide2, DcMotor rotateMotor, Servo claw) {

//Oversimplifying code to make it easy

        FinishedBasketArmPos = false;

        double rotateLimit = -12500;
        double slideLimit = 12000;
        double LimitCodeRotate = (rotateLimit*8)/9;
        double MinCodeRotate = (rotateLimit*6)/7;
        double rotateHighEnough = 0;
        double LimitCodeSlide = slideLimit;
        double MinCodeSlide = (slideLimit*40)/41;
        double armPosition = 0.37;





        slide.setDirection(DcMotorSimple.Direction.REVERSE);
        slide2.setDirection(DcMotorSimple.Direction.FORWARD);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


           if(rotateMotor.getCurrentPosition() > MinCodeRotate){
                rotateMotor.setPower(-1);
            }
            if(rotateMotor.getCurrentPosition() < LimitCodeRotate){
                rotateMotor.setPower(0.5);
            }
            if (rotateMotor.getCurrentPosition() <= MinCodeRotate && rotateMotor.getCurrentPosition() >= LimitCodeRotate){
                rotateMotor.setPower(0);
            }
               /*     if(rotateMotor.getCurrentPosition() < -(rotateLimit*1)/4 && slide.getCurrentPosition() < (slideLimit*39)/41 && rotateMotor.getCurrentPosition() < TheLimitForSlide) {
                        slide.setPower(0.5);
                        slide2.setPower(0.5);
                    }*/
            if(slide.getCurrentPosition() < MinCodeSlide && rotateMotor.getCurrentPosition() < rotateHighEnough){
                slide.setPower(1);
                slide2.setPower(1);
            }
            if(slide.getCurrentPosition() > LimitCodeSlide){
                slide.setPower(-0.5);
                slide2.setPower(-0.5);
            }
            if(slide.getCurrentPosition() <= LimitCodeSlide && slide.getCurrentPosition() >= MinCodeSlide){
                slide.setPower(0);
                slide2.setPower(0);
            }
            if(slide.getCurrentPosition() <= LimitCodeSlide && slide.getCurrentPosition() >= MinCodeSlide && rotateMotor.getCurrentPosition() >= LimitCodeRotate && rotateMotor.getCurrentPosition() <= MinCodeRotate) {
                FinishedBasketArmPos = true;
                slide.setPower(0);
                slide2.setPower(0);
                rotateMotor.setPower(0);
            }

    }
    public void DoFirstLevelAscentArmPos(DcMotor slide, DcMotor slide2, DcMotor rotateMotor, Servo claw) {

//oversimplifying code to make it easy



        double rotateLimit = -12500;
        double slideLimit = 12000;
        double LimitCodeRotate = (rotateLimit*4)/5;
        double MinCodeRotate = (rotateLimit*5)/7;
        double rotateHighEnough = 0;
        double LimitCodeSlide = (slideLimit*22)/41;
        double MinCodeSlide = (slideLimit*20)/41;
        double armPosition = 0.37;





        slide.setDirection(DcMotorSimple.Direction.REVERSE);
        slide2.setDirection(DcMotorSimple.Direction.FORWARD);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


            if(rotateMotor.getCurrentPosition() > MinCodeRotate){
                rotateMotor.setPower(-1);
            }
            if(rotateMotor.getCurrentPosition() < LimitCodeRotate){
                rotateMotor.setPower(0.5);
            }
            if (rotateMotor.getCurrentPosition() <= MinCodeRotate && rotateMotor.getCurrentPosition() >= LimitCodeRotate){
                rotateMotor.setPower(0);
            }
                /*if(rotateMotor.getCurrentPosition() > -(rotateLimit*1)/4 && slide.getCurrentPosition() < (slideLimit*20)/41 && rotateMotor.getCurrentPosition() < 0) {
                    slide.setPower(0.5);
                    slide2.setPower(0.5);
                }*/
            if(slide.getCurrentPosition() < MinCodeSlide && rotateMotor.getCurrentPosition() < rotateHighEnough){
                slide.setPower(0.5);
                slide2.setPower(0.5);
            }
            if(slide.getCurrentPosition() > LimitCodeSlide){
                slide.setPower(-0.5);
                slide2.setPower(-0.5);
            }
            if(slide.getCurrentPosition() <= LimitCodeSlide && slide.getCurrentPosition() >= MinCodeSlide){
                slide.setPower(0);
                slide2.setPower(0);
            }
            if(slide.getCurrentPosition() <= LimitCodeSlide && slide.getCurrentPosition() >= MinCodeSlide && rotateMotor.getCurrentPosition() >= LimitCodeRotate && rotateMotor.getCurrentPosition() <= MinCodeRotate) {
                // armPivot.setPosition(armPosition);
                slide.setPower(0);
                slide2.setPower(0);
                rotateMotor.setPower(0);
            }

    }
    public void DoAutoBasketArmPos(DcMotor slide, DcMotor slide2, DcMotor rotateMotor, Servo claw) {

//oversimplifying code to make it easy



        double rotateLimit = -12500;
        double slideLimit = 12000;
        double LimitCodeRotate = (rotateLimit*4)/5;
        double MinCodeRotate = (rotateLimit*5)/7;
        double rotateHighEnough = 0;
        double LimitCodeSlide = (slideLimit*22)/41;
        double MinCodeSlide = (slideLimit*20)/41;
        double armPosition = 0.37;





        slide.setDirection(DcMotorSimple.Direction.REVERSE);
        slide2.setDirection(DcMotorSimple.Direction.FORWARD);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


            if(rotateMotor.getCurrentPosition() > MinCodeRotate){
                rotateMotor.setPower(-1);
            }
            if(rotateMotor.getCurrentPosition() < LimitCodeRotate){
                rotateMotor.setPower(0.5);
            }
            if (rotateMotor.getCurrentPosition() <= MinCodeRotate && rotateMotor.getCurrentPosition() >= LimitCodeRotate){
                rotateMotor.setPower(0);
            }
                /*if(rotateMotor.getCurrentPosition() > -(rotateLimit*1)/4 && slide.getCurrentPosition() < (slideLimit*20)/41 && rotateMotor.getCurrentPosition() < 0) {
                    slide.setPower(0.5);
                    slide2.setPower(0.5);
                }*/
            if(slide.getCurrentPosition() < MinCodeSlide && rotateMotor.getCurrentPosition() < rotateHighEnough){
                slide.setPower(0.5);
                slide2.setPower(0.5);
            }
            if(slide.getCurrentPosition() > LimitCodeSlide){
                slide.setPower(-0.5);
                slide2.setPower(-0.5);
            }
            if(slide.getCurrentPosition() <= LimitCodeSlide && slide.getCurrentPosition() >= MinCodeSlide){
                slide.setPower(0);
                slide2.setPower(0);
            }
            if(slide.getCurrentPosition() <= LimitCodeSlide && slide.getCurrentPosition() >= MinCodeSlide && rotateMotor.getCurrentPosition() >= LimitCodeRotate && rotateMotor.getCurrentPosition() <= MinCodeRotate) {
                //   armPivot.setPosition(armPosition);
                slide.setPower(0);
                slide2.setPower(0);
                rotateMotor.setPower(0);
            }

    }
    public void DoAutoGrabArmPos(DcMotor slide, DcMotor slide2, DcMotor rotateMotor, Servo claw) {

//oversimplifying code to make it easy
//rL = 1259,sL = 8908  claw pinch
//1200,8900

        double rotateLimit = -12500;
        double slideLimit = 12000;
        double LimitCodeRotate = (rotateLimit*-10)/125;
        double MinCodeRotate = (rotateLimit*-15)/125;
        double rotateHighEnough = 0;// 7100 slide
        double LimitCodeSlide = (slideLimit*4)/5;
        double MinCodeSlide = (slideLimit*3)/4;
        double armPosition = 0.37;





        slide.setDirection(DcMotorSimple.Direction.REVERSE);
        slide2.setDirection(DcMotorSimple.Direction.FORWARD);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


            if(rotateMotor.getCurrentPosition() > MinCodeRotate){
                rotateMotor.setPower(-1);
            }
            if(rotateMotor.getCurrentPosition() < LimitCodeRotate){
                rotateMotor.setPower(0.5);
            }
            if (rotateMotor.getCurrentPosition() <= MinCodeRotate && rotateMotor.getCurrentPosition() >= LimitCodeRotate){
                rotateMotor.setPower(0);
            }
                /*if(rotateMotor.getCurrentPosition() > -(rotateLimit*1)/4 && slide.getCurrentPosition() < (slideLimit*20)/41 && rotateMotor.getCurrentPosition() < 0) {
                    slide.setPower(0.5);
                    slide2.setPower(0.5);
                }*/
            if(slide.getCurrentPosition() < MinCodeSlide && rotateMotor.getCurrentPosition() < rotateHighEnough){
                slide.setPower(1);
                slide2.setPower(1);
            }
            if(slide.getCurrentPosition() > LimitCodeSlide){
                slide.setPower(-0.5);
                slide2.setPower(-0.5);
            }
            if(slide.getCurrentPosition() <= LimitCodeSlide && slide.getCurrentPosition() >= MinCodeSlide){
                slide.setPower(0);
                slide2.setPower(0);
            }
            if(slide.getCurrentPosition() <= LimitCodeSlide && slide.getCurrentPosition() >= MinCodeSlide && rotateMotor.getCurrentPosition() >= LimitCodeRotate && rotateMotor.getCurrentPosition() <= MinCodeRotate) {

                slide.setPower(0);
                slide2.setPower(0);
                rotateMotor.setPower(0);


            }

    }
    public void DoInitialBarArmPos(DcMotor slide, DcMotor slide2, DcMotor rotateMotor, Servo claw) {

//oversimplifying code to make it easy

//claw now

        double rotateLimit = -12500;
        double slideLimit = 12000;
        double LimitCodeRotate = (rotateLimit*4)/5;
        double MinCodeRotate = (rotateLimit*5)/7;
        double rotateHighEnough = 250;
        double LimitCodeSlide = (slideLimit*22)/41;
        double MinCodeSlide = (slideLimit*20)/41;
        double RotateSlideLimit = slideLimit/4;
        double clawUp = 0;





        slide.setDirection(DcMotorSimple.Direction.REVERSE);
        slide2.setDirection(DcMotorSimple.Direction.FORWARD);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


            if(rotateMotor.getCurrentPosition() > MinCodeRotate && slide.getCurrentPosition() > RotateSlideLimit){
                rotateMotor.setPower(-1);
            }
            if(rotateMotor.getCurrentPosition() < LimitCodeRotate){
                rotateMotor.setPower(0.5);
            }
            if (rotateMotor.getCurrentPosition() <= MinCodeRotate && rotateMotor.getCurrentPosition() >= LimitCodeRotate){
                rotateMotor.setPower(0);
            }
                /*if(rotateMotor.getCurrentPosition() > -(rotateLimit*1)/4 && slide.getCurrentPosition() < (slideLimit*20)/41 && rotateMotor.getCurrentPosition() < 0) {
                    slide.setPower(0.5);
                    slide2.setPower(0.5);
                }*/
            if(slide.getCurrentPosition() < MinCodeSlide){
                if(rotateMotor.getCurrentPosition() <= rotateHighEnough || slide.getCurrentPosition() <= RotateSlideLimit) {
                    slide.setPower(1);
                    slide2.setPower(1);
                }
            }
            if(slide.getCurrentPosition() > LimitCodeSlide){
                slide.setPower(-0.5);
                slide2.setPower(-0.5);
            }
            if(slide.getCurrentPosition() <= LimitCodeSlide && slide.getCurrentPosition() >= MinCodeSlide){
                slide.setPower(0);
                slide2.setPower(0);
            }
            if(slide.getCurrentPosition() <= LimitCodeSlide && slide.getCurrentPosition() >= MinCodeSlide && rotateMotor.getCurrentPosition() >= LimitCodeRotate && rotateMotor.getCurrentPosition() <= MinCodeRotate) {
                //  armPivot.setPosition(armPosition);
                rotateMotor.setPower(0);
                slide.setPower(0);
                slide2.setPower(0);
            }

    }
    public void DoInitialBarDownArmPos(DcMotor slide, DcMotor slide2, DcMotor rotateMotor, Servo claw) {

//oversimplifying code to make it easy

//claw now

        double rotateLimit = -12500;
        double slideLimit = 12000;
        double LimitCodeRotate = (rotateLimit * 3) / 5;
        double MinCodeRotate = (rotateLimit * 1) / 3;
        double rotateHighEnough = 10;
        double LimitCodeSlide = (slideLimit * 36) / 120;
        double MinCodeSlide = (slideLimit * 35) / 120;
        double RotateSlideLimit = slideLimit / 4;
        double clawUp = 0;


        slide.setDirection(DcMotorSimple.Direction.REVERSE);
        slide2.setDirection(DcMotorSimple.Direction.FORWARD);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


            if (rotateMotor.getCurrentPosition() > MinCodeRotate && slide.getCurrentPosition() > RotateSlideLimit) {
                rotateMotor.setPower(-1);
            }
            if (rotateMotor.getCurrentPosition() < LimitCodeRotate) {
                rotateMotor.setPower(0.5);
            }
            if (rotateMotor.getCurrentPosition() <= MinCodeRotate && rotateMotor.getCurrentPosition() >= LimitCodeRotate) {
                rotateMotor.setPower(0);
            }
                /*if(rotateMotor.getCurrentPosition() > -(rotateLimit*1)/4 && slide.getCurrentPosition() < (slideLimit*20)/41 && rotateMotor.getCurrentPosition() < 0) {
                    slide.setPower(0.5);
                    slide2.setPower(0.5);
                }*/
            if (slide.getCurrentPosition() < MinCodeSlide && rotateMotor.getCurrentPosition() <= rotateHighEnough) {
                slide.setPower(0.5);
                slide2.setPower(0.5);
            }
            if (slide.getCurrentPosition() > LimitCodeSlide) {
                slide.setPower(-0.5);
                slide2.setPower(-0.5);
            }
            if (slide.getCurrentPosition() <= LimitCodeSlide && slide.getCurrentPosition() >= MinCodeSlide) {
                slide.setPower(0);
                slide2.setPower(0);
            }
            if (slide.getCurrentPosition() <= LimitCodeSlide && slide.getCurrentPosition() >= MinCodeSlide && rotateMotor.getCurrentPosition() >= LimitCodeRotate && rotateMotor.getCurrentPosition() <= MinCodeRotate) {
                sleep(250);
                claw.setPosition(0);
                sleep(250);
            }


    }
}

