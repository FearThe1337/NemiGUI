package r.nemiforest.map.render;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * ItemContainer is responsible for maintaining the routes added to the map.
 * It is also responsible for rendering it and maintaining a image to buffer this.
 */
public class RouteContainer {
    private BufferedImage bi;
    private ArrayList<RouteElement> routes;
    private RouteElement current;

    public RouteContainer(int width, int height){
        this.bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        this.routes = new ArrayList<>();
        this.current = null;
        render();
    }

    public void render(){
        Graphics2D g = (Graphics2D) bi.getGraphics();

        //Clear&Trans
        g.setComposite(AlphaComposite.Clear);
        g.setColor(new Color(255,255,255,255));
        g.fillRect(0,0,bi.getWidth(),bi.getHeight());
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        //Render
        for(RouteElement e : routes){
            e.render(g);
        }
    }

    public Image getImage() {
        return bi;
    }

    public RouteElement getCurrent(){
        if(current != null)
            return current;

        current = new RouteElement();
        routes.add(current);
        return current;
    }

    public RouteElement clearCurrent(){
        RouteElement e = this.current;
        this.current = null;
        return e;
    }

    public Graphics2D getGraphics() {
        return (Graphics2D) bi.getGraphics();
    }

    public boolean remove(RouteElement element) {
        boolean res = routes.remove(element);
        render();
        return res;
    }
}
