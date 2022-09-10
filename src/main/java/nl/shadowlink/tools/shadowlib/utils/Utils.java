package nl.shadowlink.tools.shadowlib.utils;

/**
 * Several usefull functions
 *
 * @author Shadow-Link
 */
public class Utils {

    /**
     * Shows a value in hex formatted as 0x####
     *
     * @param value The value to show as hex
     * @return The value as a hex string
     */
    public static String getHexString(int value) {
        String hex = Integer.toString(value, 16).toUpperCase();
        int size = 4;
        if (hex.length() > 4)
            size = 8;
        while (hex.length() != size) {
            hex = "0" + hex;
        }
        hex = "0x" + hex;
        return hex;
    }

    /**
     * Returns the shadername for the id
     *
     * @param type The id of the shader
     * @return The name of the shader as a string
     */
    public static String getShaderName(int type) {
        String ret = "Unknown";
        switch (type) {
            case 0x2b5170fd:
                ret = "Texture";
                break;
            case 0x608799c6:
                ret = "SpecularTexture";
                break;
            case 0x46b7c64f:
                ret = "NormalTexture";
                break;
            case -718597665:
                ret = "DiffuseMap1";
                break;
            case 606121937:
                ret = "DiffuseMap2";
                break;
            case -64241494:
                ret = "Vector";
                break;
            case 376311761:
                ret = "Vector";
                break;
            case 1212833469:
                ret = "Vector";
                break;
            case -15634147:
                ret = "Vector";
                break;
            case -160355455:
                ret = "Vector";
                break;
            case -2078982697:
                ret = "Vector";
                break;
            case -677643234:
                ret = "Vector";
                break;
            case -1168850544:
                ret = "Vector";
                break;
            case 424198508:
                ret = "Vector";
                break;
            case 514782960:
                ret = "Vector";
                break;
            case -260861532:
                ret = "Matrix";
                break;
        }
        ret += " (" + type + ")";
        return ret;
    }

    /**
     * Returns the shadertype for a certain id<br/>
     *
     * @param type
     * @return
     */
    public static String getShaderType(int type) {
        String ret = "Unknown " + type;
        switch (type) {
            case 0:
                ret = "Texture";
                break;
            case 4:
                ret = "Matrix";
                break;
            case 1:
                ret = "Vector";
                break;
        }
        return ret;
    }

    /**
     * TODO: Fix this
     *
     * @param fileName returns the type of file that is
     * @return
     */
    public static int getFileType(String fileName) {
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".dff")) {
            return Constants.ftDFF;
        } else if (fileName.endsWith(".txd")) {
            return Constants.ftTXD;
        } else if (fileName.endsWith(".col")) {
            return Constants.ftCOL;
        } else if (fileName.endsWith(".ipl")) {
            return Constants.ftIPL;
        } else if (fileName.endsWith(".ide")) {
            return Constants.ftIDE;
        } else if (fileName.endsWith(".wdr")) {
            return Constants.ftWDR;
        } else if (fileName.endsWith(".wdd")) {
            return Constants.ftWDD;
        } else if (fileName.endsWith(".wbn")) {
            return Constants.ftWBN;
        } else if (fileName.endsWith(".wbd")) {
            return Constants.ftWBD;
        } else if (fileName.endsWith(".wtd")) {
            return Constants.ftWTD;
        } else if (fileName.endsWith(".wft")) {
            return Constants.ftWFT;
        } else if (fileName.endsWith(".wad")) {
            return Constants.ftWAD;
        } else if (fileName.endsWith(".wpl")) {
            return Constants.ftWPL;
        } else if (fileName.endsWith(".ifp")) {
            return Constants.ftIFP;
        } else {
            return -1;
        }
    }

    public static int getResourceType(String fileName) {
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".wdr")) {
            return Constants.rtWDR;
        } else if (fileName.endsWith(".wdd")) {
            return Constants.rtWDD;
        } else if (fileName.endsWith(".wbn")) {
            return Constants.rtWBN;
        } else if (fileName.endsWith(".wbd")) {
            return Constants.rtWBD;
        } else if (fileName.endsWith(".wtd")) {
            return Constants.rtWTD;
        } else if (fileName.endsWith(".wft")) {
            return Constants.rtWFT;
        } else if (fileName.endsWith(".wad")) {
            return Constants.rtWAD;
        } else if (fileName.endsWith(".wpl")) {
            return Constants.rtWPL;
        } else {
            return -1;
        }
    }

    public static int getTotalMemSize(int Flags) {
        return (getSystemMemSize(Flags) + getGraphicsMemSize(Flags));
    }

    public static int getSystemMemSize(int Flags) {
        return (Flags & 0x7FF) << (((Flags >> 11) & 0xF) + 8);
    }

    public static int getGraphicsMemSize(int Flags) {
        return ((Flags >> 15) & 0x7FF) << (((Flags >> 26) & 0xF) + 8);
    }

}
