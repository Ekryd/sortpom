package sortpom.content;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class IgnoreSectionTokenTest {

    @Test
    void appendTextShouldNotBeSupported() {
        IgnoreSectionToken ignoreSectionToken = new IgnoreSectionToken(null, null, "value");
        assertThrows(UnsupportedOperationException.class, () -> ignoreSectionToken.appendText("whatever"));
    }
}
