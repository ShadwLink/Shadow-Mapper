package nl.shadowlink.tools.shadowlib.utils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import nl.shadowlink.tools.io.ReadFunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utils used to load GTA:IV<br/>
 *
 * @author Shadow-Link
 * @date 13 Apr 2015.
 */
public class GTAIVUtils {

    /**
     * Tag used for logging
     */
    private static final String LOG_TAG = "GTAIVUtils";

    /**
     * Hash of the encryption key
     */
    private static final String ENCRYPTION_KEY_HASH = "1ab56fed7ec3ff01227b691533975dce47d769653ff775426a96cd6d5307565d";

    /**
     * Length of the encryption key
     */
    private static final int ENCRYPTION_KEY_LENGTH = 32;

    /**
     * file name that contains the settings
     */
    private static final String VERSION_FILE_NAME = "versions.json";

    /**
     * Tries to read the key from the gtaiv.exe
     *
     * @param pGameDir The directory the gtaiv.exe can be found in
     * @return byte array that contains the encryption key
     */
    public static byte[] findKey(final String pGameDir) {
        final ReadFunctions readFunctions = new ReadFunctions();
        readFunctions.openFile(pGameDir + "GTAIV.exe");

        byte[] key = new byte[ENCRYPTION_KEY_LENGTH];
        final Version[] versions = getVersions();

        // There was a problem loading the versions return
        if (versions == null) {
            return null;
        }

        // Loop through all version to retrieve the encryption key
        for (Version version : versions) {
            readFunctions.seek(version.getVersionOffset());
            key = readFunctions.readArray(ENCRYPTION_KEY_LENGTH);

            // Check if the key is valid, if it's not continue
            if (isEncryptionKeyValid(key)) {
                Logger.getLogger(LOG_TAG).log(Level.INFO, "Found version: " + version.getVersionName());
                break;
            }
            key = null;
        }

        readFunctions.closeFile();
        return key;
    }

    /**
     * Returns an array of possible GTA_IV versions
     *
     * @return an array of possible GTA_IV versions
     */
    private static Version[] getVersions() {
        final File versionFile = new File(VERSION_FILE_NAME);
        if (versionFile.isFile()) {
            try {
                FileReader fileReader = new FileReader(versionFile);
                BufferedReader br = new BufferedReader(fileReader);
                return new Gson().fromJson(br, Version[].class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Checks if the encryption key is the valid encryption key
     *
     * @param pEncryptionKey the encryption key to check
     * @return true if the encryption key is valid, false otherwise
     */
    private static boolean isEncryptionKeyValid(final byte[] pEncryptionKey) {
        return asHex(pEncryptionKey).equals(ENCRYPTION_KEY_HASH);
    }

    /**
     * Converts a byte array to a HEX String
     *
     * @param bytes the byte array to convert
     * @return a byte array as String
     */
    private static String asHex(final byte[] bytes) {
        String result = "";
        for (byte aB : bytes) {
            result += Integer.toString((aB & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    private class Version {
        @SerializedName("name")
        private String mVersionName;
        @SerializedName("offset")
        private String mVersionOffset;

        public String getVersionName() {
            return mVersionName;
        }

        public long getVersionOffset() {
            return Long.decode(mVersionOffset);
        }
    }
}
