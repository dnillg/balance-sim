package com.dgiczi.balancesim.app.core;

import com.dgiczi.balancesim.render.SceneRenderer;
import com.dgiczi.balancesim.render.model.RenderParams;
import com.dgiczi.balancesim.render.model.RenderState;
import com.dgiczi.balancesim.simulation.SceneSimulator;
import com.dgiczi.balancesim.simulation.control.ControlStrategy;
import com.dgiczi.balancesim.simulation.control.PidBasedControlStategy;
import com.dgiczi.balancesim.simulation.model.SimulatorState;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SimulationWorker {

    private static final double HZ = 120;
    private static final double PERIOD_TIME_S = 1.0 / HZ;
    private static final long PERIOD_TIME_MS = (long) (PERIOD_TIME_S * 1000);
    private static final double MM_PER_PX = 1.0 / 2.5;
    private static final double SIMULATION_SPEED_RATIO = 1;
    private static final Logger log = LoggerFactory.getLogger(SimulationWorker.class);

    private final SceneRenderer sceneRenderer;
    private final ControlStrategy controlStrategy;

    private final GraphicsContext graphicsContext;
    private final SceneSimulator sceneSimulator;
    private final RenderParams renderParams;
    private final Thread thread;

    private volatile boolean stopped = false;
    private long lastTickTime = 0;
    private long ticks = 0;

    public SimulationWorker(Canvas canvas, SceneSimulator sceneSimulator, ControlStrategy controlStrategy, SceneRenderer sceneRenderer) {
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.sceneSimulator = sceneSimulator;
        this.controlStrategy = controlStrategy;
        this.sceneRenderer = sceneRenderer;
        this.renderParams = new RenderParams(60, 140, 33.5, MM_PER_PX);
        this.thread = new Thread(() -> this.work());
    }

    public void start() {
        thread.start();
    }

    private int work() {
        sceneSimulator.init();
        while (!stopped) {
            blockUntilTick();
            for(int i = 0; i < SIMULATION_SPEED_RATIO; i++) {
                Optional<Double> controlValue = controlStrategy.getControlValue(sceneSimulator.getState());
                performControlValue(controlValue);
                sceneSimulator.step(PERIOD_TIME_S);
                ticks++;
            }
            SimulatorState simulatorState = sceneSimulator.getState();
            RenderState state = new RenderState(simulatorState.getPosX(), simulatorState.getPosY(), simulatorState.getTilt());

            Platform.runLater(() -> sceneRenderer.render(graphicsContext, renderParams, state));
        }
        return 0;
    }

    private void performControlValue(Optional<Double> controlValue) {
        if(controlValue.isPresent()) {
            sceneSimulator.setMotorSpeed(controlValue.get());
        } else {
            sceneSimulator.setMotorOff();
        }
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

    public void stop() {
        stopped = true;
    }

    public void setMotorForward() {
        sceneSimulator.setMotorForward();
    }

    public void setMotorBackward() {
        sceneSimulator.setMotorBackWard();
    }

    public void setMotorOff() {
        sceneSimulator.setMotorOff();
    }

    public void setMotorZero() {
        sceneSimulator.setMotorZero();
    }

}
