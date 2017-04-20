package com.dgiczi.balancesim.simulation.control;

import com.dgiczi.balancesim.simulation.math.MiniPid;
import com.dgiczi.balancesim.simulation.model.SimulatorState;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PidBasedControlStategy implements ControlStrategy {

    private final MiniPid tiltPid;
    private final MiniPid distancePid;
    private final Random random = new Random();
    private double maxTiltPidOutput = 1;
    private double maxDistancePidOutput = 90;

    public PidBasedControlStategy() {
        tiltPid = new MiniPid(0.9, 0.05, 0.05);

        distancePid = new MiniPid(0.10, 0, 0.1);
        distancePid.setOutputLimits(-maxDistancePidOutput, maxDistancePidOutput);
    }

    @Override
    public Optional<Double> getControlValue(SimulatorState state) {
        double distance = state.getPosX();
        double distanceOutput = distancePid.getOutput(distance, 0);

        double tiltNoise = (random.nextDouble() - 0.5) * 0.01 * 90;
        double output = tiltPid.getOutput(-state.getTilt() + tiltNoise, 0 + distanceOutput);
        return Optional.ofNullable(output);
        //return Optional.empty();
    }

    @Override
    public void setMaxOutput(double maxOutput) {
        this.maxTiltPidOutput = maxOutput;
        tiltPid.setOutputLimits(-maxTiltPidOutput, maxTiltPidOutput);
    }

}
