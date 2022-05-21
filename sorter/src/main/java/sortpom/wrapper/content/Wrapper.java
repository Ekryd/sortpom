package sortpom.wrapper.content;

import org.dom4j.Node;

/**
 * A wrapping of an xml fragment. The fragment might be an element or a comment etc.
 *
 * @param <T> *
 * @author Bjorn Ekryd
 */
public interface Wrapper<T extends Node> {

  /**
   * Gets the wrapped content.
   *
   * @return the content
   */
  T getContent();

  /**
   * Checks if wrapper should be placed before another wrapper.
   *
   * @param wrapper the wrapper
   * @return true, if is before
   */
  boolean isBefore(Wrapper<? extends Node> wrapper);

  /**
   * Checks if is content is of type Element. Default behaviour is that it contains an element.
   *
   * @return true, if is content element
   */
  default boolean isContentElement() {
    return true;
  }

  /**
   * Checks if wrapper should be sorted. Default behaviour is that it that the Wrapper is sortable.
   *
   * @return true, if is sortable
   */
  default boolean isSortable() {
    return true;
  }

  /**
   * Output debug-friendly string
   *
   * @param indent The indentation indicates nested elements
   * @return The debug string
   */
  default String toString(String indent) {
    return indent + this;
  }
}
