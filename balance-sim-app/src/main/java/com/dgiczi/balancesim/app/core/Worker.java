package com.dgiczi.balancesim.app.core;

import com.dgiczi.balancesim.render.SceneRenderer;
import com.dgiczi.balancesim.render.model.RenderParams;
import com.dgiczi.balancesim.render.model.RenderState;
import com.dgiczi.balancesim.simulation.SceneSimulator;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Worker extends Task<Integer> {

    private static final double HZ = 60;
    private static final double PERIOD_TIME_S = 1 / HZ;
    private static final long PERIOD_TIME_MS = (long) (PERIOD_TIME_S * 1000);
    private static final double MM_PER_PX = 1.0 / 2.5;

    private final GraphicsContext graphicsContext;
    private final SceneSimulator sceneSimulator;
    private final SceneRenderer sceneRenderer;
    private final RenderParams renderParams;

    private boolean stopped = false;

    public Worker(Canvas canvas, SceneSimulator sceneSimulator, SceneRenderer sceneRenderer) {
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.sceneSimulator = sceneSimulator;
        this.sceneRenderer = sceneRenderer;
        this.renderParams = new RenderParams(60, 140, 33, MM_PER_PX);
    }

    public void stop() {
        stopped = true;
    }

    @Override
    protected Integer call() throws Exception {
        sceneSimulator.init();
        long lastTime = System.currentTimeMillis() - PERIOD_TIME_MS;
        while (!stopped) {
            long currentTime = System.currentTimeMillis();
            long diff = lastTime + PERIOD_TIME_MS - currentTime;
            if (diff > 0) {
                Thread.sleep(diff);
            }
            lastTime = System.currentTimeMillis();
            sceneSimulator.step(PERIOD_TIME_S);
            RenderState state = new RenderState(sceneSimulator.getState().getDistance(), sceneSimulator.getState().getTilt());
            Platform.runLater(() -> sceneRenderer.render(graphicsContext, renderParams, state));
        }
        return 0;
    }

}
