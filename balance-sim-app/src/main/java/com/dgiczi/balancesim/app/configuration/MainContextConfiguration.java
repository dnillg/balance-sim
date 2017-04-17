package com.dgiczi.balancesim.app.configuration;

import com.dgiczi.balancesim.render.configartion.RenderContextConfiguration;
import com.dgiczi.balancesim.simulation.configuration.SimulationContextConfiguration;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan
@Import({RenderContextConfiguration.class, SimulationContextConfiguration.class})
public class MainContextConfiguration {

}
