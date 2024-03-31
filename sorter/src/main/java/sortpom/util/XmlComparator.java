package sortpom.util;

import java.util.List;
import org.dom4j.Element;
import sortpom.verify.ElementComparator;

public class XmlComparator {
    public static XmlOrderedResult isChildrenOrdered(
            String name, List<Element> originalElementChildren, List<Element> newElementChildren) {
        var size = Math.min(originalElementChildren.size(), newElementChildren.size());
        for (var i = 0; i < size; i++) {
            var elementComparator =
                    new ElementComparator(originalElementChildren.get(i), newElementChildren.get(i));
            var elementOrdered = elementComparator.isElementOrdered();
            if (!elementOrdered.isOrdered()) {
                return elementOrdered;
            }
        }
        if (originalElementChildren.size() != newElementChildren.size()) {
            return XmlOrderedResult.childElementDiffers(
                    name, originalElementChildren.size(), newElementChildren.size());
        }
        return XmlOrderedResult.ordered();
    }
}