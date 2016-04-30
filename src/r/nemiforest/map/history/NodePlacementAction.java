package r.nemiforest.map.history;

import r.nemiforest.map.render.ImageRender;
import r.nemiforest.map.render.ItemContainer;
import r.nemiforest.map.render.RouteContainer;
import r.nemiforest.map.render.RouteElement;

/**
 * History class for undo support.
 */
public class NodePlacementAction implements HistoryElement{
    private final ImageRender renderer;
    private final ItemContainer container;
    private final ItemContainer.Entry element;

    public NodePlacementAction(ImageRender renderer, ItemContainer container, ItemContainer.Entry element){
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
