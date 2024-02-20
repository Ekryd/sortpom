package sortpom.wrapper.operation;

import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;
import org.dom4j.Node;
import sortpom.wrapper.content.ThrowAwayNewlineWrapper;
import sortpom.wrapper.content.Wrapper;

/**
 * Aggregates a number of wrappers, so that they can be treated as a recursive hierarchy.
 *
 * @author Bjorn
 */
class HierarchyWrapper {
  private Wrapper<Element> elementContent;
  private final List<Wrapper<Node>> otherContentList = new ArrayList<>();
  private final List<HierarchyWrapper> children = new ArrayList<>();

  HierarchyWrapper(Wrapper<? extends Node> wrapper) {
    addContent(wrapper);
  }

  @SuppressWarnings("unchecked")
  private void addContent(Wrapper<? extends Node> wrapper) {
    if (wrapper.isContentElement()) {
      elementContent = (Wrapper<Element>) wrapper;
    } else {
      otherContentList.add((Wrapper<Node>) wrapper);
    }
  }

  /** Traverses the initial xml element wrapper and builds hierarchy */
  void createWrappedStructure(WrapperFactory factory) {
    HierarchyWrapper currentWrapper = null;
    for (var child : elementContent.getContent().content()) {
      Wrapper<?> wrapper = factory.create(child);
      if (wrapper instanceof ThrowAwayNewlineWrapper) {
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
    var subOperation = operation.createSubOperation();

    for (var child : children) {
      child.processOperation(subOperation);
    }

    // Hook for end of process
    operation.endOfProcess();
  }

  /** Returns the whole xml structure as a readable string */
  @Override
  public String toString() {
    var operation = new ToStringOperation();
    processOperation(operation);
    return operation.toString();
  }
}
