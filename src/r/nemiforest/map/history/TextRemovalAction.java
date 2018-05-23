package r.nemiforest.map.history;

import r.nemiforest.map.render.ImageRender;
import r.nemiforest.map.render.TextContainer;
import r.nemiforest.map.render.TextElement;

/**
 * History class for undo support.
 */
public class TextRemovalAction implements HistoryElement{
    private final ImageRender renderer;
    private final TextContainer list;
    private final TextElement textElement;

    public TextRemovalAction(ImageRender renderer, TextContainer textContainer, TextElement element) {
        this.renderer = renderer;
        this.list = textContainer;
        this.textElement = element;
    }

    @Override
    public boolean undo() {
        this.list.add(textElement.getX(), textElement.getY(), textElement.getText());
        this.renderer.repaint();
        return true;
    }
}
