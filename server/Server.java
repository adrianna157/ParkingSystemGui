/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict4315.server;

import ict4315.parkingsystem.*;

import static ict4315.parkingsystem.Main.WEEKDAY_CARTYPE_DISCOUNT;
import static ict4315.parkingsystem.Main.WEEKDAY_DISCOUNT;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author student
 */
public class Server {

    static {
        System.setProperty(
                "java.util.logging.SimpleFormatter.format", "%1$tc %4$-7s (%2$s) %5$s %6$s%n");
    }

    private static final Logger logger = Logger.getLogger(Server.class.getName());

    private final int PORT = 7777;

    private final ParkingService service;

    public Server(ParkingService service) {
        this.service = service;
    }

    public void startServer() throws IOException {
        logger.info("Starting server: " + InetAddress.getLocalHost().getHostAddress());
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setReuseAddress(true);
            while (true) {
                Socket client = serverSocket.accept();
                handleClient(client);
            }
        }
    }

    private void handleClient(Socket client) {
        try {
            // Using os to return output to client
            PrintWriter os = new PrintWriter(client.getOutputStream(), true);
            String output;
            try {
                // Process requestData from client and return output as responseData
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String input = reader.readLine();
                ParkingRequest request = ParkingRequest.fromJson(input);
                ParkingResponse response = service.handleInput(request);
                output = response.toJson();
            } catch (RuntimeException ex) {
                ex.printStackTrace();
                ParkingResponse response = new ParkingResponse(500, ex.getLocalizedMessage());
                output = response.toJson();
            }

            os.println(output);

        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to read from client.", e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to close client socket.", e);
            }
        }
    }

    /**
     * Run this as: $ java ict4300.week8.server.Server
     */
    public static void main(String[] args) throws Exception {
//    ParkingOffice parkingOffice = new ParkingOffice("Office", new Address());

        Address parkingOfficeAddress = new Address("20 Joseph Street", "South Iris", "Bronx", "NY", "");
        TransactionManager transactionManager = new TransactionManager();
        PermitManager permitManager = new PermitManager();
        ParkingOffice parkingOffice = new ParkingOffice("Main Office", parkingOfficeAddress, transactionManager, permitManager);
        ParkingService service = new ParkingService(parkingOffice);

        ParkingChargeCalculatorFactory parkingChargeCalculatorFactory = new ParkingChargeCalculatorFactory();

        //Create parking Lot A with WeekdayDiscount
        Address parkingLotAddressA = new Address("214 CherryCreek", "Broomfield", "Bronx", "CO", "");
        ParkingLot parkingLotA = new ParkingLot("1", "Lot A", parkingLotAddressA, WEEKDAY_DISCOUNT, parkingChargeCalculatorFactory);
        System.out.println(String.format("Parking lot %s applies %s", parkingLotA.getName(), parkingLotA.getDiscountStrategy()));

        //Create parking Lot B with WeekdayCarTypeDiscount
        Address parkingLotAddressB = new Address("111 University Blvd", "Littelton", "Denver", "Co", "");
        ParkingLot parkingLotB = new ParkingLot("2", "Lot B", parkingLotAddressB, WEEKDAY_CARTYPE_DISCOUNT, parkingChargeCalculatorFactory);
        System.out.println(String.format("Parking lot %s applies %s", parkingLotB.getName(), parkingLotB.getDiscountStrategy()));

        //Create parking Lot C with No Discount
        Address parkingLotAddressC = new Address("121 University Blvd", "Littelton", "Denver", "Co", "");
        ParkingLot parkingLotC = new ParkingLot("3", "Lot C", parkingLotAddressC, parkingChargeCalculatorFactory);
        System.out.println("Parking lot Lot C does not apply discount strategy");

        parkingOffice.addParkingLot(parkingLotA);
        parkingOffice.addParkingLot(parkingLotB);
        parkingOffice.addParkingLot(parkingLotC);

        new Server(service).startServer();
    }
}
