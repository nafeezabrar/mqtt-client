package com.nafeezabrar.mqtt.client.ui.libs;

public class ViewSwitcher {
    protected ViewRenderer renderer;

    public ViewSwitcher(ViewRenderer renderer) {
        this.renderer = renderer;
    }

    public void switchToMainView() {
        renderer.renderView("main.fxml", 950, 700);
        renderer.setTitle("MQTT Client");
    }
}
