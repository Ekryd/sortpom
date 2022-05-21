package sortpom.content;

import org.dom4j.Comment;
import org.dom4j.Element;
import org.dom4j.ProcessingInstruction;
import org.dom4j.tree.DefaultProcessingInstruction;

/**
 * The processing instruction is also a comment which forces the indentation when the token is
 * placed within an Element. See XMLWriter.writeElementL937
 */
public class IgnoreSectionToken extends DefaultProcessingInstruction implements Comment {
  public IgnoreSectionToken(Element parent, String target, String values) {
    super(parent, target, values);
  }

  public static IgnoreSectionToken from(ProcessingInstruction content) {
    return new IgnoreSectionToken(
        content.getParent(), content.getTarget(), content.getStringValue());
  }

  @Override
  public void appendText(String text) {
    throw new UnsupportedOperationException();
  }
}
