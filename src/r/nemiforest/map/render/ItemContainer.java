package r.nemiforest.map.render;

import r.nemiforest.map.data.RenderItems;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * ItemContainer is responsible for maintaining the nodes added to the map.
 * This allows removal support.
 * The class is also responsible for rendering it and maintaining a image to buffer this.
 */
public class ItemContainer {
    private BufferedImage bi;
    private ArrayList<Entry> elements;

    public ItemContainer(int width, int height){
        this.bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        this.elements = new ArrayList<>();
        render();
    }

    public BufferedImage getImage(){
        return this.bi;
    }


    public Entry add(RenderItems item, int x, int y) {
        if(item.isPlaceable()) {
            Entry entry = new Entry(item, x, y);
            elements.add(entry);
            render((Graphics2D) bi.getGraphics(),entry);
            return entry;
        }
        return null;
    }

    public boolean remove(Entry e){
        if(elements.remove(e)){
            render();
            return true;
        }
        return false;
    }

    public Entry remove(int x, int y) {
        for(int i=elements.size()-1; i >= 0; i--){
            Entry entry = elements.get(i);
            BufferedImage mouse = entry.item.getImage();

            //Needed to center.
            int width = mouse.getWidth();
            int height = mouse.getHeight();

            int px = Math.max(0, entry.x - (width / 2));
            int py = Math.max(0, entry.y - (height / 2));

            if((x >= px && x <= (px + width)) && (y >= py && y <= (py + height))){
                int rx = x - px;
                int ry = y - py;

                int c = mouse.getRGB(rx,ry);
                c = (c&0xFF000000)>>16;
                if((c&0xFF000000) != 0){
                    elements.remove(i);
                    render();
                    return entry;
                }
            }
        }
        return null;
    }

    private void render(Graphics2D g, Entry entry){
        BufferedImage mouse = entry.item.getImage();

        //Needed to center.
        int width = mouse.getWidth();
        int height = mouse.getHeight();

        int px = Math.max(0, entry.x - (width / 2));
        int py = Math.max(0, entry.y - (height / 2));


        g.drawImage(mouse, px, py, null);
    }


    private void render(){
        Graphics2D g = (Graphics2D) bi.getGraphics();

        //Clear&Trans
        g.setComposite(AlphaComposite.Clear);
        g.setColor(new Color(255,255,255,255));
        g.fillRect(0,0,bi.getWidth(),bi.getHeight());
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        for(Entry entry : elements) {
           render(g,entry);
        }
    }

    public void add(Entry entry) {
        elements.add(entry);
        render((Graphics2D) bi.getGraphics(),entry);
    }


    public static class Entry {
        private final RenderItems item;
        private final int x;
        private final int y;

        public Entry(RenderItems item, int x, int y){

            this.item = item;
            this.x = x;
            this.y = y;
        }
    }
}
