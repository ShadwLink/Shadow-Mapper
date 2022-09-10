package nl.shadowlink.tools.shadowlib.utils;

import java.io.File;

/**
 * @author Shadow-Link
 */
public class Filter extends javax.swing.filechooser.FileFilter {
    private final String[] extension;
    private final String description;
    private final boolean exactFileName;

    public Filter(String[] extension, String description, boolean exactFileName) {
        this.extension = extension;
        this.description = description;
        this.exactFileName = exactFileName;
    }

    public boolean accept(File f) {
        boolean ret = f.isDirectory();
        for (int i = 0; i < extension.length; i++) {
            if (exactFileName) {
                if (f.getName().equalsIgnoreCase(extension[i]))
                    ret = true;
            } else {
                if (f.getName().toLowerCase().equals(extension[i]))
                    ret = true;
            }
        }
        return ret;
    }

    public String getDescription() {
        return description;
    }
}
