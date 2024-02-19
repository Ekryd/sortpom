package sortpom.output;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static sortpom.sort.XmlFragment.createXmlFragment;

import java.io.IOException;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultNamespace;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.exception.FailureException;
import sortpom.parameter.PluginParameters;

/**
 * @author bjorn
 * @since 2020-01-12
 */
class XmlOutputGeneratorTest {

  @Test
  void simulateIOExceptionToTriggerExceptionMessage() {
    var document = spy(createXmlFragment());
    // Simulate an IOException (a checked one, no less)
    when(document.node(0))
        .thenAnswer(
            invocation -> {
              throw new IOException();
            });

    var xmlOutputGenerator = new XmlOutputGenerator();
    xmlOutputGenerator.setup(
        PluginParameters.builder().setFormatting("\n", true, true, false, true).build());

    Executable testMethod = () -> xmlOutputGenerator.getSortedXml(document);

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        "Unexpected message",
        thrown.getMessage(),
        is(equalTo("Could not format pom files content")));
  }

  @Test
  void definedParentNamespaceShouldBeReused() {
    var document = createXmlFragment();
    var namespaceUri = "this is a uri";
    document
        .getRootElement()
        .setAttributes(singletonList(new DefaultAttribute("xmlns:namespace", namespaceUri)));

    var child = new DefaultElement("child");
    child.add(
        new DefaultAttribute("attr1", "value", new DefaultNamespace("namespace", namespaceUri)));
    document.getRootElement().add(child);

    var xmlOutputGenerator = new XmlOutputGenerator();
    xmlOutputGenerator.setup(
        PluginParameters.builder().setFormatting("\n", true, true, false, true).build());

    var sortedXml = xmlOutputGenerator.getSortedXml(document);
    assertThat(
        sortedXml,
        is(
            equalTo(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka xmlns:namespace=\"this is a uri\">\n"
                    + "<child namespace:attr1=\"value\"></child>\n</Gurka>\n")));
  }

  @Test
  void redefinedParentNamespaceShouldBeWrittenAgain() {
    var document = createXmlFragment();
    document
        .getRootElement()
        .setAttributes(singletonList(new DefaultAttribute("xmlns:namespace", "this is a uri")));

    var child = new DefaultElement("child");
    child.add(
        new DefaultAttribute("attr1", "value", new DefaultNamespace("namespace", "another uri")));
    document.getRootElement().add(child);

    var xmlOutputGenerator = new XmlOutputGenerator();
    xmlOutputGenerator.setup(
        PluginParameters.builder().setFormatting("\n", true, true, false, true).build());

    var sortedXml = xmlOutputGenerator.getSortedXml(document);
    assertThat(
        sortedXml,
        is(
            equalTo(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka xmlns:namespace=\"this is a uri\">\n"
                    + "<child xmlns:namespace=\"another uri\" namespace:attr1=\"value\"></child>\n</Gurka>\n")));
  }

  @Test
  void attributeCalledXmlnsShouldNotBePrinted() {
    var document = createXmlFragment();
    document.getRootElement().setAttributes(singletonList(new DefaultAttribute("xmlns", "value")));

    var xmlOutputGenerator = new XmlOutputGenerator();
    xmlOutputGenerator.setup(
        PluginParameters.builder().setFormatting("\n", true, true, false, true).build());

    var sortedXml = xmlOutputGenerator.getSortedXml(document);
    assertThat(
        sortedXml, is(equalTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka></Gurka>\n")));
  }
}
