/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Paths;

/**
 *
 * @author Kilian
 */
public class Main {

    public Main(){
        //Placement place = new Placement("D:/Rockstar Games/GTA III/data/maps/comnbtm/comNbtm.ipl", 0); //III TEST
        //Placement place = new Placement("D:/Rockstar Games/GTA Vice City/data/maps/airport/airport.ipl", 1); //VC TEST
        //IPL place = new IPL("D:/Rockstar Games/GTA San Andreas - Modding/data/maps/LA/LAe.ipl", 2); //SA TEST
        //IPL place = new IPL("D:/Rockstar Games/Grand Theft Auto IV - Modding/pc/data/maps/east/queens_w2.wpl", 3); //IV TEST
        //Nodes nodes = new Nodes("E:/bronx_e_stream1.wpl", 3); //load IPL
        Nodes nodes = new Nodes();
        //place.savePlacement();
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
