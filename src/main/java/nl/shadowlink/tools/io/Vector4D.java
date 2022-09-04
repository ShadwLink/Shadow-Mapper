package nl.shadowlink.tools.io;



/**
 * @author Shadow-Link
 */
public class Vector4D {

	public float x = 0.0f;
	public float y = 0.0f;
	public float z = 0.0f;
	public float w = 0.0f;

	public Vector4D() {
	}

	public Vector4D(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
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

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}

	public void plus(Vector4D v4d) {
		this.x += v4d.getX();
		this.y += v4d.getY();
		this.z += v4d.getZ();
		this.w += v4d.getW();
	}

	public void minus(Vector4D v4d) {
		this.x -= v4d.getX();
		this.y -= v4d.getY();
		this.z -= v4d.getZ();
		this.w -= v4d.getW();
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

	public void print(String name) {
		System.out.println(name + ": " + x + ", " + y + ", " + z + ", " + w);
	}

	public Vector4D getAxisAngle() {
		// float scale = (float)Math.sqrt(item.rotation.x * item.rotation.x + item.rotation.y * item.rotation.y +
		// item.rotation.z * item.rotation.z);
		float scale = -1.0f;
		Vector4D rot = new Vector4D();
		rot.x = x / scale;
		rot.y = y / scale;
		rot.z = z / scale;
		rot.w = (float) (Math.acos(w) * 2.0f);
		rot.w = (float) (rot.w * (180 / 3.14159265));  // from radians to degrees
		return rot;
	}

	public String toString() {
		return x + ", " + y + ", " + z + ", " + w;
	}

	public Vector3D toVector3D() {
		return new Vector3D(x, y, z);
	}

}
