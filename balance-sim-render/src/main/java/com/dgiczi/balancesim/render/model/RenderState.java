package com.dgiczi.balancesim.render.model;

public class RenderState {

    private final double disatance; //mm
    private final double tilt; //degree

    public RenderState(double disatance, double tilt) {
        this.disatance = disatance;
        this.tilt = tilt;
    }

    public double getDisatance() {
        return disatance;
    }

    public double getTilt() {
        return tilt;
    }
}
