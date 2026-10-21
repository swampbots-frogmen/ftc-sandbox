package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.DualNum;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.Twist2dDual;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Vector2dDual;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.FlightRecorder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.messages.ThreeDeadWheelInputsMessage;

@Config
public final class ThreeDeadWheelLocalizer implements Localizer {
    public static class Params {
        public double par0YTicks = -2059.2262311944878018457025723953; // y position of the first parallel encoder (in tick units)
        public double par1YTicks = 2197.5716654771178224628047837069; // y position of the second parallel encoder (in tick units)
        public double perpXTicks = -1975.6698860712427505670385966247; // x position of the perpendicular encoder (in tick units)
    }

    //Bc roadrunner isnt accurate for this, use these equations for each.
    /*
    Do 4 360 degree rotations, and find par0 ticks par1 ticks, and perp ticks. find par1 - par0 ticks.
    Make sure you rotate counterclockwise. If you rotate clockwise by accident, negate all values.
    Set it to this: L = (par1-par0)/8pi.  The pi will be different if you do less or more rotations, but for r4 full rotations, 8pi.
     For averageL, use this equation. averageL = L/2
    For perpXTicks, take perp and divide by 8pi. perpXTicks = perp/8pi.

    Next, we want to find the difference of the magnitudes of the par wheels. The magnitude is just the absolute value of their value.
    Use this equation to find the difference and always assume abs() = absolute value of.
    DifferenceM = abs(abs(par1) - abs(par0))
    Also find the average when added using this equation.
    AverageM = (abs(par1) + abs(par0))/2

    With that, you can find the percentage offset between the two using this equation.
    DifferenceM/AverageM = PercentOffset.
    Finally, you want to divide by two for the averagePercentageOffsetSpread.
    averagePercentageOffsetSpread = PercentageOffset/2

    Finally, to determine the par0YTicks and par1YTicks, use the averagePercentageOffsetSpread.
    Using your original values, use these equations.
    For abs(par0) > abs(par1) use:
    par0YTicks = averageL*(1+averagePercentageOffsetSpread)
    par1YTicks = averageL*(1-averagePercentageOffsetSpread)
    For abs(par0) < abs(par1) use:
    par0YTicks = averageL*(1-averagePercentageOffsetSpread)
    par1YTicks = averageL*(1+averagePercentageOffsetSpread)

    Finally, if par0 is negative, then negate the value of par0YTicks, and if par1 is negative, then negate the value of par1YTicks.
    You can find trackWidthTicks by subbing it in for L usually.
 */

    public static Params PARAMS = new Params();

    public final Encoder par0, par1, perp;

    public final double inPerTick;

    private int lastPar0Pos, lastPar1Pos, lastPerpPos;
    private boolean initialized;
    private Pose2d pose;

    public ThreeDeadWheelLocalizer(HardwareMap hardwareMap, double inPerTick, Pose2d initialPose) {
        // TODO: make sure your config has **motors** with these names (or change them)
        //   the encoders should be plugged into the slot matching the named motor
        //   see https://ftc-docs.firstinspires.org/en/latest/hardware_and_software_configuration/configuring/index.html
        par0 = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "rightFront")));//
        par1 = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "leftFront")));//
        perp = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "leftRear")));//


        par1.setDirection(DcMotorSimple.Direction.REVERSE);
        perp.setDirection(DcMotorSimple.Direction.REVERSE);

        // TODO: reverse encoder directions if needed
        //   par0.setDirection(DcMotorSimple.Direction.REVERSE);

        this.inPerTick = inPerTick;

        FlightRecorder.write("THREE_DEAD_WHEEL_PARAMS", PARAMS);

        pose = initialPose;
    }

    @Override
    public void setPose(Pose2d pose) {
        this.pose = pose;
    }

    @Override
    public Pose2d getPose() {
        return pose;
    }

    @Override
    public PoseVelocity2d update() {
        PositionVelocityPair par0PosVel = par0.getPositionAndVelocity();
        PositionVelocityPair par1PosVel = par1.getPositionAndVelocity();
        PositionVelocityPair perpPosVel = perp.getPositionAndVelocity();

        FlightRecorder.write("THREE_DEAD_WHEEL_INPUTS", new ThreeDeadWheelInputsMessage(par0PosVel, par1PosVel, perpPosVel));

        if (!initialized) {
            initialized = true;

            lastPar0Pos = par0PosVel.position;
            lastPar1Pos = par1PosVel.position;
            lastPerpPos = perpPosVel.position;

            return new PoseVelocity2d(new Vector2d(0.0, 0.0), 0.0);
        }

        int par0PosDelta = par0PosVel.position - lastPar0Pos;
        int par1PosDelta = par1PosVel.position - lastPar1Pos;
        int perpPosDelta = perpPosVel.position - lastPerpPos;

        Twist2dDual<Time> twist = new Twist2dDual<>(
                new Vector2dDual<>(
                        new DualNum<Time>(new double[] {
                                (PARAMS.par0YTicks * par1PosDelta - PARAMS.par1YTicks * par0PosDelta) / (PARAMS.par0YTicks - PARAMS.par1YTicks),
                                (PARAMS.par0YTicks * par1PosVel.velocity - PARAMS.par1YTicks * par0PosVel.velocity) / (PARAMS.par0YTicks - PARAMS.par1YTicks),
                        }).times(inPerTick),
                        new DualNum<Time>(new double[] {
                                (PARAMS.perpXTicks / (PARAMS.par0YTicks - PARAMS.par1YTicks) * (par1PosDelta - par0PosDelta) + perpPosDelta),
                                (PARAMS.perpXTicks / (PARAMS.par0YTicks - PARAMS.par1YTicks) * (par1PosVel.velocity - par0PosVel.velocity) + perpPosVel.velocity),
                        }).times(inPerTick)
                ),
                new DualNum<>(new double[] {
                        (par0PosDelta - par1PosDelta) / (PARAMS.par0YTicks - PARAMS.par1YTicks),
                        (par0PosVel.velocity - par1PosVel.velocity) / (PARAMS.par0YTicks - PARAMS.par1YTicks),
                })
        );

        lastPar0Pos = par0PosVel.position;
        lastPar1Pos = par1PosVel.position;
        lastPerpPos = perpPosVel.position;

        pose = pose.plus(twist.value());
        return twist.velocity().value();
    }
}
