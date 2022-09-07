

package nl.shadowlink.tools.shadowlib.model.wdr;

/**
 *
 * @author Shadow-Link
 */

public enum ResourceType{
    TextureXBOX(0x7), // xtd
    ModelXBOX(0x6D), // xdr
    Generic(0x01), // xhm / xad (Generic files as rsc?)
    Bounds(0x20), // xbd, wbd
    Particles(0x24), // xpfl
    Particles2(0x1B), // xpfl

    Texture(0x8), // wtd
    Model(0x6E), // wdr
    ModelFrag(0x70); //wft*/

    private int type;

    //de waarde tussen ( en ) bij de waarde zal gebruikt worden als
    //argument voor de constructor en wordt bijgehouden in graden
    ResourceType(int type){ this.type = type;}

    public static ResourceType get(int type){
        return Model;
    }

    }
