package org.firstinspires.ftc.teamcode.AprilTagStuff;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;

import java.util.List;

@TeleOp(name = "AprilTag Test")
public class AprilTagTest extends LinearOpMode {

    @Override
    public void runOpMode() {

        AprilTagVision vision = new AprilTagVision(hardwareMap, "Webcam 1");

        telemetry.addLine("Camera initialized");
        telemetry.addLine("Press PLAY to begin");
        telemetry.update();

        waitForStart();

        try {
            while (opModeIsActive()) {

                List<AprilTagDetection> detections = vision.getDetections();

                telemetry.addData("# Detections", detections.size());

                int detectedNumber = 0;
                String detectedTagLabel = "None";

                if (vision.isTagVisible("RED AUDIENCE")) {
                    detectedNumber = 1;
                    detectedTagLabel = "RED AUDIENCE";
                } else if (vision.isTagVisible("BLUE AUDIENCE")) {
                    detectedNumber = 2;
                    detectedTagLabel = "BLUE AUDIENCE";
                } else if (vision.isTagVisible("RED SCORING")) {
                    detectedNumber = 3;
                    detectedTagLabel = "RED SCORING";
                } else if (vision.isTagVisible("BLUE SCORING")) {
                    detectedNumber = 4;
                    detectedTagLabel = "BLUE SCORING";
                }

                telemetry.addData("Detected Number (1-4)", detectedNumber);
                telemetry.addData("Detected Tag Name", detectedTagLabel);

                // --- Final Calculated Robot Pose2d ---
                Pose2d finalRobotPose = vision.ApriltagLocalizationCalc();
                if (finalRobotPose != null) {
                    telemetry.addLine("\n=== FINAL ROBOT POSE (RoadRunner) ===");
                    telemetry.addData("  Pose X (RR Depth)", "%.2f in", finalRobotPose.position.x);
                    telemetry.addData("  Pose Y (RR Lateral)", "%.2f in", finalRobotPose.position.y);
                    telemetry.addData("  Heading (rad)", "%.3f rad", finalRobotPose.heading.toDouble());
                    telemetry.addData("  Heading (deg)", "%.1f°", Math.toDegrees(finalRobotPose.heading.toDouble()));
                } else {
                    telemetry.addLine("\n=== FINAL ROBOT POSE ===");
                    telemetry.addLine("  No target tag detected for calculation");
                }

                telemetry.addLine("------------------------------------");

                for (AprilTagDetection detection : detections) {
                    if (detection != null) {
                        int tagId = AprilTagVision.getTagId(detection);
                        String tagName = AprilTagVision.getTagName(detection);

                        telemetry.addLine(String.format("\n--- Tag ID: %d (%s) ---", tagId, tagName));

                        // Tag-specific calculated RoadRunner Pose2d
                        Pose2d tagPose = vision.ApriltagLocalizationCalc(detection);
                        if (tagPose != null) {
                            telemetry.addData("  Calc Robot X", "%.2f in", tagPose.position.x);
                            telemetry.addData("  Calc Robot Y", "%.2f in", tagPose.position.y);
                            telemetry.addData("  Calc Robot Heading", "%.1f°", Math.toDegrees(tagPose.heading.toDouble()));
                        }

                        // Raw Camera-Relative Metrics (ftcPose)
                        AprilTagPoseFtc ftcPose = AprilTagVision.getTagPose(detection);
                        if (ftcPose != null) {
                            telemetry.addData("  Cam Rel X", "%.2f in", ftcPose.x);
                            telemetry.addData("  Cam Rel Y", "%.2f in", ftcPose.y);
                            telemetry.addData("  Cam Rel Z", "%.2f in", ftcPose.z);
                            telemetry.addData("  Range (M)", "%.2f in", ftcPose.range);
                            telemetry.addData("  Bearing (horiz)", "%.1f°", ftcPose.bearing);
                            telemetry.addData("  Elevation (vert)", "%.1f°", ftcPose.elevation);
                            telemetry.addData("  Tag Yaw", "%.1f°", ftcPose.yaw);
                            telemetry.addData("  Tag Pitch", "%.1f°", ftcPose.pitch);
                            telemetry.addData("  Tag Roll", "%.1f°", ftcPose.roll);
                        }
                    }
                }

                telemetry.update();
                sleep(20);
            }
        } finally {
            vision.close();
        }
    }
}