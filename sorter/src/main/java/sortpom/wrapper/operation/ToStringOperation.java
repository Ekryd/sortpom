package sortpom.wrapper.operation;

import org.jdom.Content;
import org.jdom.Element;
import sortpom.wrapper.content.Wrapper;

import java.util.List;

/**
 * Xml hierarchy operation that returns xml content as readable text. Used by
 * Used in HierarchyWrapper.processOperation(HierarchyWrapperOperation operation)
 *
 * @author bjorn
 * @since 2013-11-02
 */
class ToStringOperation implements HierarchyWrapperOperation {
    private static final String INDENT = "  ";
    private static final int INDENT_LENGTH = INDENT.length();
    private final StringBuilder builder;
    private final String baseIndent;
    private boolean processFirstOtherContent;

    ToStringOperation() {
        builder = new StringBuilder();
        baseIndent = INDENT;
    }

    private ToStringOperation(StringBuilder builder, String baseIndent) {
        this.builder = builder;
        this.baseIndent = INDENT + baseIndent;
    }

    /** Add text before each element */
    @Override
    public void startOfProcess() {
        String previousBaseIndent = baseIndent.substring(INDENT_LENGTH);
        builder.append(previousBaseIndent).append("HierarchyWrapper{\n");
        processFirstOtherContent = true;
    }

    /** Add each 'other element' to string */
    @Override
    public void processOtherContent(Wrapper<Content> content) {
        if (processFirstOtherContent) {
            builder.append(baseIndent).append("otherContentList=").append("\n");
            processFirstOtherContent = false;
        }
        builder.append(content.toString(baseIndent)).append("\n");
    }

    /** Add each element to string */
    @Override
    public void processElement(Wrapper<Element> elementWrapper) {
        builder.append(baseIndent).append("elementContent=").append(elementWrapper).append("\n");
    }

    /** Add text before processing each child */
    @Override
    public void manipulateChildElements(List<HierarchyWrapper> children) {
        if (!children.isEmpty()) {
            builder.append(baseIndent).append("children=").append("\n");
        }
    }

    /** Add the string to build and growing indent to sub operation */
    @Override
    public HierarchyWrapperOperation createSubOperation() {
        return new ToStringOperation(builder, baseIndent);
    }

    /** Add text after processing each element */
    @Override
    public void endOfProcess() {
        builder.append(baseIndent).append('}').append("\n");
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
