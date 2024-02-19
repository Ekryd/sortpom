package sortpom.wrapper.content;

import org.dom4j.Element;
import org.dom4j.Node;
import sortpom.parameter.DependencySortOrder;

/**
 * A wrapper that contains a dependency element. The element is sorted according to a predetermined
 * order.
 *
 * @author Bjorn Ekryd
 */
public class DependencySortedWrapper extends SortedWrapper {
  private ChildElementSorter childElementSorter = ChildElementSorter.EMPTY_SORTER;

  /**
   * Instantiates a new child element sorted wrapper with a dependency element.
   *
   * @param element the element
   * @param sortOrder the sort order
   */
  public DependencySortedWrapper(Element element, int sortOrder) {
    super(element, sortOrder);
  }

  public void setSortOrder(DependencySortOrder childElementNames) {
    var children = getContent().elements();
    this.childElementSorter = new ChildElementSorter(childElementNames, children);
  }

  @Override
  public boolean isBefore(Wrapper<? extends Node> wrapper) {
    if (wrapper instanceof DependencySortedWrapper) {
      return isBeforeDependencySortedWrapper((DependencySortedWrapper) wrapper);
    }
    return super.isBefore(wrapper);
  }

  private boolean isBeforeDependencySortedWrapper(DependencySortedWrapper wrapper) {
    // SortOrder will always be same for both DependencySortedWrapper because there is only one tag
    // under dependencies
    // that is named dependency, see sortpom.wrapper.ElementWrapperCreator.isDependencyElement.
    // So comparing getSortOrder is not needed.

    return childElementSorter.compareTo(wrapper.childElementSorter);
  }

  @Override
  public String toString() {
    return "DependencySortedWrapper{" + "childElementSorter=" + childElementSorter + '}';
  }
}
