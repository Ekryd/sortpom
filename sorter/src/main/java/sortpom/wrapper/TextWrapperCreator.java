package sortpom.wrapper;

import static sortpom.wrapper.content.ThrowAwayNewlineWrapper.THROW_AWAY_NEWLINE_INSTANCE;
import static sortpom.wrapper.content.UnsortedWrapper.*;

import java.util.regex.Pattern;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import sortpom.parameter.PluginParameters;
import sortpom.wrapper.content.UnsortedWrapper;
import sortpom.wrapper.content.Wrapper;

/**
 * @author bjorn
 * @since 2012-05-19
 */
public class TextWrapperCreator {

  public static final Pattern REGEX_ONE_OR_MORE_NEWLINE = Pattern.compile("(\\r\\n|\\r|\\n)+?");
  private boolean keepBlankLines;

  public void setup(PluginParameters pluginParameters) {
    keepBlankLines = pluginParameters.keepBlankLines;
  }

  Wrapper<Node> createWrapper(Text text) {
    if (!text.getText().isBlank() || isElementSpacePreserved(text.getParent())) {
      return new UnsortedWrapper<>(text);
    }
    return blankTextNode(text);
  }

  boolean isElementSpacePreserved(Element element) {
    if (element == null) {
      return false;
    }
    var attr = element.attribute("space");

    return attr != null
        && "xml".equals(attr.getNamespacePrefix())
        && "preserve".equals(attr.getText());
  }

  Wrapper<Node> blankTextNode(Text text) {
    if (!keepBlankLines) {
      return THROW_AWAY_NEWLINE_INSTANCE;
    }
    var textContent = text.getText();
    var newLineCount = REGEX_ONE_OR_MORE_NEWLINE.matcher(textContent).results().count();
    if (newLineCount <= 1) {
      // One newline is just the linebreak between two XML elements
      return THROW_AWAY_NEWLINE_INSTANCE;
    }
    // Multiple linebreaks between XML elements indicate that a newline should be kept
    return KEEP_NEWLINE_INSTANCE;
  }
}
