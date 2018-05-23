package r.nemiforest.map.history;

import r.nemiforest.map.render.ImageRender;
import r.nemiforest.map.render.ItemContainer;
import r.nemiforest.map.render.TextContainer;
import r.nemiforest.map.render.TextElement;

import java.util.List;

/**
 * History class for undo support.
 */
public class TextPlacementAction implements HistoryElement{
    private final ImageRender renderer;
    private final TextContainer list;
    private final TextElement textElement;

    public TextPlacementAction(ImageRender renderer, TextContainer textContainer, TextElement element) {
        this.renderer = renderer;
        this.list = textContainer;
        this.textElement = element;
    }

    @Override
    public boolean undo() {
        boolean success = this.list.remove(textElement);
        this.renderer.repaint();
        return success;
    }
}
