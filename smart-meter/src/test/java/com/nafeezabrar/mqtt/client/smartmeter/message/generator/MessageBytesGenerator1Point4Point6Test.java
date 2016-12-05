package com.nafeezabrar.mqtt.client.smartmeter.message.generator;

import com.nafeezabrar.mqtt.client.smartmeter.data.Phase;
import com.nafeezabrar.mqtt.client.smartmeter.data.TokenStatus;
import com.nafeezabrar.mqtt.client.smartmeter.utility.CurrentTimestampProviderStub;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class MessageBytesGenerator1Point4Point6Test {

    private MessageBytesGenerator1Point4Point6 messageGenerator;

    @Before
    public void setUp() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, Calendar.APRIL, 12, 23, 56, 41);
        Date fixedTime = calendar.getTime();
        CurrentTimestampProviderStub currentTimestampProviderStub = new CurrentTimestampProviderStub(fixedTime);
        messageGenerator = new MessageBytesGenerator1Point4Point6(currentTimestampProviderStub);
    }

    @Test
    public void generateRegistrationMessageCorrectly() throws Exception {
        byte[] expectedMessageBytes = new byte[]{
                16, // message length
                54, 16, 90, 3, 45, 30, // meter id
                20, 16, 4, 12, 23, 56, 41, // 12-04-2015 23:56:41 YYYY MM DD HH MM SS
                1,
                87
        };
        byte[] actualMessageBytes = messageGenerator.getRegistrationMessageBytes(54169003453L, Phase.Single);
        assertArrayEquals(expectedMessageBytes, actualMessageBytes);
    }

    @Test
    public void generateRegistrationAcknowledgementMessageCorrectly() throws Exception {
        byte[] expectedMessageBytes = new byte[]{
                10, // message length
                0, // message type id
                54, 16, 90, 3, 45, 30, // meter id
                0, // not registered
                0x46 // Checksum
        };
        byte[] actualMessageBytes = messageGenerator.getRegistrationAcknowledgementMessageBytes(54169003453L, false);
        assertArrayEquals(expectedMessageBytes, actualMessageBytes);
    }

    @Test
    public void generateTokenResponseMessageCorrectly() throws Exception {
        byte[] expectedMessageBytes = new byte[]{
                26, // message length
                54, 16, 90, 3, 45, 30, // meter id
                20, 16, 4, 12, 23, 56, 41, // 12-04-2015 23:56:41 YYYY MM DD HH MM SS
                54, 43, 26, 96, 17, 48, 33, 68, 16, 3, // token no
                0, // Token Accepted
                0x6C // Checksum
        };
        byte[] actualMessageBytes = messageGenerator.getTokenResponseMessageBytes(54169003453L, "54432696174833681603", TokenStatus.Accepted);
        assertArrayEquals(expectedMessageBytes, actualMessageBytes);
    }

    @Test
    public void generatesMeterNoBytesCorrectly() throws Exception {
        assertMeterNoBytes(54169003453L, new byte[]{54, 16, 90, 3, 45, 30});
        assertMeterNoBytes(54169001234L, new byte[]{54, 16, 90, 1, 23, 40});
        assertMeterNoBytes(54169008585L, new byte[]{54, 16, 90, 8, 58, 50});
    }

    private void assertMeterNoBytes(long meterNo, byte[] expectedBytes) {
        byte[] bytes = messageGenerator.getMeterNoBytes(meterNo);
        assertArrayEquals(bytes, expectedBytes);
    }

    @Test
    public void generateTokenNoBytesCorrectly() throws Exception {
        assertTokenNoBytes("54432696174833681603", new byte[]{54, 43, 26, 96, 17, 48, 33, 68, 16, 3});
        assertTokenNoBytes("16780355075478046129", new byte[]{16, 78, 3, 55, 7, 54, 78, 4, 61, 29});
        assertTokenNoBytes("35757992630103159047", new byte[]{35, 75, 79, 92, 63, 1, 3, 15, 90, 47});
        assertTokenNoBytes("02240550933310044036", new byte[]{2, 24, 5, 50, 93, 33, 10, 4, 40, 36});
        assertTokenNoBytes("62725653277976436404", new byte[]{62, 72, 56, 53, 27, 79, 76, 43, 64, 4});
        assertTokenNoBytes("72048551949431038125", new byte[]{72, 4, 85, 51, 94, 94, 31, 3, 81, 25});
        assertTokenNoBytes("22897787900844273326", new byte[]{22, 89, 77, 87, 90, 8, 44, 27, 33, 26});
        assertTokenNoBytes("12918746528639414869", new byte[]{12, 91, 87, 46, 52, 86, 39, 41, 48, 69});
        assertTokenNoBytes("11587166570846802249", new byte[]{11, 58, 71, 66, 57, 8, 46, 80, 22, 49});
    }

    private void assertTokenNoBytes(String tokenNo, byte[] expectedBytes) {
        byte[] bytes = messageGenerator.getTokenNoBytes(tokenNo);
        assertArrayEquals(expectedBytes, bytes);
    }

    @Test
    public void generatesTimestampBytesCorrectly() throws Exception {
        assertGetTimestampBytes(2015, Calendar.DECEMBER, 31, 23, 59, 59, new byte[]{20, 15, 12, 31, 23, 59, 59});
        assertGetTimestampBytes(2016, Calendar.JANUARY, 1, 0, 0, 0, new byte[]{20, 16, 1, 1, 0, 0, 0});
        assertGetTimestampBytes(2016, Calendar.JULY, 23, 19, 45, 25, new byte[]{20, 16, 7, 23, 19, 45, 25});
    }

    private void assertGetTimestampBytes(int year, int month, int date, int hourOfDay, int minute, int second, byte[] expectedBytes) {
        Calendar timestamp = Calendar.getInstance();
        timestamp.set(year, month, date, hourOfDay, minute, second);

        byte[] bytes = messageGenerator.getTimestampBytes(timestamp.getTime());
        assertArrayEquals(expectedBytes, bytes);
    }

    @Test
    public void setChecksumCorrectly() throws Exception {
        byte[] bytes = {0, 1, 2, 3, 4, 5};
        assertSetChecksum(bytes, 0, 3, (byte) 3);
        assertSetChecksum(bytes, 0, 4, (byte) 0);
        assertSetChecksum(bytes, 0, 6, (byte) 1);
        assertSetChecksum(bytes, 1, 5, (byte) 4);
    }

    private void assertSetChecksum(byte[] bytes, int start, int end, byte expectedChecksum) {
        byte checksum = messageGenerator.getChecksum(bytes, start, end);
        assertEquals(expectedChecksum, checksum);
    }

    @Test
    public void fillUpBytesTest() throws Exception {
        byte[] originalBytes1 = {0, 1, 2, 3, 4, 5};
        byte[] replacerBytes1 = {-2, -3, -4};
        byte[] expectedBytes1 = {0, 1, -2, -3, -4, 5};
        messageGenerator.fillUpBytes(originalBytes1, replacerBytes1, 2, 5);
        assertArrayEquals(expectedBytes1, originalBytes1);
        byte[] originalBytes2 = {0, 1, 2, 3, 4, 5};
        byte[] replacerBytes2 = {-2, -3, -4};
        byte[] expectedBytes2 = {0, 1, -2, -3, -4, 5};
        messageGenerator.fillUpBytes(originalBytes2, replacerBytes2, 2, 5);
        assertArrayEquals(expectedBytes2, originalBytes2);
    }

    @Test
    public void generateDigitizeBytes() throws Exception {
        assertDigitizeBytes(123, new byte[]{1, 23});
        assertDigitizeBytes(541690012340L, new byte[]{54, 16, 90, 1, 23, 40});
    }

    private void assertDigitizeBytes(long number, byte[] expectedBytes) {
        byte[] bytes = messageGenerator.getDigitizeBytes(number);
        assertArrayEquals(expectedBytes, bytes);
    }

}
