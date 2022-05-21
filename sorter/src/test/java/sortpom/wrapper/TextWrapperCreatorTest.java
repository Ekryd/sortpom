package sortpom.wrapper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.dom4j.tree.DefaultText;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sortpom.parameter.PluginParameters;

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
            .setFormatting("\n", true, true, true)
            .build());
  }

  @Test
  void testIsEmptyLine() {
    assertFalse(textWrapperCreator.isBlankLineOrLines(new DefaultText("\n      sortpom\n  ")));
    assertFalse(textWrapperCreator.isBlankLineOrLines(new DefaultText("sortpom")));
    assertTrue(textWrapperCreator.isBlankLineOrLines(new DefaultText("\n  ")));
    assertTrue(textWrapperCreator.isBlankLineOrLines(new DefaultText("  \n  ")));
    assertTrue(textWrapperCreator.isBlankLineOrLines(new DefaultText("  \n\n  ")));
    assertTrue(textWrapperCreator.isBlankLineOrLines(new DefaultText("\n\n")));
    assertTrue(textWrapperCreator.isBlankLineOrLines(new DefaultText("  \r  ")));
    assertTrue(textWrapperCreator.isBlankLineOrLines(new DefaultText("  \r\r  ")));
    assertTrue(textWrapperCreator.isBlankLineOrLines(new DefaultText("\r\r")));
    assertTrue(textWrapperCreator.isBlankLineOrLines(new DefaultText("  \r\n  ")));
    assertTrue(textWrapperCreator.isBlankLineOrLines(new DefaultText("  \r\n\r\n  ")));
    assertTrue(textWrapperCreator.isBlankLineOrLines(new DefaultText("\r\n\r\n")));
    assertFalse(textWrapperCreator.isBlankLineOrLines(new DefaultText("  ")));
  }
}
