package com.nafeezabrar.mqtt.client;

import com.nafeezabrar.mqtt.client.events.listening.MqttClientEventListener;

import java.io.Closeable;

public interface MqttClient extends Closeable {
    void setEventListener(MqttClientEventListener mqttClientEventListener);

    void subscribe(String[] topicFilters);

    void sendMessage(String topic, byte[] message);
}
