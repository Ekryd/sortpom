package sortpom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mockStatic;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import refutils.ReflectionHelper;
import sortpom.util.XmlOrderedResult;

/** */
class SortPomServiceTest {
  private final SortPomService service = new SortPomService();
  private final ReflectionHelper serviceHelper = new ReflectionHelper(service);

  @Test
  void equalStringShouldBeSame() {
    setOriginalAndSortedXml("hello", "hello");
    var result = service.isOriginalXmlStringSorted();
    assertThat(result.isOrdered(), is(true));
  }

  @Test
  void equalWithNewlineShouldBeSame() {
    serviceHelper.setField("ignoreLineSeparators", false);

    setOriginalAndSortedXml("hello\nyou", "hello\nyou");
    var result = service.isOriginalXmlStringSorted();
    assertThat(result.isOrdered(), is(true));
  }

  @Test
  void equalWithDifferentNewlineShouldFail() {
    serviceHelper.setField("ignoreLineSeparators", false);

    setOriginalAndSortedXml("hello\nyou", "hello\r\nyou");
    var originalXmlStringSorted = service.isOriginalXmlStringSorted();
    assertThat(originalXmlStringSorted.isOrdered(), is(false));
    assertThat(
        originalXmlStringSorted.getErrorMessage(),
        is("The line separator characters differ from sorted pom"));

    setOriginalAndSortedXml("hello\r\nyou", "hello\nyou");
    originalXmlStringSorted = service.isOriginalXmlStringSorted();
    assertThat(originalXmlStringSorted.isOrdered(), is(false));
    assertThat(
        originalXmlStringSorted.getErrorMessage(),
        is("The line separator characters differ from sorted pom"));
  }

  @Test
  void equalWithDifferentLengthShouldFail() {
    serviceHelper.setField("ignoreLineSeparators", false);

    setOriginalAndSortedXml("hello", "hello\nyou");
    var originalXmlStringSorted = service.isOriginalXmlStringSorted();
    assertThat(originalXmlStringSorted.isOrdered(), is(false));
    assertThat(
        originalXmlStringSorted.getErrorMessage(),
        is("The line 2 is not considered sorted, should be 'you'"));

    setOriginalAndSortedXml("hello\nyou", "hello");
    originalXmlStringSorted = service.isOriginalXmlStringSorted();
    assertThat(originalXmlStringSorted.isOrdered(), is(false));
    assertThat(
        originalXmlStringSorted.getErrorMessage(),
        is("The line 2 is not considered sorted, should be empty"));
  }

  @Test
  void equalWithDifferentTextsShouldFail() {
    serviceHelper.setField("ignoreLineSeparators", false);

    setOriginalAndSortedXml("hello\nme", "hello\nyou");
    var originalXmlStringSorted = service.isOriginalXmlStringSorted();
    assertThat(originalXmlStringSorted.isOrdered(), is(false));
    assertThat(
        originalXmlStringSorted.getErrorMessage(),
        is("The line 2 is not considered sorted, should be 'you'"));
  }

  @Test
  void coverageIOExceptionHack() {
    serviceHelper.setField("ignoreLineSeparators", false);

    setOriginalAndSortedXml("hello\nme", "hello\nyou");
    try (var mock = mockStatic(XmlOrderedResult.class)) {
      mock.when(() -> XmlOrderedResult.lineDiffers(2, "'you'"))
          .thenAnswer(
              invocationOnMock -> {
                throw new IOException("Meh!");
              });
      service.isOriginalXmlStringSorted();
      fail();
    } catch (Exception ex) {
      assertThat(ex.getMessage(), is("Meh!"));
    }
  }

  private void setOriginalAndSortedXml(String originalXml, String sortedXml) {
    serviceHelper.setField("originalXml", originalXml);
    serviceHelper.setField("sortedXml", sortedXml);
  }
}
