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

    public static void main(String[] args) throws InterruptedException, IOException {
        MessageBytesGenerator messageBytesGenerator = createMessageBytesGenerator();

        long virtualMeterNo = 54169003454L;
        long realMeterNo = 54169007967L;

        long meterNo = realMeterNo;

        byte[] registrationMessageBytes = messageBytesGenerator.getRegistrationMessageBytes(meterNo, Phase.Single);

        byte[] registrationAcknowledgementMessageBytes = messageBytesGenerator.getRegistrationAcknowledgementMessageBytes(meterNo, false);

        byte[] electricalMessageBytes = new byte[]
                {
                        (byte) 38,
                        (byte) 54, (byte) 16, (byte) 90, (byte) 7, (byte) 96, (byte) 70,
                        (byte) 20, (byte) 16, (byte) 8, (byte) 26, (byte) 8, (byte) 1, (byte) 30, // 2016-08-24 07:01:16 YYYY MM DD HH MM SS
                        (byte) 0x56, (byte) 0x50, // Voltage (220.96)
                        (byte) 0x04, (byte) 0x4B, // Current (10.99)
                        (byte) 0x05, (byte) 0x3C, // Load Threshold (13.40)
                        (byte) 0x05, (byte) 0x13, // Active Power (12.99)
                        (byte) 0x27, (byte) 0x0F, // Max Monthly Demand (99.99)
                        (byte) 0x00, (byte) 0x00, (byte) 0x30, (byte) 0x80, // Accumulative Active Consumption 123.27
                        (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0x40, // Meter Constant
                        (byte) 0x04, (byte) 0xD2, // Power Factor (1.234)
                        (byte) 0x16, (byte) 0x2E, // Frequency (56.78)
                        (byte) 2, // Relay Status Off and Terminal Cover Open
                        (byte) 0x1A // Checksum
                };

        byte[] billingMessageBytes = new byte[]
                {
                        (byte) 39,
                        (byte) 54, (byte) 16, (byte) 90, (byte) 7, (byte) 96, (byte) 70,
                        (byte) 20, (byte) 16, (byte) 8, (byte) 24, (byte) 7, (byte) 1, (byte) 16, // 2016-08-24 07:01:16 YYYY MM DD HH MM SS
                        (byte) 0x21, (byte) 0x66, (byte) 0xD1, (byte) 0xB2, // AccumulativePurchasedCredit (5603864.82)
                        (byte) 0xFF, (byte) 0xFF, (byte) 0xE1, (byte) 0xEC, // RemainingCredit (-77.00)
                        (byte) 0x00, (byte) 0x01, (byte) 0xE9, (byte) 0xFB, // CreditUsedInCurrentMonth (1254.35)
                        (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x88, // LowCreditAlertValue (50)
                        (byte) 0x00, (byte) 0x00, (byte) 0x27, (byte) 0x10, // EmergencyCreditLimit (100)
                        (byte) 0x0C, // TariffIndex (12)
                        (byte) 0x07, // SequenceNo (7)
                        (byte) 0x00, (byte) 0x48, // KeyNo (72)
                        (byte) 0x1A // Checksum
                };

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


        String[] topicFilters = new String[]{meterTopic, registrationTopic, electricalTopic, billingTopic, tokenResponseTopic};

        String remoteServerURI = "tcp://192.168.11.50:1883";
        String pdbServerURI = "tcp://119.148.42.42:1883";
        String localServerURI = "tcp://localhost:1883";
        String cloudMqttServerURI = "tcp://m21.cloudmqtt.com:19811";
        String serverURI = remoteServerURI;
        String clientId = UUID.randomUUID().toString();

        StringToBytesConverter stringToBytesConverter = new AnySeparatorStringToBytesConverter();
        BytesToStringConverter bytesToStringConverter = new DecimalColonSeparatorBytesToStringConverter();
        MqttClient mqttClient = new PahoMqttClientWrapper(serverURI, clientId);
        DummyMqttClientWindow mqttClientWindow = new DummyMqttClientWindow(topicFilters);
        MqttClientWindowPresenter mqttClientWindowPresenter = new MqttClientWindowPresenter(mqttClientWindow, stringToBytesConverter, bytesToStringConverter, mqttClient);
        mqttClientWindowPresenter.initialize();

        mqttClientWindow.fireSubscribedButtonClicked();

//        mqttClient.sendMessage(registrationTopic, registrationMessageBytes);
//
//        mqttClient.sendMessage(meterTopic, registrationAcknowledgementMessageBytes);

//        mqttClient.sendMessage(billingTopic, billingMessageBytes);

        mqttClient.sendMessage(electricalTopic, electricalMessageBytes);

//        mqttClient.sendMessage(tokenResponseTopic, tokenResponseBytes[0]);

        System.out.println("MQTT Client in Running...");
        try {
            int totalRun = 1;
            for (int i = 0; i < totalRun; i++)
                Thread.sleep(5000);
        } finally {
            mqttClient.close();
            System.out.println("MQTT Client is Stopped");
        }
    }

    private static MessageBytesGenerator createMessageBytesGenerator() {
        return new MessageBytesGenerator1Point4Point6(new ActualCurrentTimestampProvider());
    }
}
