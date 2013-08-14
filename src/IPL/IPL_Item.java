/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IPL;

import com.nikhaldimann.inieditor.IniEditor;
import file_io.ReadFunctions;

/**
 *
 * @author Kilian
 */
public abstract class IPL_Item {

    public abstract void read(String line);
    public abstract void read(ReadFunctions rf);
    public abstract void read(ReadFunctions rf, IniEditor ini);

}
