

package nl.shadowlink.tools.shadowlib.model.wdr;

/**
 *
 * @author Shadow-Link
 */
public enum CompressionType {
    LZX(0xF505),
    Deflate(0xDA78);

    private int type;

    //de waarde tussen ( en ) bij de waarde zal gebruikt worden als
    //argument voor de constructor en wordt bijgehouden in graden
    CompressionType(int type){ this.type = type;}

    public static CompressionType get(int type){
        CompressionType ret = LZX;
        switch(type){
            case 0xF505:
                ret = LZX;
                break;
            case 0xDA78:
                ret = Deflate;
                break;
        }
        return ret;
    }
}
