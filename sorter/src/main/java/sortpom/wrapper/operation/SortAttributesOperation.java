package sortpom.wrapper.operation;

import org.jdom.Attribute;
import org.jdom.Element;
import sortpom.wrapper.content.Wrapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Xml hierarchy operation that sort all attributes of xml elements. Used by
 * Used in HierarchyWrapper.processOperation(HierarchyWrapperOperation operation)
 * @author bjorn
 * @since 2013-11-01
 */
class SortAttributesOperation implements HierarchyWrapperOperation {
    private static final Comparator<Attribute> ATTRIBUTE_COMPARATOR = Comparator.comparing(Attribute::getName);

    /** Sort attributes of each element */
    @Override
    public void processElement(Wrapper<Element> elementWrapper) {
        Element element = elementWrapper.getContent();
        element.setAttributes(getSortedAttributes(element));
    }

    private List<Attribute> getSortedAttributes(Element element) {
        final List<Attribute> attributes = getAttributeList(element);

        attributes.forEach(Attribute::detach);

        attributes.sort(ATTRIBUTE_COMPARATOR);
        return attributes;
    }

    @SuppressWarnings("unchecked")
    private List<Attribute> getAttributeList(final Element element) {
        return new ArrayList<>(element.getAttributes());
    }

}
