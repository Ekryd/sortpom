package sortpom.output;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static sortpom.sort.XmlFragment.createXmlFragment;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import org.dom4j.Document;
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
    Document document = spy(createXmlFragment());
    // Simulate an IOException (a checked one, no less)
    when(document.node(0))
        .thenAnswer(
            invocation -> {
              throw new IOException();
            });

    XmlOutputGenerator xmlOutputGenerator = new XmlOutputGenerator();
    xmlOutputGenerator.setup(
        PluginParameters.builder().setFormatting("\n", true, true, false).build());

    final Executable testMethod = () -> xmlOutputGenerator.getSortedXml(document);

    final FailureException thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        "Unexpected message",
        thrown.getMessage(),
        is(equalTo("Could not format pom files content")));
  }

  @Test
  void definedParentNamespaceShouldBeReused() {
    Document document = createXmlFragment();
    String namespaceUri = "this is a uri";
    document
        .getRootElement()
        .setAttributes(singletonList(new DefaultAttribute("xmlns:namespace", namespaceUri)));

    DefaultElement child = new DefaultElement("child");
    child.add(
        new DefaultAttribute("attr1", "value", new DefaultNamespace("namespace", namespaceUri)));
    document.getRootElement().add(child);

    XmlOutputGenerator xmlOutputGenerator = new XmlOutputGenerator();
    xmlOutputGenerator.setup(
        PluginParameters.builder().setFormatting("\n", true, true, false).build());

    String sortedXml = xmlOutputGenerator.getSortedXml(document);
    assertThat(
        sortedXml,
        is(
            equalTo(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka xmlns:namespace=\"this is a uri\">\n"
                    + "<child namespace:attr1=\"value\"></child>\n</Gurka>\n")));
  }

  @Test
  void redefinedParentNamespaceShouldBeWrittenAgain() {
    Document document = createXmlFragment();
    document
        .getRootElement()
        .setAttributes(singletonList(new DefaultAttribute("xmlns:namespace", "this is a uri")));

    DefaultElement child = new DefaultElement("child");
    child.add(
        new DefaultAttribute("attr1", "value", new DefaultNamespace("namespace", "another uri")));
    document.getRootElement().add(child);

    XmlOutputGenerator xmlOutputGenerator = new XmlOutputGenerator();
    xmlOutputGenerator.setup(
        PluginParameters.builder().setFormatting("\n", true, true, false).build());

    String sortedXml = xmlOutputGenerator.getSortedXml(document);
    assertThat(
        sortedXml,
        is(
            equalTo(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka xmlns:namespace=\"this is a uri\">\n"
                    + "<child xmlns:namespace=\"another uri\" namespace:attr1=\"value\"></child>\n</Gurka>\n")));
  }

  @Test
  void attributeCalledXmlnsShouldNotBePrinted() {
    Document document = createXmlFragment();
    document.getRootElement().setAttributes(singletonList(new DefaultAttribute("xmlns", "value")));

    XmlOutputGenerator xmlOutputGenerator = new XmlOutputGenerator();
    xmlOutputGenerator.setup(
        PluginParameters.builder().setFormatting("\n", true, true, false).build());

    String sortedXml = xmlOutputGenerator.getSortedXml(document);
    assertThat(
        sortedXml, is(equalTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka></Gurka>\n")));
  }

  @Test
  void simulateNamespaceStackNotFound() throws Exception {
    var classloader =
        new URLClassLoader(
            new URL[] {
              new File("target/classes").toURI().toURL(),
              new File("target/test-classes").toURI().toURL(),
              // Contains the dummy XMLWriter class that does not have the namespace stack field
              new File("src/test/resources/mock").toURI().toURL()
            }) {

          protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            if (name.equals("org.dom4j.io.XMLWriter")) {
              return findClass(name);
              // With no fallback to parent classloader
            }
            try {
              return findClass(name);
            } catch (ClassNotFoundException e) {
              // Could not find the class so load it from the parent
              return super.loadClass(name, resolve);
            }
          }
        };

    Class<?> clazz = classloader.loadClass("sortpom.output.PatchedXMLWriter");
    Constructor<?> constructor = clazz.getConstructors()[0];
    constructor.setAccessible(true);

    var exception =
        assertThrows(
            InvocationTargetException.class,
            () -> constructor.newInstance(null, null, false, false, false));

    var cause = exception.getCause();
    assertThat(cause.getClass().getName(), is("sortpom.exception.FailureException"));
    assertThat(
        cause.getMessage(),
        is("Internal error: Cannot access internal namespace stack in XMLWriter"));
  }
}
