/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task2client;

import java.util.Date;

/**
 *
 * @author Jiaming
 */
public class Project3Task2Client {

    // signature is used to create digital signature
    private static Signature signature = new Signature();

    //
    private static XMLWrapper xmlWrapper = new XMLWrapper();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        
        String s1 = sendHighTemp("1", "23");
        System.out.println("s1 : " + s1);

        // second low temperature
        String s2 = sendLowTemp("2", "3");
        System.out.println("s2 : " + s2);

        // third high temperature
        String s3 = sendHighTemp("1", "24");
        System.out.println("s3 : " + s3);

        // fourth high temperature
        String s4 = sendWrongHighTemperature( "100");
        System.out.println("s4 : " + s4);
        
        // Get Last Temperature from Sensor 1
        String LastTempSensor1 = getLastTemperature("1");
        System.out.println("LastTempSensor1 : " + LastTempSensor1);
        
        // Get Last Temperature from Sensor 2
        String LastTempSensor2 = getLastTemperature("2");
        System.out.println("LastTempSensor2 : " + LastTempSensor2);
        
        // Get all Temperature 
        String allTemp = getTemperatures();
        System.out.println("allTemp : " + allTemp);
    }

    public static String sendWrongHighTemperature(String temperature) {
        Date date = new Date();
        String timeStamp = date.getTime() + "";
        String type = "Celsius";
        String sign = "13513453246541235134512351251";
        return request(xmlWrapper.getXML(RequestType.High, "1", timeStamp, type, temperature, sign));
    }

    public static String sendHighTemp(String sensorID, String temperature) {
        RequestType requestType = RequestType.High;
        String xmlString = generateMessage(requestType, sensorID, temperature);
        return request(xmlString);
    }

    public static String sendLowTemp(String sensorID, String temperature) {
        RequestType requestType = RequestType.Low;
        String xmlString = generateMessage(requestType, sensorID, temperature);
        
        return request(xmlString);
    }

    public static String getLastTemperature(String sensorID) {
        String xmlString = "";
        if (sensorID.equals("1")) {
            RequestType requestType = RequestType.LastHigh;
            xmlString = generateMessage(requestType, sensorID, " ");
        } else {
            RequestType requestType = RequestType.LastLow;
            xmlString = generateMessage(requestType, sensorID, " ");
        }
        return request(xmlString);
    }

    public static String getTemperatures() {
        RequestType requestType = RequestType.allTemp;
        String xmlString = generateMessage(requestType, " ", " ");
        return request(xmlString);
    }

    public static String generateMessage(RequestType requestType, String sensorID, String temperature) {
        Date date = new Date();
        String timeStamp = date.getTime() + " ";
        String type = "Celsius";
        String sign;
        if (sensorID.equals("1")) {
            sign = signature.signSensor1(sensorID, timeStamp, type, temperature);
        } else {
            sign = signature.signSensor2(sensorID, timeStamp, type, temperature);
        }
        String xmlString = "";
        if (requestType.getIndex() == 1 || requestType.getIndex() == 2) {
            xmlString = xmlWrapper.getXML(requestType, sensorID, timeStamp, type, temperature, sign);
        } else {
            xmlString = xmlWrapper.getXML(requestType, sensorID, " ", " ", " ", " ");
        }

        return xmlString;
    }

    private static String request(java.lang.String message) {
        project3task2server.Task2Server_Service service = new project3task2server.Task2Server_Service();
        project3task2server.Task2Server port = service.getTask2ServerPort();
        return port.request(message);
    }

}
