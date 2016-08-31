package com.nafeezabrar.mqtt.client.presenting;

import com.nafeezabrar.mqtt.client.MqttClient;
import com.nafeezabrar.mqtt.client.conversion.BytesToStringConverter;
import com.nafeezabrar.mqtt.client.conversion.StringToBytesConverter;
import com.nafeezabrar.mqtt.client.data.MqttMessage;
import com.nafeezabrar.mqtt.client.events.listening.MqttClientWindowEventListener;
import com.nafeezabrar.mqtt.client.ui.MqttClientWindow;

public class MqttClientWindowPresenter {
    protected final MqttClientWindow mqttClientWindow;
    protected final StringToBytesConverter stringToBytesConverter;
    protected BytesToStringConverter bytesToStringConverter;
    protected final MqttClient mqttClient;

    public MqttClientWindowPresenter(MqttClientWindow mqttClientWindow, StringToBytesConverter stringToBytesConverter, BytesToStringConverter bytesToStringConverter, MqttClient mqttClient) {
        this.mqttClientWindow = mqttClientWindow;
        this.stringToBytesConverter = stringToBytesConverter;
        this.bytesToStringConverter = bytesToStringConverter;
        this.mqttClient = mqttClient;
    }

    public void initialize() {
        mqttClientWindow.setTopic("");
        mqttClientWindow.setMessage("");

        setListener();
    }

    private void setListener() {
        MqttClientWindowPresenter _this = this;
        mqttClient.setEventListener(_this::messageReceived);
        mqttClientWindow.setEventListener(new MqttClientWindowEventListener() {
            @Override
            public void subscribedButtonClicked() {
                _this.subscribedButtonClicked();
            }

            @Override
            public void sendButtonClicked() {
                _this.sendButtonClicked();
            }
        });
    }

    protected void subscribedButtonClicked() {
        String[] topicFilters = mqttClientWindow.getTopicFilters();
        mqttClient.subscribe(topicFilters);
    }

    protected void sendButtonClicked() {
        String topic = mqttClientWindow.getTopic();
        String message = mqttClientWindow.getMessage();
        byte[] messageBytes = stringToBytesConverter.convertStringToBytes(message);
        mqttClient.sendMessage(new MqttMessage(topic, messageBytes));
    }

    protected void messageReceived(String topic, byte[] messageBytes) {
        String message = bytesToStringConverter.convertBytesToString(messageBytes);
        mqttClientWindow.addReceivedMessage(topic, message);
    }
}
