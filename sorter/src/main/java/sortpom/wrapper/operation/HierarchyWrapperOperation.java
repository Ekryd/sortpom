package sortpom.wrapper.operation;

import org.jdom.Content;
import org.jdom.Element;
import sortpom.wrapper.content.Wrapper;

import java.util.List;

/**
 * This class gives a default implementation of an operation that traverse the xml hierarchy.
 * The operation executes on each xml element recursively 
 * @author bjorn
 * @since 2013-11-01
 */
public abstract class HierarchyWrapperOperation {
    /** Override this if the operation wants to do something before each element has been processed */
    public void startOfProcess() {
    }

    /** Override this if the operation wants to do something with each 'other content' that
     * belongs to the element being processed
     * @param content such as newlines and comments
     */
    public void processOtherContent(Wrapper<Content> content) {
    }

    /** Override this if the operation wants to do something with the actual element being processed */
    public void processElement(Wrapper<Element> element) {
    }

    /** Override this if the operation want to manipulate the child elements of element being processed */
    public void manipulateChildElements(List<HierarchyWrapper> children) {
    }

    /** Override this if the operation wants to manipulate itself before it recurses down to the children
     * of the element being processed
     * @return the same or another instance of the implementing operation
     */
    public HierarchyWrapperOperation createSubOperation() {
        return this;
    }

    /** Override this if the operation wants to do something after each element has been processed */
    public void endOfProcess() {
    }

}
