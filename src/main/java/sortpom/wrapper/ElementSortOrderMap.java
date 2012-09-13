package sortpom.wrapper;

import org.jdom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ElementSortOrderMap {
    /** Contains sort order element names and their index. */
    private final Map<String, List<ElementSortOrderInfo>> elementNameSortOrderMap = new HashMap<String, List<ElementSortOrderInfo>>();

    public ElementSortOrderMap() {
    }

    public void addElement(Element element, int sortOrder) {
        final String deepName = getDeepName(element);
        List<ElementSortOrderInfo> foundInfo = elementNameSortOrderMap.get(deepName);

        if (foundInfo == null) {
            foundInfo = new ArrayList<ElementSortOrderInfo>();
            elementNameSortOrderMap.put(deepName, foundInfo);
        } 
        foundInfo.add(new ElementSortOrderInfo(element, sortOrder));
    }

    public boolean containsElement(Element element) {
        String deepName = getDeepName(element);
        return elementNameSortOrderMap.containsKey(deepName);
    }

    public int getSortOrder(Element element) {
        return getSortOrderInfo(element).getSortOrder();
    }

    private ElementSortOrderInfo getSortOrderInfo(Element element) {
        if (element == null)
            return null;

        List<ElementSortOrderInfo> elementInfos = elementNameSortOrderMap.get(getDeepName(element));
        
        
        if (elementInfos.size() == 1) {
            return elementInfos.get(0);
        } else {
            return detectMostLikelyCandidate(element, elementInfos);
        }
    }
    
    private ElementSortOrderInfo detectMostLikelyCandidate(Element element, List<ElementSortOrderInfo> elementInfos) {
        ElementSortOrderInfo parent = getSortOrderInfo(element.getParentElement());
        
        ElementSortOrderInfo foundElement = null;
        for (ElementSortOrderInfo elementInfo : elementInfos) {
            if (elementInfo.isSameParentElement(parent)) {
                if (foundElement == null) {
                    foundElement = elementInfo;
                } else if (elementInfo.getTextMatches(element) > foundElement.getTextMatches(element)) {
                    foundElement = elementInfo;
                }
            }
        }
        return foundElement;
    }


    public String getDeepName(final Element element) {
        if (element == null) {
            return "";
        }
        return getDeepName(element.getParentElement()) + '/' + element.getName();
    }

}
