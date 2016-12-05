package com.nafeezabrar.mqtt.client.smartmeter.utility;

import java.util.Calendar;
import java.util.Date;

public class ActualCurrentTimestampProvider implements CurrentTimestampProvider {
    @Override
    public Date getCurrentTimestamp() {
        return Calendar.getInstance().getTime();
    }
}
