package r.nemiforest.map.history;

import r.nemiforest.map.render.ImageRender;
import r.nemiforest.map.render.RouteContainer;
import r.nemiforest.map.render.RouteElement;

/**
 * History class for undo support.
 */
public class PathPlacementAction implements HistoryElement{
    private final ImageRender renderer;
    private final RouteContainer container;
    private final RouteElement element;

    public PathPlacementAction(ImageRender renderer, RouteContainer container, RouteElement element){
        this.renderer = renderer;
        this.container = container;
        this.element = element;
    }

    @Override
    public boolean undo() {
        boolean success = this.container.remove(element);
        this.renderer.repaint();
        return success;
    }
}
