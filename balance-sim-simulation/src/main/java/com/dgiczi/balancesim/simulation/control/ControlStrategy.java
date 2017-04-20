package com.dgiczi.balancesim.simulation.control;

import com.dgiczi.balancesim.simulation.model.SimulatorState;

import java.util.Optional;

public interface ControlStrategy {

    Optional<Double> getControlValue(SimulatorState state);
    void setMaxOutput(double maxOutput);

}
