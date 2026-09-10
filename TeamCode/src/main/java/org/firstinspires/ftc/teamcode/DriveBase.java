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

            // idMotors();
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

    private String getStrafeDir(double lsy, double lsx, double rsy, double rsx) {
        if ((lsy <= 0 && lsy >= -0.5 && lsx <= -0.3) && (rsy <= 0 && rsy >= -0.5 && rsx <= -0.3)) {
            return "left";
        }

        if ((lsy >= 0 && lsy <= 0.5 && lsx >= 0.3) && (rsy >= 0 && rsy <= 0.5 && rsx >= 0.3)) {
            return "right";
        }

        return null;
    }
    private void move(double lsy, double lsx, double rsy, double rsx) {
        // Left Motors
        this.flPower = -lsy;
        this.blPower = -lsy;

        // Right Motors
        this.frPower = -rsy;
        this.brPower = -rsy;

        String strafeDir = getStrafeDir(lsy, lsx, rsy, rsx);

        telemetry.addData("LSY:", lsy);
        telemetry.addData("LSX:", lsx);
        telemetry.addData("RSY:", rsy);
        telemetry.addData("RSX:", rsx);
        telemetry.addData("FR Power:", this.frPower);
        telemetry.addData("BR Power:", this.brPower);
        telemetry.addData("FL Power:", this.flPower);
        telemetry.addData("BL Power:", this.blPower);
        telemetry.addData("Strafe Direction", strafeDir);

        double avgVelocity;
        if (strafeDir != null) {
            double strafePower = (lsx + rsx) / 2;

            if (strafeDir.equals("left")) {
                telemetry.addData("Strafing Left", true);
                // Left wheels inward, right wheels outward
                fl.setPower(-strafePower);
                bl.setPower(strafePower);
                fr.setPower(strafePower);
                br.setPower(-strafePower);
            } else {
                telemetry.addData("Strafing Right", true);
                // Right wheels inward, left wheels outward
                fl.setPower(strafePower);
                bl.setPower(-strafePower);
                fr.setPower(-strafePower);
                br.setPower(strafePower);
            }
        } else if ((lsy < 0 && rsy < 0) || (lsy > 0 && rsy > 0)) {
            avgVelocity = -((lsy + rsy) / 2);
            fl.setPower(avgVelocity);
            fr.setPower(avgVelocity);
            bl.setPower(avgVelocity);
            br.setPower(avgVelocity);
        } else {
            fl.setPower(this.flPower);
            fr.setPower(this.frPower);
            bl.setPower(this.blPower);
            br.setPower(this.brPower);
        }

        telemetry.update();
    }
}
