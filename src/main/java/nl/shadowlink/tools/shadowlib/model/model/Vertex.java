

package nl.shadowlink.tools.shadowlib.model.model;

/**
 *
 * @author Shadow-Link
 */
public class Vertex {
    public float x;
    public float y;
    public float z;
    public float u;
    public float v;

    private float normX;
    private float normY;
    private float normZ;

    private float tangX;
    private float tangY;
    private float tangZ;
    private float tangW;

    private byte red;
    private byte green;
    private byte blue;
    private byte alpha;

    private boolean vertexColors = false;

    public Vertex(float x, float y, float z, float u, float v){
        this.x = x;
        this.y = y;
        this.z = z;
        this.u = u;
        this.v = v;
    }

    public void setVertexColors(byte red, byte green, byte blue, byte alpha){
        vertexColors = true;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public byte getRed(){
        return red;
    }

    public byte getGreen(){
        return red;
    }

    public byte getBlue(){
        return red;
    }

    public byte getAlpha(){
        return red;
    }

    public float getVertexX(){
        return x;
    }

    public float getVertexY(){
        return y;
    }

    public float getVertexZ(){
        return z;
    }

    public float getVertexU(){
        return u;
    }

    public float getVertexV(){
        return v;
    }

    public void setVertexX(float x){
        this.x = x;
    }

    public void setVertexY(float y){
        this.y = y;
    }

    public void setVertexZ(float z){
        this.z = z;
    }

    public void setVertexU(float u){
        this.u = u;
    }

    public void setVertexV(float v){
        this.v = v;
    }

    public float getNormX() {
        return normX;
    }

    public void setNormX(float normX) {
        this.normX = normX;
    }

    public float getNormY() {
        return normY;
    }

    public void setNormY(float normY) {
        this.normY = normY;
    }

    public float getNormZ() {
        return normZ;
    }

    public void setNormZ(float normZ) {
        this.normZ = normZ;
    }

    public void setNormals(float normX, float normY, float normZ){
        this.normX = normX;
        this.normY = normY;
        this.normZ = normZ;
    }

    public void setVertexPostion(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean hasVertexColors() {
        return vertexColors;
    }

    public void setVertexColors(boolean vertexColors) {
        this.vertexColors = vertexColors;
    }

}
