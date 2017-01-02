/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task2server;

/**
 *
 * @author Jiaming
 */
public class Temperature implements Comparable<Object> {

    private String requestType;
    private String sensorId;
    private String timeStamp;
    private String type;
    private String temperature;
    private String signature;

    public Temperature(String requestType,
            String sensorId,
            String timeStamp,
            String type,
            String temperature,
            String signature) {
        this.requestType = requestType;
        this.sensorId = sensorId;
        this.timeStamp = timeStamp;
        this.type = type;
        this.temperature = temperature;
        this.signature = signature;
    }

    /**
     * Temperature will be ordered by time
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        Temperature obj = (Temperature) o;
        return this.timeStamp.compareTo(obj.timeStamp);
    }

    @Override
    public String toString() {
        return temperature;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getType() {
        return type;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getSignature() {
        return signature;
    }
    
}
