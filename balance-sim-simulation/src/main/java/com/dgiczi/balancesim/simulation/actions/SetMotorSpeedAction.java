package com.dgiczi.balancesim.simulation.actions;

import com.dgiczi.balancesim.simulation.model.BalanceBot;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.WheelJoint;

import java.util.Optional;

public class SetMotorSpeedAction implements UserAction {

    private final Optional<Double> motorSpeed;

    public SetMotorSpeedAction(Optional<Double> motorSpeed) {
        this.motorSpeed = motorSpeed;
    }

    @Override
    public void apply(BalanceBot balanceBot, World world) {
        WheelJoint joint = balanceBot.getJoint();
        if (motorSpeed.isPresent()) {
            double speed = motorSpeed.get();
            joint.setMotorSpeed((float)speed);
            joint.enableMotor(true);
        } else {
            joint.enableMotor(false);
        }
    }

    public Optional<Double> getMotorSpeed() {
        return motorSpeed;
    }

}
