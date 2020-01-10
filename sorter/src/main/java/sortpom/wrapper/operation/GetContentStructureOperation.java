package sortpom.wrapper.operation;

import org.jdom.Content;
import org.jdom.Element;
import sortpom.wrapper.content.Wrapper;

/**
 * Xml hierarchy operation that returns xml content from wrappers. Used by
 * Used in HierarchyWrapper.processOperation(HierarchyWrapperOperation operation)
 * @author bjorn
 * @since 2013-11-02
 */
class GetContentStructureOperation implements HierarchyWrapperOperation {
    private Element activeElement;
    private final Element parentElement;

    /** Initial element does not have any parent */
    GetContentStructureOperation() {
        this.parentElement = null;
    }

    private GetContentStructureOperation(Element parentElement) {
        this.parentElement = parentElement;
    }

    /** Add all 'other content' to the parent xml element */
    @Override
    public void processOtherContent(Wrapper<Content> content) {
        if (parentElement != null) {
            parentElement.addContent(content.getContent());
        }
    }

    /** Add the element to its parent */
    @Override
    public void processElement(Wrapper<Element> element) {
        activeElement = element.getContent();

        if (parentElement != null) {
            parentElement.addContent(activeElement);
        }
    }

    /** The sub operation gets active element as parent element */
    @Override
    public HierarchyWrapperOperation createSubOperation() {
        return new GetContentStructureOperation(activeElement);
    }
}
