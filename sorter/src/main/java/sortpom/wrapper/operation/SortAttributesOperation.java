package sortpom.wrapper.operation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Element;
import sortpom.wrapper.content.Wrapper;

/**
 * Xml hierarchy operation that sort all attributes of xml elements. Used by Used in
 * HierarchyWrapper.processOperation(HierarchyWrapperOperation operation)
 *
 * @author bjorn
 * @since 2013-11-01
 */
class SortAttributesOperation implements HierarchyWrapperOperation {
  private static final Comparator<Attribute> ATTRIBUTE_COMPARATOR =
      Comparator.comparing(Attribute::getName);

  /** Sort attributes of each element */
  @Override
  public void processElement(Wrapper<Element> elementWrapper) {
    var element = elementWrapper.getContent();
    element.setAttributes(getSortedAttributes(element));
  }

  private List<Attribute> getSortedAttributes(Element element) {
    var attributes = new ArrayList<>(element.attributes());

    attributes.forEach(Attribute::detach);

    attributes.sort(ATTRIBUTE_COMPARATOR);
    return attributes;
  }
}
