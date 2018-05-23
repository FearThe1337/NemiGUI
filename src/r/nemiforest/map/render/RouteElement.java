package r.nemiforest.map.render;

import java.awt.*;
import java.util.ArrayList;


/**
 * Represents 1 path action on the map.
 */
public class RouteElement {
    static class Point{
        private final int x,y;
        private Point(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    private ArrayList<Point> positions;

    public RouteElement(){
        this.positions = new ArrayList<>();
    }

    public boolean addCoordinate(Graphics2D g, int x, int y){
        if(positions.size() == 0){
            positions.add(new Point(x, y));
            render(g,x,y,x,y);
            return  true;
        } else {
            Point last = positions.get(positions.size()-1);
            if(last.x == x && last.y == y)
                return false;

            positions.add(new Point(x, y));
            render(g,last.x,last.y,x,y);

            return true;
        }
    }

    private void render(Graphics2D g, int x1, int y1, int x2, int y2){
        g.setColor(RenderConstants.ROUTE_COLOR);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));

        int dpx = Math.max(x2 - x1, x1 - x2);
        int dpy = Math.max(y2 - y1, y1 - y2);
        int steps = Math.max(dpx,dpy);

        double dsx = (x2 - x1)/(double)steps;
        double dsy = (y2 - y1)/(double)steps;

        for(int step=0; step <= steps; step++) {
            double drawX = x1 + (step * dsx);
            double drawY = y1 + (step * dsy);

            int x = (int) Math.round(drawX);
            int y = (int) Math.round(drawY);
            g.fillOval(x-5, y-5, 10, 10);
        }
    }

    public void render(Graphics2D g){
        if(positions.size() == 0)
            return;

        if(positions.size() == 1){
            Point p = positions.get(0);
            int x = p.x;
            int y = p.y;
            render(g,x,y,x,y);
            return;
        }

        Point prev = positions.get(0);
        for(int i=1; i < positions.size(); i++){
            Point curr = positions.get(i);
            render(g,prev.x,prev.y,curr.x,curr.y);
            prev = curr;
        }
    }
}
