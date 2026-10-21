package org.firstinspires.ftc.teamcode.PathFindingAutonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.PathFindingAutonomous.Pathfinder;

@Autonomous(name = "PathfinderTest")
public class PathfinderTest extends LinearOpMode {

    static double robotLength = 16.75;
    static double robotWidth = 16.75;
    static double robotStartPosX = 0;  //this is in terms of 1in
    static double robotStartPosY = 0;  //this is in terms of 1in
    static double robotCenterX = robotStartPosX + (robotWidth / 2);
    static double robotCenterY = robotStartPosY + (robotLength / 2);

    @Override
    public void runOpMode() throws InterruptedException {



        Pose2d startingPose = new Pose2d(robotCenterX, robotCenterY, Math.toRadians(0));



        MecanumDrive drive = new MecanumDrive(hardwareMap, startingPose);

        drive.localizer.setPose(startingPose);

        Pose2d currentPose = drive.localizer.getPose();


        Pathfinder pathfind = new Pathfinder();

        waitForStart();

        if (isStopRequested()) return;


        Action path = pathfind.pathfindToCoords(10, 118, 180, currentPose, drive);//counterclockwise
        telemetry.addLine(Pathfinder.getPathDebug());
        telemetry.addLine("About to run path");
        telemetry.update();
        Actions.runBlocking(path);


        currentPose = drive.localizer.getPose();



        while(!isStopRequested()){
            telemetry.addLine("Path finished");
            telemetry.addLine(Pathfinder.getPathDebug());
            telemetry.addLine(currentPose.position.x + ", " + currentPose.position.y + ", heading: " + currentPose.heading);
            telemetry.update();
        }
    }
}