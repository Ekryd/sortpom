package sortpom.wrapper;

import static sortpom.wrapper.ElementUtil.getDeepName;

import java.util.HashMap;
import java.util.Map;
import org.dom4j.Element;

/**
 * All elements from the chosen sort order (from predefined sort order or custom sort order) are
 * placed in this map along with an index that describes in which order the elements should be
 * sorted.
 */
class ElementSortOrderMap {
  /** Contains sort order element names and their index. */
  private final Map<String, Integer> elementNameSortOrderMap = new HashMap<>();

  /**
   * Add an Xml element to the map
   *
   * @param element Xml element
   * @param sortOrder an index describing the sort order (lower number == element towards the start
   *     of the file)
   */
  public void addElement(Element element, int sortOrder) {
    var deepName = getDeepName(element);
    elementNameSortOrderMap.put(deepName, sortOrder);
  }

  /** Returns true if element is in the map */
  public boolean containsElement(Element element) {
    var deepName = getDeepName(element);
    return elementNameSortOrderMap.containsKey(deepName);
  }

  /**
   * Gets the index describing the sort order (lower number == element towards the start of the
   * file)
   */
  public int getSortOrder(Element element) {
    var deepName = getDeepName(element);
    return elementNameSortOrderMap.get(deepName);
  }
}
