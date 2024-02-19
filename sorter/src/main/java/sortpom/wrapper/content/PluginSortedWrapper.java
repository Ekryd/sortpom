package sortpom.wrapper.content;

import org.dom4j.Element;
import org.dom4j.Node;
import sortpom.parameter.DependencySortOrder;

/**
 * A wrapper that contains a plugin element. The element is sorted according to a predetermined
 * order.
 *
 * @author Bjorn Ekryd
 */
public class PluginSortedWrapper extends SortedWrapper {
  private ChildElementSorter childElementSorter = ChildElementSorter.EMPTY_SORTER;

  /**
   * Instantiates a new child element sorted wrapper with a plugin element.
   *
   * @param element the element
   * @param sortOrder the sort order
   */
  public PluginSortedWrapper(Element element, int sortOrder) {
    super(element, sortOrder);
  }

  public void setSortOrder(DependencySortOrder dependencySortOrder) {
    var children = getContent().elements();
    this.childElementSorter = new ChildElementSorter(dependencySortOrder, children);
    childElementSorter.emptyGroupIdIsFilledWithDefaultMavenGroupId();
  }

  @Override
  public boolean isBefore(Wrapper<? extends Node> wrapper) {
    if (wrapper instanceof PluginSortedWrapper) {
      return isBeforePluginSortedWrapper((PluginSortedWrapper) wrapper);
    }
    return super.isBefore(wrapper);
  }

  private boolean isBeforePluginSortedWrapper(PluginSortedWrapper wrapper) {
    // SortOrder will always be same for both PluginSortedWrapper because there is only one tag
    // under plugins
    // that is named plugin, see sortpom.wrapper.ElementWrapperCreator.isPluginElement.
    // So comparing getSortOrder is not needed.

    return childElementSorter.compareTo(wrapper.childElementSorter);
  }

  @Override
  public String toString() {
    return "PluginSortedWrapper{" + "childElementSorter=" + childElementSorter + '}';
  }
}
