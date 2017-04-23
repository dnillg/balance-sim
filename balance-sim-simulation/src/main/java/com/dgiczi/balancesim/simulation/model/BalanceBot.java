package com.dgiczi.balancesim.simulation.model;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.WheelJoint;

public class BalanceBot {

    private final Body body;
    private final Body wheel;
    private final WheelJoint joint;

    public BalanceBot(Body body, Body wheel, WheelJoint joint) {
        this.body = body;
        this.wheel = wheel;
        this.joint = joint;
    }

    public Body getBody() {
        return body;
    }

    public Body getWheel() {
        return wheel;
    }

    public WheelJoint getJoint() {
        return joint;
    }
}
