/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IPL;

import com.nikhaldimann.inieditor.IniEditor;
import file_io.Message;
import file_io.ReadFunctions;
import file_io.Vector3D;
import file_io.Vector4D;
import file_io.WriteFunctions;
import shadowmapper.Finals;
import shadowmapper.Util;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kilian
 */
public class Item_INST extends IPL_Item{
    public int id; //III, VC, SA
    public String name = ""; //III, VC, SA, IV(Hash)
    public int hash = 0;
    public int interior; //VC, SA
    public Vector3D position = new Vector3D(0.0f, 0.0f, 0.0f); //III, VC, SA, IV
    public Vector3D scale = new Vector3D(0.0f, 0.0f, 0.0f); //III, VC
    public Vector4D rotation = new Vector4D(0.0f, 0.0f, 0.0f, 1.0f); //III, VC, SA, IV

    public int lod; //SA, IV
    public int unknown1, unknown2; //IV
    public float unknown3; //IV
    public float drawDistance = 300.0f; //default value

    public Vector4D axisAngle = new Vector4D(0.0f, 0.0f, 0.0f, 0.0f);
    private boolean hidden = false;
    public boolean selected = false;
    public int glListID;
    
    private int gameType = Finals.gIV;

    public Item_INST(int gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        line = line.replace(" ", "");
        String[] split = line.split(",");
        switch(gameType){
            case Finals.gSA:
                id = Integer.valueOf(split[0]);
                name = split[1];
                interior = Integer.valueOf(split[2]);
                position = new Vector3D(Float.valueOf(split[3]), Float.valueOf(split[4]), Float.valueOf(split[5]));
                rotation = new Vector4D(Float.valueOf(split[6]), Float.valueOf(split[7]), Float.valueOf(split[8]), Float.valueOf(split[9]));
                lod = Integer.valueOf(split[10]);
                break;
            case Finals.gVC:
                id = Integer.valueOf(split[0]);
                name = split[1];
                interior = Integer.valueOf(split[2]);
                position = new Vector3D(Float.valueOf(split[3]), Float.valueOf(split[4]), Float.valueOf(split[5]));
                scale = new Vector3D(Float.valueOf(split[6]), Float.valueOf(split[7]), Float.valueOf(split[8]));
                rotation = new Vector4D(Float.valueOf(split[9]), Float.valueOf(split[10]), Float.valueOf(split[11]), Float.valueOf(split[12]));
                break;
            case Finals.gIII:
                id = Integer.valueOf(split[0]);
                name = split[1];
                position = new Vector3D(Float.valueOf(split[2]), Float.valueOf(split[3]), Float.valueOf(split[4]));
                scale = new Vector3D(Float.valueOf(split[5]), Float.valueOf(split[6]), Float.valueOf(split[7]));
                rotation = new Vector4D(Float.valueOf(split[8]), Float.valueOf(split[9]), Float.valueOf(split[10]), Float.valueOf(split[11]));
                break;
        }
        axisAngle = rotation.getAxisAngle();
        this.display();
    }

    @Override
    public void read(ReadFunctions rf, IniEditor ini) {
        position = new Vector3D(rf.readFloat(), rf.readFloat(), rf.readFloat());
        rotation = new Vector4D(rf.readFloat(), rf.readFloat(), rf.readFloat(), rf.readFloat());

        long tempHash = rf.readUnsignedInt();
        name = "" + tempHash;
        hash = (int) tempHash;
        name = ini.get("Hashes", name); //temp
        if(name == null){
            name = "";
            System.out.println("ERROR Hash bestaat niet");
        }

        Message.displayMsgLow("iName: " + name);

        unknown1 = rf.readInt();
        System.out.println("Unknown1: " + unknown1);
        lod = rf.readInt();
        unknown2 = rf.readInt();
        System.out.println("Unknown2: " + unknown2);
        unknown3 = rf.readFloat();
        System.out.println("Unknown3: " + unknown3);
        //this.display();
    }

    public void display(){
        if(gameType != Finals.gIV) Message.displayMsgHigh("ID: " + id);
        System.out.println("Name: " + name);
        if(gameType == Finals.gVC || gameType == Finals.gSA) Message.displayMsgHigh("Interior: " + interior);
        System.out.println("Position: " + position.x + ", " + position.y + ", " + position.z);
        if(gameType == Finals.gIII || gameType == Finals.gVC) Message.displayMsgHigh("Scale: " + scale.x + ", " + scale.y + ", " + scale.z);
        System.out.println("Rotation: " + rotation.x + ", " + rotation.y + ", " + rotation.z + ", " + rotation.w);
        System.out.println("Lod: " + lod);
    }

    @Override
    public void read(ReadFunctions rf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void write(WriteFunctions wf){
        wf.writeVector3D(position);
        wf.writeVector4D(rotation);
        if(hash == 0){
            System.out.println("We are generating a new hash");
            System.out.println(Util.genHash(name.toLowerCase()) + " from " + name);
            long tempHash = Util.genHash(name.toLowerCase());
            hash = (int)tempHash;
            IniEditor ini = new IniEditor();
            try {
                ini.load("hashes.ini");
            } catch (IOException ex) {
                Logger.getLogger(Item_INST.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(!ini.hasOption("Hashes", "" + tempHash)){
                ini.set("Hashes", "" + tempHash, name);
                try {
                    ini.save("hashes.ini");
                } catch (IOException ex) {
                    Logger.getLogger(Item_INST.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        wf.writeInt(hash);

        wf.writeInt(unknown1);
        wf.writeInt(lod);
        wf.writeInt(unknown2);
        wf.writeFloat(unknown3);
        this.display();
    }

}
