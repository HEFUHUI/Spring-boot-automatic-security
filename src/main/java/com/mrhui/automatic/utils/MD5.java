package com.mrhui.automatic.utils;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MD5 {
    public String encode(String str,String salt) throws NoSuchAlgorithmException {
        return new Md5Hash(str,salt,2).toString();
    }
    public boolean match(String plaintext,String ciphertext,String salt){
        try {
            return encode(plaintext,salt).equals(ciphertext);
        } catch (NoSuchAlgorithmException e) {
            log.debug("md5-error->>{}",e.getMessage());
            return false;
        }
    }
}
