package nl.shadowlink.tools.shadowlib.model.model;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import nl.shadowlink.tools.io.Vector4D;

import java.util.ArrayList;

/**
 * @author Shadow-Link
 */
public class Strip {
    private ArrayList<Polygon> poly = new ArrayList();
    private ArrayList<Vertex> vert = new ArrayList();
    private int polyCount;
    private int materialIndex; // shader index

    public Vertex max = new Vertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    public Vertex min = new Vertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    public Vertex center = new Vertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    public Vector4D sphere = new Vector4D(0.0f, 0.0f, 0.0f, 0.0f);

    /**
     * Creates a new strip with the given amount of polys and materialindex
     *
     * @param polyCount     number of polys
     * @param materialIndex index of the material
     */
    public Strip(int polyCount, int materialIndex) {
        this.polyCount = polyCount;
        this.materialIndex = materialIndex;
    }

    /**
     * Adds a poly to this strip
     *
     * @param polygon to add
     */
    public void addPoly(Polygon polygon) {
        poly.add(polygon);
    }

    /**
     * Add vertex to this strip and checks if it's not already in the strip
     *
     * @param vertex to add
     * @return index where the vertex has been added
     */
    public int addVertex(Vertex vertex) {
        int ret = -1;
        if (!vert.contains(vertex)) {
            ret = vert.size();
            vert.add(vertex);
            // //Message.displayMsgLow("Added vertex at " + ret);
        } else {
            ret = vert.indexOf(vertex);
            // //Message.displayMsgLow("Vertex bestond al in deze strip " + ret);
        }
        return ret;
    }

    /**
     * Adds vertex to the strip without checking if it already exists
     *
     * @param vertex the vertex to add
     */
    public void addVertexToStrip(Vertex vertex) {
        vert.add(vertex);
        checkBounds(vertex);
    }

    public Polygon getPoly(int id) {
        return poly.get(id);
    }

    public int getPolyCount() {
        return poly.size();
    }

    public int getShaderNumber() {
        return materialIndex;
    }

    public int getVertexCount() {
        return vert.size();
    }

    public Vertex getVertex(int i) {
        return vert.get(i);
    }

    /**
     * Render this strip
     *
     * @param gl used to render this strip
     */
    public void render(GL2 gl) {

        gl.glBegin(GL.GL_TRIANGLES); //begin triangle object
        for (int i = 0; i < poly.size(); i++) { //zolang we polygons hebben

            Polygon pol = poly.get(i);
            Vertex verta = vert.get(pol.a);
            Vertex vertb = vert.get(pol.b);
            Vertex vertc = vert.get(pol.c);

            gl.glTexCoord2f(verta.u, verta.v);
            gl.glVertex3f(verta.x, verta.y, verta.z); //eerste vertex van polygon

            gl.glTexCoord2f(vertb.u, vertb.v);
            gl.glVertex3f(vertb.x, vertb.y, vertb.z); // tweede vertex van polygon

            gl.glTexCoord2f(vertc.u, vertc.v);
            gl.glVertex3f(vertc.x, vertc.y, vertc.z); // derde vertex van polygon
        }
        gl.glEnd();
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
        center.setVertexX((max.getVertexX() + min.getVertexX()) / 2);
        center.setVertexY((max.getVertexY() + min.getVertexY()) / 2);
        center.setVertexZ((max.getVertexZ() + min.getVertexZ()) / 2);
        sphere.x = center.x;
        sphere.y = center.y;
        sphere.z = center.z;
        sphere.w = getMax();
    }

    private float getMax() {
        float value = max.x;
        if (value < max.y)
            value = max.y;
        if (value < max.z)
            value = max.z;
        if (value < 0 - min.x)
            value = 0 - min.x;
        if (value < 0 - min.y)
            value = 0 - min.y;
        if (value < 0 - min.z)
            value = 0 - min.z;
        // System.out.println("Max is: " + value);
        return value;
    }

}
