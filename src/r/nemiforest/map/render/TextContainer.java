package r.nemiforest.map.render;

import r.nemiforest.map.data.RenderItems;

import javax.xml.soap.Text;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * TextContainer is responsible for maintaining the texts added to the map.
 * This allows removal support.
 * The class is also responsible for rendering it and maintaining a image to buffer this.
 */
public class TextContainer {
    private final BufferedImage bi;
    private final ArrayList<TextElement> elements;
    private final Font font = getFont();

    public TextContainer(int width, int height){
        this.bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        this.elements = new ArrayList<>();
        render();
    }

    public BufferedImage getImage(){
        return this.bi;
    }

    public TextElement add(int x, int y, String text) {
        TextElement element = new TextElement(x,y,text);
        elements.add(element);
        render();
        return element;
    }

    public boolean remove(TextElement e) {
        if(elements.remove(e)) {
            render();
            return true;
        }
        return false;
    }

    public TextElement remove(int x, int y) {
        for(int i=elements.size()-1; i >= 0; i--){
            TextElement element = elements.get(i);
            FontMetrics metrics = bi.getGraphics().getFontMetrics(font);

            int width = metrics.stringWidth(element.getText());
            int height = metrics.getHeight();

            int minX = element.getX();
            int minY = element.getY() - height;
            int maxX = minX + width;
            int maxY = minY + height;

            if( x >= minX && x <= maxX && y >= minY && y <= maxY) {
                elements.remove(element);
                render();
                return element;
            }
        }
        return null;
    }

    private void render() {
        Graphics2D g = (Graphics2D) bi.getGraphics();

        //Clear&Trans
        g.setComposite(AlphaComposite.Clear);
        g.setColor(new Color(255, 255, 255, 255));
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        for (TextElement entry : elements) {
            renderText(g,entry);
        }
    }

    private void renderText(Graphics2D g, TextElement element) {
        renderText(g, element.getX(), element.getY(), element.getText());
    }

    public void renderText(Graphics2D g, int x, int y, String text) {
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.drawString(text, x, y);
    }

    private Font getFont() {
        Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        for (Font font1 : fonts) {
            if(font1.getName().startsWith("Arial"))
                return font1.deriveFont(24.0f).deriveFont(Font.BOLD);
        }

        return fonts[0].deriveFont(24.0f).deriveFont(Font.BOLD);
    }
}
