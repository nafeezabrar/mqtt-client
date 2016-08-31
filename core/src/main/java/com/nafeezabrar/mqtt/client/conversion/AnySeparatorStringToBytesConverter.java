package com.nafeezabrar.mqtt.client.conversion;

import java.util.StringTokenizer;

public class AnySeparatorStringToBytesConverter implements StringToBytesConverter {

    @Override
    public byte[] convertStringToBytes(String message) {
        StringTokenizer tokenizer = new StringTokenizer(message, ", \t\n:-");

        byte[] bytes = new byte[tokenizer.countTokens()];
        for (int i = 0; tokenizer.hasMoreTokens(); i++) {
            String token = tokenizer.nextToken();
            bytes[i] = Integer.decode(token).byteValue();
        }

        return bytes;
    }
}
