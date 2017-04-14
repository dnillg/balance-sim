package com.dgiczi.balancesim.render.model;

public class SceneParams {

    private double width; //mm
    private double height; //mm
    private double wheelRadius; //mm
    private double mmPerPx;

    public SceneParams(double width, double height, double wheelRadius, double mmPerPx) {
        this.height = height;
        this.width = width;
        this.wheelRadius = wheelRadius;
        this.mmPerPx = mmPerPx;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getWheelRadius() {
        return wheelRadius;
    }

    public double getMmPerPx() {
        return mmPerPx;
    }
}
