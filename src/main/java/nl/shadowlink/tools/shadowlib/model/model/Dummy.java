package nl.shadowlink.tools.shadowlib.model.model;

import com.nikhaldimann.inieditor.IniEditor;
import nl.shadowlink.tools.io.Vector3D;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shadow-Link
 */
public class Dummy {
    private String name;

    private int index;
    private int parent;
    private int child;

    private Vector3D rotation1;
    private Vector3D rotation2;
    private Vector3D rotation3;

    private Vector3D translation;

    private int flags;
    private int type = 0;

    public Dummy(String name) {
        this.name = name;
    }

    public Dummy() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public Vector3D getRotation1() {
        return rotation1;
    }

    public void setRotation1(Vector3D rotation1) {
        this.rotation1 = rotation1;
    }

    public Vector3D getRotation2() {
        return rotation2;
    }

    public void setRotation2(Vector3D rotation2) {
        this.rotation2 = rotation2;
    }

    public Vector3D getRotation3() {
        return rotation3;
    }

    public void setRotation3(Vector3D rotation3) {
        this.rotation3 = rotation3;
    }

    public Vector3D getTranslation() {
        return translation;
    }

    public void setTranslation(Vector3D translation) {
        this.translation = translation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypeByName() {
        try {
            IniEditor ini = new IniEditor();
            ini.load("dummys.ini");
            if (ini.hasOption("dummys", name)) {
                System.out.println("DummyName Exists");
                this.type = Integer.valueOf(ini.get("dummys", name));
            } else {
                System.out.println("Dummyname doesn't exist");
            }
        } catch (IOException ex) {
            Logger.getLogger(Dummy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
