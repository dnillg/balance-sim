package com.dgiczi.balancesim.simulation.actions;

import com.dgiczi.balancesim.simulation.model.BalanceBot;
import org.jbox2d.dynamics.World;

public interface UserAction {

    void apply(BalanceBot balanceBot, World world);

}
