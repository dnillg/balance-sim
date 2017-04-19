package com.dgiczi.balancesim.app.scenes;

import com.dgiczi.balancesim.app.core.SimulationWorkerFactory;
import com.dgiczi.balancesim.app.core.SimulationWorker;
import com.dgiczi.balancesim.simulation.model.SimulatorParams;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VisualizationScene {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final String TITLE = "Balance-Sim";
    private static final Logger log = LoggerFactory.getLogger(VisualizationScene.class);

    private final SimulatorParams simulatorParams = new SimulatorParams(
            60, 140, 33.5, 800, 200,
            0, 0, 1000000, 100);
    private final SimulationWorkerFactory simulationWorkerFactory;

    private SimulationWorker simulationWorker;

    private Stage stage;
    private Scene scene;
    private BorderPane mainBorderPane;
    private Canvas canvas;

    public VisualizationScene(SimulationWorkerFactory simulationWorkerFactory) {
        this.simulationWorkerFactory = simulationWorkerFactory;
    }

    public void init(Stage stage) {
        if (this.stage != null) {
            throw new IllegalStateException("Stage has already been initialized!");
        }
        this.stage = stage;
        initStage();
        stage.show();
        resetSimulation();
    }

    private void resetSimulation() {
        if (simulationWorker != null) {
            simulationWorker.stop();
        }
            simulationWorkerFactory.setCanvas(canvas);
            simulationWorkerFactory.setParams(simulatorParams);
            simulationWorker = simulationWorkerFactory.build();
            simulationWorker.start();
    }

    private void registerEventHandlers(Stage stage) {
        stage.setOnCloseRequest(getWindowCloseHandler());
        stage.addEventHandler(KeyEvent.KEY_PRESSED, getKeyEventHandler());
        stage.addEventHandler(KeyEvent.KEY_RELEASED, getKeyEventHandler());
    }

    private EventHandler<KeyEvent> getKeyEventHandler() {
        return (keyEvent)->{
            if(keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                if (keyEvent.getCode() == KeyCode.LEFT) {
                    simulationWorker.setMotorBackward();
                    keyEvent.consume();
                } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                    simulationWorker.setMotorForward();
                    keyEvent.consume();
                } else if (keyEvent.getCode() == KeyCode.DOWN) {
                    simulationWorker.setMotorZero();
                    keyEvent.consume();
                }
            } else if(keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
                if (keyEvent.getCode() == KeyCode.R) {
                    resetSimulation();
                    keyEvent.consume();
                } else if (keyEvent.getCode() == KeyCode.LEFT) {
                    simulationWorker.setMotorOff();
                    keyEvent.consume();
                } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                    simulationWorker.setMotorOff();
                    keyEvent.consume();
                } else if (keyEvent.getCode() == KeyCode.DOWN) {
                    simulationWorker.setMotorZero();
                    keyEvent.consume();
                }
            }
        };
    }

    private void initStage() {
        stage.setTitle(TITLE);
        mainBorderPane = new BorderPane();

        canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        canvas.widthProperty().bind(mainBorderPane.widthProperty());
        canvas.heightProperty().bind(mainBorderPane.heightProperty());

        mainBorderPane.setCenter(canvas);

        scene = new Scene(mainBorderPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);

        registerEventHandlers(stage);
    }


    private EventHandler<WindowEvent> getWindowCloseHandler() {
        return event -> {
            if(simulationWorker != null) {
                simulationWorker.stop();
            }
        };
    }



}
