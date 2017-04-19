package com.dgiczi.balancesim.simulation.model;


import com.dgiczi.balancesim.simulation.math.Triangle;
import com.sun.javafx.fxml.builder.TriangleMeshBuilder;
import javafx.scene.shape.TriangleMesh;

public class SimulatorParams {

    private final double bodyWidth; //mm
    private final double bodyHeight; //mm
    private final double wheelRadius; //mm
    private final double bodyMass; //kg;
    private final double wheelsMass; //kg;
    private final double centroidX; //mm
    private final double centroidY; //mm
    private final double maxTorque;
    private final double maxSpeed; //unitPerSec

    public SimulatorParams(double bodyWidth, double bodyHeight, double wheelRadius, double bodyMass, double wheelsMass,
                           double centroidX, double centroidY, double maxTorque, double maxSpeed) {
        this.bodyWidth = bodyWidth;
        this.bodyHeight = bodyHeight;
        this.wheelRadius = wheelRadius;
        this.bodyMass = bodyMass;
        this.wheelsMass = wheelsMass;
        this.centroidX = centroidX;
        this.centroidY = centroidY;
        this.maxTorque = maxTorque;
        this.maxSpeed = maxSpeed;
    }

    public SimulatorParams withScale(double scale) {
        return new SimulatorParams(
                bodyWidth * scale, bodyHeight * scale, wheelRadius * scale,
                bodyMass, wheelsMass, centroidX * scale,
                centroidY * scale, maxTorque, maxSpeed * scale);
    };

    public double getBodyWidth() {
        return bodyWidth;
    }

    public double getBodyHeight() {
        return bodyHeight;
    }

    public double getWheelRadius() {
        return wheelRadius;
    }

    public double getWheelDiameter() {
        return wheelRadius * 2;
    }

    public double getBodyMass() {
        return bodyMass;
    }

    public double getWheelsMass() {
        return wheelsMass;
    }

    public double getCentroidX() {
        return centroidX;
    }

    public double getCentroidY() {
        return centroidY;
    }

    public double getMaxTorque() {
        return maxTorque;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }
}
