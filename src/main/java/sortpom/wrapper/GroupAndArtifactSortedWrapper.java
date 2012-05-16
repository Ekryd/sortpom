package sortpom.wrapper;

import org.jdom.Content;
import org.jdom.Element;

/**
 * A wrapper that contains an element. The element is sorted according to a predetermined order.
 * 
 * @author Bjorn Ekryd
 */
public class GroupAndArtifactSortedWrapper extends SortedWrapper {
    private final String groupId;
    private final String artifactId;

    /**
     * Instantiates a new group and artifact sorted wrapper with a dependency or plugin element.
     * 
     * @param element the element
     * @param sortOrder the sort order
     */
    public GroupAndArtifactSortedWrapper(final Element element, final int sortOrder) {
        super(element, sortOrder);
        this.groupId = element.getChildText("groupId", element.getNamespace());
        this.artifactId = element.getChildText("artifactId", element.getNamespace());
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
        int compare = compareStrings(groupId, wrapper.groupId);
        if (compare != 0) {
            return compare < 0;
        }
        compare = compareStrings(artifactId, wrapper.artifactId);
        return compare < 0;
    }


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
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                '}';
    }
}
