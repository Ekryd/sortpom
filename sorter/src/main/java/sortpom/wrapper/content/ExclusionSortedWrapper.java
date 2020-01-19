package sortpom.wrapper.content;

import org.jdom.Content;
import org.jdom.Element;
import sortpom.parameter.DependencySortOrder;

import java.util.List;

/**
 * A wrapper that contains a exclusion element. The element is sorted according to a predetermined order.
 *
 * @author Bjorn Ekryd
 */
public class ExclusionSortedWrapper extends SortedWrapper {
    private ChildElementSorter childElementSorter = ChildElementSorter.EMPTY_SORTER;

    /**
     * Instantiates a new child element sorted wrapper with a exclusion element.
     *
     * @param element   the element
     * @param sortOrder the sort order
     */
    public ExclusionSortedWrapper(final Element element, final int sortOrder) {
        super(element, sortOrder);
    }

    @SuppressWarnings("unchecked")
    public void setSortOrder(DependencySortOrder childElementNames) {
        List<Element> children = getContent().getChildren();
        this.childElementSorter = new ChildElementSorter(childElementNames, children);
    }

    @Override
    public boolean isBefore(final Wrapper<? extends Content> wrapper) {
        if (wrapper instanceof ExclusionSortedWrapper) {
            return isBeforeExclusionSortedWrapper((ExclusionSortedWrapper) wrapper);
        }
        return super.isBefore(wrapper);
    }

    private boolean isBeforeExclusionSortedWrapper(final ExclusionSortedWrapper wrapper) {
        // SortOrder will always be same for both ExclusionSortedWrapper because there is only one tag under exclusions
        // that is named exclusion, see sortpom.wrapper.ElementWrapperCreator.isExclusionElement.
        // So comparing getSortOrder is not needed.

        return childElementSorter.compareTo(wrapper.childElementSorter);
    }

    @Override
    public String toString() {
        return "ExclusionSortedWrapper{" +
                "childElementSorter=" + childElementSorter +
                '}';
    }

}
