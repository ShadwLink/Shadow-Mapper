package nl.shadowlink.tools.shadowlib.water;


import nl.shadowlink.tools.io.Vector3D;

/**
 * @author Shadow-Link
 */
public class WaterPoint {
    public Vector3D coord;
    public float speedX;
    public float speedY;
    public float unknown;
    public float waveHeight;

    public WaterPoint(String line) {
        String[] split = line.split(" ");
        // System.out.println("WaterPoint " + split.length);
        coord = new Vector3D(Float.valueOf(split[0]), Float.valueOf(split[1]), Float.valueOf(split[2]));
        speedX = Float.valueOf(split[3]);
        speedY = Float.valueOf(split[4]);
        unknown = Float.valueOf(split[5]);
        waveHeight = Float.valueOf(split[6]);
    }

}
