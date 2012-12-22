package sortpom.wrapper;

import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aggregates a number of wrappers, so that they can be sorted as a group.
 *
 * @author Bjorn
 */
public class GroupWrapper implements WrapperOperations {
    private Wrapper<Element> elementContent;
    private final List<Wrapper<? extends Content>> otherContentList = new ArrayList<Wrapper<? extends Content>>();
    private final List<GroupWrapper> children = new ArrayList<GroupWrapper>();

    private static final AttributeComparator ATTRIBUTE_COMPARATOR = new AttributeComparator();

    public GroupWrapper(final Wrapper<? extends Content> wrapper) {
        addContent(wrapper);
    }

    @Override
    public final void createWrappedStructure(final WrapperFactory factory) {
        GroupWrapper currentWrapper = null;
        for (Content child : castToContentList(elementContent)) {
            Wrapper<?> wrapper = factory.create(child);
            if (wrapper instanceof ThrowAwayContentWrapper) {
                continue;
            }
            if (currentWrapper == null) {
                currentWrapper = new GroupWrapper(wrapper);
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

    @Override
    public final void detachStructure() {
        if (elementContent != null) {
            elementContent.getContent().detach();
        }
        for (WrapperOperations child : children) {
            child.detachStructure();
        }
        for (Wrapper<? extends Content> content : otherContentList) {
            content.getContent().detach();
        }
    }

    @Override
    public final List<Content> getWrappedStructure() {
        List<Content> returnValue = new ArrayList<Content>();
        for (Wrapper<? extends Content> content : otherContentList) {
            returnValue.add(content.getContent());
        }
        if (elementContent != null) {
            elementContent.getContent().removeContent();
            elementContent.getContent().addContent(getWrappedChildren());
            returnValue.add(elementContent.getContent());
        }
        return returnValue;
    }

    @Override
    public final void sortStructureAttributes() {
        if (elementContent != null) {
            elementContent.getContent().setAttributes(getSortedAttributes(elementContent));
        }
        for (WrapperOperations child : children) {
            child.sortStructureAttributes();
        }
    }

    @Override
    public final void sortStructureElements() {
        for (int i = 0; i < children.size(); i++) {
            GroupWrapper wrapperImpl = children.get(i);
            final Wrapper<Element> wrapper = wrapperImpl.elementContent;
            if (wrapper != null) {
                if (wrapper.isResortable()) {
                    boolean done = false;
                    for (int j = 0; !done && j < i; j++) {
                        if (wrapper.isBefore(children.get(j).elementContent)) {
                            children.remove(i);
                            children.add(j, wrapperImpl);
                            done = true;
                        }
                    }
                }
                wrapperImpl.sortStructureElements();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void addContent(final Wrapper<? extends Content> wrapper) {
        if (wrapper.isContentElement()) {
            elementContent = (Wrapper<Element>) wrapper;
        } else {
            otherContentList.add(wrapper);
        }

    }

    @SuppressWarnings("unchecked")
    private List<Attribute> castToAttributeList(final Element element) {
        return new ArrayList<Attribute>(element.getAttributes());
    }

    @SuppressWarnings("unchecked")
    private List<Content> castToContentList(final Wrapper<Element> elementContent) {
        if (elementContent == null) {
            return new ArrayList<Content>();
        }
        return new ArrayList<Content>(elementContent.getContent().getContent());
    }

    private boolean containsElement() {
        return elementContent != null;
    }

    private List<Attribute> getSortedAttributes(final Wrapper<Element> elementContent) {
        final List<Attribute> attributes = castToAttributeList(elementContent.getContent());
        for (Attribute attribute : attributes) {
            attribute.detach();
        }
        Collections.sort(attributes, ATTRIBUTE_COMPARATOR);
        return attributes;
    }

    private List<Content> getWrappedChildren() {
        List<Content> returnValue = new ArrayList<Content>();
        for (WrapperOperations child : children) {
            returnValue.addAll(child.getWrappedStructure());
        }
        return returnValue;
    }

    @Override
    public String toString() {
        return toString("");
    }

    String toString(String baseIndent) {
        StringBuilder builder = new StringBuilder();
        builder.append(baseIndent).append("GroupWrapper{\n");
        String indent = "  " + baseIndent;
        toStringElementContent(indent, builder);
        toStringOtherContentList(indent, builder);
        toStringChildren(indent, builder);
        builder.append(indent).append('}');
        return builder.toString();
    }

    private void toStringElementContent(String indent, StringBuilder builder) {
        if (elementContent != null) {
            builder.append(indent).append("elementContent=").append(elementContent).append("\n");
        }
    }

    private void toStringOtherContentList(String indent, StringBuilder builder) {
        if (otherContentList.size() != 0) {
            builder.append(indent).append(", otherContentList=").append("\n");
            for (Wrapper<? extends Content> wrapper : otherContentList) {
                builder.append(wrapper.toString(indent)).append("\n");
            }
        }
    }

    private void toStringChildren(String indent, StringBuilder builder) {
        if (children.size() != 0) {
            builder.append(indent).append(", children=").append("\n");
            for (GroupWrapper child : children) {
                builder.append(child.toString(indent)).append("\n");
            }
        }
    }

}
