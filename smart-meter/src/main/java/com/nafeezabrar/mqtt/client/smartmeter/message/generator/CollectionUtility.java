package com.nafeezabrar.mqtt.client.smartmeter.message.generator;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtility {
    public static <T> List<T> reverse(List<T> forward) {
        List<T> backward = new ArrayList<>();

        for (int i = forward.size() - 1; i >= 0; i--)
            backward.add(forward.get(i));

        return backward;
    }

    public static byte[] toArray(List<Byte> bytes) {
        byte[] byteArray = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            byteArray[i] = bytes.get(i);
        }
        return byteArray;
    }
}
