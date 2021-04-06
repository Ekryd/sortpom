package sortpom.wrapper.content;

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

    @SuppressWarnings("unchecked")
    public void setSortOrder(DependencySortOrder dependencySortOrder) {
        List<Element> children = getContent().getChildren();
        this.childElementSorter = new ChildElementSorter(dependencySortOrder, children);
        childElementSorter.emptyGroupIdIsFilledWithDefaultMavenGroupId();
    }

    @Override
    public boolean isBefore(final Wrapper<? extends Content> wrapper) {
        if (wrapper instanceof PluginSortedWrapper) {
            return isBeforePluginSortedWrapper((PluginSortedWrapper) wrapper);
        }
        return super.isBefore(wrapper);
    }

    private boolean isBeforePluginSortedWrapper(final PluginSortedWrapper wrapper) {
        // SortOrder will always be same for both PluginSortedWrapper because there is only one tag under plugins
        // that is named plugin, see sortpom.wrapper.ElementWrapperCreator.isPluginElement.
        // So comparing getSortOrder is not needed.

        return childElementSorter.compareTo(wrapper.childElementSorter);
    }

    @Override
    public String toString() {
        return "PluginSortedWrapper{" +
                "childElementSorter=" + childElementSorter +
                '}';
    }
}
