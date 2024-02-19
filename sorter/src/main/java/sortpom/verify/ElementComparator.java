package sortpom.verify;

import java.util.List;
import org.dom4j.Element;
import sortpom.util.XmlOrderedResult;

/**
 * @author bjorn
 * @since 2012-07-01
 */
public class ElementComparator {
  private final Element originalElement;
  private final Element newElement;

  public ElementComparator(Element originalElement, Element newElement) {
    this.originalElement = originalElement;
    this.newElement = newElement;
  }

  public XmlOrderedResult isElementOrdered() {
    if (!originalElement.getName().equals(newElement.getName())) {
      return XmlOrderedResult.nameDiffers(originalElement.getName(), newElement.getName());
    }
    if (isEqualsIgnoringWhitespace()) {
      return XmlOrderedResult.textContentDiffers(
          originalElement.getName(), originalElement.getText(), newElement.getText());
    }
    return isChildrenOrdered(
        originalElement.getName(), originalElement.elements(), newElement.elements());
  }

  private boolean isEqualsIgnoringWhitespace() {
    return !originalElement
        .getText()
        .replaceAll("\\s", "")
        .equals(newElement.getText().replaceAll("\\s", ""));
  }

  private XmlOrderedResult isChildrenOrdered(
      String name, List<Element> originalElementChildren, List<Element> newElementChildren) {
    var size = Math.min(originalElementChildren.size(), newElementChildren.size());
    for (var i = 0; i < size; i++) {
      var elementComparator =
          new ElementComparator(originalElementChildren.get(i), newElementChildren.get(i));
      var elementOrdered = elementComparator.isElementOrdered();
      if (!elementOrdered.isOrdered()) {
        return elementOrdered;
      }
    }
    if (originalElementChildren.size() != newElementChildren.size()) {
      return XmlOrderedResult.childElementDiffers(
          name, originalElementChildren.size(), newElementChildren.size());
    }
    return XmlOrderedResult.ordered();
  }
}
