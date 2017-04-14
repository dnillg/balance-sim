package com.dgiczi.balancesim.app;

import com.dgiczi.balancesim.app.configuration.MainContextConfiguration;
import com.dgiczi.balancesim.render.com.SceneRenderer;
import com.dgiczi.balancesim.render.model.SceneParams;
import com.dgiczi.balancesim.render.model.State;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MainContextConfiguration.class)
public class App extends Application {

    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(App.class);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Balance-Sim");
        BorderPane mainBorderPane = new BorderPane();

        Canvas canvas = new Canvas(800, 600);
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.TOP_LEFT);

//        canvas.widthProperty().bind(stackPane.widthProperty());
//        canvas.heightProperty().bind(stackPane.heightProperty());
//        canvas.widthProperty().addListener(redrawOnResizeListener);
//        canvas.heightProperty().addListener(redrawOnResizeListener);
        GraphicsContext gc = canvas.getGraphicsContext2D();
//        gc.setFill(Color.BLUE);
//        gc.fillRect(10, 10, 300, 300);
        SceneParams params = new SceneParams(60, 140, 33, 1.0/2.5);
        State state = new State(12, -10);
        SceneRenderer renderer = new SceneRenderer();
        renderer.render(gc, params, state);

        stackPane.getChildren().addAll(canvas);
        mainBorderPane.setCenter(stackPane);

        Scene scene = new Scene(mainBorderPane, 800, 600);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

}
