package com.dgiczi.balancesim.render.model;

public class RenderParams {

    private final double bodyWidth; //mm
    private final double bodyHeight; //mm
    private final double wheelRadius; //mm
    private final double mmPerPx;

    public RenderParams(double bodyWidth, double bodyHeight, double wheelRadius, double mmPerPx) {
        this.bodyHeight = bodyHeight;
        this.bodyWidth = bodyWidth;
        this.wheelRadius = wheelRadius;
        this.mmPerPx = mmPerPx;
    }

    public double getBodyHeight() {
        return bodyHeight;
    }

    public double getBodyWidth() {
        return bodyWidth;
    }

    public double getWheelRadius() {
        return wheelRadius;
    }

    public double getMmPerPx() {
        return mmPerPx;
    }
}
