package sortpom.output;

import static java.util.Optional.ofNullable;

import java.io.IOException;
import java.io.Writer;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultText;
import sortpom.content.NewlineText;
import sortpom.parameter.IndentAttribute;

/** Overriding XMLWriter to be able to handle SortPom formatting options */
class PatchedXMLWriter extends XMLWriter {

  private final OutputFormat format;
  private final boolean indentBlankLines;
  private final IndentAttribute indentAttribute;
  private final boolean spaceBeforeCloseEmptyElement;
  private final boolean endWithNewline;

  public PatchedXMLWriter(
      Writer writer,
      OutputFormat format,
      boolean spaceBeforeCloseEmptyElement,
      boolean indentBlankLines,
      IndentAttribute indentAttribute,
      boolean endWithNewline) {
    super(writer, format);
    this.format = format;
    this.indentBlankLines = indentBlankLines;
    this.indentAttribute = indentAttribute;
    this.spaceBeforeCloseEmptyElement = spaceBeforeCloseEmptyElement;
    this.endWithNewline = endWithNewline;
  }

  @Override
  public void write(Document doc) throws IOException {
    writeDeclaration();

    if (doc.getDocType() != null) {
      indent();
      writeDocType(doc.getDocType());
    }

    for (int i = 0, size = doc.nodeCount(); i < size; i++) {
      var node = doc.node(i);
      writeNode(node);
    }

    if (endWithNewline) {
      writePrintln();
    }
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

  /** Handle indentAttribute option */
  @Override
  protected void writeAttribute(Attribute attribute) throws IOException {
    var qualifiedName = attribute.getQualifiedName();
    if (indentAttribute == IndentAttribute.ALL
        || (indentAttribute == IndentAttribute.SCHEMA_LOCATION
            && "xsi:schemaLocation".equals(qualifiedName))) {
      writePrintln();
      indent();
      writeString(format.getIndent());
      writeString(format.getIndent());
    } else {
      writer.write(" ");
    }
    writer.write(qualifiedName);
    writer.write("=");

    var quote = format.getAttributeQuoteCharacter();
    writer.write(quote);

    writeEscapeAttributeEntities(attribute.getValue());

    writer.write(quote);
    lastOutputNodeType = Node.ATTRIBUTE_NODE;
  }

  @Override
  protected void writeNamespace(String prefix, String uri) throws IOException {
    if (indentAttribute == IndentAttribute.ALL) {
      writePrintln();
      indent();
      writeString(format.getIndent());
      writeString(format.getIndent());
    } else {
      writer.write(" ");
    }

    if ((prefix != null) && (!prefix.isEmpty())) {
      writer.write("xmlns:");
      writer.write(prefix);
      writer.write("=\"");
    } else {
      writer.write("xmlns=\"");
    }

    writer.write(uri);
    writer.write("\"");
  }
}
