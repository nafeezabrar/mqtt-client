package com.nafeezabrar.mqtt.client.ui.libs;

import javafx.scene.Parent;
import javafx.scene.Scene;


public class SceneFactory {
    public Scene createScene(Parent view, double width, double height) {
        return new Scene(view, width, height);
    }
}
