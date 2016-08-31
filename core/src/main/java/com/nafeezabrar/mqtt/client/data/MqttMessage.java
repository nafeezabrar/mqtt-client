package com.nafeezabrar.mqtt.client.data;

import java.util.Arrays;
import java.util.Objects;

public class MqttMessage {
    private final String topic;
    private final byte[] bytes;

    public MqttMessage(String topic, byte[] bytes) {
        this.topic = topic;
        this.bytes = bytes;
    }

    public String getTopic() {
        return topic;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || this.getClass() != that.getClass()) return false;
        return equals((MqttMessage) that);
    }

    private boolean equals(MqttMessage that) {
        return Objects.equals(this.topic, that.topic) && Arrays.equals(this.bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, bytes);
    }
}
