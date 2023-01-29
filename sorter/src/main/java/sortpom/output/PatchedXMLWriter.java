package sortpom.output;

import static java.util.Optional.ofNullable;

import java.io.IOException;
import java.io.Writer;
import org.dom4j.Attribute;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultText;
import sortpom.content.NewlineText;

/** Overriding XMLWriter to be able to handle SortPom formatting options */
class PatchedXMLWriter extends XMLWriter {

  private final OutputFormat format;
  private final boolean indentBlankLines;
  private final boolean indentSchemaLocation;
  private final boolean spaceBeforeCloseEmptyElement;

  public PatchedXMLWriter(
      Writer writer,
      OutputFormat format,
      boolean spaceBeforeCloseEmptyElement,
      boolean indentBlankLines,
      boolean indentSchemaLocation) {
    super(writer, format);
    this.format = format;
    this.indentBlankLines = indentBlankLines;
    this.indentSchemaLocation = indentSchemaLocation;
    this.spaceBeforeCloseEmptyElement = spaceBeforeCloseEmptyElement;
  }

  /** Handle spaceBeforeCloseEmptyElement option */
  @Override
  protected void writeEmptyElementClose(String qualifiedName) throws IOException {
    if (!format.isExpandEmptyElements() && spaceBeforeCloseEmptyElement) {
      // add an extra place before closing tag
      writer.write(' ');
    }
    super.writeEmptyElementClose(qualifiedName);
  }

  /** Fixing a bug with processing instructions */
  @Override
  protected void writeProcessingInstruction(ProcessingInstruction pi) throws IOException {
    // Place the processing instruction on own line (instead of same line as previous element)
    writePrintln();
    indent();
    writer.write("<?");
    writer.write(pi.getName());
    writer.write(" ");
    writer.write(pi.getText());
    writer.write("?>");

    lastOutputNodeType = Node.PROCESSING_INSTRUCTION_NODE;
  }

  /** Handle Custom NewLineTest node and potential indent of empty line */
  @Override
  protected void writeNodeText(Node node) throws IOException {
    if (node instanceof NewlineText) {
      // Handle our own NewlineText
      writePrintln();
      if (indentBlankLines) {
        // If blank lines should be indented
        indent();
      }
    } else {
      // Check if attribute xml:preserve is used
      if (isElementSpacePreserved(node.getParent())) {
        super.writeNodeText(node);
      } else {
        writeTrimmedText(node);
      }
    }
  }

  private void writeTrimmedText(Node node) throws IOException {
    var text = ofNullable(node.getText()).map(String::trim).filter(s -> !s.isEmpty());

    if (text.isPresent()) {
      // Test if this text node has siblings in the parent node
      if (node.getParent().content().size() > 1) {
        writePrintln();
        indent();
      }

      super.write(new DefaultText(text.orElseThrow()));
    }
  }

  /** Handle indentSchemaLocation option */
  @Override
  protected void writeAttribute(Attribute attribute) throws IOException {
    String qualifiedName = attribute.getQualifiedName();
    if (indentSchemaLocation && "xsi:schemaLocation".equals(qualifiedName)) {
      writePrintln();
      writeString(format.getIndent());
      writeString(format.getIndent());
    }
    writer.write(" ");
    writer.write(qualifiedName);
    writer.write("=");

    char quote = format.getAttributeQuoteCharacter();
    writer.write(quote);

    writeEscapeAttributeEntities(attribute.getValue());

    writer.write(quote);
    lastOutputNodeType = Node.ATTRIBUTE_NODE;
  }
}
