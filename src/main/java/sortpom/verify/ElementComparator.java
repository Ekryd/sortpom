package sortpom.verify;

import org.jdom.Element;

import java.util.List;

/**
 * @author bjorn
 * @since 2012-07-01
 */
public class ElementComparator {
    private final Element originalElement;
    private final Element newElement;

    public ElementComparator(Element originalElement, Element newElement) {
        this.originalElement = originalElement;
        this.newElement = newElement;
    }

    private ElementComparator(Object originalElement, Object newElement) {
        this.originalElement = (Element) originalElement;
        this.newElement = (Element) newElement;
    }

    public boolean isElementOrdered() {
        if (!originalElement.getName().equals(newElement.getName())) {
            return false;
        }
        return isChildrenOrdered(originalElement.getChildren(), newElement.getChildren());
    }

    private boolean isChildrenOrdered(List originalElementChildren, List newElementChildren) {
        int size = originalElementChildren.size();
        if (size != newElementChildren.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            ElementComparator elementComparator = new ElementComparator(originalElementChildren.get(i), newElementChildren.get(i));
            if (!elementComparator.isElementOrdered()) {
                return false;
            }
        }
        return true;
    }


}
