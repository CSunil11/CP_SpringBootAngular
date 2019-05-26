package com.ackermans.criticalpath.utils;

import java.security.spec.KeySpec;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class EncryptDecryptUtils {
	
	static final Logger logger = LoggerFactory.getLogger(EncryptDecryptUtils.class);

 	private static final String UNICODE_FORMAT = "UTF-8";
    private static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec ks;
    private SecretKeyFactory skf;
    private static Cipher cipher;
    byte[] arrayBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    private static SecretKey key;

    /**
     *  This is a encryptdecrypt constructor that contain a particular value
     * 
     * @throws Exception
     */
    public EncryptDecryptUtils() throws Exception {
        myEncryptionKey = "ThisIsSparta_ThisIsAMSSysteam_ThisIsSparta";
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = skf.generateSecret(ks);
    }


    /**
     *  This is a encrypt method which is used to encrypt string
     * 
     * @param unencryptedString
     * @return
     */
    public static Optional<String> encrypt(String unencryptedString) {
        
    	String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encodeBase64(encryptedText, false, true));
        } catch (Exception e) {
            logger.error("While encrypting String : "+unencryptedString, e);
        }
        return Optional.ofNullable(encryptedString);
    }


    /**
     * This is a decrypt method which is used to decrypt string
     * 
     * @param encryptedString
     * @return
     */
    public static Optional<String> decrypt(String encryptedString) {
    	
    	if(encryptedString == null)
    		return null;
    	
        String decryptedText=null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(encryptedString.getBytes());
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= new String(plainText);
        } catch (Exception e) {
        	logger.error("While decrypting String : "+encryptedString, e);
        }
        return Optional.ofNullable(decryptedText);
    }
}
