package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Drawing;

@TeleOp(name = "Odometry Encoder Test")
public class OdometryTest extends LinearOpMode {

    @Override
    public void runOpMode() {

        Encoder par0 = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "rightFront")));//
        Encoder par1 = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "leftFront")));//
        Encoder perp = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "leftRear")));//

        // Apply the same direction reversals here
        par1.setDirection(DcMotorSimple.Direction.REVERSE);
        perp.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addLine("=== Odometry Encoder Test ===");
        telemetry.addLine("Press START, then move the robot.");
        telemetry.addLine("Encoder positions will be recorded at START.");
        telemetry.update();

        waitForStart();

        // Record encoder positions when the test starts
        int startPar0 = par0.getPositionAndVelocity().position;
        int startPar1 = par1.getPositionAndVelocity().position;
        int startPerp = perp.getPositionAndVelocity().position;

        while (opModeIsActive()) {

            // Current encoder positions
            int currentPar0 = par0.getPositionAndVelocity().position;
            int currentPar1 = par1.getPositionAndVelocity().position;
            int currentPerp = perp.getPositionAndVelocity().position;

            // Movement since START
            int deltaPar0 = currentPar0 - startPar0;
            int deltaPar1 = currentPar1 - startPar1;
            int deltaPerp = currentPerp - startPerp;

            // Difference between the two parallel encoders.
            // This is the quantity used to determine robot rotation.
            int parallelDifference = deltaPar0 - deltaPar1;

            telemetry.addLine("=== Current Position ===");
            telemetry.addData("Par 0", currentPar0);
            telemetry.addData("Par 1", currentPar1);
            telemetry.addData("Perp", currentPerp);

            telemetry.addLine("");
            telemetry.addLine("=== Movement Since START ===");
            telemetry.addData("Par 0 Δ", deltaPar0);
            telemetry.addData("Par 1 Δ", deltaPar1);
            telemetry.addData("Perp Δ", deltaPerp);

            telemetry.addLine("");
            telemetry.addLine("=== Rotation Data ===");
            telemetry.addData("Par 0 - Par 1", parallelDifference);

            telemetry.update();

            sleep(20);
        }
    }
}