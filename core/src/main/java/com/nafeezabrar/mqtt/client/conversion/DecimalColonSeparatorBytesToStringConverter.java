package com.nafeezabrar.mqtt.client.conversion;

public class DecimalColonSeparatorBytesToStringConverter implements BytesToStringConverter {
    private final char SEPARATOR = ':';

    @Override
    public String convertBytesToString(byte[] bytes) {
        if (bytes.length == 0)
            return "";
        else {
            StringBuilder builder = new StringBuilder();
            builder.append(getFormatted(bytes[0]));
            for (int i = 1; i < bytes.length; i++) {
                builder.append(SEPARATOR);
                builder.append(getFormatted(bytes[i]));
            }
            return builder.toString();
        }
    }

    protected String getFormatted(byte aByte) {
        return String.format("%02d", aByte);
    }
}
