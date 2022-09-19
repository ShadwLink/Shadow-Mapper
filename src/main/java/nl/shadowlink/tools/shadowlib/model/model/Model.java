package nl.shadowlink.tools.shadowlib.model.model;

import com.jogamp.opengl.GL2;
import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.io.Vector4D;
import nl.shadowlink.tools.io.WriteFunctions;
import nl.shadowlink.tools.shadowlib.model.dff.LoadDFF;
import nl.shadowlink.tools.shadowlib.model.dff.NewConvert;
import nl.shadowlink.tools.shadowlib.model.wdr.*;
import nl.shadowlink.tools.shadowlib.model.wft.FragTypeModel;
import nl.shadowlink.tools.shadowlib.texturedic.TextureDic;
import nl.shadowlink.tools.shadowlib.utils.GameType;
import nl.shadowlink.tools.shadowlib.utils.hashing.OneAtATimeHasher;

import java.util.ArrayList;

/**
 * This class contains all the information for one model
 *
 * @author Shadow-Link
 */
public class Model {
    private ArrayList<Element> elements = new ArrayList<Element>();
    public Vector4D center;
    public Vector4D boundsMin;
    public Vector4D boundsMax;

    private boolean loaded = false;
    public int flags;
    public int size;

    private int sysSize = 0;

    public Model() {
    }

    public void attachTXD(TextureDic txd) {
        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).attachTXD(txd);
        }
    }

    /**
     * Load a WDD file
     *
     * @param img      Bytereader that contains the wdd file, current offset should be at the start of the wdd
     * @param fileSize Size of the file to be loaded
     * @param WDD      Current WDR that needs to be loaded from the WDD
     * @return model
     */
    public Model loadWDD(ByteReader img, int fileSize, String WDD) {
        this.reset();

        byte stream[];

        ResourceFile res = new ResourceFile();
        stream = res.read(img, fileSize);

        int sysSize = res.getSystemSize();

        ByteReader br = new ByteReader(stream, 0);

        long vtable = br.readUInt32();
        long blockMapAddress = br.readOffset();

        long unknown0 = br.readUInt32();
        long unknown1 = br.readUInt32();
        //Message.displayMsgLow("0: " + unknown0);
        // Message.displayMsgLow("1: " + unknown1);

        int hashOffset = br.readOffset();
        int hashCount = br.readUInt16();
        // Message.displayMsgLow("Hashes Offset: "
        // + Utils.getHexString(hashOffset));
        // Message.displayMsgLow("Count: " + hashCount);
        int size = br.readUInt16();

        int save = br.getCurrentOffset();

        br.setCurrentOffset(hashOffset);

        long wddHash = OneAtATimeHasher.getHashKey(WDD);
        long[] wdrHashes = new long[hashCount];

        for (int i = 0; i < hashCount; i++) {
            wdrHashes[i] = br.unsignedInt();
        }

        br.setCurrentOffset(save);

        int drawPOffset = br.readOffset();
        // //Message.displayMsgLow("Drawable Offset: " +
        // Utils.getHexString(drawPOffset));
        int drawPCount = br.readUInt16();
        int drawPSize = br.readUInt16();

        br.setCurrentOffset(drawPOffset);

        int[] wdrOffsets = new int[hashCount];

        for (int i = 0; i < hashCount; i++) {
            wdrOffsets[i] = br.readOffset();
        }

        int j = 0;
        while (wdrHashes[j] != wddHash) {
            j++;
        }
        // Message.displayMsgLow("WDD: " + wdrNames[j] + " "
        // + Utils.getHexString(wdrOffsets[j]));
        br.setCurrentOffset(wdrOffsets[j]);

        DrawableModel sys = new DrawableModel();
        sys.readSystemMemory(br);

        Element element = new Element();
        createModelFromDrawable(element, sys, sysSize, stream, br);
        elements.add(element);

        loaded = true;

        return this;
    }

    /**
     * Creates a model from WDR
     *
     * @param element Element to store this WDR in
     * @param sys     The drawable
     * @param sysSize Size of the system memory
     * @param stream  Stream
     * @param br      ByteReader
     */
    private void createModelFromDrawable(Element element, DrawableModel sys, int sysSize, byte[] stream, ByteReader br) {
        ModelFile mf = new ModelFile();

        if (sys.shaderGroupOffset != -1) {
            for (int i = 0; i < sys.shaderGroup.Shaders.Count; i++) { // create
                // shaders
                for (int shaderNames = 0; shaderNames < sys.shaderGroup.Shaders._items.get(i).shader.ShaderParamNames.Count; shaderNames++) {
                    if (726757629 == sys.shaderGroup.Shaders._items.get(i).shader.ShaderParamNames.Values.get(shaderNames)) {
                        if (sys.shaderGroup.Shaders._items.get(i).shader.ShaderParamOffsets.Values.get(shaderNames) != -1) {
                            ShaderParamTexture shader = (ShaderParamTexture) sys.shaderGroup.Shaders._items.get(i).shader.ShaderParams
                                    .get(shaderNames);
                            element.createShader(shader.TextureName);
                            // //Message.displayMsgLow("Found diffusemap: "
                            // + shader.TextureName);
                        }
                    }
                }
            }
        }

        if (sys.mModelCollection.length != 0) {
            Model2 test = sys.mModelCollection[0]._items.get(0);// load all
            // geometries
            // into one
            // model
            for (int i = 0; i < test.Geometries._items.size(); i++) {
                Geometry geo = test.Geometries._items.get(i);
                element.createStrip(geo.FaceCount, i);
                // //Message.displayMsgLow("Adding vertices");

                mf.addVertices(element, stream, geo.vertexBuffer.DataOffset + sysSize, geo.indexBuffer.DataOffset + sysSize, geo.VertexCount,
                        geo.FaceCount, geo.VertexStride, i);
                // //Message.displayMsgLow("Adding polygons");
                mf.addPolygons(element, stream, geo.vertexBuffer.DataOffset + sysSize, geo.indexBuffer.DataOffset + sysSize, geo.VertexCount,
                        geo.FaceCount, geo.VertexStride, i);
            }
        }

        if (sys.shaderGroup != null) {
            if (sys.shaderGroup.TextureDictionaryOffset != 0) {
                br.setCurrentOffset(sys.shaderGroup.TextureDictionaryOffset);
                TextureDic wtd = new TextureDic("embed", br, GameType.GTA_IV, false, sysSize);
                element.attachTXD(wtd);
            }
        }
    }

    /**
     * Loads a WDR file
     *
     * @param br       ByteReader that contains the WDR file, current offset should be set to start of WDR
     * @param fileSize Size of the file
     * @return this
     */
    public Model loadWDR(ByteReader br, int fileSize) {
        this.reset();

        byte stream[];

        DrawableModel sys = new DrawableModel();

        ResourceFile res = new ResourceFile();
        stream = res.read(br, fileSize);

        sysSize = res.getSystemSize();

        ByteReader br2 = new ByteReader(stream, 0);
        sys.readSystemMemory(br2);
        center = sys.Center;
        boundsMin = sys.BoundsMin;
        boundsMax = sys.BoundsMax;

        Element element = new Element();
        createModelFromDrawable(element, sys, sysSize, stream, br2);
        elements.add(element);
        element = null;

        loaded = true;

        return this;
    }

    public DrawableModel loadWDRSystem(ByteReader br, int fileSize) {
        this.reset();

        byte stream[];

        DrawableModel sys = new DrawableModel();

        ResourceFile res = new ResourceFile();
        stream = res.read(br, fileSize);

        ByteReader br2 = new ByteReader(stream, 0);
        sys.readSystemMemory(br2);
        center = sys.Center;
        boundsMin = sys.BoundsMin;
        boundsMax = sys.BoundsMax;

        return sys;
    }

    /**
     * Loads a WFT file
     *
     * @param br       ByteReader that contains the WFT file, current offset should be set to start of WFT
     * @param fileSize Size of the file
     * @return this
     */
    public Model loadWFT(ByteReader br, int fileSize) {
        this.reset();

        byte stream[];

        ResourceFile res = new ResourceFile();
        stream = res.read(br, fileSize);

        sysSize = res.getSystemSize();

        ByteReader br2 = new ByteReader(stream, 0);

        FragTypeModel wft = new FragTypeModel();
        wft.read(br2);

        createModelFromFragType(wft, sysSize, stream, br2);

        loaded = true;

        return this;
    }

    /**
     * Created drawables from WFT
     *
     * @param wft     the wft
     * @param sysSize System size
     * @param stream  Stream
     * @param br      Bytereader
     */
    private void createModelFromFragType(FragTypeModel wft, int sysSize, byte[] stream, ByteReader br) {
        for (int g = 0; g < wft.drawables.size(); g++) {
            Element element = new Element();
            // System.out.println("Loading " + g);
            DrawableModel sys = wft.drawables.get(g);

            createModelFromDrawable(element, sys, sysSize, stream, br);
            elements.add(element);
            element = null;
        }
    }

    /**
     * Load a WDD file from File
     *
     * @param file
     *        String to the file
     * @param gl
     *        used to load textures
     * @param wdrName
     *        Name of the WDR
     * @return this
     */
    /* public Model loadWDD(String file, GL gl, String wdrName){ this.reset(); ReadFunctions rf = new ReadFunctions();
     * rf.openFile(file); ByteReader br = rf.getByteReader(); loadWDD(br, -1, gl, wdrName); loaded = true; return this;
     * } */

    /**
     * Load WDR file from file
     *
     * @param file String to file to load
     * @return this
     */
    public Model loadWDR(String file) {
        this.reset();
        ReadFunctions rf = new ReadFunctions(file);
        ByteReader br = rf.getByteReader(rf.moreToRead());
        loadWDR(br, -1);
        loaded = true;
        return this;
    }

    /**
     * Load WFT file from file
     *
     * @param file
     *        String to file
     * @param gl
     *        used for embedded textures
     * @return this
     */
    /* public Model loadWFT(String file, GL gl){ this.reset(); ReadFunctions rf = new ReadFunctions();
     * rf.openFile(file); ByteReader br = rf.getByteReader(); loadWFT(br, -1, gl); loaded = true; return this; } */

    /**
     * Load DFF from file
     *
     * @param file String to file
     * @return this
     */
    public Model loadDFF(String file) {
        ReadFunctions rf = new ReadFunctions(file);
        ByteReader br = rf.getByteReader();
        loadDFF(br);
        loaded = true;
        return this;
    }

    /**
     * Load DFF from byteReader
     *
     * @param br ByteReader that contains the dff, offset should be set to start of dff
     * @return this
     */
    public Model loadDFF(ByteReader br) {
        this.reset();
        Element element = new Element();
        new LoadDFF(br, element);
        elements.add(element);
        loaded = true;
        return this;
    }

    /**
     * Checks if the model is loaded
     *
     * @return true if this model is loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Set true if this model finished loading
     *
     * @param loaded true if this model finished loading
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * Render this model
     *
     * @param gl used to render this model
     */
    public void render(GL2 gl) {
        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).render(gl);
        }
    }

    /**
     * Reset this model
     */
    public void reset() {
        elements.clear();
        loaded = false;
    }

    public Element getElement(int id) {
        if (elements.size() > id) {
            return elements.get(id);
        } else {
            return null;
        }
    }

    public void convertToWDR(WriteFunctions wf) {
        new NewConvert(this, wf);
    }

    public void createElement() {
        elements.add(new Element());
    }

}
