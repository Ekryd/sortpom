package sortpom.wrapper.content;

import static sortpom.wrapper.content.Phase.compareTo;

import org.dom4j.Element;
import org.dom4j.Node;

/**
 * A wrapper that contains a execution element. The element is sorted according to: 1 no phase and
 * no id 2 no phase and id (sorted by id value) 3 standard phase and no id (sorted by Maven phase
 * order) 4 standard phase and id (sorted by Maven phase order and then id value) 5 other phase and
 * no id (sorted by phase value) 6 other phase and id (sorted by phase value and then id value)
 */
public class ExecutionSortedWrapper extends SortedWrapper {
  private final Phase phase;
  private final String id;

  /**
   * Instantiates a new child element sorted wrapper with a plugin element.
   *
   * @param element the element
   * @param sortOrder the sort order
   */
  public ExecutionSortedWrapper(Element element, int sortOrder) {
    super(element, sortOrder);
    var children = getContent().elements();

    phase =
        children.stream()
            .filter(e -> e.getName().equals("phase") && e.getText() != null)
            .map(e -> Phase.getPhase(e.getTextTrim()))
            .findFirst()
            .orElse(null);

    id =
        children.stream()
            .filter(e -> e.getName().equals("id"))
            .map(Element::getTextTrim)
            .findFirst()
            .orElse("");
  }

  @Override
  public boolean isBefore(Wrapper<? extends Node> wrapper) {
    if (wrapper instanceof ExecutionSortedWrapper) {
      return isBeforeWrapper((ExecutionSortedWrapper) wrapper);
    }
    return super.isBefore(wrapper);
  }

  private boolean isBeforeWrapper(ExecutionSortedWrapper wrapper) {
    var compare = compareTo(phase, wrapper.phase);
    if (compare != 0) {
      return compare < 0;
    }
    return id.compareTo(wrapper.id) < 0;
  }

  @Override
  public String toString() {
    return "ExecutionSortedWrapper{" + "phase=" + phase + ", id='" + id + '\'' + '}';
  }
}
