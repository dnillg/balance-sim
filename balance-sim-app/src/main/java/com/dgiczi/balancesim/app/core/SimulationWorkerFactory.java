package com.dgiczi.balancesim.app.core;

import com.dgiczi.balancesim.simulation.SceneSimulator;
import com.dgiczi.balancesim.simulation.control.ControlStrategy;
import com.dgiczi.balancesim.simulation.model.SimulationParams;
import com.dgiczi.balancesim.simulation.model.SimulationState;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SimulationWorkerFactory {

    private final ObjectProvider<SceneSimulator> sceneSimulatorFactory;
    private final ObjectProvider<SimulationWorker> simulationWorkerFactory;

    private SimulationParams params;
    private ObjectProvider<ControlStrategy> controlStrategyFactory;
    private Consumer<SimulationState> stateCallback;

    public SimulationWorkerFactory(ObjectProvider<SceneSimulator> sceneSimulatorFactory,
                                   ObjectProvider<SimulationWorker> simulationWorkerFactory) {
        this.sceneSimulatorFactory = sceneSimulatorFactory;
        this.simulationWorkerFactory = simulationWorkerFactory;
    }

    public SimulationWorker build() {
        if (params == null || controlStrategyFactory == null) {
            throw new IllegalStateException("Builder is not initialized!");
        }
        SceneSimulator sceneSimulator = sceneSimulatorFactory.getObject(params);
        ControlStrategy controlStrategy = controlStrategyFactory.getObject(params);
        SimulationWorker simulationWorker = simulationWorkerFactory.getObject(
                sceneSimulator, controlStrategy, stateCallback);
        return simulationWorker;
    }

    public void setParams(SimulationParams params) {
        this.params = params;
    }

    public void setControlStrategyFactory(ObjectProvider<ControlStrategy> controlStrategyFactory) {
        this.controlStrategyFactory = controlStrategyFactory;

    }

    public void setStateCallback(Consumer<SimulationState> stateCallback) {
        this.stateCallback = stateCallback;
    }
}
