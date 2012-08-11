package sortpom.verify;

import org.jdom.Element;
import sortpom.util.XmlOrderedResult;

import java.util.List;

/**
 * @author bjorn
 * @since 2012-07-01
 */
public class ElementComparator {
    private final Element originalElement;
    private final Element newElement;
    private XmlOrderedResult orderedResult;

    public ElementComparator(Element originalElement, Element newElement) {
        this.originalElement = originalElement;
        this.newElement = newElement;
    }

    private ElementComparator(Object originalElement, Object newElement) {
        this.originalElement = (Element) originalElement;
        this.newElement = (Element) newElement;
    }

    public XmlOrderedResult isElementOrdered() {
        if (!originalElement.getName().equals(newElement.getName())) {
            return XmlOrderedResult.notOrdered(originalElement.getName(), newElement.getName());
        } else {
            return isChildrenOrdered(originalElement.getName(), originalElement.getChildren(), newElement.getChildren());
        }
    }

    private XmlOrderedResult isChildrenOrdered(String name, List originalElementChildren, List newElementChildren) {
        int size = originalElementChildren.size();
        if (size != newElementChildren.size()) {
            throw new IllegalStateException(String.format("Somehow the element %s has different number of children", name));
        }
        for (int i = 0; i < size; i++) {
            ElementComparator elementComparator = new ElementComparator(originalElementChildren.get(i), newElementChildren.get(i));
            XmlOrderedResult elementOrdered = elementComparator.isElementOrdered();
            if (!elementOrdered.isOrdered()) {
                return elementOrdered;
            }
        }
        return XmlOrderedResult.ordered();
    }
}
