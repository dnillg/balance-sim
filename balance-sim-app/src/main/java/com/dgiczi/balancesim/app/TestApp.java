package com.dgiczi.balancesim.app;

import com.dgiczi.balancesim.simulation.SceneSimulator;
import com.dgiczi.balancesim.simulation.model.SimulationParams;
import org.jbox2d.common.Vec2;
import org.jbox2d.testbed.framework.*;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

import javax.swing.*;

public class TestApp extends TestbedTest {

    public static final String GRAVITY_SETTING = "Gravity";

    @Override
    public void initTest(boolean argDeserialized) {
        //setTitle("Couple of Things Test");
        //m_world = new World(new Vec2(0.0F, -10.0F));
        getWorld().setGravity(new Vec2(0, -100));

        SimulationParams simulationParams = new SimulationParams(
                60, 140, 33.5, 800, 200,
                0, 0, 900000000, 10);
        simulationParams = simulationParams.withScale(0.1);
        SceneSimulator.addGround(getWorld());
        SceneSimulator.addBalanceRobotParts(getWorld(), simulationParams);
        getWorld().getJointList().getBodyA().applyForce(new Vec2(-100000f, 0), new Vec2(0f, 0));
    }

    @Override
    public String getTestName() {
        return "Couple of Things";
    }

    public static void main(String[] args) {
        TestbedModel model = new TestbedModel();         // create our model

// add tests
        model.addCategory("My Tests");
        model.addTest(new TestApp());

        model.getSettings().addSetting(new TestbedSetting(GRAVITY_SETTING, TestbedSetting.SettingType.ENGINE, false));

        TestbedPanel panel = new TestPanelJ2D(model);    // create our testbed panel

        JFrame testbed = new TestbedFrame(model, panel, TestbedController.UpdateBehavior.UPDATE_CALLED); // put both into our testbed frame
// etc
        testbed.setVisible(true);
        testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
