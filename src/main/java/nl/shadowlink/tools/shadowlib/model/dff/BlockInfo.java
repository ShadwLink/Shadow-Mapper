

package nl.shadowlink.tools.shadowlib.model.dff;

/**
 *
 * @author Shadow-Link
 */
public class BlockInfo {
    public int size;
    public int offset;
    public int strip;
    public int type;
    public int id;

    public BlockInfo(int id){
        this.id = id;
        System.out.println("Creating new block " + id);
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStrip() {
        return strip;
    }

    public void setStrip(int strip) {
        this.strip = strip;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



}
