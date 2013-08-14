/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IMG;

import file_io.ByteReader;
import file_io.Message;
import file_io.ReadFunctions;
import file_io.WriteFunctions;
import shadowmapper.Finals;
import shadowmapper.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

/**
 *
 * @author Kilian
 */
public class IMG_IV {
    private byte[] ident = new byte[4];

    public void loadImg(IMG image) {
        Message.displayMsgHigh("Started IV");

        ReadFunctions rf = new ReadFunctions();
        rf.openFile(image.getFileName());

        ident[0] = rf.readByte();
        ident[1] = rf.readByte();
        ident[2] = rf.readByte();
        ident[3] = rf.readByte();
        if(ident[0] == 82 && ident[1] == 42 && ident[2] == 78 && ident[3] == -87){
            Message.displayMsgHigh("Unencryped IMG");
            readUnEncryptedImg(rf, image);
        }else{
            image.encrypted = true;
            Message.displayMsgHigh("Encrypted IMG");
            readEncryptedImg(rf, image);
        }
        rf.closeFile();
    }

    public void saveImg(IMG img){
        System.out.println("Saving IV IMG");
        WriteFunctions wf = new WriteFunctions();
        wf.openFile(img.getFileName()+".temp");
        ReadFunctions rf = new ReadFunctions();
        rf.openFile(img.getFileName());
        wf.writeByte(82);//write R*N start bytes
        wf.writeByte(42);
        wf.writeByte(78);
        wf.writeByte(-87);
        wf.writeInt(3); //write version number
        wf.writeInt(img.Items.size()); //write item count
        int tableSize = 0;
        for(int i = 0; i < img.Items.size(); i++){//calculate table size
            tableSize += img.Items.get(i).getName().length() + 17;
        }
        wf.writeInt(tableSize);
        wf.writeShort(0x0010);//table item size (0x10)
        wf.writeShort(0x00E9); //unknown yet

        int offset = 0x800;
        while(tableSize+20 > offset){
            offset += 0x800;
        }
        int padding = offset - tableSize-20;

        for(int i = 0; i < img.Items.size(); i++){
            if(img.Items.get(i).isResource()){
                wf.writeInt(img.Items.get(i).getFlags()); //write the flags
            }else{
                wf.writeInt(img.Items.get(i).getSize()); //size
            }
            wf.writeInt(img.Items.get(i).getType()); //type
            wf.writeInt(offset/0x800); //offset
            offset += img.Items.get(i).getSize(); // next offset
            int pad = 0;
            while((offset % 0x800) != 0){
                offset += 1;
                pad += 1;
                //System.out.println("Offset: " + offset + " " + pad);
            }
            wf.writeShort((img.Items.get(i).getSize()+pad)/0x800); // blocks
            if(img.Items.get(i).isResource()){
                wf.writeShort(pad + 8192); //padding
            }else{
                wf.writeShort(pad);
            }
        }
        for(int i = 0; i < img.Items.size(); i++){
            wf.writeNullTerminatedString(img.Items.get(i).getName());
        }
        for(int i = 0; i < padding; i++){
            wf.writeByte(0);
        }
        int cOffset = 0x800;
        for(int i = 0; i < img.Items.size(); i++){
            rf.seek(img.Items.get(i).getOffset());
            byte[] array = rf.readArray(img.Items.get(i).getSize());
            wf.writeArray(array);
            cOffset += img.Items.get(i).getSize();
            while((cOffset % 0x800) != 0){
                cOffset += 1;
                wf.writeByte(0);
            }
        }
        rf.closeFile();
        wf.closeFile();
        File origFile = new File(img.getFileName());
        try {
            if(origFile.canWrite()){
                System.out.println("Can write");
            }
            if(origFile.canExecute()){
                System.out.println("Can execute");
            }
            if(origFile.delete()){
                System.out.println("Deleted " + img.getFileName());
            }else{
                System.out.println("Not deleted");
            }
        }catch(Exception ex){
            System.out.println("Unable to delete " + img.getFileName() + " " + ex);
        }
        File newFile = new File(img.getFileName() + ".temp");
        if(newFile.renameTo(new File(img.getFileName()))){
            System.out.println("rename file");
        }else{
            System.out.println("Unable to rename file");
        }
        newFile.delete();
        origFile = null;
        newFile = null;
    }

    private void increaseTypeCounter(String itemName, IMG img){
        itemName = itemName.toLowerCase();
        if(itemName.endsWith(".cut")){
            img.cutCount++;
        }else if(itemName.endsWith(".wad")){
                img.wadCount++;
        }else if(itemName.endsWith(".wbd")){
                img.wbdCount++;
        }else if(itemName.endsWith(".wbn")){
                img.wbnCount++;
        }else if(itemName.endsWith(".wdr")){
                img.wdrCount++;
        }else if(itemName.endsWith(".wdd")){
                img.wddCount++;
        }else if(itemName.endsWith(".wft")){
                img.wftCount++;
        }else if(itemName.endsWith(".wpl")){
                img.wplCount++;
        }else if(itemName.endsWith(".wtd")){
                img.wtdCount++;
        }
    }

    public void readUnEncryptedImg(ReadFunctions rf, IMG image){
        ArrayList<IMG_Item> items = new ArrayList();
        Message.displayMsgHigh("Version 3: " + rf.readInt());
        int itemCount = rf.readInt();
        image.itemCount = itemCount;
        System.out.println("Item Count: " + itemCount);
        System.out.println("Table Size: " + rf.readInt());
        System.out.println("Size of table items: " + Util.getHexString(rf.readShort()));
        System.out.println("Unknown: " + rf.readShort());

        //read table
        for(int curItem = 0; curItem < itemCount; curItem++){
            IMG_Item item = new IMG_Item();
            int itemSize = rf.readInt(); //or flags
            int itemType = rf.readInt();
            int itemOffset = rf.readInt()*0x800;
            int usedBlocks = rf.readShort();
            int Padding = (rf.readShort() & 0x7FF);
            if(itemType <= 0x6E){
                item.setFlags(itemSize);
                itemSize = Util.getTotalMemSize(itemSize);
            }
            System.out.println("-------------------------------");
            System.out.println("Offset: " + Util.getHexString(itemOffset));
            System.out.println("Size: " + itemSize + " bytes");
            System.out.println("Type: " + Util.getHexString(itemType) + " " + itemType);
            System.out.println("UsedBlocks: " + usedBlocks);
            System.out.println("Padding: " + Padding);
            item.setOffset(itemOffset);
            item.setSize(usedBlocks*0x800-Padding);
            item.setType(itemType);
            items.add(item);
        }

        //read names
        for(int curName = 0; curName < itemCount; curName++){
            String name = rf.readNullTerminatedString();
            items.get(curName).setName(name);
            increaseTypeCounter(name, image);
            //items.get(curName).setType(Util.getFileType(name, image));
            Message.displayMsgHigh(name);
        }

        if(items.size() > 0) image.setItems(items);
        else image.setItems(null);
    }

    public void readEncryptedImg(ReadFunctions test, IMG img){
        ArrayList<IMG_Item> items = new ArrayList();

        byte[] key = img.key;

        byte[] data = withIdent(test, key);

        ByteReader br = new ByteReader(data, 0);
        int id = br.ReadUInt32();
        int version = br.ReadUInt32();
        int itemCount = br.ReadUInt32();
        int tableSize = br.ReadUInt32();

        System.out.println("ID: " + id);
        System.out.println("Version: " + version);
        System.out.println("Number of items: " + itemCount);
        System.out.println("Size of table: " + tableSize);

        int itemSize = test.readShort();
        int unknown = test.readShort();
        
        int namesSize = tableSize - (itemCount * itemSize);
        
        System.out.println("Item size: " + itemSize);
        System.out.println("Unknown: " + unknown);
        System.out.println("Names: " + namesSize);

        for(int i = 0; i < itemCount; i++){
            data = decrypt16byteBlock(test, key); //decrypt all table items
            br = new ByteReader(data, 0);
            IMG_Item item = new IMG_Item();
            itemSize = br.ReadUInt32(); //or flags
            int itemType = br.ReadUInt32();
            int itemOffset = br.ReadUInt32()*2048;
            int usedBlocks = br.ReadUInt16();
            int Padding = (br.ReadUInt16() & 0x7FF);
            if(itemType <= 0x6E){
                item.setFlags(itemSize);
                itemSize = Util.getTotalMemSize(itemSize);
            }
            System.out.println("-------------------------------");
            System.out.println("Offset: " + Util.getHexString(itemOffset));
            System.out.println("Size: " + itemSize + " bytes");
            System.out.println("Type: " + Util.getHexString(itemType));
            System.out.println("UsedBlocks: " + usedBlocks);
            System.out.println("Padding: " + Padding);
            item.setOffset(itemOffset);
            item.setSize(usedBlocks*0x800-Padding);
            item.setType(itemType);
            items.add(item);
        }

        int i = 0;
        byte[] names = new byte[namesSize];
        while(i < namesSize){
            data = decrypt16byteBlock(test, key); //decrypt all table names
            for(int j = 0; j < 16; j++){
                names[i+j] = data[j];
            }
            i += 16;
            if((i + 16) > namesSize){
                String lastName = test.readNullTerminatedString();
                byte[] lastBytes = lastName.getBytes();
                for(int j = 0; j < lastBytes.length; j++){
                    names[i + j] = lastBytes[j];
                }
                i += 16;
            }
        }

        br = new ByteReader(names, 0);

        for(i = 0; i < itemCount; i++){
            String name = br.ReadNullTerminatedString();
            items.get(i).setName(name);
            increaseTypeCounter(name, img);
            System.out.println("Name" + i + ": " + name);
            br.ReadByte();
        }

        //rf.closeFile();
        test.closeFile();

        if(items.size() > 0) img.setItems(items);
        else img.setItems(null);
    }

    public byte[] withIdent(ReadFunctions test, byte[] key){
        byte data[] = new byte[16];

        data[0] = ident[0];
        data[1] = ident[1];
        data[2] = ident[2];
        data[3] = ident[3];

        for(int j = 4; j < 16; j++){
            data[j] = test.readByte();
        }
        for (int j = 1; j <= 16; j++){            // 16 (pointless) repetitions
            try {
                data = decryptAES(key, data);
            } catch (Exception ex) {
                Logger.getLogger(IMG_IV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return data;
    }

    public byte[] decrypt16byteBlock(ReadFunctions test, byte[] key){
        byte data[] = new byte[16];
        for(int j = 0; j < 16; j++){
            data[j] = test.readByte();
        }
        for (int j = 1; j <= 16; j++){            // 16 (pointless) repetitions
            try {
                data = decryptAES(key, data);
            } catch (Exception ex) {
                Logger.getLogger(IMG_IV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return data;
    }

    public byte[] encryptAES(byte key[], byte msg[]) throws Exception{

        SecretKeySpec skeySpec = new SecretKeySpec(key, "Rijndael");

        // Instantiate the cipher
        Cipher cipher = Cipher.getInstance("Rijndael/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        return cipher.doFinal(msg);
    }

    public byte[] decryptAES(byte key[], byte msg[]) throws Exception{

        SecretKeySpec skeySpec = new SecretKeySpec(key, "Rijndael");

        // Instantiate the cipher
        Cipher cipher = Cipher.getInstance("Rijndael/ECB/NoPadding");
        try{
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        }catch(Exception ex) {
            JOptionPane.showMessageDialog(null, "You didn't install the JCE unlimited strength files. Follow the usage instructions in the readme.\nThis program will now exit.");
            System.exit(0);
        }

        return cipher.doFinal(msg);
    }



}
