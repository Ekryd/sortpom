package sortpom.wrapper;

import org.jdom.Element;
import sortpom.parameter.DependencySortOrder;

import java.util.*;

/**
 * @author bjorn
 * @since 2012-09-20
 */
public class ChildElementSorter {
    public static final ChildElementSorter EMPTY_SORTER = new ChildElementSorter();
    private final String[] childElementNames;
    private final String[] childElementTexts;

    public ChildElementSorter(DependencySortOrder dependencySortOrder, List<Element> children) {
        this.childElementNames = dependencySortOrder.getChildElementNames();
        this.childElementTexts = findChildElementTexts(children);
    }

    private ChildElementSorter() {
        this.childElementNames = new String[0];
        this.childElementTexts = new String[0];
    }

    private String[] findChildElementTexts(List<Element> children) {
        Map<String, Element> childrenMap = getChildrenMappedByUpperCaseName(children);
        List<String> childElementNameList = new ArrayList<String>();

        for (String childElementName : childElementNames) {
            childElementNameList.add(getChildText(childrenMap, childElementName));
        }
        
        return childElementNameList.toArray(new String[childElementNameList.size()]);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Element> getChildrenMappedByUpperCaseName(List<Element> children) {
        Map<String, Element> map = new HashMap<String, Element>();

        for (Element child : children) {
            map.put(child.getName().toUpperCase(), child);
        }

        return map;
    }

    private String getChildText(Map<String, Element> children, String childElementName) {
        Element element = children.get(childElementName.toUpperCase());
        if (element == null) {
            return "";
        }
        String text = element.getText();

        return text == null ? "" : text;
    }

    public boolean compareTo(ChildElementSorter childElementSorter) {
        if (childElementTexts.length != childElementSorter.childElementTexts.length) {
            throw new IllegalStateException(String.format("This should not happen this: %s that: %s", toString(), childElementSorter.toString()));
        }

        return compareTexts(childElementSorter.childElementTexts) < 0;
    }


    private int compareTexts(String[] otherChildElementTexts) {
        int compare;

        for (int i = 0, childElementNamesLength = childElementNames.length; i < childElementNamesLength; i++) {
            String childElementName = childElementNames[i];
            String childElementText = childElementTexts[i];
            String otherChildElementText = otherChildElementTexts[i];
            if (childElementName.equalsIgnoreCase("scope")) {
                compare = compareScope(childElementText, otherChildElementText);
            } else {
                compare = childElementText.compareToIgnoreCase(otherChildElementText);
            }

            if (compare != 0) {
                return compare;
            }
        }

        return 0;
    }

    private int compareScope(String childElementText, String otherChildElementText) {
        return Scope.getScope(childElementText).compareTo(Scope.getScope(otherChildElementText));
    }

    @Override
    public String toString() {
        return "ChildElementSorter{" +
                "childElementTexts=" + (childElementTexts == null ? null : Arrays.asList(childElementTexts)) +
                '}';
    }

    public void setEmptyPluginGroupIdValue(String value) {
        for (int i = 0, childElementNamesLength = childElementNames.length; i < childElementNamesLength; i++) {
            if ("groupId".equalsIgnoreCase(childElementNames[i]) && childElementTexts[i].isEmpty()) {
                childElementTexts[i] = value;   
            }

        }
    }

    private enum Scope {
        COMPILE, PROVIDED, SYSTEM, RUNTIME, IMPORT, TEST, OTHER;

        public static Scope getScope(String scope) {
            if (scope == null || scope.isEmpty()) {
                return COMPILE;
            }
            Scope[] values = Scope.values();
            for (Scope value : values) {
                if (scope.equalsIgnoreCase(value.name())) {
                    return value;
                }
            }
            return OTHER;
        }
    }
}
