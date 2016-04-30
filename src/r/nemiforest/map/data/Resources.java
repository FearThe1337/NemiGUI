package r.nemiforest.map.data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Resources - this class contains all standard resources that are used by the application.
 */
public class Resources {
    private static Resources instance = null;

    public final BufferedImage EXAMPLE;
    public final BufferedImage LEGEND;
    public final BufferedImage BACKGROUND;
    public final BufferedImage NODE_CHEST;
    public final BufferedImage NODE_EXPLORER;
    public final BufferedImage NODE_FLOWER;
    public final BufferedImage NODE_FUNGAL;
    public final BufferedImage NODE_MONK;
    public final BufferedImage NODE_MUSHROOM;
    public final BufferedImage NODE_OBELISK;
    public final BufferedImage NODE_POOL;
    public final BufferedImage NODE_ROCKS;
    public final BufferedImage NODE_PAHLTI;
    public final BufferedImage NODE_TRAITOR;
    public final BufferedImage DELETE;


    public Resources(){
        try{
            BACKGROUND = loadImage("Background.PNG");
            LEGEND = loadImage("Legend_Overlay.PNG");
            NODE_CHEST = loadImage("Node_Chest.PNG");
            NODE_EXPLORER = loadImage("Node_Explorer.PNG");
            NODE_FLOWER = loadImage("Node_Flower.PNG");
            NODE_FUNGAL = loadImage("Node_Fungal.PNG");
            NODE_MONK = loadImage("Node_Monk.PNG");
            NODE_MUSHROOM = loadImage("Node_Mushroom.PNG");
            NODE_OBELISK = loadImage("Node_Obelisk.PNG");
            NODE_POOL = loadImage("Node_Pool.PNG");
            NODE_ROCKS = loadImage("Node_Rocks.PNG");
            NODE_PAHLTI = loadImage("Trinks_Pahtli.PNG");
            NODE_TRAITOR = loadImage("Trinks_Traitor.PNG");
            EXAMPLE = loadImage("example.PNG");
            DELETE = loadImage("delete.PNG");
        }catch(Exception e){
            throw new Error(e);
        }
    }
    private BufferedImage loadImage(String name) throws IOException {
        ClassLoader cl = Resources.class.getClassLoader();
        URL url = cl.getResource("res/"+name);
        return ImageIO.read(url);
    }

    public static Resources instance(){
        return instance;
    }

    private static void init() {
        synchronized (Resources.class) {
            if (instance == null) {
                instance = new Resources();
            }
        }
    }

    static{
        init();
    }
}
