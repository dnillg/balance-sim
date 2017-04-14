package com.dgiczi.balancesim.render.model;

/**
 * Created by Reactorx2 on 2017. 04. 13..
 */
public class State {

    private int disatance; //mm
    private double tilt; //degree

    public State(int disatance, double tilt) {
        this.disatance = disatance;
        this.tilt = tilt;
    }

    public int getDisatance() {
        return disatance;
    }

    public double getTilt() {
        return tilt;
    }
}
