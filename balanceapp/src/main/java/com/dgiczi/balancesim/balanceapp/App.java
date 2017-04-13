package com.dgiczi.balancesim.balanceapp;

import com.dgiczi.balancesim.balanceapp.configuration.MainContextConfiguration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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
        Scene scene = new Scene(mainBorderPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
