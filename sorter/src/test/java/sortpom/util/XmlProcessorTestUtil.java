package sortpom.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import org.dom4j.Element;
import org.dom4j.Node;
import refutils.ReflectionHelper;
import sortpom.XmlProcessor;
import sortpom.output.XmlOutputGenerator;
import sortpom.parameter.PluginParameters;
import sortpom.wrapper.WrapperFactoryImpl;
import sortpom.wrapper.content.AlphabeticalSortedWrapper;
import sortpom.wrapper.content.UnsortedWrapper;
import sortpom.wrapper.content.Wrapper;
import sortpom.wrapper.operation.HierarchyRootWrapper;
import sortpom.wrapper.operation.WrapperFactory;

/** Test utility */
public class XmlProcessorTestUtil {
  private boolean sortAlphabeticalOnly = false;
  private boolean keepBlankLines = true;
  private boolean endWithNewline = true;
  private boolean indentBlankLines = false;
  private String predefinedSortOrder = "recommended_2008_06";
  private boolean expandEmptyElements = true;
  private String lineSeparator = "\r\n";

  private XmlProcessor xmlProcessor;
  private XmlOutputGenerator xmlOutputGenerator;
  private boolean spaceBeforeCloseEmptyElement = true;
  private boolean sortModules = false;
  private String sortDependencies;
  private String sortPlugins;
  private boolean sortProperties = false;

  public static XmlProcessorTestUtil create() {
    return new XmlProcessorTestUtil();
  }

  private XmlProcessorTestUtil() {}

  public void testInputAndExpected(final String inputFileName, final String expectedFileName)
      throws Exception {
    String actual = sortXmlAndReturnResult(inputFileName);

    try (var fileInputStream = new FileInputStream(expectedFileName)) {
      final String expected = new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8);

      assertEquals(expected, actual);
    }
  }

  public String sortXmlAndReturnResult(String inputFileName) throws Exception {
    setup(inputFileName);
    xmlProcessor.sortXml();
    return xmlOutputGenerator.getSortedXml(xmlProcessor.getNewDocument());
  }

  public void testVerifyXmlIsOrdered(final String inputFileName) throws Exception {
    setup(inputFileName);
    xmlProcessor.sortXml();
    assertTrue(xmlProcessor.isXmlOrdered().isOrdered());
  }

  public void testVerifyXmlIsNotOrdered(final String inputFileName, String infoMessage)
      throws Exception {
    setup(inputFileName);
    xmlProcessor.sortXml();
    XmlOrderedResult xmlOrdered = xmlProcessor.isXmlOrdered();
    assertFalse(xmlOrdered.isOrdered());
    assertEquals(infoMessage, xmlOrdered.getErrorMessage());
  }

  private void setup(String inputFileName) throws Exception {
    PluginParameters pluginParameters =
        PluginParameters.builder()
            .setPomFile(null)
            .setFileOutput(false, ".bak", null, false)
            .setEncoding("UTF-8")
            .setFormatting(
                lineSeparator,
                expandEmptyElements,
                spaceBeforeCloseEmptyElement,
                keepBlankLines,
                endWithNewline)
            .setIndent(2, indentBlankLines, false)
            .setSortOrder(predefinedSortOrder + ".xml", null)
            .setSortEntities(
                sortDependencies, "", null, sortPlugins, sortProperties, sortModules, false)
            .build();

    String xml;
    try (var fileInputStream = new FileInputStream(inputFileName)) {
      xml = new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    final FileUtil fileUtil = new FileUtil();
    fileUtil.setup(pluginParameters);

    WrapperFactory wrapperFactory = new WrapperFactoryImpl(fileUtil);
    ((WrapperFactoryImpl) wrapperFactory).setup(pluginParameters);

    xmlProcessor = new XmlProcessor(wrapperFactory);

    xmlOutputGenerator = new XmlOutputGenerator();
    xmlOutputGenerator.setup(pluginParameters);

    if (sortAlphabeticalOnly) {
      wrapperFactory =
          new WrapperFactory() {

            @Override
            public HierarchyRootWrapper createFromRootElement(final Element rootElement) {
              return new HierarchyRootWrapper(new AlphabeticalSortedWrapper(rootElement));
            }

            @SuppressWarnings("unchecked")
            @Override
            public <T extends Node> Wrapper<T> create(final T content) {
              if (content instanceof Element) {
                Element element = (Element) content;
                return (Wrapper<T>) new AlphabeticalSortedWrapper(element);
              }
              return new UnsortedWrapper<>(content);
            }
          };
    } else {
      new ReflectionHelper(wrapperFactory).setField(fileUtil);
    }
    new ReflectionHelper(xmlProcessor).setField(wrapperFactory);
    xmlProcessor.setOriginalXml(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
  }

  public XmlProcessorTestUtil sortAlphabeticalOnly() {
    sortAlphabeticalOnly = true;
    return this;
  }

  public XmlProcessorTestUtil keepBlankLinesFalse() {
    keepBlankLines = false;
    return this;
  }

  public XmlProcessorTestUtil endWithNewlineFalse() {
    endWithNewline = false;
    return this;
  }

  public XmlProcessorTestUtil lineSeparator(String lineSeparator) {
    this.lineSeparator = lineSeparator;
    return this;
  }

  public XmlProcessorTestUtil indentBlankLines() {
    indentBlankLines = true;
    return this;
  }

  public XmlProcessorTestUtil predefinedSortOrder(String predefinedSortOrder) {
    this.predefinedSortOrder = predefinedSortOrder;
    return this;
  }

  public XmlProcessorTestUtil sortModules() {
    this.sortModules = true;
    return this;
  }

  public XmlProcessorTestUtil sortDependencies(String sortDependencies) {
    this.sortDependencies = sortDependencies;
    return this;
  }

  public XmlProcessorTestUtil sortPlugins(String sortPlugins) {
    this.sortPlugins = sortPlugins;
    return this;
  }

  public XmlProcessorTestUtil noSpaceBeforeCloseEmptyElement() {
    this.spaceBeforeCloseEmptyElement = false;
    return this;
  }

  public XmlProcessorTestUtil sortProperties() {
    this.sortProperties = true;
    return this;
  }

  public XmlProcessorTestUtil expandEmptyElements(boolean expand) {
    this.expandEmptyElements = expand;
    return this;
  }
}
