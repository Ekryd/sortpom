package sortpom.wrapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.dom4j.dom.DOMText;
import org.dom4j.tree.DefaultText;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sortpom.parameter.PluginParameters;
import sortpom.wrapper.content.ThrowAwayNewlineWrapper;
import sortpom.wrapper.content.UnsortedWrapper;

/**
 * @author bjorn
 * @since 2012-06-19
 */
class TextWrapperCreatorTest {
  private final TextWrapperCreator textWrapperCreator = new TextWrapperCreator();

  @BeforeEach
  void setup() {
    textWrapperCreator.setup(
        PluginParameters.builder()
            .setEncoding("UTF-8")
            .setFormatting("\n", true, true, true, true)
            .build());
  }

  @Test
  void testIsEmptyLine() {
    assertKeepNewline("\n      \n  ");
    assertKeepNewline("  \n\n  ");
    assertKeepNewline("\n\n");
    assertKeepNewline("\n\n\n");
    assertKeepNewline("\n\n\n\n\n\n\n");
    assertKeepNewline("  \r\r  ");
    assertKeepNewline("\r\r");
    assertKeepNewline("  \r\n\r\n  ");
    assertKeepNewline("\r\n\r\n");

    assertNoSpecialNewline("");
    assertNoSpecialNewline("\n  ");
    assertNoSpecialNewline("  \n  ");
    assertNoSpecialNewline("  \r  ");
    assertNoSpecialNewline("  \r\n  ");
    assertNoSpecialNewline("  ");
  }

  private void assertKeepNewline(String text) {
    assertThat(
        textWrapperCreator.blankTextNode(new DefaultText(text)),
        is(UnsortedWrapper.KEEP_NEWLINE_INSTANCE));
  }

  private void assertNoSpecialNewline(String text) {
    assertThat(
        textWrapperCreator.blankTextNode(new DefaultText(text)),
        is(ThrowAwayNewlineWrapper.THROW_AWAY_NEWLINE_INSTANCE));
  }

  @Test
  void textNodeWithNullParentShouldNotCrash() {
    var text = new DOMText("Hi!");
    assertThat(textWrapperCreator.isElementSpacePreserved(text.getParent()), is(false));
  }
}
