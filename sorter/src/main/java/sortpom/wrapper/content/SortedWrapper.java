package sortpom.wrapper.content;

import org.jdom.Content;
import org.jdom.Element;

/**
 * A wrapper that contains an element. The element is sorted according to a predetermined order.
 *
 * @author Bjorn Ekryd
 */
public class SortedWrapper implements Wrapper<Element> {
    private final int sortOrder;
    private final Element element;

    /**
     * @param element   The wrapped element
     * @param sortOrder The sort order, lower value is placed higher up
     */
    public SortedWrapper(final Element element, final int sortOrder) {
        this.element = element;
        this.sortOrder = sortOrder;
    }

    @Override
    public Element getContent() {
        return element;
    }

    @Override
    public boolean isBefore(final Wrapper<? extends Content> wrapper) {
        return !(wrapper instanceof SortedWrapper) || isBeforeSortedWrapper((SortedWrapper) wrapper);
    }

    private boolean isBeforeSortedWrapper(SortedWrapper wrapper) {
        return wrapper.sortOrder > sortOrder;
    }

    @Override
    public String toString() {
        return "SortedWrapper{" +
                "element=" + element +
                '}';
    }
}
