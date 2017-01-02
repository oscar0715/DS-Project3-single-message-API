/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task2server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Jiaming
 */
@WebService(serviceName = "Task2server")
public class Task2server {

    private XMLParser xmlParser = new XMLParser();
    private Authentication auth = new Authentication();
    List<Temperature> list1 = new ArrayList<>();
    List<Temperature> list2 = new ArrayList<>();

    /**
     * Web service operation
     * The service is to receive message
     * Then judge what kind of request it is.
     * Then use other methods to do the Business Requirement.
     */
    @WebMethod(operationName = "request")
    public String request(@WebParam(name = "message") String message) {
        //TODO write your implementation code here:
        Temperature temperature = xmlParser.parse(message);

        switch (temperature.getRequestType()) {
            case "1":
                return highTemperature(temperature);
            case "2":
                return lowTemperature(temperature);
            case "3":
                return getLastTemperature(temperature.getSensorId());
            case "4":
                return getLastTemperature(temperature.getSensorId());
            case "5":
                return getTemperatures();
        }

        return "Error Request";
    }
    
    public String highTemperature(Temperature temperature) {
        boolean authResult = auth.authenticateSensor1(temperature.getSensorId(), temperature.getTimeStamp(), temperature.getType(), temperature.getTemperature(), temperature.getSignature());

        if (authResult) {
            list1.add(temperature);
            return "success! Insert high temperature " + temperature;
        } else {
            return "invalid signature method";
        }
    }

    public String lowTemperature(Temperature temperature) {
        boolean authResult = auth.authenticateSensor2(temperature.getSensorId(), temperature.getTimeStamp(), temperature.getType(), temperature.getTemperature(), temperature.getSignature());

        if (authResult) {
            list2.add(temperature);
            return "success! Insert low temperature " + temperature;
        } else {
            return "invalid signature method";
        }
    }

    public String getLastTemperature(String sensorID) {
        //TODO write your implementation code here:
        if (sensorID.equals("1")) {
            if (list1.size() > 0) {
                return list1.get(list1.size() - 1).toString();
            }
            return "No record";

        } else {
            if (list2.size() > 0) {
                return list2.get(list2.size() - 1).toString();
            }
            return "No record";
        }
    }

    public String getTemperatures() {
        //TODO write your implementation code here:
        List<Temperature> result = new ArrayList<>();
        result.addAll(list1);
        result.addAll(list2);
        // Temperature are sorted by the time when inserting
        Collections.sort(result);
        return result.toString();
    }
}
