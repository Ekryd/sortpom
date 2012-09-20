package sortpom.wrapper;

import org.jdom.Content;
import org.jdom.Element;
import sortpom.parameter.DependencySortOrder;

import java.util.List;

/**
 * A wrapper that contains a dependency element. The element is sorted according to a predetermined order.
 *
 * @author Bjorn Ekryd
 */
public class DependencySortedWrapper extends SortedWrapper {
    private ChildElementSorter childElementSorter = ChildElementSorter.EMPTY_SORTER;

    /**
     * Instantiates a new child element sorted wrapper with a dependency element.
     *
     * @param element   the element
     * @param sortOrder the sort order
     */
    public DependencySortedWrapper(final Element element, final int sortOrder) {
        super(element, sortOrder);
    }

    public void setSortOrder(DependencySortOrder childElementNames) {
        List<Element> children = (List<Element>) getContent().getChildren();
        this.childElementSorter = new ChildElementSorter(childElementNames, children);
    }

    @Override
    public boolean isBefore(final Wrapper<? extends Content> wrapper) {
        if (wrapper instanceof DependencySortedWrapper) {
            return isBeforeDependencySortedWrapper((DependencySortedWrapper) wrapper);
        }
        return super.isBefore(wrapper);
    }

    private boolean isBeforeDependencySortedWrapper(final DependencySortedWrapper wrapper) {
        // Sort order rules before sorting by scope, groupId etc.
        if (wrapper.getSortOrder() != getSortOrder()) {
            return super.isBefore(wrapper);
        }

        return childElementSorter.compareTo(wrapper.childElementSorter);
    }

    @Override
    public String toString() {
        return "DependencySortedWrapper{" +
                "childElementSorter=" + childElementSorter +
                '}';
    }

}
