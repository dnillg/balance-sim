package com.dgiczi.balancesim.simulation.model;

public class SimulatorState {

    private final double posX; //mm
    private final double posY; //mm
    private final double tilt; //degree (+|-)
    private final double speed; //mm/s
    private final double tiltSpeed; //degPerSec (+|-)

    public SimulatorState(double posX, double posY, double tilt, double speed, double tiltSpeed) {
        this.posX = posX;
        this.posY = posY;
        this.tilt = tilt;
        this.speed = speed;
        this.tiltSpeed = tiltSpeed;
    }

    public SimulatorState withScale(double scale) {
        return new SimulatorState(
                posX * scale, posY * scale,  tilt, speed * scale, tiltSpeed);
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getTilt() {
        return tilt;
    }

    public double getSpeed() {
        return speed;
    }

    public double getTiltSpeed() {
        return tiltSpeed;
    }
}
