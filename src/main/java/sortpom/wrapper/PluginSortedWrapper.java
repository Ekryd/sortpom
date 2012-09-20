package sortpom.wrapper;

import org.jdom.Content;
import org.jdom.Element;
import sortpom.parameter.DependencySortOrder;

import java.util.List;

/**
 * A wrapper that contains a plugin element. The element is sorted according to a predetermined order.
 *
 * @author Bjorn Ekryd
 */
public class PluginSortedWrapper extends SortedWrapper {
    private static final String EMPTY_PLUGIN_GROUP_ID_VALUE = "org.apache.maven.plugins";
    private ChildElementSorter childElementSorter = ChildElementSorter.EMPTY_SORTER;

    /**
     * Instantiates a new child element sorted wrapper with a plugin element.
     *
     * @param element   the element
     * @param sortOrder the sort order
     */
    public PluginSortedWrapper(final Element element, final int sortOrder) {
        super(element, sortOrder);
    }

    public void setSortOrder(DependencySortOrder dependencySortOrder) {
        List<Element> children = (List<Element>) getContent().getChildren();
        this.childElementSorter = new ChildElementSorter(dependencySortOrder, children);
        childElementSorter.setEmptyPluginGroupIdValue(EMPTY_PLUGIN_GROUP_ID_VALUE);
    }

    @Override
    public boolean isBefore(final Wrapper<? extends Content> wrapper) {
        if (wrapper instanceof PluginSortedWrapper) {
            return isBeforePluginSortedWrapper((PluginSortedWrapper) wrapper);
        }
        return super.isBefore(wrapper);
    }

    private boolean isBeforePluginSortedWrapper(final PluginSortedWrapper wrapper) {
        // Sort order rules before sorting by artifactId, groupId etc.
        if (wrapper.getSortOrder() != getSortOrder()) {
            return super.isBefore(wrapper);
        }

        return childElementSorter.compareTo(wrapper.childElementSorter);
    }

    @Override
    public String toString() {
        return "PluginSortedWrapper{" +
                "childElementSorter=" + childElementSorter +
                '}';
    }
}
