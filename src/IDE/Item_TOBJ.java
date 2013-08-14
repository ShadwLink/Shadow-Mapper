/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IDE;

import file_io.Message;
import file_io.Vector3D;
import file_io.Vector4D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kilian
 */
public class Item_TOBJ extends IDE_Item{
    private int gameType;
    public String modelName; //III, VC, SA, IV
    public String textureName; //III, VC, SA, IV
    public int objectCount; //III, VC, SA
    public float[] drawDistance; //III, VC, SA, IV
    public int flag1; //III, VC, SA, IV
    public int flag2; //IV
    public Vector3D boundsMin = new Vector3D(0.0f, 0.0f, 0.0f); //IV
    public Vector3D boundsMax = new Vector3D(0.0f, 0.0f, 0.0f); //IV
    public Vector4D boundsSphere = new Vector4D(0.0f, 0.0f, 0.0f, 0.0f); //IV
    public String WDD; //IV
    public int timedFlags; //?

    public Item_TOBJ(int gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        line = line.replace(" ", "");
        String split[] = line.split(",");
        modelName = split[0];
        textureName = split[1];
        drawDistance = new float[1];
        drawDistance[0] = Float.valueOf(split[2]);
        flag1 = Integer.valueOf(split[3]);
        flag2 = Integer.valueOf(split[4]);
        boundsMin = new Vector3D(Float.valueOf(split[5]), Float.valueOf(split[6]), Float.valueOf(split[7]));
        boundsMax = new Vector3D(Float.valueOf(split[8]), Float.valueOf(split[9]), Float.valueOf(split[10]));
        boundsSphere = new Vector4D(Float.valueOf(split[11]), Float.valueOf(split[12]), Float.valueOf(split[13]),Float.valueOf(split[14]));
        WDD = split[15];
        timedFlags = Integer.valueOf(split[16]);
    }

    public void save(BufferedWriter output){
        try {
            String line = modelName;
            line += ", " + textureName;
            line += ", " + drawDistance[0];
            line += ", " + flag1;
            line += ", " + flag2;
            line += ", " + boundsMin.x;
            line += ", " + boundsMin.y;
            line += ", " + boundsMin.z;
            line += ", " + boundsMax.x;
            line += ", " + boundsMax.y;
            line += ", " + boundsMax.z;
            line += ", " + boundsSphere.x;
            line += ", " + boundsSphere.y;
            line += ", " + boundsSphere.z;
            line += ", " + boundsSphere.w;
            line += ", " + WDD;
            line += ", " + timedFlags;
            output.write(line + "\n");
            System.out.println("Line: " + line);
        } catch (IOException ex) {
            Logger.getLogger(Item_OBJS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
