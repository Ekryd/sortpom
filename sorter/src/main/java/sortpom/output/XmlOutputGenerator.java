package sortpom.output;

import java.io.IOException;
import java.io.StringWriter;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import sortpom.exception.FailureException;
import sortpom.parameter.IndentAttribute;
import sortpom.parameter.PluginParameters;
import sortpom.util.StringLineSeparatorWriter;

/** Handles all generation of xml. */
public class XmlOutputGenerator {

  private String encoding;
  private String indentCharacters;
  private boolean expandEmptyElements;
  private boolean indentBlankLines;
  private IndentAttribute indentAttribute;
  private boolean spaceBeforeCloseEmptyElement;
  private String lineSeparator;
  private boolean endWithNewline;

  /** Setup default configuration */
  public void setup(PluginParameters pluginParameters) {
    this.indentCharacters = pluginParameters.indentCharacters;
    this.encoding = pluginParameters.encoding;
    this.expandEmptyElements = pluginParameters.expandEmptyElements;
    this.indentBlankLines = pluginParameters.indentBlankLines;
    this.indentAttribute = pluginParameters.indentAttribute;
    this.spaceBeforeCloseEmptyElement = pluginParameters.spaceBeforeCloseEmptyElement;
    this.lineSeparator = pluginParameters.lineSeparatorUtil.toString();
    this.endWithNewline = pluginParameters.endWithNewline;
  }

  /**
   * Returns the sorted xml as an OutputStream.
   *
   * @return the sorted xml
   */
  public String getSortedXml(Document newDocument) {
    try (var out = new StringWriter();
        var writer = new StringLineSeparatorWriter(out, lineSeparator)) {
      XMLWriter xmlWriter =
          new PatchedXMLWriter(
              writer,
              createPrettyFormat(),
              spaceBeforeCloseEmptyElement,
              indentBlankLines,
              indentAttribute,
              endWithNewline);
      xmlWriter.write(newDocument);
      writer.writeDelayedNewline();
      return writer.toString();
    } catch (IOException ioex) {
      throw new FailureException("Could not format pom files content", ioex);
    }
  }

  private OutputFormat createPrettyFormat() {
    var outputFormat = new OutputFormat(indentCharacters);
    outputFormat.setNewlines(true);
    outputFormat.setExpandEmptyElements(expandEmptyElements);
    outputFormat.setNewLineAfterDeclaration(false);
    outputFormat.setEncoding(encoding);
    return outputFormat;
  }
}
