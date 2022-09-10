package nl.shadowlink.tools.shadowmapper.render;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.io.Vector3D;
import nl.shadowlink.tools.shadowlib.ide.Item_OBJS;
import nl.shadowlink.tools.shadowlib.img.IMG_Item;
import nl.shadowlink.tools.shadowlib.ipl.Item_INST;
import nl.shadowlink.tools.shadowlib.model.model.Model;
import nl.shadowlink.tools.shadowlib.texturedic.TextureDic;
import nl.shadowlink.tools.shadowlib.utils.GameType;
import nl.shadowlink.tools.shadowmapper.gui.FileManager;
import nl.shadowlink.tools.shadowmapper.gui.Finals;

import java.util.ArrayList;

/**
 * @author Shadow-Link
 */
public class RenderMap {
    private int glDisplayList[] = new int[0];

    private Camera camera;

    public FileManager fm;

    public boolean pick = false;
    public boolean reload = false;
    public boolean loading = false;
    public boolean added = false;

    private final GameType gameType;

    public Item_OBJS tempIDE = null;
    public Item_INST tempIPL = null;

    public RenderMap(GameType gameType) {
        this.gameType = gameType;
    }

    private void loadMap(GL2 gl) {

        for (int i = 0; i < glDisplayList.length; i++) {
            gl.glDeleteLists(glDisplayList[i], 1);
        }

        // TestLoadBar lb = new TestLoadBar();
        // TestLoadBar lb = new TestLoadBar();
        // Thread worker = new Thread(lb);
        // worker.start();

        // lb.setLabelText("blah1");

        glDisplayList = null;
        ArrayList<Boolean> boolList = new ArrayList();
        ArrayList<Item_OBJS> ideList = new ArrayList();
        for (int iplCount = 0; iplCount < fm.ipls.length; iplCount++) { // door
            // alle
            // IPL's
            if (fm.ipls[iplCount].selected) { // kijk of het een geselecteerde
                // ipl is
                ArrayList<Item_INST> items_inst = fm.ipls[iplCount].items_inst; // alle
                // instances
                // van
                // de
                // ipl
                for (int iplItem = 0; iplItem < items_inst.size(); iplItem++) { //
                    int ideNumber = 0;
                    Item_OBJS ideItem = (Item_OBJS) fm.ides[ideNumber].findItem(items_inst.get(iplItem).name);
                    while (ideItem == null) {
                        ideNumber++;
                        if (ideNumber < fm.ides.length) {
                            ideItem = (Item_OBJS) fm.ides[ideNumber].findItem(items_inst.get(iplItem).name);
                        } else {
                            System.out.println("I really can't find in IDE: " + items_inst.get(iplItem).name);
                            break;
                        }
                    }
                    if (ideItem != null) {
                        boolean found = false;
                        for (int i = 0; i < ideList.size(); i++) {
                            if (ideList.get(i).equals(ideItem)) {
                                found = true;
                                items_inst.get(iplItem).glListID = i + 1;
                                items_inst.get(iplItem).drawDistance = ideItem.drawDistance[0];
                            }
                        }
                        if (!found) {
                            items_inst.get(iplItem).glListID = ideList.size() + 1;
                            items_inst.get(iplItem).drawDistance = ideItem.drawDistance[0];
                            ideList.add(ideItem);
                            boolList.add(false);
                        }
                    } else {
                        items_inst.get(iplItem).glListID = 0;
                    }
                }
                fm.ipls[iplCount].itemsLoaded = true;
            }
        }
        glDisplayList = new int[ideList.size() + 1];
        System.out.println("ideList Size: " + ideList.size());

        for (int imgNumber = 0; imgNumber < fm.imgs.length; imgNumber++) {
            ReadFunctions rf = new ReadFunctions(fm.imgs[imgNumber].getFileName()); // open the img file
            System.out.println("Opened: " + fm.imgs[imgNumber].getFileName());
            for (int i = 0; i < ideList.size(); i++) {
                if (!boolList.get(i)) {
                    String modelName = "";
                    IMG_Item item = null;
                    if (!ideList.get(i).WDD.equals("null")) {
                        modelName = ideList.get(i).WDD + ".wdd";
                        item = fm.imgs[imgNumber].findItem(modelName);
                    } else {
                        modelName = ideList.get(i).modelName + ".wdr";
                        item = fm.imgs[imgNumber].findItem(modelName);
                        if (item == null)
                            item = fm.imgs[imgNumber].findItem(ideList.get(i).modelName + ".wft");
                    }
                    if (item != null) {
                        rf.seek(item.getOffset());
                        ByteReader br = rf.getByteReader(item.getSize());
                        Model mdl = null;
                        if (item.getName().endsWith(".wdr")) {
                            System.out.println(item.getName());
                            mdl = new Model().loadWDR(br, item.getSize()); // load
                            // the
                            // model
                            // from
                            // img
                        } else if (item.getName().endsWith(".wdd")) {
                            mdl = new Model().loadWDD(br, item.getSize(), ideList.get(i).modelName);
                        } else if (item.getName().endsWith(".wft")) {
                            System.out.println("Loading WFT: " + item.getName());
                            mdl = new Model().loadWFT(br, item.getSize());
                        }
                        br = null;
                        String texName = ideList.get(i).textureName + ".wtd";
                        item = fm.imgs[imgNumber].findItem(texName);
                        if (item != null) {
                            rf.seek(item.getOffset());
                            br = rf.getByteReader(item.getSize());
                            TextureDic txd = new TextureDic(texName, br, gameType, item.getSize());

                            if (mdl != null) {
                                mdl.attachTXD(txd.texName, txd.textureId);
                            }
                        }
                        glDisplayList[i + 1] = gl.glGenLists(1);
                        gl.glNewList(glDisplayList[i + 1], GL2.GL_COMPILE);
                        if (mdl != null)
                            mdl.render(gl);
                        else
                            drawCube(gl, 10, 0.1f, 0.5f, 0.9f);
                        gl.glEndList();
                        mdl.reset();
                        mdl = null;
                        boolList.set(i, true);
                    }
                }
                rf.closeFile();
            }
        }
        boolList = null;
        ideList = null;
    }

    private void loadAddedModel(GL2 gl) {
        int tempList[] = glDisplayList; // store all glInts to a temp array
        glDisplayList = new int[tempList.length + 1]; // make the displaylist
        // one bigger

        for (int i = 0; i < tempList.length; i++) {
            glDisplayList[i] = tempList[i];
        }

        IMG_Item item = null;
        int imgID = -1;
        int i = 0;
        while (item == null || i < fm.imgs.length) {
            if (!tempIDE.WDD.equals("null"))
                item = fm.imgs[i].findItem(tempIDE.WDD + ".wdd");
            else {
                item = fm.imgs[i].findItem(tempIDE.modelName + ".wdr");
                if (item == null)
                    item = fm.imgs[i].findItem(tempIDE.modelName + ".wft");
            }
            if (item != null)
                imgID = i;
            i++;
        }

        if (item != null) {
            ReadFunctions rf = new ReadFunctions(fm.imgs[imgID].getFileName());
            rf.seek(item.getOffset());
            ByteReader br = rf.getByteReader(item.getSize());
            Model mdl = null;
            if (item.getName().endsWith(".wdr")) {
                System.out.println(item.getName());
                mdl = new Model().loadWDR(br, item.getSize()); // load the
                // model
                // from img
            } else if (item.getName().endsWith(".wdd")) {
                mdl = new Model().loadWDD(br, item.getSize(), tempIDE.modelName);
            } else if (item.getName().endsWith(".wft")) {
                System.out.println("Loading WFT: " + item.getName());
                mdl = new Model().loadWFT(br, item.getSize());
            }
            br = null;
            item = fm.imgs[imgID].findItem(tempIDE.textureName + ".wtd");
            if (item != null) {
                rf.seek(item.getOffset());
                br = rf.getByteReader(item.getSize());
                TextureDic txd = new TextureDic(tempIDE.textureName + ".wtd", br, gameType, item.getSize());

                if (mdl != null) {
                    mdl.attachTXD(txd.texName, txd.textureId);
                }
            }

            glDisplayList[tempList.length] = gl.glGenLists(1);
            gl.glNewList(glDisplayList[tempList.length], GL2.GL_COMPILE);
            if (mdl != null)
                mdl.render(gl);
            else
                drawCube(gl, 10, 0.1f, 0.5f, 0.9f);
            gl.glEndList();
            mdl.reset();
            rf.closeFile();

            tempIPL.glListID = tempList.length;
        } else {
            tempIPL.glListID = 0;
        }

        tempIDE = null;
        tempIPL = null;
    }

    public void init(GL gl, Camera camera, FileManager fm) {
        this.fm = fm;
        this.camera = camera;
    }

    public void display(GL2 gl) {
        if (!loading && fm != null) {
            if (reload) {
                loading = true;
                System.out.println("Started loading");
                loadMap(gl);
                reload = false;
                loading = false;
                System.out.println("Loading finished");
            }
            if (added) {
                loading = true;
                System.out.println("Loading added model");
                loadAddedModel(gl);
                loading = false;
                added = false;
                System.out.println("Loading added model finished");
            }
            gl.glPushName(Finals.pickMap);
            for (int j = 0; j < fm.ipls.length; j++) {
                if (fm.ipls[j].selected && fm.ipls[j].itemsLoaded) {
                    gl.glPushName(j);
                    for (int i = 0; i < fm.ipls[j].items_inst.size(); i++) {// instances
                        // rendering
                        Item_INST item = fm.ipls[j].items_inst.get(i);
                        if (!item.name.toLowerCase().contains("lod")) {
                            int drawID = i;
                            /*
                             * while(getDistance(camera.pos, item.position) >
                             * item.drawDistance){ if(item.lod == -1){ drawID =
                             * -1; break; }else{ drawID = item.lod; item =
                             * fm.ipls
                             * [fm.ipls[j].lodWPL].items_inst.get(item.lod); } }
                             */
                            if (getDistance(camera.pos, item.position) < item.drawDistance) {
                                if (drawID != -1) {
                                    gl.glPushName(drawID);
                                    if (item.selected) {
                                        gl.glColor3f(0.9f, 0, 0);
                                    } else {
                                        gl.glColor3f(1, 1, 1);
                                    }

                                    gl.glPushMatrix();

                                    gl.glTranslatef(item.position.x, item.position.y, item.position.z);
                                    gl.glRotatef(item.axisAngle.w, item.axisAngle.x, item.axisAngle.y, item.axisAngle.z);

                                    gl.glCallList(glDisplayList[item.glListID]);

                                    gl.glPopMatrix();
                                    gl.glPopName();
                                }
                            }
                        }
                    }
                    gl.glPopName();
                }
            }
            gl.glPopName();
        }
        gl.glFlush();
    }

    public float getDistance(Vector3D test1, Vector3D test2) {
        float dx = test1.x - test2.x;
        float dy = (0 - test1.z) - test2.y;
        float dz = test1.y - test2.z;
        float distance = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
        return distance;
    }

    public void drawCube(GL2 gl, float size, float red, float green, float blue) {
        gl.glColor3f(red, green, blue); // Set The Color To Green
        gl.glBegin(gl.GL_QUADS); // Start Drawing The Cube

        gl.glVertex3f(size, size, -size); // Top Right Of The Quad (Top)
        gl.glVertex3f(-size, size, -size); // Top Left Of The Quad (Top)
        gl.glVertex3f(-size, size, size); // Bottom Left Of The Quad (Top)
        gl.glVertex3f(size, size, size); // Bottom Right Of The Quad (Top)

        gl.glVertex3f(size, -size, size); // Top Right Of The Quad (Bottom)
        gl.glVertex3f(-size, -size, size); // Top Left Of The Quad (Bottom)
        gl.glVertex3f(-size, -size, -size); // Bottom Left Of The Quad (Bottom)
        gl.glVertex3f(size, -size, -size); // Bottom Right Of The Quad (Bottom)

        gl.glVertex3f(size, size, size); // Top Right Of The Quad (Front)
        gl.glVertex3f(-size, size, size); // Top Left Of The Quad (Front)
        gl.glVertex3f(-size, -size, size); // Bottom Left Of The Quad (Front)
        gl.glVertex3f(size, -size, size); // Bottom Right Of The Quad (Front)

        gl.glVertex3f(size, -size, -size); // Bottom Left Of The Quad (Back)
        gl.glVertex3f(-size, -size, -size); // Bottom Right Of The Quad (Back)
        gl.glVertex3f(-size, size, -size); // Top Right Of The Quad (Back)
        gl.glVertex3f(size, size, -size); // Top Left Of The Quad (Back)

        gl.glVertex3f(-size, size, size); // Top Right Of The Quad (Left)
        gl.glVertex3f(-size, size, -size); // Top Left Of The Quad (Left)
        gl.glVertex3f(-size, -size, -size); // Bottom Left Of The Quad (Left)
        gl.glVertex3f(-size, -size, size); // Bottom Right Of The Quad (Left)

        gl.glVertex3f(size, size, -size); // Top Right Of The Quad (Right)
        gl.glVertex3f(size, size, size); // Top Left Of The Quad (Right)
        gl.glVertex3f(size, -size, size); // Bottom Left Of The Quad (Right)
        gl.glVertex3f(size, -size, -size); // Bottom Right Of The Quad (Right)

        gl.glEnd();

        gl.glColor3f(1.0f, 1.0f, 1.0f);
    }

    public void addLoadModelToLoad(Item_INST tempIPL, Item_OBJS tempIDE) {
        this.tempIPL = tempIPL;
        this.tempIDE = tempIDE;
        added = true;
    }

}
