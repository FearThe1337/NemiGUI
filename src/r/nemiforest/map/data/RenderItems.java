package r.nemiforest.map.data;

import java.awt.image.BufferedImage;

/**
 * RenderItems - this class contains all items that can be placed on the nemi forest maps.
 * Paths are NOT included on these.
 */
public enum RenderItems {
    CHEST(Resources.instance().NODE_CHEST, "Chest"),
    FUNGAL(Resources.instance().NODE_FUNGAL, "Fungal growth"),
    FLOWERS(Resources.instance().NODE_FLOWER, "Mysterious flowers"),
    MUSHROOMS(Resources.instance().NODE_MUSHROOM, "Mushrooms"),
    ROCKS(Resources.instance().NODE_ROCKS, "Red rocks"),
    OBELISK(Resources.instance().NODE_OBELISK, "Obelisk"),
    EXPLORER(Resources.instance().NODE_EXPLORER, "Explorer"),
    MONK(Resources.instance().NODE_MONK, "Dead monk"),
    POOL(Resources.instance().NODE_POOL, "Water pool"),

    PLANT(Resources.instance().NODE_PAHLTI, "Pahtli plant"),
    TRAITOR(Resources.instance().NODE_TRAITOR,"Traitor"),

    DELETE(false,Resources.instance().DELETE,"Delete nodes"),
    TEXT(false,Resources.instance().TEXT,"Add text");
    ;
    private final boolean placeable;
    private final BufferedImage img;
    private final String name;

    RenderItems(boolean isPlaceable, BufferedImage img, String name){
        this.placeable = isPlaceable;
        this.img = img;
        this.name = name;
    }

    RenderItems(BufferedImage img, String name){
        this(true,img,name);
    }

    public boolean isPlaceable() {
        return placeable;
    }

    public BufferedImage getImage(){
        return this.img;
    }

    public String getName() {
        return name;
    }
}
