package ict4315.parkingsystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingResponseTest {

    @Test
    public void testConstructorAndToString() {
        ParkingResponse response = new ParkingResponse(200, "OK");

        assertEquals("ParkingResponse{statusCode=200, message='OK'}", response.toString());
    }

    @Test
    public void testToJson() {
        ParkingResponse response = new ParkingResponse(200, "OK");

        String expectedJson = "{\"statusCode\":200,\"message\":\"OK\"}";
        assertEquals(expectedJson, response.toJson());
    }

    @Test
    public void testFromJson() {
        String json = "{\"statusCode\":200,\"message\":\"OK\"}";
        ParkingResponse expectedResponse = new ParkingResponse(200, "OK");

        ParkingResponse actualResponse = ParkingResponse.fromJson(json);
        assertEquals(expectedResponse.toString(), actualResponse.toString());
    }
}
