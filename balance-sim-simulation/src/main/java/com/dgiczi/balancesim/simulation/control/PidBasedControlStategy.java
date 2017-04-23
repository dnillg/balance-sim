package com.dgiczi.balancesim.simulation.control;

import com.dgiczi.balancesim.simulation.SceneSimulator;
import com.dgiczi.balancesim.simulation.actions.SetMotorSpeedAction;
import com.dgiczi.balancesim.simulation.actions.UserAction;
import com.dgiczi.balancesim.simulation.math.MiniPid;
import com.dgiczi.balancesim.simulation.model.SimulationParams;
import com.dgiczi.balancesim.simulation.model.SimulationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PidBasedControlStategy implements ControlStrategy {

    private static final Logger log = LoggerFactory.getLogger(PidBasedControlStategy.class);
    private static final double MAX_DISTANCE_PID_OUTPUT = 90; // offset degree

    private final MiniPid tiltPid;
    private final MiniPid distancePid;
    private final Random random = new Random();

    public PidBasedControlStategy() {
        tiltPid = new MiniPid(0.9, 0.05, 0.05);

        double amp = 0.3;
        distancePid = new MiniPid(0.50 * amp, 0.00001* amp, 1.25* amp);
        distancePid.setOutputLimits(-MAX_DISTANCE_PID_OUTPUT, MAX_DISTANCE_PID_OUTPUT);
    }

    public PidBasedControlStategy(SimulationParams params) {
        this();
        params.withScale(SceneSimulator.SCALE);
        tiltPid.setOutputLimits(params.getMaxSpeed());
    }

    @Override
    public UserAction getControlValue(SimulationState state) {
        double distance = state.getPosX();
        double distanceOutput = distancePid.getOutput(distance, 0);
        //log.info("I: " + distancePid.getErrorSum());

        double tiltNoise = (random.nextDouble() - 0.5) * 0.01 * 90;
        double output = tiltPid.getOutput(-state.getTilt() + tiltNoise, 0 + distanceOutput);
        return new SetMotorSpeedAction(Optional.ofNullable(output));
    }

}
