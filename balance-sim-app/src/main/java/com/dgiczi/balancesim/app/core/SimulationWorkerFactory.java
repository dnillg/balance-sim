package com.dgiczi.balancesim.app.core;

import com.dgiczi.balancesim.render.SceneRenderer;
import com.dgiczi.balancesim.simulation.SceneSimulator;
import com.dgiczi.balancesim.simulation.control.ControlStrategy;
import com.dgiczi.balancesim.simulation.model.SimulatorParams;
import javafx.scene.canvas.Canvas;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.naming.ldap.Control;


@Component
public class SimulationWorkerFactory {

    private final SceneRenderer sceneRenderer;
    private final ControlStrategy controlStrategy;
    private final ConfigurableApplicationContext context;

    private SimulatorParams params;
    private Canvas canvas;

    public SimulationWorkerFactory(SceneRenderer sceneRenderer, ControlStrategy controlStrategy, ConfigurableApplicationContext context) {
        this.sceneRenderer = sceneRenderer;
        this.context = context;
        this.controlStrategy = controlStrategy;
    }

    public SimulationWorker build() {
        if(params == null || canvas == null){
            throw new IllegalStateException("Builder is not initialized!");
        }
        SceneSimulator sceneSimulator = context.getBeanFactory().getBean(SceneSimulator.class, params);
        controlStrategy.setMaxOutput(params.getMaxSpeed());
        SimulationWorker simulationWorker = context.getBeanFactory().getBean(SimulationWorker.class, canvas, sceneSimulator, controlStrategy, sceneRenderer);
        return simulationWorker;
    }

    public void setParams(SimulatorParams params) {
        this.params = params;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
}
