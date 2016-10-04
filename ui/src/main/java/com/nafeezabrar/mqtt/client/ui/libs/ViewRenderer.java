package com.nafeezabrar.mqtt.client.ui.libs;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ViewRenderer {
    protected Stage stage;
    protected SceneLoader sceneLoader;
    protected SceneFactory sceneFactory;

    public ViewRenderer(Stage stage, SceneLoader sceneLoader, SceneFactory sceneFactory) {
        this.stage = stage;
        this.sceneLoader = sceneLoader;
        this.sceneFactory = sceneFactory;
    }

    public void renderView(String fxmlFileName, double width, double height) {
        Parent view = sceneLoader.loadView(fxmlFileName);
        Scene scene = sceneFactory.createScene(view, width, height);
        stage.setScene(scene);
    }

    public void setTitle(String title) {
        stage.setTitle(title);
    }
}
