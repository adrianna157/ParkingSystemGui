/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict4315.server;

import ict4315.parkingsystem.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author student
 */
public class ParkingService {

    protected final ParkingOffice parkingOffice;

    public ParkingService(ParkingOffice parkingOffice) {
        this.parkingOffice = parkingOffice;
    }

    protected ParkingResponse handleInput(ParkingRequest request) {
        return performCommand(request);
    }

    private ParkingResponse performCommand(ParkingRequest request) {
        Map<String, String> args = request.getProperties();
        ParkingResponse response;
        switch (request.getCommandName()) {
            case "Register Customer":
                Address customerAddress = new Address(args.get("Address 1"), args.get("Address 2"), args.get("City"), args.get("State"), args.get("Zipcode"));
                try {
                    response = new ParkingResponse(200, parkingOffice.register(args.get("First Name"), args.get("Last Name"), args.get("Phone number"), customerAddress));
                } catch (Exception ex) {
                    Logger.getLogger(ParkingService.class.getName()).log(Level.SEVERE, null, ex);
                    response = new ParkingResponse(500, ex.getLocalizedMessage());
                }
                break;

            case "Register Vehicle":
                try {
                    response = new ParkingResponse(200, parkingOffice.register(CarType.valueOf(args.get("COMPACT/SUV")), args.get("License"), args.get("Customer Id")));
                } catch (Exception ex) {
                    Logger.getLogger(ParkingService.class.getName()).log(Level.SEVERE, null, ex);
                    response = new ParkingResponse(500, ex.getLocalizedMessage());
                }
                break;

            case "Finish Parking": {
                try {
                    ParkingLot parkingLot = parkingOffice.getParkingLot(args.get("Parking lot Id"));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime dateTimeIn = LocalDateTime.parse(args.get("Time in"), formatter);
                    LocalDateTime dateTimeOut = LocalDateTime.parse(args.get("Time out"), formatter);
                    ParkingPermit permit = parkingOffice.getParkingPermit(args.get("Permit Id"));
                    ParkingEvent event = new ParkingEvent(parkingLot, dateTimeIn, dateTimeOut, permit);
                    response = new ParkingResponse(200, parkingOffice.park(event));
                } catch (Exception ex) {
                    Logger.getLogger(ParkingService.class.getName()).log(Level.SEVERE, null, ex);
                    response = new ParkingResponse(500, ex.getLocalizedMessage());
                }
            }
            break;

            case "Get Charges":
            {
                try {
                    response = new ParkingResponse(200, parkingOffice.getParkingCharges(parkingOffice.getCustomer(args.get("Customer Id"))));
                } catch (Exception ex) {
                    Logger.getLogger(ParkingService.class.getName()).log(Level.SEVERE, null, ex);
                    response = new ParkingResponse(500, ex.getLocalizedMessage());
                }
            }
            break;

            default:
                response = new ParkingResponse(400, "Command is not found");
        }
        return response;
    }

}
