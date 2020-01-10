package sortpom.wrapper.operation;

import org.jdom.Content;
import org.jdom.Element;
import sortpom.wrapper.content.Wrapper;

/**
 * Xml hierarchy operation that detaches a xml child from its parent. Used by
 * Used in HierarchyWrapper.processOperation(HierarchyWrapperOperation operation)
 *
 * @author bjorn
 * @since 2013-11-01
 */
class DetachOperation implements HierarchyWrapperOperation {
    /** Detach each 'other content' */
    @Override
    public void processOtherContent(Wrapper<Content> contentWrapper) {
        contentWrapper.getContent().detach();
    }

    /** Detach each xml element */
    @Override
    public void processElement(Wrapper<Element> elementWrapper) {
        Element content = elementWrapper.getContent();
        content.detach();
        content.removeContent();
    }

}
