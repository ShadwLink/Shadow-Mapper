package nl.shadowlink.tools.shadowlib.water;

/**
 * @author Shadow-Link
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Water water = new Water("D:/Rockstar Games/Grand Theft Auto IV - Modding/common/data/water.dat", 3); //IV TEST);
        water.read();
    }

}
