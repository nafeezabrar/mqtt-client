package com.nafeezabrar.mqtt.client.ui;

import com.nafeezabrar.mqtt.client.ui.events.listening.ViewActivatedListener;
import com.nafeezabrar.mqtt.client.ui.libs.SceneFactory;
import com.nafeezabrar.mqtt.client.ui.libs.SceneLoader;
import com.nafeezabrar.mqtt.client.ui.libs.ViewRenderer;
import com.nafeezabrar.mqtt.client.ui.libs.ViewSwitcher;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class UiApplication extends Application {

    public static MqttClientWindow ActualWindow;
    public static ViewActivatedListener viewActivatedListener;

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneLoader sceneLoader = new SceneLoader();
        SceneFactory sceneFactory = new SceneFactory();
        ViewRenderer viewRender = new ViewRenderer(primaryStage, sceneLoader, sceneFactory);
        ViewSwitcher viewSwitcher = new ViewSwitcher(viewRender);
        viewSwitcher.switchToMainView();

        primaryStage.show();
    }

    public static void run(String[] args) {
        launch(args);
    }

    public static void activated() {
        viewActivatedListener.viewActivated();
    }
}
