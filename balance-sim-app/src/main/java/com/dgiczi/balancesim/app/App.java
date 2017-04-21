package com.dgiczi.balancesim.app;

import com.dgiczi.balancesim.app.configuration.MainContextConfiguration;
import com.dgiczi.balancesim.app.scenes.visualization.VisualizationScene;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MainContextConfiguration.class)
public class App extends Application {

    private ConfigurableApplicationContext context;
    private VisualizationScene visualtizationScene;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(App.class);
    }

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        visualtizationScene = context.getBean(VisualizationScene.class);
        visualtizationScene.init(primaryStage);
        visualtizationScene.show();
    }

}
