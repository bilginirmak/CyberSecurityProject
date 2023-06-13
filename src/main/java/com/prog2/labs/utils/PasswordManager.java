package com.prog2.labs.utils;

import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 * The Password Manager class.
 *   Encrypt and Validate encrypted password
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class PasswordManager {

    /**
     * Method encryptPassword
     * 
     * @param inputPassword
     * @return encrypted password
     */
	public static String encryptPassword(String inputPassword) {
        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        return encryptor.encryptPassword(inputPassword);
    }

    /**
     * Method checkPassword
     * 
     * @param inputPassword
     * @param encryptedStoredPassword
     * @return boolean (valid | not valid)
     */
    public static boolean checkPassword(String inputPassword, String encryptedStoredPassword) {
        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        return encryptor.checkPassword(inputPassword, encryptedStoredPassword);
    }
}
