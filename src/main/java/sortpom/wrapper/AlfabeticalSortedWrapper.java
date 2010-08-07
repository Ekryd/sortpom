package sortpom.wrapper;

import org.jdom.Content;
import org.jdom.Element;

/**
 * En wrapper som låter sitt element bli sorterat i alfabetisk ordning
 *
 * @author Bjorn
 */
public class AlfabeticalSortedWrapper implements Wrapper<Element> {
    private Element element;

    /**
     * @param element
     */
    public AlfabeticalSortedWrapper(final Element element) {
        this.element = element;
    }

    @Override
    public Element getContent() {
        return element;
    }

    @Override
    public boolean isBefore(final Wrapper<? extends Content> wrapper) {
        return wrapper instanceof AlfabeticalSortedWrapper
                && ((AlfabeticalSortedWrapper) wrapper).getContent().getName().compareTo(getContent().getName()) >= 0;
    }

    @Override
    public boolean isBiggerSortOrder(final Wrapper<? extends Content> wrapper) {
        if (wrapper == null) {
            return true;
        }
        if (!(wrapper instanceof AlfabeticalSortedWrapper)) {
            return false;
        }
        return ((AlfabeticalSortedWrapper) wrapper).getContent().getName().compareTo(getContent().getName()) <= 0;
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
