package nl.shadowlink.tools.shadowlib.utils;

import javax.crypto.Cipher;
import java.security.NoSuchAlgorithmException;

/**
 * Helper functions for encryption
 * <p/>
 * Created by Kilian 26/11/2014 22:04
 */
public class EncryptionUtils {

    /**
     * Checks if the Unlimited strength library is installed
     *
     * @return True if installed, false otherwise
     */
    public static final boolean isUnlimitedStrengthInstalled() {
        try {
            if (Cipher.getMaxAllowedKeyLength("AES") > 128) {
                return true;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }
}
