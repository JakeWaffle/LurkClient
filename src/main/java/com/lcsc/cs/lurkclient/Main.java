package com.lcsc.cs.lurkclient;

import com.lcsc.cs.lurkclient.scenes.*;

import javax.swing.JFrame;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Student
 */
public class Main {
    private String currentSceneName     = "NULL";
    private SceneInterface currentScene;
    private Map<String, SceneInterface> possibleScenes;

    public void initializeStates() {
        this.possibleScenes = new HashMap<String, SceneInterface>();

        this.possibleScenes.put("Login", new Login());
    }

    public void changeScene(String nextState) {
        this.currentSceneName   = nextState;
        this.currentScene       = this.possibleScenes.get(this.currentSceneName);
    }

    public void mainLoop() {
        boolean done = false;
        while (!done) {
            //primaryStage.setScene(scene);
            //primaryStage.show();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Main().mainLoop();
    }
}
