package sortpom.wrapper;

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
     * @param element
     * @param sortOrder
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
        if (wrapper instanceof SortedWrapper) {
            return ((SortedWrapper) wrapper).sortOrder > sortOrder;
        }
        return true;
    }

    @Override
    public boolean isBiggerSortOrder(final Wrapper<? extends Content> wrapper) {
        if (wrapper == null) {
            return true;
        }
        if (!(wrapper instanceof SortedWrapper)) {
            return false;
        }
        return ((SortedWrapper) wrapper).sortOrder <= sortOrder;
    }

    @Override
    public boolean isContentElement() {
        return true;
    }

    @Override
    public boolean isResortable() {
        return true;
    }

    int getSortOrder() {
        return sortOrder;
    }

}
