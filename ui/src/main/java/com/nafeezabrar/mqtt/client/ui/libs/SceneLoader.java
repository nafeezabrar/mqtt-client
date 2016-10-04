package com.nafeezabrar.mqtt.client.ui.libs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;


public class SceneLoader {
    public Parent loadView(String fxmlFileName) {
        try {
            return FXMLLoader.load(getClass().getResource(fxmlFileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
