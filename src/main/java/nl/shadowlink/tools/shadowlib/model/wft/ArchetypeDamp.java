package nl.shadowlink.tools.shadowlib.model.wft;

import nl.shadowlink.tools.io.ByteReader;

/**
 * @author Shadow-Link
 */
public class ArchetypeDamp {

    public void read(ByteReader br) {
        int VTable = br.readUInt32();
        System.out.println("VTable: " + VTable);

    }

}
