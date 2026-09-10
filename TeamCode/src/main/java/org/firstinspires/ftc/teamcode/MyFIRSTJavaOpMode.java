package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp

public class MyFIRSTJavaOpMode extends LinearOpMode {
    private Gyroscope imu;
    private DcMotor wheel;
    private DcMotor hex;
    private DigitalChannel digitalTouch;
    private NormalizedColorSensor color;
    private Servo wrist;

    @Override
    public void runOpMode() {
        imu = hardwareMap.get(Gyroscope.class, "imu");
        wheel = hardwareMap.get(DcMotor.class, "wheel");
        hex = hardwareMap.get(DcMotor.class, "hex");
        wrist = hardwareMap.get(Servo.class, "wrist");
        // digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
        color = hardwareMap.get(NormalizedColorSensor.class, "color");
        // servoTest = hardwareMap.get(Servo.class, "servoTest");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        double wheelPower = 0;
        double hexPower = 0;

        while (opModeIsActive()) {
            wheelPower = -this.gamepad1.left_stick_x;
            hexPower = -this.gamepad1.right_stick_x;
            wheel.setPower(wheelPower);
            hex.setPower(hexPower);

            // check to see if we need to move the servo.
            if (gamepad1.y) {
                // move to 0 degrees.
                wrist.setPosition(0);
                telemetry.addData("yPressed", true);
            } else if (gamepad1.x || gamepad1.b) {
                telemetry.addData("bPressed", true);
                // move to 90 degrees.
                wrist.setPosition(0.5);
            } else if (gamepad1.a) {
                telemetry.addData("aPressed", true);
                // move to 180 degrees.
                wrist.setPosition(1);
            }
            // set digital channel to input mode.
            //digitalTouch.setMode(DigitalChannel.Mode.INPUT);
            // is button pressed?
            //if (digitalTouch.getState() == false) {
                // button is pressed.
                //telemetry.addData("Button", "PRESSED");
            //} else {
                // button is not pressed.
                //telemetry.addData("Button", "NOT PRESSED");
            //}

            NormalizedRGBA colors = color.getNormalizedColors();
            final float[] hsvValues = new float[3];
            Color.colorToHSV(colors.toColor(), hsvValues);
            telemetry.addLine()
                    .addData("Red", "%.3f", colors.red)
                    .addData("Green", "%.3f", colors.green)
                    .addData("Blue", "%.3f", colors.blue);

            if (color instanceof DistanceSensor) {
                telemetry.addData("Distance (cm)", "%.3f", ((DistanceSensor) color).getDistance(DistanceUnit.CM));
            }

            telemetry.addData("Status", "Running");

            telemetry.addData("Wheel Power", wheelPower);
            telemetry.addData("Hex Power", hexPower);
            telemetry.addData("Motor Power", wheel.getPower());
            telemetry.addData("Motor Power", hex.getPower());
            telemetry.addData("Status", "Running");

            telemetry.update();
        }
    }
}