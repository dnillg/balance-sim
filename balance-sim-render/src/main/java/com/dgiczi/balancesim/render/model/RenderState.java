package com.dgiczi.balancesim.render.model;

public class RenderState {

    private final double posX; //mm
    private final double posY; //mm
    private final double tilt; //degree

    public RenderState(double posX, double posY, double tilt) {
        this.posX = posX;
        this.posY = posY;
        this.tilt = tilt;
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
}
