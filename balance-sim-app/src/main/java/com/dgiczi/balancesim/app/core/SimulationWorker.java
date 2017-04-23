package com.dgiczi.balancesim.app.core;

import com.dgiczi.balancesim.simulation.SceneSimulator;
import com.dgiczi.balancesim.simulation.actions.SetMotorSpeedAction;
import com.dgiczi.balancesim.simulation.actions.UserAction;
import com.dgiczi.balancesim.simulation.control.ControlStrategy;
import com.dgiczi.balancesim.simulation.model.SimulationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SimulationWorker {

    private static final double HZ = 120;
    private static final double PERIOD_TIME_S = 1.0 / HZ;
    private static final long PERIOD_TIME_MS = (long) (PERIOD_TIME_S * 1000);
    private static final double DEFAULT_SIMULATION_SPEED_RATIO = 1;
    private static final Logger log = LoggerFactory.getLogger(SimulationWorker.class);

    private final ControlStrategy controlStrategy;
    private final SceneSimulator sceneSimulator;
    private final Thread thread;
    private final Consumer<SimulationState> stateCallback;

    private volatile boolean stopped = false;
    private long lastTickTime = 0;
    private long ticks = 0;

    public SimulationWorker(SceneSimulator sceneSimulator, ControlStrategy controlStrategy, Consumer<SimulationState> stateCallback) {
        this.sceneSimulator = sceneSimulator;
        this.controlStrategy = controlStrategy;
        this.thread = new Thread(() -> this.work());
        this.stateCallback = stateCallback;
    }

    public void start() {
        thread.start();
    }

    private int work() {
        sceneSimulator.init();
        while (!stopped) {
            blockUntilTick();
            for (int i = 0; i < DEFAULT_SIMULATION_SPEED_RATIO; i++) {
                UserAction action = controlStrategy.getControlValue(sceneSimulator.getState());
                //sceneSimulator.applyUserAction(action);
                sceneSimulator.step(PERIOD_TIME_S);
                ticks++;
            }
            SimulationState simulationState = sceneSimulator.getState();

            if (stateCallback != null) {
                stateCallback.accept(simulationState);
            }
        }
        return 0;
    }

    private void performControlValue(Optional<Double> controlValue) {
        SetMotorSpeedAction action = new SetMotorSpeedAction(controlValue);
        sceneSimulator.applyUserAction(action);
    }

    private void blockUntilTick() {
        try {
            long diff = lastTickTime + PERIOD_TIME_MS - System.currentTimeMillis();
            if (diff > 0) {
                Thread.sleep(diff);
            }
            lastTickTime = System.currentTimeMillis();
        } catch (InterruptedException e) {
            log.warn("Interupted exception occurred!", e);
        }
    }

    public void softStop() {
        stopped = true;
    }

    public boolean isRunning() {
        if (thread != null && thread.isAlive()) {
            return true;
        } else {
            return false;
        }
    }

    public void applyUserAction(UserAction action) {
        sceneSimulator.applyUserAction(action);
    }

    public void setMotorForward() {
        sceneSimulator.applyUserAction(new SetMotorSpeedAction(Optional.of(-10.0)));
    }

    public void setMotorBackward() {
        sceneSimulator.applyUserAction(new SetMotorSpeedAction(Optional.of(10.0)));
    }

    public void setMotorOff() {
        sceneSimulator.applyUserAction(new SetMotorSpeedAction(Optional.empty()));
    }

    public void setMotorZero() {
        sceneSimulator.applyUserAction(new SetMotorSpeedAction(Optional.of(0.0)));
    }

}
