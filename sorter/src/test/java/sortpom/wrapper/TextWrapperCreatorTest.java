package sortpom.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    assertEquals(
        UnsortedWrapper.KEEP_NEWLINE_INSTANCE,
        textWrapperCreator.blankTextNode(new DefaultText(text)));
  }

  private void assertNoSpecialNewline(String text) {
    assertEquals(
        ThrowAwayNewlineWrapper.THROW_AWAY_NEWLINE_INSTANCE,
        textWrapperCreator.blankTextNode(new DefaultText(text)));
  }

  @Test
  void textNodeWithNullParentShouldNotCrash() {
    var text = new DOMText("Hi!");
    assertFalse(textWrapperCreator.isElementSpacePreserved(text.getParent()));
  }
}
