package org.firstinspires.ftc.teamcode.PathFindingAutonomous;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;

import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name = "CloseToFlower")
public class CloseToFlower extends LinearOpMode {

    static double robotLength = 18;
    static double robotWidth = 18;
    static double robotStartPosX = 0;  //this is in terms of 1in
    static double robotStartPosY = 0;  //this is in terms of 1in
    static double robotCenterX = robotStartPosX + (robotWidth / 2);
    static double robotCenterY = robotStartPosY + (robotLength / 2);

    private DcMotorEx launcher = null;
    private DcMotor intake = null;
    private CRServo leftIntakeServo = null;
    private CRServo rightIntakeServo = null;
    private CRServo windmillServo = null;

    private MecanumDrive drive = null;
    private Pathfinder pathfind = null;
    private Pose2d currentPose = null;

    public final int LAUNCHER_TARGET_VELOCITY = 1265; //2678 RPM
    public final int LAUNCHER_MIN_VELOCITY = 1255; //2571 RPM

    public boolean velocityReached = false;

    @Override
    public void runOpMode() throws InterruptedException {

        intake = hardwareMap.get(DcMotor.class, "intake");
        launcher = hardwareMap.get(DcMotorEx.class, "launcher");
        windmillServo = hardwareMap.get(CRServo.class, "windmillServo");
        leftIntakeServo = hardwareMap.get(CRServo.class, "left_intake_servo");
        rightIntakeServo = hardwareMap.get(CRServo.class, "right_intake_servo");

        intake.setZeroPowerBehavior(BRAKE);

        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

        leftIntakeServo.setPower(0);
        rightIntakeServo.setPower(0);
        windmillServo.setPower(0);
        leftIntakeServo.setDirection(DcMotorSimple.Direction.REVERSE);
        rightIntakeServo.setDirection(DcMotorSimple.Direction.FORWARD);
        windmillServo.setDirection(DcMotorSimple.Direction.FORWARD);

        Pose2d startingPose = new Pose2d(144-robotCenterX, 94-robotCenterY, Math.toRadians(0));

        drive = new MecanumDrive(hardwareMap, startingPose);
        drive.localizer.setPose(startingPose);
        currentPose = drive.localizer.getPose();

        pathfind = new Pathfinder();

        waitForStart();

        if (isStopRequested()) return;
        sleep(10000);
        launch();


        startIntake();
        pathfindTo(120-robotLength, 90-robotLength, 0);
        driveDirectTo(142-robotLength, 100-robotLength, 25);
        driveDirectTo(142-robotLength, 100-robotLength, 0);
        driveDirectTo(142-robotLength, 100-robotLength, 25);
        driveDirectTo(142-robotLength, 100-robotLength, 0);
        sleep(1000);
        stopIntake();
        driveDirectTo(130-robotLength, 94-robotLength, 0);
        pathfindTo(100, 134, 0);

        while(!isStopRequested()){
            telemetry.addLine("Path finished");
            telemetry.addLine(Pathfinder.getPathDebug());
            telemetry.addLine(currentPose.position.x + ", " + currentPose.position.y + ", heading: " + currentPose.heading);
            telemetry.update();
        }
    }

    void launch() {
        /*
         * Calling gamepad1.right_bumper returns a boolean which will be true if the bumper is
         * held down, and false if it is not. Notably, this will continue to be true for every
         * cycle of our code that the driver holds down that bumper.
         * The first step of our launch() function is checking to see if the user is currently
         * holding down the right gamepad. If they are, then we want to start spinning up the launcher.
         * Otherwise, we start spinning the launcher down.
         */
        while(!velocityReached) {
            launcher.setVelocity(LAUNCHER_TARGET_VELOCITY);


            /*
             * Here we ask if the driver is currently pressing the right bumper, AND the launcher is
             * spinning fast enough to make a successful shot. If it is, then we will turn on the
             * windmill servo to start feeding the elements into the launcher motor. We also
             * add some power to the intake power. This can sometimes help dislodge stuck elements from
             * inside the hopper.
             */
            if (launcher.getVelocity() > LAUNCHER_MIN_VELOCITY) {
                windmillServo.setPower(1);
                velocityReached = true;
                sleep(4000);
                windmillServo.setPower(0);
                launcher.setVelocity(0);
            } else {
                windmillServo.setPower(0);
            }
        }
        velocityReached = false;
    }

    void telemetryDebugPathfind(){
        telemetry.addLine(Pathfinder.getPathDebug());
        telemetry.addLine("About to run path");
        telemetry.update();

        TelemetryPacket packet = new TelemetryPacket();
        Pathfinder.drawMap(packet.fieldOverlay());
        FtcDashboard.getInstance().sendTelemetryPacket(packet);
    }
    public void pathfindTo(double x, double y, double heading) {
        Action path = pathfind.pathfindToCoords(x, y, heading, currentPose, drive);
        telemetryDebugPathfind();
        Actions.runBlocking(path);
        currentPose = drive.localizer.getPose();
    }

    public void driveDirectTo(double x, double y, double heading) {
        Action path = pathfind.pathfindDirectToCoords(x, y, heading, currentPose, drive);
        telemetryDebugPathfind();
        Actions.runBlocking(path);
        currentPose = drive.localizer.getPose();
    }

    public void startIntake(){
        intake.setPower(1);
        leftIntakeServo.setPower(1);
        rightIntakeServo.setPower(1);
        sleep(100);
    }
    public void stopIntake(){
        intake.setPower(0);
        leftIntakeServo.setPower(0);
        rightIntakeServo.setPower(0);
    }
}