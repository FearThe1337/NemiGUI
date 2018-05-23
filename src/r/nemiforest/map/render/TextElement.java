package r.nemiforest.map.render;

public class TextElement {
    private final int x,y;
    private final String text;

    public TextElement(int x, int y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getText() {
        return text;
    }
}
