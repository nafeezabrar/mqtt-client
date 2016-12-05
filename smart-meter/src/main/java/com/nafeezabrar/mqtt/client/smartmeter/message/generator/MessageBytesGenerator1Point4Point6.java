package com.nafeezabrar.mqtt.client.smartmeter.message.generator;

import com.nafeezabrar.mqtt.client.smartmeter.data.Phase;
import com.nafeezabrar.mqtt.client.smartmeter.data.TokenStatus;
import com.nafeezabrar.mqtt.client.smartmeter.utility.CurrentTimestampProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.nafeezabrar.mqtt.client.smartmeter.message.generator.CollectionUtility.reverse;

public class MessageBytesGenerator1Point4Point6 implements MessageBytesGenerator {
    protected static final int registrationMessageLength = 16;
    protected static final int registrationAcknowledgementMessageLength = 10;
    protected static final byte registrationAcknowledgementMessageTypeId = 0;
    protected static final int tokenResponseMessageLength = 26;
    protected final CurrentTimestampProvider currentTimestampProvider;

    public MessageBytesGenerator1Point4Point6(CurrentTimestampProvider currentTimestampProvider) {
        this.currentTimestampProvider = currentTimestampProvider;
    }

    @Override
    public byte[] getRegistrationMessageBytes(long meterNo, Phase phase) {
        byte[] bytes = createMeterMessageBytesWithInitialFillUp(registrationMessageLength, meterNo);

        bytes[14] = (byte) (phase == Phase.Single ? 1 : 3);
        bytes[bytes.length - 1] = getChecksum(bytes, 0, bytes.length);

        return bytes;
    }

    @Override
    public byte[] getRegistrationAcknowledgementMessageBytes(long meterNo, boolean isRegistered) {
        byte[] bytes = createServerMessageBytesWithInitialFillUp(registrationAcknowledgementMessageLength, registrationAcknowledgementMessageTypeId, meterNo);

        bytes[8] = (byte) (isRegistered ? 1 : 0);
        bytes[bytes.length - 1] = getChecksum(bytes, 0, bytes.length);

        return bytes;
    }

    @Override
    public byte[] getTokenResponseMessageBytes(long meterNo, String tokenNo, TokenStatus tokenStatus) {
        byte[] bytes = createMeterMessageBytesWithInitialFillUp(tokenResponseMessageLength, meterNo);
        byte[] tokenNoBytes = getTokenNoBytes(tokenNo);
        fillUpBytes(bytes, tokenNoBytes, 14, 24);
        bytes[24] = tokenStatus.getMessageByte();
        bytes[bytes.length - 1] = getChecksum(bytes, 0, bytes.length);
        return bytes;
    }

    public byte getChecksum(byte[] bytes, int start, int end) {
        byte checksum = 0;

        for (int i = start; i < end; i++)
            checksum ^= bytes[i];

        return checksum;
    }

    private byte[] createMeterMessageBytesWithInitialFillUp(int length, long meterNo) {
        byte[] bytes = new byte[length];
        bytes[0] = (byte) length;
        byte[] meterNoBytes = getMeterNoBytes(meterNo);
        fillUpBytes(bytes, meterNoBytes, 1, 7);
        byte[] timestampBytes = getTimestampBytes(currentTimestampProvider.getCurrentTimestamp());
        fillUpBytes(bytes, timestampBytes, 7, 14);
        return bytes;
    }

    private byte[] createServerMessageBytesWithInitialFillUp(int length, byte messageTypeId, long meterNo) {
        byte[] bytes = new byte[length];
        bytes[0] = (byte) length;
        bytes[1] = messageTypeId;
        byte[] meterNoBytes = getMeterNoBytes(meterNo);
        fillUpBytes(bytes, meterNoBytes, 2, 8);
        return bytes;
    }

    public byte[] getMeterNoBytes(long meterNo) {
        return getDigitizeBytes(meterNo * 10);
    }

    public byte[] getDigitizeBytes(long number) {
        List<Byte> bytes = new ArrayList<>();
        do {
            bytes.add((byte) (number % 100));
            number /= 100;
        } while (number != 0);
        return CollectionUtility.toArray(reverse(bytes));
    }

    public void fillUpBytes(byte[] originalBytes, byte[] replacerBytes, int start, int end) {
        for (int i = start; i < end; i++) {
            originalBytes[i] = replacerBytes[i - start];
        }
    }

    public byte[] getTimestampBytes(Date timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);

        byte[] bytes = new byte[7];

        bytes[0] = (byte) (calendar.get(Calendar.YEAR) / 100);
        bytes[1] = (byte) (calendar.get(Calendar.YEAR) % 100);
        bytes[2] = (byte) (calendar.get(Calendar.MONTH) + 1 - Calendar.JANUARY);
        bytes[3] = (byte) calendar.get(Calendar.DATE);
        bytes[4] = (byte) calendar.get(Calendar.HOUR_OF_DAY);
        bytes[5] = (byte) calendar.get(Calendar.MINUTE);
        bytes[6] = (byte) calendar.get(Calendar.SECOND);

        return bytes;
    }

    public byte[] getTokenNoBytes(String tokenNo) {
        int tokenLength = 10;
        byte[] bytes = new byte[tokenLength];
        for (int i = 0; i < tokenLength; i++) {
            int upper = tokenNo.charAt(2 * i) - '0';
            int lower = tokenNo.charAt(2 * i + 1) - '0';
            bytes[i] = (byte) (upper * 10 + lower);
        }
        return bytes;
    }
}
