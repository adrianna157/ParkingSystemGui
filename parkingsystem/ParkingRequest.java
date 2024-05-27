package ict4315.parkingsystem;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ParkingRequest {
    private String commandName;
    private Properties properties;

    public ParkingRequest(String commandName, Properties properties) {
        this.commandName = commandName;
        this.properties = properties;
    }

    public String getCommandName() {
        return commandName;
    }

    public Map<String, String> getProperties() {
        // Convert Properties to Map
        Map<String, String> map = new HashMap<>();
        for (String name : properties.stringPropertyNames()) {
            map.put(name, properties.getProperty(name));
        }
        return map;
    }
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public static ParkingRequest fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ParkingRequest.class);
    }

    @Override
    public String toString() {
        return "ParkingRequest{" +
                "commandName='" + commandName + '\'' +
                ", properties=" + properties +
                '}';
    }
}
