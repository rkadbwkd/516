/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.demo.common.file.xroshot.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec; 
 
/**
* @class Crypt.java
* @brief xroshot연동 암호화 클래스
*
* @author TFM 개발팀
* @date 2014.3.19
* @version 1.0.0.1
* @Modification  Information
*   수정일          수정자                수정내용
*  ----------------------------------------------------------------------------
*  2014.3.19
**/
public class Crypt {
    public static byte[] encrypto1(byte[] bytes) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(bytes);
        byte[] hashBytes = md.digest();

        /*
        printHEX(hashBytes);
        */

        byte[] derivedKey = createCryptDeriveKey(hashBytes, "SHA1", 20);
        return derivedKey;
    }

    public static byte[] createCryptDeriveKey(byte[] hBaseData, String hashAlgorithm, int requiredLength) {
        int keyLength = hBaseData.length;
        byte[] derivedKey = new byte[requiredLength];
        /*if(keyLength >= requiredLength) {
            for(int i = 0; i < requiredLength; i++) {
                derivedKey[i] = hBaseData[i];
            }
            return derivedKey;
        }*/
        byte[] buff1 = new byte[64];
        byte[] buff2 = new byte[64];
        Arrays.fill(buff1, (byte) 0x36);
        Arrays.fill(buff2, (byte) 0x5C);
        for(int i = 0; i < keyLength; i++) {
            buff1[i] ^= hBaseData[i];
            buff2[i] ^= hBaseData[i];
        }
        try {
            MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
            md.reset();
            // use the named algorithm to hash those buffers
            byte[] result1 = md.digest(buff1);
            md.reset();
            byte[] result2 = md.digest(buff2);
            for(int i = 0; i < requiredLength; i++) {
                if(i < result1.length) {
                    derivedKey[i] = result1[i];
                }
                else {
                    break;
                    //derivedKey[i] = result2[i - result1.length];
                }
            }
        }
        catch(NoSuchAlgorithmException nsae) {nsae.printStackTrace(); }
        return derivedKey;
    }
 
    public static String encrypto2(byte[] data1, byte[] data2) throws Exception {
        //printHEX(data1);

        byte[] enc1 = encrypto1(data2);
        SecretKeySpec sks = new SecretKeySpec(enc1, 0, 16, "AES");
        byte[] iv = new byte[16];
        IvParameterSpec ivps = new IvParameterSpec(iv);
        //printHEX(ivps.getIV());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sks, ivps);
        byte[] enc2 = cipher.doFinal(data1);

        /*
        printHEX(enc1);
        printHEX(enc2);
        */ 
        String enc3 = org.apache.commons.codec.binary.Base64.encodeBase64String(enc2);
        return enc3;
    }

    public static String decrypto2(String data, byte[] key) throws Exception {
        byte[] encKey = encrypto1(key);

        byte[] decData = org.apache.commons.codec.binary.Base64.decodeBase64(data);

        /*
        printHEX(decData);
        */

        SecretKeySpec sks = new SecretKeySpec(encKey, 0, 16, "AES");
        byte[] iv = new byte[16];
        IvParameterSpec ivps = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, sks, ivps);
        byte[] dec1 = cipher.doFinal(decData);

        /*
        printHEX(encKey);
        printHEX(dec1);
        */

        return new String(dec1);
    }
}

