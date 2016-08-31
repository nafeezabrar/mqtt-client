package com.nafeezabrar.mqtt.client.conversion;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DecimalColonSeparatorBytesToStringConverterTest {

    private DecimalColonSeparatorBytesToStringConverter bytesToStringConverter;

    @Before
    public void setUp() throws Exception {
        bytesToStringConverter = new DecimalColonSeparatorBytesToStringConverter();
    }

    private void assertConvertToString(byte[] bytes, String expected) {
        String string = bytesToStringConverter.convertBytesToString(bytes);
        assertEquals(expected, string);
    }

    @Test
    public void returnsEmptyStringForNoBytes() throws Exception {
        assertConvertToString(new byte[0], "");
    }

    @Test
    public void couldSeparateSingleBytesWithZeroPadding() throws Exception {
        assertConvertToString(new byte[]{12}, "12");
        assertConvertToString(new byte[]{0}, "00");
        assertConvertToString(new byte[]{1}, "01");
    }

    @Test
    public void couldSeparateManyBytesWithZeroPadding() throws Exception {
        assertConvertToString(new byte[]{12, 13}, "12:13");
        assertConvertToString(new byte[]{2, 13, 14}, "02:13:14");
        assertConvertToString(new byte[]{12, 13, 23, 0, 89, 48, 9, 23}, "12:13:23:00:89:48:09:23");
    }
}