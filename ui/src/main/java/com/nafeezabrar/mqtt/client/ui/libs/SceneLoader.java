package com.nafeezabrar.mqtt.client.ui.libs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;


public class SceneLoader {
    private Class loaderClass;

    public SceneLoader(Class loaderClass) {
        this.loaderClass = loaderClass;
    }

    public Parent loadView(String fxmlFileName) {
        try {
            return FXMLLoader.load(loaderClass.getResource(fxmlFileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
