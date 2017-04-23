package com.dgiczi.balancesim.simulation.control;

import com.dgiczi.balancesim.simulation.actions.UserAction;
import com.dgiczi.balancesim.simulation.model.SimulationState;

public interface ControlStrategy {

    UserAction getControlValue(SimulationState state);

}
