package r.nemiforest.map.history;

import r.nemiforest.map.render.ImageRender;
import r.nemiforest.map.render.ItemContainer;

/**
 * History class for undo support.
 */
public class NodeRemovalAction implements HistoryElement{
    private final ImageRender renderer;
    private final ItemContainer container;
    private final ItemContainer.Entry element;

    public NodeRemovalAction(ImageRender renderer, ItemContainer container, ItemContainer.Entry element){
        this.renderer = renderer;
        this.container = container;
        this.element = element;
    }

    @Override
    public boolean undo() {
        this.container.add(element);
        this.renderer.repaint();
        return true;
    }
}
