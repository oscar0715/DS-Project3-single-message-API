/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task2server;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 *
 * @author Jiaming
 */
public class Authentication {

    // sensor1 public key
    private BigInteger e1 = new BigInteger("65537");
    private BigInteger n1 = new BigInteger("2688520255179015026237478731436571621031218154515572968727588377065598663770912513333018006654248650656250913110874836607777966867106290192618336660849980956399732967369976281500270286450313199586861977623503348237855579434471251977653662553");

    // sensor2 public key
    private BigInteger e2 = new BigInteger("65537");
    private BigInteger n2 = new BigInteger("3377327302978002291107433340277921174658072226617639935915850494211665206881371542569295544217959391533224838918040006450951267452102275224765075567534720584260948941230043473303755275736138134129921285428767162606432396231528764021925639519");

    // authenticate Senor1
    public boolean authenticateSensor1(String sensorId, String timeStamp, String type, String temperature, String signature) {
        return authenticate(sensorId, timeStamp, type, temperature, signature, e1, n1);
    }

    // authenticate Senor2
    public boolean authenticateSensor2(String sensorId, String timeStamp, String type, String temperature, String signature) {
        return authenticate(sensorId, timeStamp, type, temperature, signature, e2, n2);
    }

    // general authenticate function
    public boolean authenticate(String sensorId, String timeStamp, String type, String temperature, String signature, BigInteger e, BigInteger d) {
        
        byte[] hash = getHashValue(sensorId, timeStamp, type, temperature);
        BigInteger decypt = rsaDecypt(new BigInteger(signature), e, d);
        return compareBytes(hash, decypt.toByteArray());
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
        byte[] hash = caculateHashValue(originalString, "SHA");

        
        byte[] result = hash;;
        // when the biginteger used to encrypt is negative, 
        // we add an additional 0 to make it positive
        if (new BigInteger(hash).compareTo(BigInteger.ZERO) < 0) {
            // add 0 to the front of the array
            result = new byte[hash.length + 1];
            result[0] = 0;
            for (int i = 1; i < result.length; i++) {
                result[i] = hash[i - 1];
            }
        }
        
        return result;
    }

    /**
     * calculate the hash result of a string
     *
     * @param originalString
     * @param hashFunction
     * @return
     */
    public byte[] caculateHashValue(String originalString, String hashFunction) {

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

    /**
     * Decrypt message
     *
     * @param message
     * @param n
     * @param d
     * @return
     */
    public BigInteger rsaDecypt(BigInteger c, BigInteger e, BigInteger n) {
        // System.out.println("encrypted message  : " + c);
        BigInteger clear = c.modPow(e, n);
        return clear;
    }

    /**
     * Compare two bytes
     *
     * @param hash
     * @param decrypt
     * @return
     */
    public boolean compareBytes(byte[] hash, byte[] decrypt) {
        // System.out.println("hash  : " + Arrays.toString(hash));
        // System.out.println("decrypt  : " + Arrays.toString(decrypt));
        if (hash.length != decrypt.length) {
            return false;
        }
        for (int i = 0; i < hash.length; i++) {
            if (hash[i] != decrypt[i]) {
                return false;
            }
        }
        return true;
    }
}
