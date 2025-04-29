package model;

import java.time.Instant;

public class Event {
    private final String rawData;
    private final Instant timestamp;

    public Event(String data){
        this.rawData = data;
        this.timestamp = Instant.now();
    }

    public String getRawData(){
        return this.rawData;
    }

    @Override
    public String toString() {
        return "Event [" +
                "raw data='" + rawData + '\'' +
                ", timestamp=" + timestamp +
                ']';
    }
}
