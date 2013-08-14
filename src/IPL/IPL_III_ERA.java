/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IPL;

import shadowmapper.Finals;
import file_io.Message;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kilian
 */
public class IPL_III_ERA {
    private BufferedReader input;
    private FileReader fileReader;

    private int readItem = -1;

    public void loadPlacement(IPL ipl) {
        if(openIPL(ipl.getFileName())){
            try {
                String line = null; //not declared within while loop
                while ((line = input.readLine()) != null) {
                    if (readItem == -1) {
                        if (line.startsWith("#")) {
                            Message.displayMsgHigh("Commentaar: " + line);
                        } else if (line.startsWith("inst")) {
                            readItem = Finals.pINST;
                            Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("cull")) {
                            readItem = Finals.pCULL;
                            Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("path")) {
                            readItem = Finals.pPATH;
                            Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("grge")) {
                            readItem = Finals.pGRGE;
                            Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("enex")) {
                            readItem = Finals.pENEX;
                            Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("pick")) {
                            readItem = Finals.pPICK;
                            Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("jump")) {
                            readItem = Finals.pJUMP;
                            Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("tcyc")) {
                            readItem = Finals.pTCYC;
                            Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("auzo")) {
                            readItem = Finals.pAUZO;
                            Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("mult")) {
                            readItem = Finals.pMULT;
                            Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("cars")) {
                            readItem = Finals.pCARS;
                            Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("occl")) {
                            readItem = Finals.pOCCL;
                            Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("zone")) {
                            readItem = Finals.pZONE;
                            Message.displayMsgHigh("Started reading item " + readItem);
                        }
                    } else {
                        if (line.startsWith("#")) {
                            Message.displayMsgHigh("Commentaar: " + line);
                        }else if (line.startsWith("end")) {
                            Message.displayMsgHigh("Item " + readItem + " ended");
                            readItem = -1;
                        } else {
                            IPL_Item item = null;
                            switch(readItem){
                                case Finals.pINST:
                                    item = new Item_INST(ipl.getGameType());
                                    ipl.items_inst.add((Item_INST)item);
                                    break;
                                case Finals.pAUZO:
                                    item = new Item_AUZO(ipl.getGameType());
                                    break;
                                case Finals.pCARS:
                                    item = new Item_CARS(ipl.getGameType());
                                    break;
                                case Finals.pCULL:
                                    item = new Item_CULL(ipl.getGameType());
                                    break;
                                case Finals.pENEX:
                                    item = new Item_ENEX(ipl.getGameType());
                                    break;
                                case Finals.pJUMP:
                                    item = new Item_JUMP(ipl.getGameType());
                                    break;
                                case Finals.pGRGE:
                                    item = new Item_GRGE(ipl.getGameType());
                                    break;
                                case Finals.pMULT:
                                    item = new Item_MULT(ipl.getGameType());
                                    break;
                                case Finals.pOCCL:
                                    item = new Item_OCCL(ipl.getGameType());
                                    break;
                                case Finals.pPATH:
                                    item = new Item_PATH(ipl.getGameType());
                                    break;
                                case Finals.pPICK:
                                    item = new Item_PICK(ipl.getGameType());
                                    break;
                                case Finals.pTCYC:
                                    item = new Item_TCYC(ipl.getGameType());
                                    break;
                                case Finals.pZONE:
                                    item = new Item_ZONE(ipl.getGameType());
                                    break;
                                default:
                                    Message.displayMsgHigh("Unknown item " + line);
                            }
                            item.read(line);
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(IPL_III_ERA.class.getName()).log(Level.SEVERE, null, ex);
            }
            closeIPL();
        }else{
            Message.displayMsgHigh("Error: Can't open file");
        }
        ipl.loaded = true;
    }

    public boolean openIPL(String fileName){
        try {
            fileReader = new FileReader(fileName);
            input =  new BufferedReader(fileReader);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public boolean closeIPL(){
        try {
            input.close();
            fileReader.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public void save(IPL ipl){

    }

}
