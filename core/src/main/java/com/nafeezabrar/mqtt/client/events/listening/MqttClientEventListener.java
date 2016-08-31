package com.nafeezabrar.mqtt.client.events.listening;

public interface MqttClientEventListener {
    void messageReceived(String topic, byte[] message);
}
