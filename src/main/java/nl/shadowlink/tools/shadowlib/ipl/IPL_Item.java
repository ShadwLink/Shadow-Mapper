package nl.shadowlink.tools.shadowlib.ipl;

import com.nikhaldimann.inieditor.IniEditor;
import nl.shadowlink.tools.io.ReadFunctions;

/**
 * @author Shadow-Link
 */
public abstract class IPL_Item {

    public abstract void read(String line);

    public abstract void read(ReadFunctions rf);

    public abstract void read(ReadFunctions rf, IniEditor ini);

}
