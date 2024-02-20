package sortpom.content;

import org.dom4j.tree.AbstractText;

/**
 * The NewlineText is not really a special case of text. The special handling of NewlineText is done
 * in XmlProcessor.PatchedXMLWriter
 */
public class NewlineText extends AbstractText {
  public static final NewlineText INSTANCE = new NewlineText();
  private static final long serialVersionUID = -7552189498553321263L;

  private NewlineText() {}

  /**
   * This returns a <code>String</code> representation of the <code>NewlineText</code>, suitable for
   * debugging.
   */
  @Override
  public String toString() {
    return "[NewLine]";
  }

  @Override
  public String getText() {
    return "";
  }
}
