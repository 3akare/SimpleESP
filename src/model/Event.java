package model;

import java.time.Instant;

public class Event {
    private String rawData;
    private final Instant timestamp;

    public Event(String data){
        this.rawData = data;
        this.timestamp = Instant.now();
    }

    public String getRawData(){
        return this.rawData;
    }

    public String setRawData(String rawData){
        this.rawData = rawData;
        return rawData;
    }

    @Override
    public String toString() {
        return "Event [" +
                "raw data='" + rawData + '\'' +
                ", timestamp=" + timestamp +
                ']';
    }
}
