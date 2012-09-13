package sortpom.wrapper;

import org.jdom.Element;

import java.util.List;

/**
 * @author bjorn
 * @since 2012-09-06
 */
class ElementSortOrderInfo {
    private final Element element;
    private final int sortOrder;
    private Element cachedElementToMatch;
    private int cachedElementToMatchNumberOfTextMatches;

    public ElementSortOrderInfo(Element element, int sortOrder) {
        this.element = element;
        this.sortOrder = sortOrder;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public int getTextMatches(Element elementToMatch) {
        if (elementToMatch != this.cachedElementToMatch) {
            this.cachedElementToMatch = elementToMatch;
            cachedElementToMatchNumberOfTextMatches = getTextMatches(element, elementToMatch);
        }

        return cachedElementToMatchNumberOfTextMatches;
    }

    @SuppressWarnings("unchecked")
    private static int getTextMatches(Element sortOrderElement, Element elementToMatch) {
        if (elementToMatch == null) {
            return 0;
        }

        int foundTextMatches = 0;
        String textNormalize = sortOrderElement.getTextNormalize();
        if (textNormalize.isEmpty()) {
            // This is the most common case, just continue
        } else {
            if (textNormalize.equalsIgnoreCase(elementToMatch.getTextNormalize())) {
                // Found match
                foundTextMatches++;
            } else {
                // sort order text found, but was not same. Cannot ever match
                return -1;
            }
        }

        for (Element sortOrderElementChild : (List<Element>) sortOrderElement.getChildren()) {
            Element elementToMatchChild = elementToMatch.getChild(sortOrderElementChild.getName(), sortOrderElementChild.getNamespace());
            int textMatches = getTextMatches(sortOrderElementChild, elementToMatchChild);
            if (textMatches == -1) {
                // the child returned an illegal match. Then the parent cannot ever match either
                return -1;
            }
            foundTextMatches += textMatches;
        }

        return foundTextMatches;
    }

    public boolean isSameParentElement(ElementSortOrderInfo parent) {
        Element parentElement = this.element.getParentElement();
        if (parentElement == null && parent == null) {
            return true;
        }
        return parentElement == parent.element;
    }
}
