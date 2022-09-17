package nl.shadowlink.tools.shadowlib.ipl;

import nl.shadowlink.tools.shadowlib.utils.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Shadow-Link
 */
public class IPL_III_ERA {
    private BufferedReader input;
    private FileReader fileReader;

    private int readItem = -1;

    public void loadPlacement(IPL ipl) {
        if (openIPL(ipl.getFileName())) {
            try {
                String line = null; //not declared within while loop
                while ((line = input.readLine()) != null) {
                    if (readItem == -1) {
                        if (line.startsWith("#")) {
                            //Message.displayMsgHigh("Commentaar: " + line);
                        } else if (line.startsWith("inst")) {
                            readItem = Constants.pINST;
                            //Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("cull")) {
                            readItem = Constants.pCULL;
                            //Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("path")) {
                            readItem = Constants.pPATH;
                            //Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("grge")) {
                            readItem = Constants.pGRGE;
                            //Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("enex")) {
                            readItem = Constants.pENEX;
                            //Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("pick")) {
                            readItem = Constants.pPICK;
                            //Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("jump")) {
                            readItem = Constants.pJUMP;
                            //Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("tcyc")) {
                            readItem = Constants.pTCYC;
                            //Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("auzo")) {
                            readItem = Constants.pAUZO;
                            //Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("mult")) {
                            readItem = Constants.pMULT;
                            //Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("cars")) {
                            readItem = Constants.pCARS;
                            //Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("occl")) {
                            readItem = Constants.pOCCL;
                            //Message.displayMsgHigh("Started reading item " + readItem);
                        } else if (line.startsWith("zone")) {
                            readItem = Constants.pZONE;
                            //Message.displayMsgHigh("Started reading item " + readItem);
                        }
                    } else {
                        if (line.startsWith("#")) {
                            //Message.displayMsgHigh("Commentaar: " + line);
                        } else if (line.startsWith("end")) {
                            //Message.displayMsgHigh("Item " + readItem + " ended");
                            readItem = -1;
                        } else {
                            IplItem item = null;
                            switch (readItem) {
                                case Constants.pINST:
                                    item = new Item_INST(ipl.getGameType());
                                    ipl.items_inst.add((Item_INST) item);
                                    break;
                                case Constants.pAUZO:
                                    item = new Item_AUZO(ipl.getGameType());
                                    break;
                                case Constants.pCARS:
                                    item = new Item_CARS(ipl.getGameType());
                                    break;
                                case Constants.pCULL:
                                    item = new Item_CULL(ipl.getGameType());
                                    break;
                                case Constants.pENEX:
                                    item = new Item_ENEX(ipl.getGameType());
                                    break;
                                case Constants.pJUMP:
                                    item = new Item_JUMP(ipl.getGameType());
                                    break;
                                case Constants.pGRGE:
                                    item = new Item_GRGE(ipl.getGameType());
                                    break;
                                case Constants.pMULT:
                                    item = new Item_MULT(ipl.getGameType());
                                    break;
                                case Constants.pOCCL:
                                    item = new Item_OCCL(ipl.getGameType());
                                    break;
                                case Constants.pPATH:
                                    item = new Item_PATH(ipl.getGameType());
                                    break;
                                case Constants.pPICK:
                                    item = new Item_PICK(ipl.getGameType());
                                    break;
                                case Constants.pTCYC:
                                    item = new Item_TCYC(ipl.getGameType());
                                    break;
                                case Constants.pZONE:
                                    item = new Item_ZONE(ipl.getGameType());
                                    break;
                                default:
                                    //Message.displayMsgHigh("Unknown item " + line);
                            }
                            item.read(line);
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(IPL_III_ERA.class.getName()).log(Level.SEVERE, null, ex);
            }
            closeIPL();
        } else {
            //Message.displayMsgHigh("Error: Can't open file");
        }
        ipl.loaded = true;
    }

    public boolean openIPL(String fileName) {
        try {
            fileReader = new FileReader(fileName);
            input = new BufferedReader(fileReader);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public boolean closeIPL() {
        try {
            input.close();
            fileReader.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public void save(IPL ipl) {

    }

}
