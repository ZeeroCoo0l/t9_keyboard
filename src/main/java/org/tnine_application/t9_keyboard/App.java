package org.tnine_application.t9_keyboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("GUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 450);
        Engine engine = fxmlLoader.getController();
        stage.setOnCloseRequest(windowEvent -> {
            engine.saveData();
            System.out.println("Application is closed. Data is saved. ");
        });


        stage.setResizable(false);
        stage.setTitle("T9 Keyboard!");
        stage.setScene(scene);
        //stage.onCloseRequestProperty().set((windowEvent -> ));
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}