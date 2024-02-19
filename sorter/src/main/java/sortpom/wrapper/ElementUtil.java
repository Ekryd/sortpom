package sortpom.wrapper;

import org.dom4j.Element;

/**
 * Contains utility methods for Xml elements
 *
 * @author bjorn
 * @since 2013-10-21
 */
class ElementUtil {
  /** Hidden constructor */
  private ElementUtil() {}

  /** Returns fully qualified name for an Xml element. */
  static String getDeepName(Element element) {
    if (element == null) {
      return "";
    }
    return getDeepName(element.getParent()) + '/' + element.getName();
  }

  /** Returns true if an elements parents name is same as argument */
  static boolean isElementParentName(Element element, String name) {
    var parent = element.getParent();
    if (parent == null) {
      return false;
    }
    return isElementName(parent, name);
  }

  /** Returns true if an elements name is same as argument */
  static boolean isElementName(Element element, String name) {
    return element.getName().equals(name);
  }
}
