package com.nafeezabrar.mqtt.client.smartmeter.message.generator;

import com.nafeezabrar.mqtt.client.smartmeter.data.EventCounts;
import com.nafeezabrar.mqtt.client.smartmeter.data.Phase;
import com.nafeezabrar.mqtt.client.smartmeter.data.TokenStatus;

public interface MessageBytesGenerator {
    byte[] getRegistrationMessageBytes(long meterNo, Phase phase);

    byte[] getRegistrationAcknowledgementMessageBytes(long meterNo, boolean isRegistered);

    byte[] getTokenResponseMessageBytes(long meterNo, String tokenNo, TokenStatus tokenStatus);

    byte[] getEventStatusMessageBytes(long meterNo, EventCounts eventCounts);
}
