

package nl.shadowlink.tools.shadowlib.model.wdr;


import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;

/**
 *
 * @author Shadow-Link
 */
public class Compression {
    private CompressionType type;

    public void setCodec(CompressionType type){
        this.type = type;
    }

    public void compress(DataOutputStream destination, byte[] source){

        DeflaterOutputStream deflater = new DeflaterOutputStream(destination, new Deflater(Deflater.DEFAULT_COMPRESSION, true));

        for(int i = 0; i < source.length-1; i++){
            try {
                deflater.write(source[i]);
            } catch (IOException ex) {
                Logger.getLogger(Compression.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            deflater.finish();
        } catch (IOException ex) {
            Logger.getLogger(Compression.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Message.displayMsgHigh("Deflate finished");
    }

    public byte[] decompress(byte[] source, int totalSize) {
        byte[] dataBuffer = new byte[totalSize+1];
        try {
            Inflater inflater = new Inflater(true);
            inflater.setInput(source);
            inflater.inflate(dataBuffer);
            inflater.end();
        } catch (DataFormatException ex) {
            Logger.getLogger(Compression.class.getName()).log(Level.SEVERE, null, ex);
        }
        //writeToFile(dataBuffer);
        return dataBuffer;
    }

    /*public void writeToFile(byte[] source){
        WriteFunctions wf = new WriteFunctions();
        FileOutputStream file_out = wf.openFile("wft2.dec");
        DataOutputStream data_out = new DataOutputStream(file_out);
        for(int i = 0; i < source.length-1; i++){
            wf.writeByte(data_out, source[i]);
        }
        wf.closeFile(file_out, data_out);
    }*/
}
