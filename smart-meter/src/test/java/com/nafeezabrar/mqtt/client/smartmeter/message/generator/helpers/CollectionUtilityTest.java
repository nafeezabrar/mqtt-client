package com.nafeezabrar.mqtt.client.smartmeter.message.generator.helpers;

import com.nafeezabrar.mqtt.client.smartmeter.message.generator.CollectionUtility;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class CollectionUtilityTest {

    @Test
    public void reverseListCorrectly() throws Exception {
        List<Integer> forwardSequence1 = new ArrayList<>();
        forwardSequence1.add(1);
        forwardSequence1.add(2);
        forwardSequence1.add(3);
        forwardSequence1.add(4);
        forwardSequence1.add(5);
        List<Integer> expectedBackwardSequence1 = new ArrayList<>();
        expectedBackwardSequence1.add(5);
        expectedBackwardSequence1.add(4);
        expectedBackwardSequence1.add(3);
        expectedBackwardSequence1.add(2);
        expectedBackwardSequence1.add(1);
        assertReverse(forwardSequence1, expectedBackwardSequence1);

        List<Integer> forwardSequence2 = new ArrayList<>();
        forwardSequence2.add(1);
        forwardSequence2.add(2);
        forwardSequence2.add(3);
        List<Integer> expectedBackwardSequence2 = new ArrayList<>();
        expectedBackwardSequence2.add(3);
        expectedBackwardSequence2.add(2);
        expectedBackwardSequence2.add(1);

        assertReverse(forwardSequence2, expectedBackwardSequence2);
    }

    private void assertReverse(List<Integer> forwardSequence, List<Integer> expectedBackwardSequence) {
        List<Integer> backwardSequence = CollectionUtility.reverse(forwardSequence);
        Assert.assertEquals(expectedBackwardSequence, backwardSequence);
    }

    @Test
    public void generatesByteArrayFromList() throws Exception {
        List<Byte> bytes1 = new ArrayList<>();
        bytes1.add((byte) 12);
        bytes1.add((byte) 13);
        bytes1.add((byte) 14);
        bytes1.add((byte) 15);
        assertToArray(bytes1, new byte[]{12, 13, 14, 15});
        List<Byte> bytes2 = new ArrayList<>();
        bytes2.add((byte) 1);
        bytes2.add((byte) 5);
        bytes2.add((byte) 0);
        bytes2.add((byte) 56);
        assertToArray(bytes2, new byte[]{1, 5, 0, 56});
    }

    private void assertToArray(List<Byte> bytes, byte[] expectedBytes) {
        byte[] byteArray = CollectionUtility.toArray(bytes);
        assertArrayEquals(expectedBytes, byteArray);
    }
}
