package r.nemiforest.map.render;

import r.nemiforest.map.data.Constants;
import r.nemiforest.map.data.RenderItems;
import r.nemiforest.map.data.Resources;
import r.nemiforest.map.history.History;
import r.nemiforest.map.history.NodePlacementAction;
import r.nemiforest.map.history.NodeRemovalAction;
import r.nemiforest.map.history.PathPlacementAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

/**
 * Bit of an unorganized class...
 * but contains the core stuff.
 */
public class ImageRender extends JPanel {
    // Mouse data.
    private int mx, my;
    private boolean mouseInside = false;
    private RenderItems item;

    // Various layers & data containers
    private BufferedImage mapLayer;
    private ItemContainer itemContainer;
    private RouteContainer paths;
    private History history;


    public ImageRender(){
        item = null;
        this.history = new History();
        addMouseListener();

        BufferedImage bg = Resources.instance().BACKGROUND;
        int width = bg.getWidth();
        int height = bg.getHeight();

        this.paths = new RouteContainer(width,height);
        this.itemContainer = new ItemContainer(width,height);
        Dimension d = new Dimension(width,height);

        this.setMinimumSize(d);
        this.setPreferredSize(d);
        this.setMaximumSize(d);
    }

    public void undo(){
        this.history.undo();
    }

    public void paintMap(Graphics graphicsIn, boolean renderMouse){
        Graphics2D g = (Graphics2D) graphicsIn;
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        //BACKGROUND
        BufferedImage img = Resources.instance().BACKGROUND;

        if(this.getWidth() != img.getWidth() || this.getHeight() != img.getHeight()){
            this.setSize(img.getWidth(),img.getHeight());
        }

        // Background
        g.drawImage(img,0,0,null);

        // Imported/pasted map
        if(this.mapLayer != null){
            renderMap(g);
        }

        //Line layer
        g.drawImage(paths.getImage(),0,0,null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        // Icon layer
        g.drawImage(itemContainer.getImage(),0,0,null);

        // Legend (top layer)
        g.drawImage(Resources.instance().LEGEND,0,0,null);

        // Current layer.
        if(renderMouse && mouseInside){
            if(item != null) {
                BufferedImage mouseImage = item.getImage();

                //Needed to center.
                int width = mouseImage.getWidth();
                int height = mouseImage.getHeight();

                int px = Math.max(0, mx - (width / 2));
                int py = Math.max(0, my - (height / 2));

                g.drawImage(mouseImage, px, py, null);
            }else{
                g.setColor(new Color(255,255,0,180));
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
                g.setColor(new Color(255,255,0,180));
                g.fillOval(mx-5, my-5, 10, 10);
            }
        }
    }


    public void paintComponent(Graphics graphicsIn){
        paintMap(graphicsIn,true);
    }

    private void renderMap(Graphics2D g) {
        int iw = mapLayer.getWidth();
        int ih = mapLayer.getHeight();

        // Screen pixels available per image pixel.
        double ix = Constants.map_dx / (double) iw;
        double iy = Constants.map_dy / (double) ih;

        // Use the minimum so both the X and Y fit without a distorted scale.
        double scale = Math.min(ix,iy);

        int renderWidth = (int) (scale * iw);
        int renderHeight = (int) (scale * ih);

        int xo = (Constants.map_dx - renderWidth)/2;
        int yo = (Constants.map_dy - renderHeight)/2;


        int x1 = Constants.map_minX + xo;
        int y1 = Constants.map_minY + yo;

        g.drawImage(mapLayer,x1, y1,renderWidth,renderHeight,null);

    }

    private void addMouseListener(){
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(ImageRender.this.item != null)
                    return;

                RouteElement route = paths.getCurrent();
                if(route.addCoordinate(paths.getGraphics(),e.getX(),e.getY()))
                    repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mx = e.getX();
                my = e.getY();
                repaint();
            }
        });


        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() != MouseEvent.BUTTON1)
                    return;

                int x = e.getX();
                int y = e.getY();

                if(x > 1 && x < 710 && y > 0 && y < 770){
                    if(item != null && item.isPlaceable()){
                        ItemContainer.Entry entry = itemContainer.add(item,x,y);
                        if(entry != null){
                            history.add(new NodePlacementAction(ImageRender.this,itemContainer,entry));
                            repaint();
                        }
                    } else if(RenderItems.DELETE.equals(item)){
                        ItemContainer.Entry entry = itemContainer.remove(x,y);
                        if(entry != null){
                            history.add(new NodeRemovalAction(ImageRender.this,itemContainer,entry));
                            repaint();
                        }
                    }
                }}
            @Override
            public void mouseReleased(MouseEvent e) {
                RouteElement element = paths.clearCurrent();
                if(element != null)
                    history.add(new PathPlacementAction(ImageRender.this,paths,element));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                mouseInside = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseInside = false;
            }
        });
    }

    public void setItem(RenderItems item){
        if(this.item != null){
            if(this.item.equals(item))
                item = null;
        }

        this.item = item;
        this.repaint();
    }

    public void setMenu(JPopupMenu menu){
        this.setComponentPopupMenu(menu);
    }

    public void setMap(BufferedImage image){
        this.mapLayer = image;
        this.repaint();
    }

}
