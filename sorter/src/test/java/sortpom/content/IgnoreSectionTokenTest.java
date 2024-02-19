package sortpom.content;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class IgnoreSectionTokenTest {

  @Test
  void appendTextShouldNotBeSupported() {
    var ignoreSectionToken = new IgnoreSectionToken(null, null, "value");
    assertThrows(
        UnsupportedOperationException.class, () -> ignoreSectionToken.appendText("whatever"));
  }
}
