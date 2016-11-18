package com.nafeezabrar.mqtt.client.conversion;

import java.util.StringTokenizer;

public class AnySeparatorStringToBytesConverter implements StringToBytesConverter {

    private final String hexRegex = "^0[xX][\\da-fA-F]+$";

    @Override
    public byte[] convertStringToBytes(String message) {
        StringTokenizer tokenizer = new StringTokenizer(message, ", \t\n:-");

        byte[] bytes = new byte[tokenizer.countTokens()];
        for (int i = 0; tokenizer.hasMoreTokens(); i++) {
            String token = tokenizer.nextToken();
            if (token.matches(hexRegex)) {
                bytes[i] = Integer.decode(token).byteValue();
            } else {
                bytes[i] = (byte) Integer.parseInt(token);
            }
        }

        return bytes;
    }
}
