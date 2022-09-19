package nl.shadowlink.tools.shadowlib.model.model;

import com.jogamp.opengl.GL2;

import java.util.ArrayList;

/**
 * @author Shadow-Link
 */
public class Element {

    private ArrayList<Strip> strips = new ArrayList();
    private ArrayList<Vertex> vert = new ArrayList();
    private ArrayList<Shader> shaders = new ArrayList();

    private Vertex max = new Vertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Vertex min = new Vertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Vertex center = new Vertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

    private boolean hasVertexColors = false;
    private boolean hasNormals = false;

    private int polygons;
    private int vertices;

    private boolean visible = true;

    /**
     * Get the shader from the list of shaders
     *
     * @param id of the shader you want
     * @return the requested shader
     */
    public Shader getShader(int i) {
        return shaders.get(i);
    }

    /**
     * Created a new empty element
     */
    public Element() {
    }

    /**
     * Attaches a TXD to this element
     */
    public void attachTXD(TextureDic txd) {
        // TODO: Why can this be null?
        if (txd == null) {
            return;
        }

//        for (int i = 0; i < shaders.size(); i++) {
//            // //Message.displayMsgLow("Started Shader " + i + " name is " + shaders.get(i).getTextureName());
//            for (int j = 0; j < texNames.length; j++) {
//                // //Message.displayMsgLow("texName" + j + ": " + texNames[j]);
//                if (texNames[j] != null) {
//                    if (shaders.get(i).getTextureName().equalsIgnoreCase(texNames[j])) {
//                        System.out.println("Connected tex: " + shaders.get(i).getTextureName() + " with " + texNames[j]);
//                        // //Message.displayMsgLow("Found " + j);
//                        shaders.get(i).setGLTex(texID[j]);
//                    }
//                }
//            }
//        }
    }

    /**
     * Checks if this element contains normals
     *
     * @return true if it has normals
     */
    public boolean hasNormals() {
        return hasNormals;
    }

    /**
     * Sets if this element contains normals
     *
     * @param hasNormals true if it has normals
     */
    public void hasNormals(boolean hasNormals) {
        this.hasNormals = hasNormals;
    }

    /**
     * Create a new empty shader
     */
    public void createShader() {
        shaders.add(new Shader());
    }

    /**
     * Create a new shader with a texturename
     *
     * @param textureName the name of the texture
     */
    public void createShader(String textureName) {
        Shader temp = new Shader();
        temp.setTextureName(textureName);
        shaders.add(temp);
    }

    /**
     * Returns the amount of shaders in this element
     *
     * @return shader count
     */
    public int getShaderCount() {
        return shaders.size();
    }

    /**
     * Checks if this element should be rendered or not
     *
     * @return true if it should be rendered
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Set if this element should be rendered or not
     *
     * @param visible true if it should be rendered
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * returns the number of trianglestrips used by this element
     *
     * @return Stripcount
     */
    public int getStripCount() {
        return strips.size();
    }

    /**
     * Reset all values of this element: Vertices Strips Shaders hasVertexColors polygon Count Vertex Count Visible
     */
    public void reset() {
        vert.clear();
        strips.clear();
        shaders.clear();
        hasVertexColors = false;
        polygons = 0;
        vertices = 0;
        visible = false;
    }

    /**
     * Create a new strip with an given amount of polys and an material index
     *
     * @param polyCount     the number of polygons this strip will contain
     * @param materialIndex id of the material used by this strip
     */
    public void createStrip(int polyCount, int materialIndex) {
        strips.add(new Strip(polyCount, materialIndex));
    }

    /**
     * Returns a strip from the strip array
     *
     * @param id id of the strip
     * @return The strip
     */
    public Strip getStrip(int id) {
        return strips.get(id);
    }

    /**
     * Create a new polygon for this model
     *
     * @param a Vertex a
     * @param b Vertex b
     * @param c Vertex c
     */
    public void createModelPoly(int a, int b, int c, int stripIndex, boolean check) {
        // //Message.displayMsgLow("Original poly: " + a + ", " + b + ", " + c);
        if (check) {
            a = strips.get(stripIndex).addVertex(vert.get(a));
            b = strips.get(stripIndex).addVertex(vert.get(b));
            c = strips.get(stripIndex).addVertex(vert.get(c));
        }
        // //Message.displayMsgLow("Created poly: " + a + ", " + b + ", " + c);
        strips.get(stripIndex).addPoly(new Polygon(a, b, c));
    }

    public void addVertexToStrip(float x, float y, float z, float u, float v, int stripIndex) {
        strips.get(stripIndex).addVertexToStrip(new Vertex(x, y, z, u, v));
    }

    /**
     * Create a new vertex for this model
     *
     * @param x X Position of the vertex
     * @param y Y Position of the vertex
     * @param z Z Position of the vertex
     */
    public void createModelVertex(int id, float x, float y, float z, float u, float v) {
        if (vert.size() <= id) {
            vert.add(new Vertex(x, y, z, u, v));
        } else {
            if (u == -1) {
                u = vert.get(id).getVertexU();
                v = vert.get(id).getVertexV();
            }
            vert.set(id, new Vertex(x, y, z, u, v));
        }
        checkBounds(new Vertex(x, y, z, u, v));
    }

    /**
     * Check if the vertex is out of the current bounds if so it will set the bounds of this element to the current
     * vertex
     *
     * @param vertex Vertex to check this with
     */
    public void checkBounds(Vertex vertex) {
        if (vertex.getVertexX() > max.getVertexX())
            max.setVertexX(vertex.getVertexX());
        if (vertex.getVertexY() > max.getVertexY())
            max.setVertexY(vertex.getVertexY());
        if (vertex.getVertexZ() > max.getVertexZ())
            max.setVertexZ(vertex.getVertexZ());
        if (vertex.getVertexX() < min.getVertexX())
            min.setVertexX(vertex.getVertexX());
        if (vertex.getVertexY() < min.getVertexY())
            min.setVertexY(vertex.getVertexY());
        if (vertex.getVertexZ() < min.getVertexZ())
            min.setVertexZ(vertex.getVertexZ());
    }

    /**
     * Calculate the center of this element
     */
    public void createCenter() {
        center.setVertexX((max.getVertexX() + min.getVertexX()) / 2);
        center.setVertexY((max.getVertexY() + min.getVertexY()) / 2);
        center.setVertexZ((max.getVertexZ() + min.getVertexZ()) / 2);
    }

    /**
     * Displays the current bounds and center of this element
     */
    public void displayBounds() {
        /* //Message.displayMsgLow("Max: "); //Message.displayMsgLow(max.getVertexX() + ", " +
         * max.getVertexY() + ", " + max.getVertexZ()); //Message.displayMsgLow("Min: ");
         * //Message.displayMsgLow(min.getVertexX() + ", " + min.getVertexY() + ", " + min.getVertexZ());
         * //Message.displayMsgLow("Center: "); //Message.displayMsgLow(center.getVertexX() + ", " +
         * center.getVertexY() + ", " + center.getVertexZ()); */
    }

    /**
     * Returns the center of this element as vertex
     *
     * @return Vertex with the values of the center
     */
    public Vertex getCenter() {
        return center;
    }

    /**
     * Returns the max of this element as vertex
     *
     * @return Vertex with max values of this element
     */
    public Vertex getMax() {
        return max;
    }

    /**
     * Returns the min of this element as vertex
     *
     * @return Vertex with min values of this element
     */
    public Vertex getMin() {
        return min;
    }

    /**
     * Set the UV Coordinates of a vertex
     *
     * @param id Vertex ID
     * @param u  Vertex U Coordinates
     * @param v  Vertex V Coordinates
     */
    public void setModelUV(int id, float u, float v) {
        Vertex vertx = vert.get(id);
        vertx.setVertexU(u);
        vertx.setVertexV(v);
    }

    /**
     * Returns the U Coordinates of a vertex
     *
     * @param id ID of the vertex
     * @return The U Coordinate of the vertex
     */
    public float getModelMapU(int id) {
        Vertex vertx = vert.get(id);
        return vertx.getVertexU();
    }

    /**
     * Returns the V Coordinates of a vertex
     *
     * @param id ID of the vertex
     * @return The V Coordinate of the vertex
     */
    public float getModelMapV(int id) {
        Vertex vertx = vert.get(id);
        return vertx.getVertexV();
    }

    /**
     * Returns the X Position of the vertex
     *
     * @param id Vertex ID
     * @return the X Position of the vertex
     */
    public float getVertexX(int id) {
        Vertex vertx = vert.get(id);
        return vertx.getVertexX();
    }

    /**
     * Returns the Y Position of the vertex
     *
     * @param id Vertex ID
     * @return the Y Position of the vertex
     */
    public float getVertexY(int id) {
        Vertex vertx = vert.get(id);
        return vertx.getVertexY();
    }

    /**
     * Returns the Z Position of the vertex
     *
     * @param id Vertex ID
     * @return the Z Position of the vertex
     */
    public float getVertexZ(int id) {
        Vertex vertx = vert.get(id);
        return vertx.getVertexZ();
    }

    /**
     * Returns the a vertex of the polygon
     *
     * @param id Polygon ID
     * @return the a vertex of the polygon
     */
    public int getPolygonA(int stripIndex, int id) {
        Polygon polyn = strips.get(stripIndex).getPoly(id);
        return polyn.a;
    }

    /**
     * Returns the b vertex of the polygon
     *
     * @param id Polygon ID
     * @return the b vertex of the polygon
     */
    public int getPolygonB(int stripIndex, int id) {
        Polygon polyn = strips.get(stripIndex).getPoly(id);
        return polyn.b;
    }

    /**
     * Returns the c vertex of the polygon
     *
     * @param id Polygon ID
     * @return the c vertex of the polygon
     */
    public int getPolygonC(int stripIndex, int id) {
        Polygon polyn = strips.get(stripIndex).getPoly(id);
        return polyn.c;
    }

    /**
     * Returns the number of polygons in this model
     *
     * @return the numbers of polygons in this model
     */
    public int getPolygons() {
        return polygons;
    }

    /**
     * Returns the number of vertices in this model
     *
     * @return the numbers of vertices in this model
     */
    public int getVertices() {
        return vertices;
    }

    /**
     * Set number of polygons used in this model
     *
     * @param polygons number of polygons
     */
    public void setPolygons(int polygons) {
        this.polygons = polygons;
    }

    /**
     * Set number of vertices used in this model
     *
     * @param vertices number of vertices
     */
    public void setVertices(int vertices) {
        this.vertices = vertices;
    }

    /**
     * Set if this element contains vertex colors
     *
     * @param vertexColors true if it contains vertex colors
     */
    public void setHasVertexColors(boolean vertexColors) {
        this.hasVertexColors = vertexColors;
    }

    /**
     * Check if this element contains vertex colors
     *
     * @return true if this element contains vertex colors
     */
    public boolean getHasVertexColors() {
        return hasVertexColors;
    }

    /**
     * Returns the vertex specified by id
     *
     * @param id the id of the wanted vertex
     * @return Vertex
     */
    public Vertex getVertex(int id) {
        return vert.get(id);
    }

    /**
     * Returns the polygon specified by id
     *
     * @param stripIndex the strip you want to take this polygon from
     * @param id         the id of the polygon
     * @return polygon
     */
    public Polygon getPolygon(int stripIndex, int id) {
        return strips.get(stripIndex).getPoly(id);
    }

    /**
     * Render this element if visible is true
     *
     * @param gl used to render this element
     */
    public void render(GL2 gl) {
        if (visible) {
            for (int i = 0; i < strips.size(); i++) {
                if (shaders.size() > i)
                    gl.glBindTexture(gl.GL_TEXTURE_2D, shaders.get(strips.get(i).getShaderNumber()).getGLTex());
                strips.get(i).render(gl);
            }
        }
    }

}
