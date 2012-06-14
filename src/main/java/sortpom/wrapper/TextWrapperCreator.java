package sortpom.wrapper;

import org.jdom.Content;
import org.jdom.Text;
import sortpom.jdomcontent.NewlineText;
import sortpom.parameter.PluginParameters;

/**
 * @author bjorn
 * @since 2012-05-19
 */
public class TextWrapperCreator {
    private boolean keepBlankLines;

    public TextWrapperCreator() {
    }

    public void setup(PluginParameters pluginParameters) {
        keepBlankLines = pluginParameters.keepBlankLines;
    }

    public Wrapper<? extends Content> createWrapper(Text text) {
        if (isSingleNewLine(text)) {
            return ThrowAwayContentWrapper.INSTANCE;
        } else if (isBlankLineOrLines(text)) {
            return new UnsortedWrapper<Content>(new NewlineText());
        }
        return new UnsortedWrapper<Text>(text);
    }

    private boolean isSingleNewLine(Text content) {
        return content.getText().matches("[\\t ]*[\\r]?[\\n]?[\\t ]*");
    }

    private boolean isBlankLineOrLines(Text content) {
        if (!keepBlankLines) {
            return false;
        }
        return content.getText().matches("\\s*?(\\n.*?\\n|\\r.*?\\r)\\s*");
    }
}
