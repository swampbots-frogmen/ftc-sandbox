package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="StarterBot Autonomous", group="StarterBot")
public class StarterBotAutonomous extends LinearOpMode {
    // Declare OpMode members.
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotorEx launcher = null;
    private DcMotor intake = null;
    private CRServo leftIntakeServo = null;
    private CRServo rightIntakeServo = null;
    private CRServo windmillServo = null;

    public final int LAUNCHER_TARGET_VELOCITY = 1265; //2678 RPM
    public final int LAUNCHER_MIN_VELOCITY = 1255; //2571 RPM

    public boolean velocityReached = false;

    /*
     * These four variables store the power we need to apply to the motors. In other cases, we may
     * choose to declare these variables inside the mecanumDrive() function, instead we declare them
     * here so that we can access them in our main loop for telemetry.
     */
    double leftFrontPower;
    double rightFrontPower;
    double leftBackPower;
    double rightBackPower;

    double intakePower;

    // Declare runtime timer
    private ElapsedTime runtime = new ElapsedTime();
    double secondsPerInches = 1500/96;
    double secondsPerDegrees = Math.PI * secondsPerInches*2 * 1/360;

    @Override
    public void runOpMode() {
        /*
         * Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step.
         */
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        intake = hardwareMap.get(DcMotor.class, "intake");
        launcher = hardwareMap.get(DcMotorEx.class, "launcher");
        windmillServo = hardwareMap.get(CRServo.class, "windmillServo");
        leftIntakeServo = hardwareMap.get(CRServo.class, "left_intake_servo");
        rightIntakeServo = hardwareMap.get(CRServo.class, "right_intake_servo");

        /*
         * To drive forward, most robots need the motor on one side to be reversed,
         * because the axles point in opposite directions. Pushing the left stick forward
         * MUST make robot go forward. So adjust these two lines based on your first test drive.
         * Note: The settings here assume direct drive on left and right wheels. Gear
         * Reduction or 90 Deg drives may require direction flips
         */
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        /*
         * Setting zeroPowerBehavior to BRAKE enables a "brake mode". This causes the motor to
         * slow down much faster when it is coasting. This creates a much more controllable
         * drivetrain. As the robot stops much quicker.
         */
        leftFrontDrive.setZeroPowerBehavior(BRAKE);
        rightFrontDrive.setZeroPowerBehavior(BRAKE);
        leftBackDrive.setZeroPowerBehavior(BRAKE);
        rightBackDrive.setZeroPowerBehavior(BRAKE);
        intake.setZeroPowerBehavior(BRAKE);

        /*
         * Here we set our launcher to the RUN_USING_ENCODER runmode.
         * If you notice that you have no control over the velocity of the motor, it just jumps
         * right to a number much higher than your set point, make sure that your encoders are plugged
         * into the port right beside the motor itself. And that the motors polarity is consistent
         * through any wiring.
         */

        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

        /*
         * set Feeders to an initial value to initialize the servo controller
         */
        leftIntakeServo.setPower(0);
        rightIntakeServo.setPower(0);
        windmillServo.setPower(0);

        /*
         * Much like our drivetrain motors, we set the right intake servo to reverse so that both
         * servos work to pull elements into the intake.
         */
        leftIntakeServo.setDirection(DcMotorSimple.Direction.REVERSE);
        rightIntakeServo.setDirection(DcMotorSimple.Direction.FORWARD);
        windmillServo.setDirection(DcMotorSimple.Direction.FORWARD);

        /*
         * Tell the driver that initialization is complete.
         */
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // 3. Wait for the driver to press PLAY
        waitForStart();
        runtime.reset();

        // 4. Step 1: Drive Forward for 2.0 seconds
        if (opModeIsActive()) {
          //  setDrivePower(0.5, 0.5, 0.5, 0.5);
         launch();
        /* sleep(1000);
         backward(5);
         sleep(200);
         right(44);
            sleep(200);
            intake.setPower(1);
            leftIntakeServo.setPower(1);
            rightIntakeServo.setPower(1);
            sleep(200);
         forward(5);
            sleep(2000);
            intake.setPower(0);
            leftIntakeServo.setPower(0);
            rightIntakeServo.setPower(0);
            sleep(200);
            backward(5);
            sleep(200);
            left(44);
            sleep(200);
            forward(5);
            sleep(200);*/

         //   turnRight(360);
          /*  runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 1.5)) {
                telemetry.addData("Path", "Leg 1: Drive Forward");
                telemetry.addData("Time", "%2.5f S", runtime.seconds());
                telemetry.update();
            }

            // Step 2: Stop for 1.0 second
            //stopRobot();
            //sleep(1000);

            // Step 3: Spin Right for 1.5 seconds
           // setDrivePower(-0.5, -0.5, -0.5, -0.5);

            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 1.5)) {
                telemetry.addData("Path", "Leg 2: Spin Right");
                telemetry.addData("Time", "%2.5f S", runtime.seconds());
                telemetry.update();
            }

            // Always stop the robot at the end of the autonomous period
            stopRobot();*/
            telemetry.addData("Path", "Complete");
            telemetry.update();
        }
    }

    // Helper method to apply power to all motors easily
    public void setDrivePower(double lf, double rf, double lb, double rb) {
        leftFrontDrive.setPower(lf);
        rightFrontDrive.setPower(rf);
        leftBackDrive.setPower(lb);
        rightBackDrive.setPower(rb);
    }

    public void forward(double inches){
        setDrivePower(0.5, 0.5, 0.5, 0.5);
        sleep((long)(inches*secondsPerInches+250));
        stopRobot();
    }
    public void backward(double inches){
        setDrivePower(-0.5, -0.5, -0.5, -0.5);
        sleep((long)(inches*secondsPerInches+250));
        stopRobot();
    }
    public void left(double inches){
        setDrivePower(0.5, 0.5, -0.5, -0.5);
        sleep((long)(inches*secondsPerInches +250));
        stopRobot();
    }
    public void right(double inches){
        setDrivePower(-0.5, -0.5, 0.5, 0.5);
        sleep((long)(inches*secondsPerInches+250));
        stopRobot();
    }
    public void turnRight(double degrees) {
        setDrivePower(0.5, -0.5, 0.5, -0.5);
        sleep((long)(secondsPerDegrees*degrees+250));
        stopRobot();
    }
    public void turnLeft(double degrees){
        setDrivePower(-0.5, 0.5, -0.5, 0.5);
        sleep((long)(secondsPerDegrees*degrees+250));
        stopRobot();
    }

    // Helper method to stop all motors
    public void stopRobot() {
        setDrivePower(0, 0, 0, 0);
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
                sleep(6000);
                windmillServo.setPower(0);
                launcher.setVelocity(0);
            } else {
                windmillServo.setPower(0);
            }
        }
    }
}
