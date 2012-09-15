package sortpom.wrapper;

import org.jdom.Content;
import org.jdom.Element;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A wrapper that contains an element. The element is sorted according to a predetermined order.
 *
 * @author Bjorn Ekryd
 */
public class GroupAndArtifactSortedWrapper extends SortedWrapper {
    private String[] childElementNames;
    private String[] childElementTexts = new String[0];

    /**
     * Instantiates a new group and artifact sorted wrapper with a dependency or plugin element.
     *
     * @param element   the element
     * @param sortOrder the sort order
     */
    public GroupAndArtifactSortedWrapper(final Element element, final int sortOrder) {
        super(element, sortOrder);
//        this.groupId = element.getChildText("groupId", element.getNamespace());
//        this.artifactId = element.getChildText("artifactId", element.getNamespace());
//        this.scope = Scope.getScope(element.getChildText("scope", element.getNamespace()));
    }

    public void setSortOrder(String[] childElementNames) {
        this.childElementNames = childElementNames;
        Map<String, Element> children = getChildrenMappedByUpperCaseName(getContent());
        childElementTexts = new String[childElementNames.length];

        for (int i = 0, childElementNamesLength = childElementNames.length; i < childElementNamesLength; i++) {
            String childElementName = childElementNames[i];
            childElementTexts[i] = getChildText(children, childElementName);
        }
    }

    private Map<String, Element> getChildrenMappedByUpperCaseName(Element content) {
        Map<String, Element> map = new HashMap<String, Element>();

        List<Element> children = content.getChildren();
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

    @Override
    public boolean isBefore(final Wrapper<? extends Content> wrapper) {
        if (wrapper instanceof GroupAndArtifactSortedWrapper) {
            return isBeforeGroupAndArtifactSortedWrapper((GroupAndArtifactSortedWrapper) wrapper);
        }
        return super.isBefore(wrapper);
    }

    private boolean isBeforeGroupAndArtifactSortedWrapper(final GroupAndArtifactSortedWrapper wrapper) {
        // Continue if sort order are equal
        if (wrapper.getSortOrder() != getSortOrder()) {
            return super.isBefore(wrapper);
        }

        if (childElementTexts.length != wrapper.childElementTexts.length) {
            throw new IllegalStateException(String.format("This should not happen this: %s that: %s", toString(), wrapper.toString()));
        }

        return compareTexts(wrapper.childElementTexts) < 0;
//        int compare = 0;
//        if (sortByScope) {
//            compare = scope.compareTo(wrapper.scope);
//        }
//        if (compare == 0 && sortByName) {
//            compare = compareByName(wrapper.groupId, wrapper.artifactId);
//        }

//        return compare < 0;
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

//    private int compareByName(String otherGroupId, String otherArtifactId) {
//        int compare = compareStrings(groupId, otherGroupId);
//        if (compare != 0) {
//            return compare;
//        }
//        return compareStrings(artifactId, otherArtifactId);
//    }


    private int compareStrings(final String s1, final String s2) {
        if (s1 == null) {
            return -1;
        }
        if (s2 == null) {
            return 1;
        }
        return s1.compareToIgnoreCase(s2);
    }

    @Override
    public String toString() {
        return "GroupAndArtifactSortedWrapper{" +
                "childElementTexts=" + (childElementTexts == null ? null : Arrays.asList(childElementTexts)) +
                '}';
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
