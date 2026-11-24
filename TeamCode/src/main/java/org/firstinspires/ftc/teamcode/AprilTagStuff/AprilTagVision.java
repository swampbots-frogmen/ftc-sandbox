package org.firstinspires.ftc.teamcode.AprilTagStuff;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.lang.reflect.Field;
import java.util.List;

public class AprilTagVision {

    private final AprilTagProcessor aprilTag;
    private final VisionPortal visionPortal;
    private final double cameraYawOffset = 180.0;
    private final double cameraPitchOffset = 48.3;

    // Camera offsets relative to robot center (top-left origin frame)
    private double downFromCenter = 1.00;  // Offset in -Z / down direction from robot center (inches) basically down is positive, not negative.
    private double rightFromCenter = -1.00; // Offset in +X / right direction from robot center (inches)

    // 3D Tag Position Vectors (X, Y, Z in inches)
    public static final Position BLUE_AUDIENCE_POS = new Position(DistanceUnit.INCH, 56.7, 48.2, 57.5, 0);
    public static final Position RED_AUDIENCE_POS  = new Position(DistanceUnit.INCH, 87.3, 48.2, 57.5, 0);
    public static final Position BLUE_SCORING_POS  = new Position(DistanceUnit.INCH, 56.7, 48.2, 86.5, 0);
    public static final Position RED_SCORING_POS   = new Position(DistanceUnit.INCH, 87.3, 48.2, 86.5, 0);



    /**
     * Default constructor using standard camera ("Webcam 1" or "Webcam").
     */
    public AprilTagVision(HardwareMap hardwareMap) {
        this(hardwareMap, "Webcam 1");
    }

    /**
     * Constructor specifying camera name.
     */
    public AprilTagVision(HardwareMap hardwareMap, String cameraName) {
        this(hardwareMap, cameraName, 3);
    }

    /**
     * Overloaded constructor for legacy signature compatibility.
     */
    public AprilTagVision(HardwareMap hardwareMap,
                          double param1, double param2, double param3,
                          double param4, double param5, double param6) {
        this(hardwareMap, "Webcam 1");
    }

    /**
     * Overloaded constructor for legacy signature compatibility.
     */
    public AprilTagVision(HardwareMap hardwareMap, String cameraName,
                          double param1, double param2, double param3,
                          double param4, double param5, double param6) {
        this(hardwareMap, cameraName);
    }

    /**
     * Full constructor allowing camera name and decimation setting.
     */
    public AprilTagVision(HardwareMap hardwareMap, String cameraName, int decimation) {

        aprilTag = new AprilTagProcessor.Builder().build();

        if (decimation > 0) {
            aprilTag.setDecimation(decimation);
        }

        WebcamName webcamName = null;
        if (cameraName != null && !cameraName.trim().isEmpty()) {
            try {
                webcamName = hardwareMap.get(WebcamName.class, cameraName);
            } catch (Exception ignored) {}
        }
        if (webcamName == null) {
            try {
                webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
            } catch (Exception ignored) {}
        }
        if (webcamName == null) {
            try {
                webcamName = hardwareMap.get(WebcamName.class, "Webcam");
            } catch (Exception ignored) {}
        }
        if (webcamName == null && cameraName != null) {
            webcamName = hardwareMap.get(WebcamName.class, cameraName);
        }

        visionPortal = new VisionPortal.Builder()
                .setCamera(webcamName)
                .addProcessor(aprilTag)
                .enableLiveView(true)
                .build();
    }

    public List<AprilTagDetection> getDetections() {
        return aprilTag.getDetections();
    }

    public AprilTagProcessor getProcessor() {
        return aprilTag;
    }

    public VisionPortal getVisionPortal() {
        return visionPortal;
    }

    public void setDecimation(float decimation) {
        aprilTag.setDecimation(decimation);
    }

    public void close() {
        visionPortal.close();
    }

    /**
     * Calculates and returns the robot's RoadRunner Pose2d (X, Z, heading) on the field
     * based on the detected AprilTag. Returns null if no valid pose is computed.
     */
    public Pose2d ApriltagLocalizationCalc(AprilTagDetection detection) {
        if (detection == null) return null;

        AprilTagPoseFtc ftcPose = getTagPose(detection);
        if (ftcPose == null) return null;

        double magnitude = ftcPose.range;
        double bearing   = ftcPose.bearing;   // Horizontal direction angle to tag center
        double elevation = ftcPose.elevation; // Vertical direction angle to tag center

        double relativeYaw = ftcPose.yaw;




        String tagName = getTagName(detection);
        int tagId = getTagId(detection);

        double tagYawOffset = 0.0;
        double tagPitchOffset = 29.2;
        int caseId = 5;

        if (tagName.equalsIgnoreCase("RED AUDIENCE")) {
            tagYawOffset = 0.0;
            tagPitchOffset = 90.0-29.2;  //Subtract from 90 since tag is 29.2 degrees below 90.0 degrees.
            caseId = 0;
        } else if (tagName.equalsIgnoreCase("BLUE AUDIENCE")) {
            tagYawOffset = 0.0;
            tagPitchOffset = 90.0-29.2;
            caseId = 1;
        } else if (tagName.equalsIgnoreCase("RED SCORING")) {
            tagYawOffset = 180.0;
            tagPitchOffset = 90.0-29.2;
            caseId = 2;
        } else if (tagName.equalsIgnoreCase("BLUE SCORING")) {
            tagYawOffset = 180.0;
            tagPitchOffset = 90.0-29.2;
            caseId = 3;
        }

        double originalPhiy   = Math.toRadians(cameraYawOffset);

        double phip   = Math.toRadians(cameraPitchOffset);
        double thetap = Math.toRadians(tagPitchOffset);
        double thetay = Math.toRadians(tagYawOffset);
        double yaw    = Math.toRadians(bearing);
        double pitch  = Math.toRadians(elevation);
        double M      = magnitude;
        double tcyaw = Math.toRadians(relativeYaw);

        double yaw2 = thetay - tcyaw - originalPhiy;

        double phiy = originalPhiy + yaw2;

             double Fx = M*cos(yaw)*cos(pitch)*cos(phiy-thetay) + M*sin(pitch)*sin(thetap)*(sin(thetay-phiy)) +
                     M*cos(pitch)*sin(yaw)*cos(thetap)*sin(phiy-thetay);

             double Fy = M*cos(yaw)*cos(pitch)*sin(phip)*sin(phiy-thetay) + M*sin(pitch)*cos(thetap)*cos(phip) +
                    M*sin(pitch)*sin(thetap)*sin(phip)*cos(phiy-thetay) + M*cos(pitch)*sin(yaw)*sin(thetap)*cos(phip) -
                    M*cos(pitch)*sin(yaw)*sin(phip)*cos(thetap)*(cos(phiy-thetay));

             double Fz = M*cos(yaw)*cos(pitch)*cos(phip)*sin(thetay-phiy) + M*sin(pitch)*sin(phip)*cos(thetap) -
                    M*sin(pitch)*sin(thetap)*cos(phip)*cos(phiy-thetay) + M*cos(pitch)*sin(yaw)*sin(thetap)*sin(phip) +
                    M*cos(pitch)*sin(yaw)*cos(phip)*cos(thetap)*(cos(phiy-thetay));



        Position tagPos = null;

        switch (caseId) {
            case 0: // RED AUDIENCE
                tagPos = RED_AUDIENCE_POS;
                break;
            case 1: // BLUE AUDIENCE
                tagPos = BLUE_AUDIENCE_POS;
                break;
            case 2: // RED SCORING
                tagPos = RED_SCORING_POS;
                break;
            case 3: // BLUE SCORING
                tagPos = BLUE_SCORING_POS;
                break;
            default:
                return null;
        }

        // 1. Calculate Camera Field Position
        double cameraX = tagPos.x - Fx;
        double cameraY = tagPos.y - Fy;
        double cameraZ = tagPos.z - Fz;

        double distanceToCenterX = rightFromCenter;
        double distanceToCenterZ = downFromCenter;

        double M2 = Math.sqrt(distanceToCenterX*distanceToCenterX + distanceToCenterZ*distanceToCenterZ);

        //cYawOffset = atan2(distanceToCenterZ/distanceToCenterX)

        double cYawOffset = Math.atan2(distanceToCenterZ,distanceToCenterX);




        // TrueXDistance = Mcos(yaw2 + cYawOffset), TrueZDistance = Msin(yaw2 + cYawOffset)

        double TrueXDistance = M2*cos(yaw2 + cYawOffset);
        double TrueZDistance = M2*sin(yaw2 + cYawOffset);

        //Now, we need to localize it from the camera to the center of the robot.

        double trueXCoord = cameraX - TrueXDistance;
        double trueZCoord = cameraZ - TrueZDistance;
        double trueBearing = yaw2;
        //the z is in x and x in z spot bc roadrunner values are different.
        return new Pose2d(trueZCoord, trueXCoord, trueBearing);
    }

    public Pose2d ApriltagLocalizationCalc() {
        List<AprilTagDetection> detections = getDetections();
        if (detections != null) {
            for (AprilTagDetection detection : detections) {
                if (detection != null) {
                    Pose2d pose = ApriltagLocalizationCalc(detection);
                    if (pose != null) {
                        return pose;
                    }
                }
            }
        }
        return null;
    }

    public static int getTagId(AprilTagDetection detection) {
        if (detection == null) return -1;
        try {
            Field idField = detection.getClass().getField("id");
            return idField.getInt(detection);
        } catch (Exception ignored) {}
        try {
            Field metaField = detection.getClass().getField("metadata");
            Object meta = metaField.get(detection);
            if (meta != null) {
                Field idField = meta.getClass().getField("id");
                return idField.getInt(meta);
            }
        } catch (Exception ignored) {}
        return -1;
    }

    public static String getTagName(AprilTagDetection detection) {
        if (detection == null) return "Unknown";
        try {
            Field metaField = detection.getClass().getField("metadata");
            Object meta = metaField.get(detection);
            if (meta != null) {
                Field nameField = meta.getClass().getField("name");
                Object name = nameField.get(meta);
                if (name != null) return name.toString();
            }
        } catch (Exception ignored) {}
        return "Unknown";
    }

    public static AprilTagPoseFtc getTagPose(AprilTagDetection detection) {
        if (detection == null) return null;
        try {
            Field poseField = detection.getClass().getField("ftcPose");
            Object pose = poseField.get(detection);
            if (pose instanceof AprilTagPoseFtc) {
                return (AprilTagPoseFtc) pose;
            }
        } catch (Exception ignored) {}
        return null;
    }

    public boolean isTagVisible(int... targetIDs) {
        for (AprilTagDetection detection : aprilTag.getDetections()) {
            if (detection != null) {
                int tagId = getTagId(detection);
                for (int id : targetIDs) {
                    if (tagId == id) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isTagVisible(String... targetNames) {
        for (AprilTagDetection detection : aprilTag.getDetections()) {
            if (detection != null) {
                String tagName = getTagName(detection);
                int tagId = getTagId(detection);
                for (String targetName : targetNames) {
                    if (tagName != null && tagName.equalsIgnoreCase(targetName)) {
                        return true;
                    }
                    if (targetName != null && String.valueOf(tagId).equalsIgnoreCase(targetName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean detectWithRetries(LinearOpMode opMode, String... targetNames) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            if (isTagVisible(targetNames)) {
                return true;
            }
            if (attempt < 3 && opMode.opModeIsActive()) {
                opMode.sleep(100);
            }
        }
        return false;
    }

    public boolean detectWithRetries(LinearOpMode opMode, int... targetIDs) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            if (isTagVisible(targetIDs)) {
                return true;
            }
            if (attempt < 3 && opMode.opModeIsActive()) {
                opMode.sleep(100);
            }
        }
        return false;
    }

    public boolean waitForTag(
            LinearOpMode opMode,
            long timeoutMs,
            int... targetIDs) {

        long startTime = System.currentTimeMillis();
        int consecutiveDetections = 0;

        while (opMode.opModeIsActive()
                && System.currentTimeMillis() - startTime < timeoutMs) {

            if (isTagVisible(targetIDs)) {
                consecutiveDetections++;

                if (consecutiveDetections >= 3) {
                    return true;
                }
            } else {
                consecutiveDetections = 0;
            }

            opMode.sleep(50);
        }

        return false;
    }

    public boolean waitForTag(
            LinearOpMode opMode,
            long timeoutMs,
            String... targetNames) {

        long startTime = System.currentTimeMillis();
        int consecutiveDetections = 0;

        while (opMode.opModeIsActive()
                && System.currentTimeMillis() - startTime < timeoutMs) {

            if (isTagVisible(targetNames)) {
                consecutiveDetections++;

                if (consecutiveDetections >= 3) {
                    return true;
                }
            } else {
                consecutiveDetections = 0;
            }

            opMode.sleep(50);
        }

        return false;
    }
    public double sin(double value){
        return Math.sin(value);
    }
    public double cos(double value){
        return Math.cos(value);
    }
}