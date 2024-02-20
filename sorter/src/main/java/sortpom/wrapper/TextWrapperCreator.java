package sortpom.wrapper;

import java.util.regex.Pattern;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import sortpom.parameter.PluginParameters;
import sortpom.wrapper.content.SingleNewlineInTextWrapper;
import sortpom.wrapper.content.UnsortedWrapper;
import sortpom.wrapper.content.Wrapper;

/**
 * @author bjorn
 * @since 2012-05-19
 */
public class TextWrapperCreator {

  public static final Pattern REGEX_ONE_NEWLINE =
      Pattern.compile("^[\\t ]*(\\r|\\n|\\r\\n)[\\t ]*$");
  public static final Pattern REGEX_ONE_OR_MORE_NEWLINE = Pattern.compile("^\\s*?([\\r\\n])\\s*$");
  private boolean keepBlankLines;

  public void setup(PluginParameters pluginParameters) {
    keepBlankLines = pluginParameters.keepBlankLines;
  }

  Wrapper<Node> createWrapper(Text text) {
    if (isElementSpacePreserved(text.getParent())) {
      return new UnsortedWrapper<>(text);
    }
    if (isSingleNewLine(text)) {
      return SingleNewlineInTextWrapper.INSTANCE;
    } else if (isBlankLineOrLines(text)) {
      return UnsortedWrapper.NEWLINE_TEXT_WRAPPER_INSTANCE;
    }
    return new UnsortedWrapper<>(text);
  }

  private boolean isElementSpacePreserved(Element element) {
    if (element == null) {
      return false;
    }
    var attr = element.attribute("space");

    return attr != null
        && "xml".equals(attr.getNamespacePrefix())
        && "preserve".equals(attr.getText());
  }

  private boolean isSingleNewLine(Text content) {
    return REGEX_ONE_NEWLINE.matcher(content.getText()).matches();
  }

  boolean isBlankLineOrLines(Text content) {
    if (!keepBlankLines) {
      return false;
    }
    return REGEX_ONE_OR_MORE_NEWLINE.matcher(content.getText()).matches();
  }
}
