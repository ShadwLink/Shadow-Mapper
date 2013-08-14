/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IDE;

/**
 *
 * @author Kilian
 */
public class Main {

    public Main(){
        //IDE ide = new IDE("D:/Rockstar Games/GTA III/data/maps/comnbtm/comNbtm.ide", 0); //III TEST
        //IDE ide = new IDE("D:/Rockstar Games/GTA Vice City/data/maps/airport/airport.ide", 1); //VC TEST
        //IDE ide = new IDE("D:/Rockstar Games/GTA San Andreas - Modding/data/maps/LA/LAe.ide", 2); //SA TEST
        //IDE ide = new IDE("D:/Rockstar Games/Grand Theft Auto IV - Modding/pc/data/maps/east/queens_w2.ide", 3, true); //IV TEST
        IDE ide = new IDE("D:/Rockstar Games/Grand Theft Auto IV - Modding/common/data/vehicles.ide", 3, true); //IV TEST
        //ide.save();
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }

}
