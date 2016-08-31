package com.nafeezabrar.mqtt.client;

import com.nafeezabrar.mqtt.client.events.listening.MqttClientEventListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;

public class PahoMqttClientWrapper implements MqttClient, MqttCallback {

    private final org.eclipse.paho.client.mqttv3.MqttClient mqttClient;
    private MqttClientEventListener eventListener;

    public PahoMqttClientWrapper(String serverURI, String clientId) {
        try {
            mqttClient = new org.eclipse.paho.client.mqttv3.MqttClient(serverURI, clientId);
            mqttClient.setCallback(this);
            mqttClient.connect();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setEventListener(MqttClientEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void subscribe(String[] topicFilters) {
        try {
            mqttClient.subscribe(topicFilters);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessage(String topic, byte[] message) {
        try {
            mqttClient.publish(topic, new MqttMessage(message));
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if (eventListener != null) {
            eventListener.messageReceived(topic, message.getPayload());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    @Override
    public void close() throws IOException {
        try {
            mqttClient.disconnect();
            mqttClient.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
