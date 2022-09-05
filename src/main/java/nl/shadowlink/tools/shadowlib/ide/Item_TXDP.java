

package nl.shadowlink.tools.shadowlib.ide;



import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shadow-Link
 */
public class Item_TXDP extends IDE_Item{
    private int gameType;
    public String texDic;
    public String texDicParent;

    public Item_TXDP(int gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        line = line.replace(" ", "");
        String split[] = line.split(",");
        texDic = split[0];
        texDicParent = split[1];
    }

    public void save(BufferedWriter output){
        try {
            String line = texDic;
            line += ", " + texDicParent;
            output.write(line + "\n");
        } catch (IOException ex) {
            Logger.getLogger(Item_OBJS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
