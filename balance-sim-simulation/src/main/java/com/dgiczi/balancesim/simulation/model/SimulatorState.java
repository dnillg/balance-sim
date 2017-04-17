package com.dgiczi.balancesim.simulation.model;

public class SimulatorState {

    private final double disatance; //mm
    private final double tilt; //degree (+|-)
    private final double wheelSpeed; //degPerSec (+|-)
    private final double tiltSpeed; //degPerSec (+|-)

    public SimulatorState(double disatance, double tilt, double wheelSpeed, double tiltSpeed) {
        this.disatance = disatance;
        this.tilt = tilt;
        this.wheelSpeed = wheelSpeed;
        this.tiltSpeed = tiltSpeed;
    }

    public double getDistance() {
        return disatance;
    }

    public double getTilt() {
        return tilt;
    }

    public double getWheelSpeed() {
        return wheelSpeed;
    }

    public double getTiltSpeed() {
        return tiltSpeed;
    }
}
