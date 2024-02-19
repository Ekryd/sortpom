package sortpom.wrapper.content;

import org.dom4j.Element;
import org.dom4j.Node;

/**
 * En wrapper lets its element be sorted in alphabetical order
 *
 * @author Bjorn
 */
public class AlphabeticalSortedWrapper implements Wrapper<Element> {
  private final Element element;

  public AlphabeticalSortedWrapper(Element element) {
    this.element = element;
  }

  @Override
  public Element getContent() {
    return element;
  }

  @Override
  public boolean isBefore(Wrapper<? extends Node> wrapper) {
    return wrapper instanceof AlphabeticalSortedWrapper
        && isBeforeAlphabeticalSortedWrapper((AlphabeticalSortedWrapper) wrapper);
  }

  private boolean isBeforeAlphabeticalSortedWrapper(AlphabeticalSortedWrapper wrapper) {
    return wrapper.getContent().getName().compareTo(getContent().getName()) > 0;
  }

  @Override
  public String toString() {
    return "AlphabeticalSortedWrapper{"
        + "element="
        + (element == null ? "null" : element.toString().replaceAll(".+@[^ ]+ ", ""))
        + '}';
  }
}
