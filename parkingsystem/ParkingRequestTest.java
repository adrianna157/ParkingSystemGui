package ict4315.parkingsystem;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingRequestTest {

    @Test
    public void testConstructorAndToString() {
        Properties properties = new Properties();
        properties.setProperty("key", "value");
        ParkingRequest request = new ParkingRequest("command", properties);

        assertEquals("ParkingRequest{commandName='command', properties={key=value}}", request.toString());
    }

    @Test
    public void testToJson() {
        Properties properties = new Properties();
        properties.setProperty("key", "value");
        ParkingRequest request = new ParkingRequest("command", properties);

        String expectedJson = "{\"commandName\":\"command\",\"properties\":{\"key\":\"value\"}}";
        assertEquals(expectedJson, request.toJson());
    }

    @Test
    public void testFromJson() {
        String json = "{\"commandName\":\"command\",\"properties\":{\"key\":\"value\"}}";
        ParkingRequest expectedRequest = new ParkingRequest("command", new Properties() {{
            setProperty("key", "value");
        }});

        ParkingRequest actualRequest = ParkingRequest.fromJson(json);
        assertEquals(expectedRequest.toString(), actualRequest.toString());
    }
    @Test
    public void testGetCommandName() {
        Properties properties = new Properties();
        properties.setProperty("key", "value");
        ParkingRequest request = new ParkingRequest("command", properties);

        assertEquals("command", request.getCommandName());
    }

    @Test
    public void testGetProperties() {
        Properties properties = new Properties();
        properties.setProperty("key", "value");
        ParkingRequest request = new ParkingRequest("command", properties);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key", "value");

        assertEquals(expectedMap, request.getProperties());
    }
}
