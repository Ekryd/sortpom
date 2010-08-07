package sortpom.wrapper;

import org.jdom.Content;
import org.jdom.Element;

/**
 * En wrapper som låter sitt element bli sorterat i enligt en förutbestämd angiven ordning
 *
 * @author Bjorn
 */
public class SortedWrapper implements Wrapper<Element> {
    private final int sortOrder;
    private Element element;

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

        } else {
            return true;
        }
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

}
