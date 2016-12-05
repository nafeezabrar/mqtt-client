package com.nafeezabrar.mqtt.client;

import com.nafeezabrar.mqtt.client.conversion.AnySeparatorStringToBytesConverter;
import com.nafeezabrar.mqtt.client.conversion.BytesToStringConverter;
import com.nafeezabrar.mqtt.client.conversion.DecimalColonSeparatorBytesToStringConverter;
import com.nafeezabrar.mqtt.client.conversion.StringToBytesConverter;
import com.nafeezabrar.mqtt.client.presenting.MqttClientWindowPresenter;
import com.nafeezabrar.mqtt.client.smartmeter.data.Phase;
import com.nafeezabrar.mqtt.client.smartmeter.data.TokenStatus;
import com.nafeezabrar.mqtt.client.smartmeter.message.generator.MessageBytesGenerator;
import com.nafeezabrar.mqtt.client.smartmeter.message.generator.MessageBytesGenerator1Point4Point6;
import com.nafeezabrar.mqtt.client.smartmeter.utility.ActualCurrentTimestampProvider;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

public class Main {

    private static final String registrationTopic = "E/1/RegReq";
    private static final String electricalTopic = "E/1/Elec";
    private static final String billingTopic = "E/1/Billing";
    private static final String tokenResponseTopic = "E/1/TknRsp";
    private static String meterTopic;
    private static String testTopic;

    public static void main(String[] args) throws InterruptedException, IOException {
        MessageBytesGenerator messageBytesGenerator = createMessageBytesGenerator();

        long virtualMeterNo = 54169003454L;
        long realMeterNo = 54169007967L;

        long meterNo = realMeterNo;

        byte[] registrationMessageBytes = messageBytesGenerator.getRegistrationMessageBytes(meterNo, Phase.Single);

        byte[] registrationAcknowledgementMessageBytes = messageBytesGenerator.getRegistrationAcknowledgementMessageBytes(meterNo, false);

        String electricalMessage = "" +
                "33 " +
                "54, 16, 90, 03, 45, 30, " +
                "20, 16,  8, 24,  12,  1, 16, " +
                "86, 80, " +
                "04, 75, " +
                "12, 25, " +
                "05, 19, " +
                "39, 15, " +
                " 0,  0, 22, 100, " +
                " 0,  0,  0, 12, " +
                "0x1A ";

        String loadLimitCrossedElectricalMessage = "" +
                "33 " +
                "54, 16, 90, 03, 45, 30, " +
                "20, 16,  8, 24,  7,  1, 16, " +
                "86, 80, " +
                "04, 75, " +
                "12, 25, " +
                "0x52, 0x09, " +
                "39, 15, " +
                " 0,  0, 22, 100, " +
                " 0,  0,  0, 12, " +
                "0x1A ";

        String billingMessage = "" +
                "39 " +
                "54, 16, 90, 03, 45, 30, " +
                "20, 16,  8, 24,  8,  1, 16, " +
                "0x01, 0xA4, 0x17, 0xE7, " +
                "0x00, 0x00, 0x80, 0x20, " +
                "0x00, 0x01, 0x1E, 0x49, " +
                "0x00, 0x00, 0x3A, 0x98, " +
                "0x00, 0x00, 0xC3, 0x50, " +
                "0x03, " +
                "0x0A, " +
                "0x00, 0x7F, " +
                "0x1A ";

        String[] tokenNos = new String[]{
                "05939856526755136829",
                "42770434566396632140",
                "10043551617096882638",
                "02822441512214139131",
                "07717719459071657035",
                "47290691673567845332",
                "38683176240694238207",
                "31289023873713232298",
                "16917547874173314546",
                "68686632163044618151",
                "47423289913273047500",
                "13012786046504062793",
                "00397147373362612155",
                "40395342922280226643",
                "03117914763824666597",
                "12827625832270369749",
                "42725725342012119204",
                "63058020555388699168",
                "45914914455940165558",
                "38160990963783833395"
        };

        byte[][] tokenResponseBytes = new byte[tokenNos.length][];

        for (int i = 0; i < tokenNos.length; i++) {
            tokenResponseBytes[i] = messageBytesGenerator.getTokenResponseMessageBytes(meterNo, tokenNos[i], TokenStatus.Accepted);
        }

        meterTopic = MessageFormat.format("E/1/{0}0", meterNo);


        testTopic = "Test";
        String[] topicFilters = new String[]{meterTopic, registrationTopic, electricalTopic, billingTopic, tokenResponseTopic, testTopic};

        String remoteServerURI = "tcp://192.168.11.50:1883";
        String localServerURI = "tcp://localhost:1883";
        String cloudMqttServerURI = "tcp://m21.cloudmqtt.com:19811";
        String serverURI = cloudMqttServerURI;
        String clientId = UUID.randomUUID().toString();

        StringToBytesConverter stringToBytesConverter = new AnySeparatorStringToBytesConverter();
        BytesToStringConverter bytesToStringConverter = new DecimalColonSeparatorBytesToStringConverter();
        MqttClient mqttClient = new PahoMqttClientWrapper(serverURI, clientId);
        DummyMqttClientWindow mqttClientWindow = new DummyMqttClientWindow(topicFilters);
        MqttClientWindowPresenter mqttClientWindowPresenter = new MqttClientWindowPresenter(mqttClientWindow, stringToBytesConverter, bytesToStringConverter, mqttClient);
        mqttClientWindowPresenter.initialize();

        mqttClientWindow.fireSubscribedButtonClicked();

//        String registrationMessage = bytesToStringConverter.convertBytesToString(registrationMessageBytes);
//        mqttClientWindow.sendMessage(registrationTopic, registrationMessage);

//        String registrationAcknowledgementMessage = bytesToStringConverter.convertBytesToString(registrationAcknowledgementMessageBytes);
//        mqttClientWindow.sendMessage(meterTopic, registrationAcknowledgementMessage);

//        mqttClientWindow.sendMessage(billingTopic, billingMessage);


//        mqttClientWindow.sendMessage(electricalTopic, electricalMessage);
//        mqttClientWindow.sendMessage(electricalTopic, loadLimitCrossedElectricalMessage);

//        mqttClient.setEventListener((topic, message) -> {
//            System.out.println(String.format("Received Topic - %s Message - %s", topic, bytesToStringConverter.convertBytesToString(message)));
//
//            if (message[0] == 19 && message[1] == 1) {
//                int sourcePadding = 8;
//                int destinationPadding = 14;
//                for (int i = 0; i < 10; i++) {
//                    tokenResponseBytes[destinationPadding + i] = message[sourcePadding + i];
//                }
//
//                String tokenResponseMessage = bytesToStringConverter.convertBytesToString(tokenResponseBytes);
//                System.out.println(String.format("Sending Topic - %s Message - %s", topic, tokenResponseMessage));
//                mqttClientWindow.sendMessage(tokenResponseTopic, tokenResponseMessage);
//            }
//        });

        String tokenResponseMessage = bytesToStringConverter.convertBytesToString(tokenResponseBytes[0]);
        mqttClientWindow.sendMessage(tokenResponseTopic, tokenResponseMessage);

        System.out.println("MQTT Client in Running...");
        try {
            int totalRun = 12;
            for (int i = 0; i < totalRun; i++) {
                mqttClientWindow.sendMessage(testTopic, "1:2:3");
                Thread.sleep(5000);
            }
        } finally {
            mqttClient.close();
            System.out.println("MQTT Client is Stopped");
        }
    }

    private static MessageBytesGenerator createMessageBytesGenerator() {
        return new MessageBytesGenerator1Point4Point6(new ActualCurrentTimestampProvider());
    }
}
