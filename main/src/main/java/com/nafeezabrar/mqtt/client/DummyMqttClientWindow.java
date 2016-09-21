package com.nafeezabrar.mqtt.client;

import com.nafeezabrar.mqtt.client.events.listening.MqttClientWindowEventListener;
import com.nafeezabrar.mqtt.client.ui.MqttClientWindow;

class DummyMqttClientWindow implements MqttClientWindow {
    protected String topic;
    protected String message;
    protected final String[] topicFilters;
    protected MqttClientWindowEventListener eventListener;

    public DummyMqttClientWindow(String[] topicFilters) {
        this.topicFilters = topicFilters;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setEventListener(MqttClientWindowEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void addReceivedMessage(String topic, String message) {
        System.out.println(String.format("Topic - %s Message - %s", topic, message));
    }

    @Override
    public String[] getTopicFilters() {
        return topicFilters;
    }

    void sendMessage(String registrationTopic, String registrationMessage) {
        setTopic(registrationTopic);
        setMessage(registrationMessage);
        fireSendButtonClicked();
    }

    void fireSendButtonClicked() {
        if (eventListener != null) eventListener.sendButtonClicked();
    }

    void fireSubscribedButtonClicked() {
        if (eventListener != null) eventListener.subscribedButtonClicked();
    }
}
