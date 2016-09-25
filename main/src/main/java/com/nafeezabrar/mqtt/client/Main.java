package com.nafeezabrar.mqtt.client;

import com.nafeezabrar.mqtt.client.conversion.AnySeparatorStringToBytesConverter;
import com.nafeezabrar.mqtt.client.conversion.BytesToStringConverter;
import com.nafeezabrar.mqtt.client.conversion.DecimalColonSeparatorBytesToStringConverter;
import com.nafeezabrar.mqtt.client.conversion.StringToBytesConverter;
import com.nafeezabrar.mqtt.client.presenting.MqttClientWindowPresenter;

import java.io.IOException;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        String registrationTopic = "E/1/M/REG";
        String registrationMessage = "16 " +
                "54 16 90 03 45 30 " +
                " 9 20 20 15 20 25 53 " +
                "0 " +
                "45";

        String electricalTopic = "E/1/ELEC";
        String electricalMessage = "" +
                "16," +
                "54, 16, 90, 03, 45, 30" +
                " 8, 24, 20, 16,  7,  1, 16, " +
                "86, 80, " +
                "04, 75, " +
                "05, 19, " +
                "39, 15, " +
                " 0,  0, 22, 100, " +
                " 0,  0, 48, 39, " +
                "0x01, " +
                "0x1A ";

        String relayStatusOffElectricalMessage = "" +
                "16," +
                "54, 16, 90,  3, 45, 30" +
                " 8, 24, 20, 16,  7,  1, 16, " +
                "86, 80, " +
                " 4, 75, " +
                " 5, 19, " +
                "39, 15, " +
                " 0,  0, 22, 100, " +
                " 0,  0, 48, 39, " +
                "0x00, " +
                "0x1A ";

        String loadLimitCrossedElectricalMessage = "" +
                "16," +
                "54, 16, 90, 03, 45, 30" +
                " 8, 24, 20, 16,  7,  1, 16, " +
                "86, 80, " +
                "04, 75, " +
                "0x52, 0x09, " +
                "39, 15, " +
                " 0,  0, 22, 100, " +
                " 0,  0, 48, 39, " +
                "0x01, " +
                "0x1A ";

        String[] topicFilters = new String[]{"E/1/54169003453"};

        String serverURI = "tcp://192.168.11.50:1883";
        String clientId = UUID.randomUUID().toString();

        StringToBytesConverter stringToBytesConverter = new AnySeparatorStringToBytesConverter();
        BytesToStringConverter bytesToStringConverter = new DecimalColonSeparatorBytesToStringConverter();
        MqttClient mqttClient = new PahoMqttClientWrapper(serverURI, clientId);
        DummyMqttClientWindow mqttClientWindow = new DummyMqttClientWindow(topicFilters);
        MqttClientWindowPresenter mqttClientWindowPresenter = new MqttClientWindowPresenter(mqttClientWindow, stringToBytesConverter, bytesToStringConverter, mqttClient);
        mqttClientWindowPresenter.initialize();

        mqttClientWindow.fireSubscribedButtonClicked();

        mqttClientWindow.sendMessage(registrationTopic, registrationMessage);

//        mqttClientWindow.sendMessage(electricalTopic, electricalMessage);
//        mqttClientWindow.sendMessage(electricalTopic, relayStatusOffElectricalMessage);
        mqttClientWindow.sendMessage(electricalTopic, loadLimitCrossedElectricalMessage);

        while (true) {
            Thread.sleep(5000);
        }
    }
}
