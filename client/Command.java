/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict4315.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/**
 *
 * @author student
 */

public class Command {
  private final String name;
  private final String command;
  private final List<String> fieldNames;
  
  public Command(String name, String command, List<String> fieldNames) {
    this.name = name;
    this.command = command;
    this.fieldNames = Collections.unmodifiableList(fieldNames);
  }
  
  public String name() {
    return name;
  }
  
  public List<String> fieldNames() {
    return fieldNames;
  }

  public String execute(Map<String, String> data) {
    try {
      // Convert Map to JSON string
      Gson gson = new Gson();
      String jsonData = gson.toJson(data);

      // Convert JSON string back to Map
      Type type = new TypeToken<Map<String, String>>(){}.getType();
      Map<String, String> dataMap = gson.fromJson(jsonData, type);


      // Send JSON string to server and receive JSON response
      String jsonResponse = Client.runCommand(command, dataMap);

      // Convert JSON response to Map
      Map<String, String> responseMap = gson.fromJson(jsonResponse, type);

      return responseMap.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return e.getMessage();
    }
  }
}
