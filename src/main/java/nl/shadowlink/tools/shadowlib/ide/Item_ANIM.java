package nl.shadowlink.tools.shadowlib.ide;

import nl.shadowlink.tools.shadowlib.utils.GameType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shadow-Link
 */
public class Item_ANIM extends IdeItem {
    private GameType gameType;

    public String modelName;
    public String textureName;

    public Item_ANIM(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        line = line.replace(" ", "");
        String split[] = line.split(",");

        modelName = split[0];
        textureName = split[1];

//		ModelName, TextureName, Wad, DrawDist, Flag1, Flag2, (Bounds min)X,Y,Z, (Bounds max)X,Y,Z, (Bounds Sphere)X,Y,Z, Radius, WDD
    }

    public void save(BufferedWriter output) {
        try {
            String line = "";
            output.write(line + "\n");
            System.out.println("Line: " + line);
        } catch (IOException ex) {
            Logger.getLogger(Item_OBJS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
