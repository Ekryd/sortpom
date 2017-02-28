package sortpom;

import org.apache.commons.io.IOUtils;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import sortpom.jdomcontent.NewlineText;
import sortpom.parameter.LineSeparatorUtil;
import sortpom.parameter.PluginParameters;
import sortpom.util.BufferedLineSeparatorOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

/**
 * Handles all generation of xml.
 */
public class XmlOutputGenerator {
    private String encoding;
    private LineSeparatorUtil lineSeparatorUtil;
    private String indentCharacters;
    private boolean expandEmptyElements;
    private boolean indentBlankLines;

    /** Setup default configuration */
    public void setup(PluginParameters pluginParameters) {
        this.indentCharacters = pluginParameters.indentCharacters;
        this.lineSeparatorUtil = pluginParameters.lineSeparatorUtil;
        this.encoding = pluginParameters.encoding;
        this.expandEmptyElements = pluginParameters.expandEmptyElements;
        this.indentBlankLines = pluginParameters.indentBlankLines;
    }

    /**
     * Returns the sorted xml as an OutputStream.
     *
     * @return the sorted xml
     * @throws java.io.IOException if there's any problem writing the xml
     */
    public ByteArrayOutputStream getSortedXml(Document newDocument) throws IOException {
        ByteArrayOutputStream sortedXml = new ByteArrayOutputStream();
        BufferedLineSeparatorOutputStream bufferedLineOutputStream =
                new BufferedLineSeparatorOutputStream(lineSeparatorUtil.toString(), sortedXml);

        XMLOutputter xmlOutputter = new PatchedXMLOutputter(bufferedLineOutputStream, indentBlankLines);
        xmlOutputter.setFormat(createPrettyFormat());
        xmlOutputter.output(newDocument, bufferedLineOutputStream);

        IOUtils.closeQuietly(bufferedLineOutputStream);
        return sortedXml;
    }

    private Format createPrettyFormat() {
        final Format prettyFormat = Format.getPrettyFormat();
        prettyFormat.setExpandEmptyElements(expandEmptyElements);
        prettyFormat.setEncoding(encoding);
        prettyFormat.setLineSeparator("\n");
        prettyFormat.setIndent(indentCharacters);
        return prettyFormat;
    }

    private static class PatchedXMLOutputter extends XMLOutputter {
        private final BufferedLineSeparatorOutputStream bufferedLineOutputStream;
        private final boolean indentBlankLines;

        PatchedXMLOutputter(BufferedLineSeparatorOutputStream bufferedLineOutputStream, boolean indentBlankLines) {
            this.bufferedLineOutputStream = bufferedLineOutputStream;
            this.indentBlankLines = indentBlankLines;
            XMLOutputter.preserveFormat.setLineSeparator("\n");
        }

        /** Stop XMLOutputter from printing comment <!-- --> chars if it is just a newline */
        @Override
        protected void printComment(Writer stringWriter, Comment comment) throws IOException {
            if (comment instanceof NewlineText) {
                if (!indentBlankLines) {
                    clearIndentationForCurrentLine(stringWriter);
                }
            } else {
                super.printComment(stringWriter, comment);
            }
        }

        private void clearIndentationForCurrentLine(Writer stringWriter) throws IOException {
            // Force all xml lines to be written to stream (via the writer)
            stringWriter.flush();

            // Remove all inset that has just been written since last newline
            bufferedLineOutputStream.clearLineBuffer();
        }
    }
}
