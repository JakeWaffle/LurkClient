package com.lcsc.cs.lurkclient;

import com.lcsc.cs.lurkclient.scenes.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Student
 */
public class Main extends Application {
    private String currentScene     = "Login";
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lurk Client");

        boolean done = false;
        while (!done) {
            //primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    public Map<String, SceneInterface> initializeStates() {
        Map<String, SceneInterface> possibleScenes = new HashMap<String, SceneInterface>();

        possibleScenes.put("Login", new Login());

        return possibleScenes;
    }

    public void changeScene(String nextState) {

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    }
}
