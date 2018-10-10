package sortpom.wrapper.operation;

import org.jdom.Content;
import org.jdom.Element;
import sortpom.wrapper.content.SingleNewlineInTextWrapper;
import sortpom.wrapper.content.Wrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregates a number of wrappers, so that they can be treated as a recursive hierarchy.
 *
 * @author Bjorn
 */
class HierarchyWrapper {
    private Wrapper<Element> elementContent;
    private final List<Wrapper<Content>> otherContentList = new ArrayList<>();
    private final List<HierarchyWrapper> children = new ArrayList<>();

    HierarchyWrapper(final Wrapper<? extends Content> wrapper) {
        addContent(wrapper);
    }

    @SuppressWarnings("unchecked")
    private void addContent(final Wrapper<? extends Content> wrapper) {
        if (wrapper.isContentElement()) {
            elementContent = (Wrapper<Element>) wrapper;
        } else {
            otherContentList.add((Wrapper<Content>) wrapper);
        }

    }

    /** Traverses the initial xml element wrapper and builds hierarchy */
    void createWrappedStructure(final WrapperFactory factory) {
        HierarchyWrapper currentWrapper = null;
        for (Content child : castToContentList(elementContent)) {
            Wrapper<?> wrapper = factory.create(child);
            if (wrapper instanceof SingleNewlineInTextWrapper) {
                continue;
            }
            if (currentWrapper == null) {
                currentWrapper = new HierarchyWrapper(wrapper);
                children.add(currentWrapper);
            } else {
                currentWrapper.addContent(wrapper);
            }
            if (currentWrapper.containsElement()) {
                currentWrapper.createWrappedStructure(factory);
                currentWrapper = null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private List<Content> castToContentList(final Wrapper<Element> elementContent) {
        return new ArrayList<>(elementContent.getContent().getContent());
    }

    private boolean containsElement() {
        return elementContent != null;
    }

    /** Returns the base element */
    Wrapper<Element> getElementContent() {
        return elementContent;
    }

    /** Template method to traverse xml hierarchy */
    void processOperation(HierarchyWrapperOperation operation) {
        // Hook for start
        operation.startOfProcess();

        // Hook to process other content (newlines and comments)
        otherContentList.forEach(operation::processOtherContent);

        if (elementContent != null && elementContent.isContentElement()) {
            // Hook to process element
            operation.processElement(elementContent);
        }

        // Hook to manipulate the order of the children
        operation.manipulateChildElements(children);

        // Hook to modify the operation before traversing sub levels
        HierarchyWrapperOperation subOperation = operation.createSubOperation();

        for (HierarchyWrapper child : children) {
            child.processOperation(subOperation);
        }

        // Hook for end of process
        operation.endOfProcess();
    }

    /** Returns the whole xml structure as a readable string */
    @Override
    public String toString() {
        ToStringOperation operation = new ToStringOperation();
        processOperation(operation);
        return operation.toString();
    }
}
