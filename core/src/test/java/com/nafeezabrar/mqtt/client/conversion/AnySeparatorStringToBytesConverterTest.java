package com.nafeezabrar.mqtt.client.conversion;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class AnySeparatorStringToBytesConverterTest {

    private AnySeparatorStringToBytesConverter stringToBytesConverter;

    @Before
    public void setUp() throws Exception {
        stringToBytesConverter = new AnySeparatorStringToBytesConverter();
    }

    @Test
    public void couldConvertEmptyString() throws Exception {
        byte[] bytes = stringToBytesConverter.convertStringToBytes("");

        assertArrayEquals(new byte[0], bytes);
    }

    @Test
    public void couldConvertCommaSeparatedString() throws Exception {
        byte[] bytes = stringToBytesConverter.convertStringToBytes("20,50,127");
        assertArrayEquals(new byte[]{20, 50, 127}, bytes);
    }

    @Test
    public void couldConvertWhitespaceSeparatedString() throws Exception {
        byte[] bytes = stringToBytesConverter.convertStringToBytes("20\t50\n127 226");
        assertArrayEquals(new byte[]{20, 50, 127, (byte) 226}, bytes);
    }

    @Test
    public void couldConvertColonHyphenSeparatedString() throws Exception {
        byte[] bytes = stringToBytesConverter.convertStringToBytes("20 : 50 - 127");
        assertArrayEquals(new byte[]{20, 50, 127}, bytes);
    }

    @Test
    public void couldConvertHexDecOctNumbers() throws Exception {
        byte[] bytes = stringToBytesConverter.convertStringToBytes("0020 : 0xFF - 0x1A");
        assertArrayEquals(new byte[]{16, (byte) 0xFF, 26}, bytes);
    }
}