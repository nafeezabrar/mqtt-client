package com.nafeezabrar.mqtt.client;

import com.nafeezabrar.mqtt.client.conversion.AnySeparatorStringToBytesConverter;
import com.nafeezabrar.mqtt.client.conversion.BytesToStringConverter;
import com.nafeezabrar.mqtt.client.conversion.DecimalColonSeparatorBytesToStringConverter;
import com.nafeezabrar.mqtt.client.conversion.StringToBytesConverter;
import com.nafeezabrar.mqtt.client.presenting.MqttClientWindowPresenter;
import com.nafeezabrar.mqtt.client.ui.MqttClientWindow;
import com.nafeezabrar.mqtt.client.ui.UiApplication;

import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        String serverURI = "tcp://iot.eclipse.org:1883";
        String clientId = UUID.randomUUID().toString();

        UiApplication.viewActivatedListener = () -> {
            MqttClientWindow mqttClientWindow = UiApplication.ActualWindow;
            StringToBytesConverter stringToBytesConverter = new AnySeparatorStringToBytesConverter();
            BytesToStringConverter bytesToStringConverter = new DecimalColonSeparatorBytesToStringConverter();
            MqttClient mqttClient = new PahoMqttClientWrapper(serverURI, clientId);
            MqttClientWindowPresenter mqttClientWindowPresenter = new MqttClientWindowPresenter(mqttClientWindow, stringToBytesConverter, bytesToStringConverter, mqttClient);
            mqttClientWindowPresenter.initialize();
        };

        UiApplication.run(args);
    }
}
