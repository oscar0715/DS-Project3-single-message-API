/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task2client;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 *
 * @author Jiaming
 */
public class Signature {

    // sensor 1 private key
    BigInteger d1 = new BigInteger("339177647280468990599683753475404338964037287357290649639740920420195763493261892674937712727426153831055473238029100340967145378283022484846784794546119352371446685199413453480215164979267671668216248690393620864946715883011485526549108913");
    BigInteger n1 = new BigInteger("2688520255179015026237478731436571621031218154515572968727588377065598663770912513333018006654248650656250913110874836607777966867106290192618336660849980956399732967369976281500270286450313199586861977623503348237855579434471251977653662553");

    // sensor 2 private key
    BigInteger d2 = new BigInteger("3056791181023637973993616177812006199813736824485077865613630525735894915491742310306893873634385114173311225263612601468357849028784296549037885481727436873247487416385339479139844441975358720061511138956514526329810536684170025186041253009");
    BigInteger n2 = new BigInteger("3377327302978002291107433340277921174658072226617639935915850494211665206881371542569295544217959391533224838918040006450951267452102275224765075567534720584260948941230043473303755275736138134129921285428767162606432396231528764021925639519");

    // signature for sensor1
    public String signSensor1(String sensorId, String timeStamp, String type, String temperature) {
        return sign(sensorId, timeStamp, type, temperature, n1, d1);
    }

    // signature for sensor2
    public String signSensor2(String sensorId, String timeStamp, String type, String temperature) {
        return sign(sensorId, timeStamp, type, temperature, n2, d2);
    }

    // general signature function
    public String sign(String sensorId, String timeStamp, String type, String temperature, BigInteger n, BigInteger d) {
        byte[] hash = getHashValue(sensorId, timeStamp, type, temperature);

    // System.out.println("hash = " + Arrays.toString(hash));
        BigInteger signature = rsaEncrypt(hash, d, n);
    // System.out.println("signature = " + signature);   
        return signature.toString();
    }

    /**
     * digital signature using private key Use the public key (d,n) to encrypt
     * the message
     *
     * @param message
     * @param n
     * @param e
     * @return byte[]
     */
    public BigInteger rsaEncrypt(byte[] message, BigInteger d, BigInteger n) {
        BigInteger m = new BigInteger(message); // m is the original clear text
        BigInteger c = m.modPow(d, n);
        return c;
    }

    /**
     * calculate the hash of the string after concatenate all the parts sensor
     * ID + TimeStamp + type + temperature value
     *
     * @param sensorId
     * @param timeStamp
     * @param type
     * @param temperature
     * @return
     */
    public byte[] getHashValue(String sensorId, String timeStamp, String type, String temperature) {

        // concatenate all the parts
        String originalString = sensorId + timeStamp + type + temperature;

        // caculate hash
        byte[] hash = calculateHashValue(originalString, "SHA");

        // when the biginteger used to encrypt is negative, 
        // we add an additional 0 to make it positive
        byte[] result = hash;
        if (new BigInteger(hash).compareTo(BigInteger.ZERO) < 0) {
            // add 0 to the front of the array
            result = new byte[hash.length + 1];
            result[0] = 0;
            for (int i = 1; i < result.length; i++) {
                result[i] = hash[i - 1];
            }
        }

        // System.out.println("hash : " + Arrays.toString(result));
        return result;
    }

    /**
     * calculate the hash result of a string
     *
     * @param originalString
     * @param hashFunction
     * @return
     */
    public byte[] calculateHashValue(String originalString, String hashFunction) {

        MessageDigest md = null;
        byte[] result = null;
        try {
            // md is a MessageDigest object that implements the specified digest algorithm.
            md = MessageDigest.getInstance(hashFunction);
            md.reset();
            // Updates the digest using the byte array of originalString.
            md.update(originalString.getBytes("UTF-8"));
            // Performs a final update on the digest using the specified array of bytes, then completes the digest computation.
            result = md.digest();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
}
