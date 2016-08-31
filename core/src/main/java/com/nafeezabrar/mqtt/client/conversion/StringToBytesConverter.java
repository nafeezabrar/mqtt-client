package com.nafeezabrar.mqtt.client.conversion;

public interface StringToBytesConverter {
    byte[] convertStringToBytes(String message);
}
