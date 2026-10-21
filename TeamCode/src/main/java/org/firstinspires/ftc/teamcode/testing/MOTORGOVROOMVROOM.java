package org.firstinspires.ftc.teamcode.testing;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous()
public class MOTORGOVROOMVROOM extends LinearOpMode {
    DcMotor vroom = null;
    Servo funnyBOI = null;

    @Override
    public void runOpMode() {

        telemetry.setAutoClear(true);
        vroom = hardwareMap.dcMotor.get("vroom");
        funnyBOI = hardwareMap.servo.get("funnyBOI");
        boolean isMax = false;

        waitForStart();

        if(isStopRequested()) return;
        double servoPos = 0;
            while(opModeIsActive()){
                vroom.setPower(1);




                if(isMax == false){
                    telemetry.addData("Not maxed", true);
                    servoPos += 1/50;
                    funnyBOI.setPosition(servoPos);
                    if(servoPos >= 0.8){
                        isMax = true;
                    }
                }else if(isMax == true){
                    telemetry.addData("Is Maxed", true);
                    servoPos -= 1/50;
                    funnyBOI.setPosition(servoPos);
                    if(servoPos <= 0.2){
                        isMax = false;
                    }
                }


                telemetry.addData("Servo Pos:", funnyBOI.getPosition());
                telemetry.addData("Motor Speed:", vroom.getPower());
                telemetry.addData("Servo Max?", isMax);
                telemetry.addData("ServoPosVar", servoPos);
                telemetry.update();
            }


    }



}
