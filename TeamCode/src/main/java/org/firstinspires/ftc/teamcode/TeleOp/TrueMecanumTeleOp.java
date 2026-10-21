package org.firstinspires.ftc.teamcode.TeleOp;  //calls the package it is located in

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;  //Imports the linearOpMode class
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;  //Imports the teleOp class
import com.qualcomm.robotcore.hardware.DcMotor;  //Imports the DcMotor class
import com.qualcomm.robotcore.hardware.DcMotorSimple;  //Imports the DcMotorSimple class
import com.qualcomm.robotcore.hardware.Servo;  //Imports the Servo class

@TeleOp  //Tells you what mode this extends, and allows you to call it on the driver station
public class TrueMecanumTeleOp extends LinearOpMode {  //creates the mecanum teleOp class. It is a sub class of LinearOpMode, as it extends the class
    public void runOpMode() throws InterruptedException {  //creates a method that runs op mode. Does not catch the interrupted exception error
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("leftFront");  //Calls the front left motor from configuration
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("leftRear");  //Calls the back left motor from configuration
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("rightFront");  //Calls the front right motor from configuration
        DcMotor backRightMotor = hardwareMap.dcMotor.get("rightRear");  //Calls the back right motor from configuration
        Servo claw = hardwareMap.get(Servo.class, "claw");
        DcMotor slide = hardwareMap.dcMotor.get("slide");
       // Servo push = hardwareMap.get(Servo.class, "push");
        DcMotor rotateMotor = hardwareMap.dcMotor.get("rotateMotor");
        DcMotor slide2 = hardwareMap.dcMotor.get("slide2");
        double rotateLimit = -9700;
        double slideLimit = 12000;
        double LimitCodeRotate = 0;
        double MinCodeRotate = 0;
        double rotateHighEnough = 0;
        double LimitCodeSlide = 0;
        double MinCodeSlide = 0;
        double armPosition = 0;
        /*
        double
         */
        double position = 0.85;
        double originalPos = position;
        boolean continueBasketArmPos = false;
        boolean continue0Pos = false;
        boolean continueAPos = false;


        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);  //sets the front left motor to reverse when going forward
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);  //sets the front right motor to forward when going forward
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);  //sets the back left motor to reverse when going forward
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);  //sets the back right motor to forward when going forward
        slide.setDirection(DcMotorSimple.Direction.REVERSE);
        slide2.setDirection(DcMotorSimple.Direction.FORWARD);

        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);




        waitForStart();

        if (isStopRequested()) return;  //if you stop the program, it returns and ends the program

        while (opModeIsActive()) {  //while the op mode is running, do this

            double y = -gamepad1.left_stick_y;  //creates a number when we use the y position of the left joystick, y value must be reversed, as indicated by the -
            double x = gamepad1.left_stick_x;  //creates a number when we use the x position of the left joystick, the * 1.1 counteracts imperfect strafing
            double turn = gamepad1.right_stick_x * 1.1;  //creates a number when we use the x position of the right joystick
            double SlowMode = 1 - gamepad1.left_trigger * 0.4; // originally * 0.6  //creates a number that reads the left triggers number, and multiplies it by 0.8, the lowest number possible is 0.2
            double SlideSlowMode = 1 - gamepad2.right_trigger * 0.4;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(turn), 1);  //creates a number that will divide the motors so the power doesn't exceed 1

            double frontLeftPower = (y + x + turn) / denominator;  //creates the power we need to go for the front left motor mecanum wheel.
            double backLeftPower = (y - x + turn) / denominator;  //creates the power we need to go for the back left motor mecanum wheel.
            double frontRightPower = (y - x - turn) / denominator;  //creates the power we need to go for the front right motor mecanum wheel.
            double backRightPower = (y + x - turn) / denominator;  //creates the power we need to go for the back right motor mecanum wheel.


            frontLeftMotor.setPower(frontLeftPower * SlowMode * 0.5);  //sets the power of the front left motor and multiplies it by slow mode
            backLeftMotor.setPower(backLeftPower * SlowMode * 0.5);  //sets the power of the back left motor and multiplies it by slow mode
            frontRightMotor.setPower(frontRightPower * SlowMode * 0.5);  //sets the power of the front right motor and multiplies it by slow mode
            backRightMotor.setPower(backRightPower * SlowMode * 0.5);  //sets the power of the back right motor and multiplies it by slow mode

        /*    if(gamepad2.left_bumper){
                push.setPosition(0);
            }else if(gamepad2.left_trigger > 0.1){
                push.setPosition(1);
            }else{
                push.setPosition(0.5);
            }*/


            if (gamepad2.dpad_up) {  //detects whether y was pressed on game pad 1
                claw.setPosition(0.4);//setPower(0.5);  //sets the intake to take pixels
//closes
            } else if (gamepad2.dpad_down) {  //detects whether b was pressed on game pad 1 if y was not pressed
                claw.setPosition(0);  //sets the intake to shoot pixels
            }//opens
           /* if (gamepad2.left_bumper && position <= 1) {  //detects whether y was pressed on game pad 1
                //    if(!(position > 0.75)) {
                armPivot.setPosition(position);//setPower(0.5);  //sets the intake to take pixels
                position += 0.003;
                //   }
            } else if (gamepad2.left_trigger > 0.1 && position >= 0) {  //detects whether b was pressed on game pad 1 if y was not pressed
                //     if(!(position < 0)) {
                armPivot.setPosition(position);  //sets the intake to shoot pixels
                position -= 0.003;
                //     }
            }*/



            if (gamepad2.y == true) {

                continueBasketArmPos = true;
                continue0Pos = false;
                continueAPos = false;
                LimitCodeRotate = (rotateLimit*4)/5;
                MinCodeRotate = (rotateLimit*5)/7;
                rotateHighEnough = 0;
                LimitCodeSlide = slideLimit;
                MinCodeSlide = (slideLimit*39)/41;
                armPosition = 0.47;

            }
                if (continueBasketArmPos == true){//doc.  We subbed variable for values and used proportions
                if(rotateMotor.getCurrentPosition() > MinCodeRotate){
                    rotateMotor.setPower(-1);
                }
                if(rotateMotor.getCurrentPosition() < LimitCodeRotate){
                    rotateMotor.setPower(0.5);
                }
                if (rotateMotor.getCurrentPosition() <= MinCodeRotate && rotateMotor.getCurrentPosition() >= LimitCodeRotate){
                    rotateMotor.setPower(0);
                }
            /*    if(rotateMotor.getCurrentPosition() > -(rotateLimit*1)/4 && slide.getCurrentPosition() < (slideLimit*39)/41 && rotateMotor.getCurrentPosition() < 0) {
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
                    position = armPosition;
                  //  armPivot.setPosition(armPosition);
                    continueBasketArmPos = false;
                    armPosition = 0;
                    MinCodeRotate = 0;
                    MinCodeSlide = 0;
                    LimitCodeSlide = 0;
                    LimitCodeRotate = 0;
                    rotateHighEnough = 0;
                }
            }



                if(gamepad2.x == true){

                    continue0Pos = true;
                    continueBasketArmPos = false;
                    continueAPos =false;
                    LimitCodeRotate = rotateLimit/-76;
                    MinCodeRotate = -1;
                    rotateHighEnough = 0;
                    LimitCodeSlide = slideLimit/-80;
                    MinCodeSlide = slideLimit/80;
                    armPosition = originalPos;

                }
                if(continue0Pos == true){
                    if (rotateMotor.getCurrentPosition() < MinCodeRotate) {
                        rotateMotor.setPower(1);
                    }
                    if(rotateMotor.getCurrentPosition() > LimitCodeRotate){
                        rotateMotor.setPower(-0.25);
                    }
                    if(rotateMotor.getCurrentPosition() >= MinCodeRotate && rotateMotor.getCurrentPosition() <= LimitCodeRotate){
                        rotateMotor.setPower(0);
                    }
                    if(slide.getCurrentPosition() > MinCodeSlide){
                        slide.setPower(-0.25);
                        slide2.setPower(-0.25);
                    }
                    if(slide.getCurrentPosition() < LimitCodeSlide){
                        slide.setPower(0.25);
                        slide2.setPower(0.25);
                    }
                    if(slide.getCurrentPosition() <= MinCodeSlide && slide.getCurrentPosition() >= LimitCodeSlide){
                        slide.setPower(0);
                        slide2.setPower(0);
                    }
                    if(slide.getCurrentPosition() <= MinCodeSlide && slide.getCurrentPosition() >= LimitCodeSlide && rotateMotor.getCurrentPosition() >= MinCodeRotate && rotateMotor.getCurrentPosition() <= LimitCodeRotate){
                        position = armPosition;
                       // armPivot.setPosition(position);
                        continue0Pos = false;
                        armPosition = 0;
                        MinCodeRotate = 0;
                        MinCodeSlide = 0;
                        LimitCodeSlide = 0;
                        LimitCodeRotate = 0;
                        rotateHighEnough = 0;
                    }
                }



                if(gamepad2.a == true){
                    continueAPos = true;
                    continue0Pos = false;
                    continueBasketArmPos = false;
                   // LimitCodeRotate = rotateLimit/-76;
                   // MinCodeRotate = -1;
                  //  rotateHighEnough = 0;
                  //  LimitCodeSlide = slideLimit/-80;
                   // MinCodeSlide = slideLimit/80;
                   // armPosition = originalPos;
                }
                if(continueAPos == true){
                    if(slide.getCurrentPosition() < (slideLimit*28)/41){
                        slide.setPower(0.5);
                        slide2.setPower(0.5);
                    }
                    if(slide.getCurrentPosition() > (slideLimit*30)/41){
                        slide.setPower(-0.5);
                        slide2.setPower(-0.5);
                    }
                    if(slide.getCurrentPosition() >= (slideLimit*28)/41 && slide.getCurrentPosition() <= (slideLimit*30)/41){
                        slide.setPower(0);
                        slide2.setPower(0);
                    }
                    if(rotateMotor.getCurrentPosition() < 2800){
                        rotateMotor.setPower(1);
                    }
                    if(rotateMotor.getCurrentPosition() > 3000){
                        rotateMotor.setPower(-0.25);
                    }
                    if(rotateMotor.getCurrentPosition() >= 2800 && rotateMotor.getCurrentPosition() <= 3000){
                        rotateMotor.setPower(0);
                    }
                    if(slide.getCurrentPosition() >= (slideLimit*28)/41 && slide.getCurrentPosition() <= (slideLimit*30)/41 && rotateMotor.getCurrentPosition() >= 2800 && rotateMotor.getCurrentPosition() <= 3000){
                        position = 0.363;
                      //  armPivot.setPosition(position);
                        continueAPos = false;
                        armPosition = 0;
                        MinCodeRotate = 0;
                        MinCodeSlide = 0;
                        LimitCodeSlide = 0;
                        LimitCodeRotate = 0;
                        rotateHighEnough = 0;
                    }
                }
            //a = intake pos
            if(gamepad2.right_stick_y > 0.1){
                rotateMotor.setPower(1);
        }else if(gamepad2.right_stick_y < -0.1 && rotateMotor.getCurrentPosition() > rotateLimit){
            rotateMotor.setPower(-1);
        }else{
                if(continue0Pos == false && continueBasketArmPos == false && continueAPos == false) {
                    rotateMotor.setPower(0);
                }
        }
        if (gamepad2.left_stick_y < -0.1) {
          slide.setPower(1*SlideSlowMode);
          slide2.setPower(1*SlideSlowMode);
        } else if(gamepad2.left_stick_y > 0.1){
            slide.setPower(-1*SlideSlowMode);
            slide2.setPower(-1*SlideSlowMode);
        }else{
            if(continue0Pos == false && continueBasketArmPos == false && continueAPos == false) {
                slide.setPower(0);
                slide2.setPower(0);
            }
        }

           // Launcher.setPosition(LaunchServoPos);  //sets the launcher servo's position to the launch servo pos variable

            telemetry.addData("FrontRight:", frontRightMotor.getPower());  //adds a line of data that reads the front right motor's power
            telemetry.addData("FrontLeft:", frontLeftMotor.getPower());  //adds a line of data that reads the front left motor's power
            telemetry.addData("BackRight:", backRightMotor.getPower());  //adds a line of data that reads the back right motor's power
            telemetry.addData("BackLeft:", backLeftMotor.getPower());  //adds a line of data that reads the back left motor's power
            telemetry.addData("IS WORKING", gamepad1.dpad_up);  //adds a line of data the tells you whether the dpad up is pressed
            telemetry.addData("Slow mode:", SlowMode);  //adds a line of data telling you the slow mode's variable number
            telemetry.addData("y controller value", y);
            telemetry.addData("x controller value", x);
            telemetry.addData("turn controller value", turn);
            telemetry.addData("denominator controller value", denominator);
            telemetry.addData("intake position", claw.getPosition());
            telemetry.addData("slide power", slide.getPower());
            telemetry.addData("gamepad2 x button value", gamepad2.x);
            telemetry.addData("armPivot pos", position);
            telemetry.addData("RotateMotorPos", rotateMotor.getCurrentPosition());
            telemetry.addData("slideDistance", slide.getCurrentPosition());
            telemetry.update();  //This code updates the telemetry class.  Basically, this means every single time the code runs through the while loop, this code will be refreshed into a new output, or updated from the previous.
        }
    }

}