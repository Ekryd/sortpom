package sortpom.wrapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.wrapper.content.SingleNewlineInTextWrapper;

/**
 * All method should throw exception since the element should be throw away, except for the toString
 * method
 *
 * @author bjorn
 * @since 2012-06-14
 */
class SingleNewlineInTextWrapperTest {

  @Test
  void testGetContent() {
    Executable testMethod = SingleNewlineInTextWrapper.INSTANCE::getContent;

    var thrown = assertThrows(UnsupportedOperationException.class, testMethod);

    assertThat(thrown.getMessage(), is(nullValue()));
  }

  @Test
  void testIsBefore() {
    Executable testMethod = () -> SingleNewlineInTextWrapper.INSTANCE.isBefore(null);

    var thrown = assertThrows(UnsupportedOperationException.class, testMethod);

    assertThat(thrown.getMessage(), is(nullValue()));
  }

  @Test
  void testIsContentElement() {
    Executable testMethod = SingleNewlineInTextWrapper.INSTANCE::isContentElement;

    var thrown = assertThrows(UnsupportedOperationException.class, testMethod);

    assertThat(thrown.getMessage(), is(nullValue()));
  }

  @Test
  void testIsResortable() {
    Executable testMethod = SingleNewlineInTextWrapper.INSTANCE::isSortable;

    var thrown = assertThrows(UnsupportedOperationException.class, testMethod);

    assertThat(thrown.getMessage(), is(nullValue()));
  }

  @Test
  void testToString() {
    assertThat(
        SingleNewlineInTextWrapper.INSTANCE.toString("  "), is("  SingleNewlineInTextWrapper"));
  }
}
