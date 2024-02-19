package sortpom.wrapper.operation;

import java.util.List;
import org.dom4j.Element;
import sortpom.wrapper.content.Wrapper;

/**
 * Xml hierarchy operation that all xml elements. Used by Used in
 * HierarchyWrapper.processOperation(HierarchyWrapperOperation operation)
 *
 * @author bjorn
 * @since 2013-11-01
 */
class SortChildrenOperation implements HierarchyWrapperOperation {

  /** Sort all children of an element */
  @Override
  public void manipulateChildElements(List<HierarchyWrapper> children) {
    for (var i = 0; i < children.size(); i++) {
      var wrapperImpl = children.get(i);
      var wrapper = wrapperImpl.getElementContent();

      if (wrapper != null && wrapper.isSortable()) {
        insertChildInSortedOrder(children, i, wrapperImpl, wrapper);
      }
    }
  }

  private void insertChildInSortedOrder(
      List<HierarchyWrapper> children,
      int indexOfChild,
      HierarchyWrapper wrapperImpl,
      Wrapper<Element> wrapper) {
    for (var j = 0; j < indexOfChild; j++) {
      if (wrapper.isBefore(children.get(j).getElementContent())) {
        children.remove(indexOfChild);
        children.add(j, wrapperImpl);
        return;
      }
    }
  }
}
