package com.dgiczi.balancesim.app.core;

import com.dgiczi.balancesim.render.SceneRenderer;
import com.dgiczi.balancesim.simulation.SceneSimulator;
import com.dgiczi.balancesim.simulation.control.ControlStrategy;
import com.dgiczi.balancesim.simulation.model.SimulatorParams;
import javafx.scene.canvas.Canvas;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.naming.ldap.Control;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SimulationWorkerFactory {

    private final ConfigurableApplicationContext context;

    private SimulatorParams params;
    private ControlStrategy controlStrategy;

    public SimulationWorkerFactory(ConfigurableApplicationContext context) {
        this.context = context;
    }

    public SimulationWorker build() {
        if(params == null){
            throw new IllegalStateException("Builder is not initialized!");
        }
        SceneSimulator sceneSimulator = context.getBeanFactory().getBean(SceneSimulator.class, params);
        SimulationWorker simulationWorker = context.getBeanFactory().getBean(SimulationWorker.class, sceneSimulator, controlStrategy);
        return simulationWorker;
    }

    public void setParams(SimulatorParams params) {
        this.params = params;
    }

    public void setControlStrategy(ControlStrategy controlStrategy) {
        this.controlStrategy = controlStrategy;
    }
}
