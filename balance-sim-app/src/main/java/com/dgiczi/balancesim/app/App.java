package com.dgiczi.balancesim.app;

import com.dgiczi.balancesim.app.configuration.MainContextConfiguration;
import com.dgiczi.balancesim.app.core.Worker;
import com.dgiczi.balancesim.render.SceneRenderer;
import com.dgiczi.balancesim.simulation.SceneSimulator;
import com.dgiczi.balancesim.simulation.model.SimulatorParams;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MainContextConfiguration.class)
public class App extends Application {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(App.class);
    }

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        primaryStage.setTitle("Balance-Sim");
        BorderPane mainBorderPane = new BorderPane();
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);

        canvas.widthProperty().bind(mainBorderPane.widthProperty());
        canvas.heightProperty().bind(mainBorderPane.heightProperty());
//        canvas.widthProperty().addListener(redrawOnResizeListener);
//        canvas.heightProperty().addListener(redrawOnResizeListener);

        mainBorderPane.setCenter(canvas);
        Scene scene = new Scene(mainBorderPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();

        SimulatorParams simulatorParams = new SimulatorParams(3.0, 12, 3.5, 800, 200, 0, 0, 1000);
        Worker worker = createWorker(canvas, simulatorParams);
        primaryStage.setOnCloseRequest(getWindowCloseHandler(worker));
        startWorker(canvas, worker);
    }

    private EventHandler<WindowEvent> getWindowCloseHandler(Worker worker) {
        return new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                worker.stop();
            }
        };
    }

    private Worker createWorker(Canvas canvas, SimulatorParams simulatorParams) {
        SceneSimulator sceneSimulator = context.getBean(SceneSimulator.class, simulatorParams);
        SceneRenderer sceneRenderer = context.getBean(SceneRenderer.class);
        return context.getBeanFactory().getBean(Worker.class, canvas, sceneSimulator, sceneRenderer);
    }

    private void startWorker(Canvas canvas, Worker worker) {
        Thread workerThread = new Thread(worker);
        workerThread.start();
    }

}
