/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IMG;

/**
 *
 * @author Kilian
 */
public class IMG_Item {
    private int type;
    private int offset;
    private int size;
    private String name;

    private boolean resource = false;
    private int flags = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        if(type < 1000){
            resource = true;
        }
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public boolean isResource() {
        return resource;
    }

    public void setResource(boolean resource) {
        this.resource = resource;
    }



}
