package com.nafeezabrar.mqtt.client.ui;

import com.nafeezabrar.mqtt.client.events.listening.MqttClientWindowEventListener;

public interface MqttClientWindow {
    String getTopic();

    void setTopic(String topic);

    String getMessage();

    void setMessage(String message);

    void setEventListener(MqttClientWindowEventListener eventListener);

    void addReceivedMessage(String topic, String message);

    String[] getTopicFilters();
}
