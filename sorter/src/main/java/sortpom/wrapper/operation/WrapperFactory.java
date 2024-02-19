package sortpom.wrapper.operation;

import org.dom4j.Element;
import org.dom4j.Node;
import sortpom.wrapper.content.Wrapper;

/**
 * Creates wrappers around xml fragments.
 *
 * @author Bjorn Ekryd
 */
public interface WrapperFactory {

  /** Creates wrapper around a root element. */
  HierarchyRootWrapper createFromRootElement(Element rootElement);

  /** Creates wrapper around xml content. */
  <T extends Node> Wrapper<T> create(T content);
}
