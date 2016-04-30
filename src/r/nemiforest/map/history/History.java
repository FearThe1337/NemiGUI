package r.nemiforest.map.history;

import java.util.LinkedList;

/**
 * Extremely simple class for basic UNDO support.
 */
public class History {
    private LinkedList<HistoryElement> elements;

    public History(){
        elements = new LinkedList<>();
    }

    public void undo(){
        if(elements.size() > 0)
            elements.removeLast().undo();
    }

    public void add(HistoryElement e){
        elements.addLast(e);
    }
}
