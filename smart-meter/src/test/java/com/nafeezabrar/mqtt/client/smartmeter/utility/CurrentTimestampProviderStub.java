package com.nafeezabrar.mqtt.client.smartmeter.utility;

import java.util.Date;

public class CurrentTimestampProviderStub implements CurrentTimestampProvider {
    private Date timestamp;

    public CurrentTimestampProviderStub(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public Date getCurrentTimestamp() {
        return  timestamp;
    }
}
