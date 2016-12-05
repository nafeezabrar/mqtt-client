package com.nafeezabrar.mqtt.client.smartmeter.data;

public enum TokenStatus {
    Accepted,
    Rejected,
    Old,
    Used,
    Ok,
    Error01,
    Error02,
    Error03,
    Error04,
    Error05,
    Error06,
    Error07,
    Error08,
    PleaseSecond;

    public byte getMessageByte() {
        switch (this) {
            case Accepted:
                return (byte) 0;
            case Rejected:
                return (byte) 1;
            case Old:
                return (byte) 2;
            case Used:
                return (byte) 3;
            case Ok:
                return (byte) 4;
            case Error01:
                return (byte) 5;
            case Error02:
                return (byte) 6;
            case Error03:
                return (byte) 7;
            case Error04:
                return (byte) 8;
            case Error05:
                return (byte) 9;
            case Error06:
                return (byte) 10;
            case Error07:
                return (byte) 11;
            case Error08:
                return (byte) 12;
            case PleaseSecond:
                return (byte) 13;
            default:
                return (byte) 0;
        }
    }
}
