package nl.shadowlink.tools.io;

/**
 * @author Shadow-Link
 */
public class Vector3D {

    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;

    public Vector3D() {
    }

    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void plus(Vector3D v3d) {
        this.x += v3d.getX();
        this.y += v3d.getY();
        this.z += v3d.getZ();
    }

    public void minus(Vector3D v3d) {
        this.x -= v3d.getX();
        this.y -= v3d.getY();
        this.z -= v3d.getZ();
    }

    public void maal(Vector3D v3d) {
        this.x *= v3d.getX();
        this.y *= v3d.getY();
        this.z *= v3d.getZ();
    }

    public Vector3D maal(float maal) {
        this.x *= maal;
        this.y *= maal;
        this.y *= maal;
        return this;
    }

    public Vector4D normalize() {
        double norm;

        norm = 1.0 / Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        this.x *= norm;
        this.y *= norm;
        this.z *= norm;
        Vector4D v4d = new Vector4D(this.x, this.y, this.z, 0.0f);
        return v4d;
    }

    @Override
    public String toString() {
        return x + ", " + y + ", " + z;
    }

}
