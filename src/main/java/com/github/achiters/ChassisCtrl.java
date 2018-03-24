package com.github.achiters;

import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class ChassisCtrl {

    private static double WHEEL_DIAMETER = 43.2;
    private static double WHEEL_OFFSET = 60;
    private static String WHEEL_1_PORT_NAME = "A";
    private static String WHEEL_2_PORT_NAME = "D";
    private static double ROTATE_ANGLE = 90;
    private static double MOVE_DISTANCE = 1;

    private final MovePilot pilot;

    public synchronized void turnLeft() {
        pilot.rotate(ROTATE_ANGLE);
    }

    public synchronized void turnRight() {
        pilot.rotate(-ROTATE_ANGLE);
    }

    public synchronized void moveForward() {
        pilot.travel(MOVE_DISTANCE);
    }

    public synchronized void moveBack() {
        pilot.travel(-MOVE_DISTANCE);
    }

    public ChassisCtrl() {
        EV3 ev3 = LocalEV3.get();

        RegulatedMotor motor1 = new EV3LargeRegulatedMotor(ev3.getPort(WHEEL_1_PORT_NAME));
        RegulatedMotor motor2 = new EV3LargeRegulatedMotor(ev3.getPort(WHEEL_2_PORT_NAME));

        Wheel wheel1 = WheeledChassis.modelWheel(motor1, WHEEL_DIAMETER).offset(WHEEL_OFFSET);
        Wheel wheel2 = WheeledChassis.modelWheel(motor2, WHEEL_DIAMETER).offset(-WHEEL_OFFSET);

        Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, WheeledChassis.TYPE_DIFFERENTIAL);

        pilot = new MovePilot(chassis);
    }
}
