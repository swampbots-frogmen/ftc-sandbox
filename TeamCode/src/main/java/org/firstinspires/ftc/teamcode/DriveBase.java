package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;

@TeleOp
public class DriveBase extends LinearOpMode {
    private DcMotor fl;
    private DcMotor fr;
    private DcMotor bl;
    private DcMotor br;

    double flPower = 0;
    double frPower = 0;
    double blPower = 0;
    double brPower = 0;

    @Override
    public void runOpMode() {
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");

        waitForStart();

        while (opModeIsActive()) {
            this.move(
                this.gamepad1.left_stick_y,
                this.gamepad1.left_stick_x,
                this.gamepad1.right_stick_y,
                this.gamepad1.right_stick_x
            );

            idMotors();
        }
    }

    private void idMotors() {
        // Should spin front-left motor
        if (this.gamepad1.left_trigger > 0) {
            this.flPower = 1;
            fl.setPower(this.flPower);
        }

        // Should spin front-right motor
        if (this.gamepad1.right_trigger > 0) {
            this.frPower = 1;
            fr.setPower(this.frPower);
        }

        // Should spin back-left motor
        if (this.gamepad1.left_bumper) {
            this.blPower = 1;
            bl.setPower(this.blPower);
        }

        // Should spin back-right motor
        if (this.gamepad1.right_bumper) {
            this.brPower = 1;
            br.setPower(this.brPower);
        }

    }

    private void move(double lsy, double lsx, double rsy, double rsx) {
        this.flPower = -lsy;
        this.frPower = -lsy;
        this.blPower = -lsy;
        this.brPower = -lsy;

        fl.setPower(this.flPower);
        fr.setPower(this.frPower);
        bl.setPower(this.blPower);
        br.setPower(this.brPower);
    }
}
