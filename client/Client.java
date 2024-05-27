/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict4315.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ict4315.parkingsystem.ParkingRequest;
import ict4315.parkingsystem.ParkingResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

/**
 *
 * @author student
 */
public class Client {

    public static final String[][] COMMANDS = new String[][]{
        {"Register Customer", "CUSTOMER", "First Name", "Last Name", "Phone number", "Address 1", "Address 2", "City", "State", "Zipcode"},
        {"Register Vehicle", "CAR", "COMPACT/SUV", "License", "Customer Id"},
//        {"Start Parking", "PARK", "Permit Id", "Time"},
        {"Finish Parking", "PARK", "Parking lot Id", "Permit Id", "Time in", "Time out"},
        {"Get Charges", "CHARGES", "Customer Id", "Permit Id"},};

    private static final int PORT = 7777;
    private static final String SERVER = "localhost";

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String runCommand(String data)
            throws IOException {

        InetAddress host = InetAddress.getByName(SERVER);
        String jsonResult = "";
        try (Socket link = new Socket(host, PORT)) {
            PrintWriter out = new PrintWriter(link.getOutputStream(), true);
            out.println(data);
            out.flush();

            // connect to server
            System.out.println("You are now connected to: " + host.getHostAddress());

            // Read JSON string from server and convert to ResponseData object
            Scanner in = new Scanner(link.getInputStream());
            if (in.hasNextLine()) {
                String jsonResponse = in.nextLine();
                ParkingResponse responseData = gson.fromJson(jsonResponse, ParkingResponse.class);
                jsonResult = gson.toJson(responseData);
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
        } finally {
            return jsonResult;
        }
    }

    public static Map<String, Command> commands() {
        Map<String, Command> commands = new HashMap<>();
        for (String[] description : COMMANDS) {
            commands.put(description[1], new Command(description[0], description[1],
                    Arrays.asList(description).subList(2, description.length)));
        }
        return commands;
    }

    /**
     * Run this as: $ java ict4300.week8.client.Client COMMAND label1=value1
     * label2=value2 ... Use LIST to get the list of commands and their labels.
     */
    public static void main(String[] args) throws IOException {

        if (args.length == 0 || args[0].equals("LIST")) {
            System.out.println("Here are the commands we know about.");
            System.out.println(
                    "Usage: $ java ict4300.week8.client.Client COMMAND label1=value1 label2=value2 ...");
            System.out.println();
            for (String[] description : COMMANDS) {
                System.out.format("%s: %s ", description[0], description[1]);
                for (int i = 2; i < description.length; ++i) {
                    System.out.format("%s=value ", description[i].replaceAll(" ", "").toLowerCase());
                }
                System.out.println();
            }
            return;
        }

        Command command = commands().get(args[0]);
        if (command == null) {
            System.out.println("Unrecognised command: " + args[0]);
            System.out.print("Known commands: ");
            String comma = "";
            for (String[] description : COMMANDS) {
                System.out.print(comma + description[1]);
                comma = ", ";
            }
            System.out.println();
            return;
        }
        Map<String, String> values = new LinkedHashMap<>();
        for (String label : command.fieldNames()) {
            for (int i = 0; i < args.length; ++i) {
                if (args[i].startsWith(label.replaceAll(" ", "").toLowerCase())) {
                    values.put(label, args[i].replaceAll(".*=", ""));
                    break;
                }
            }
        }
    }
}
