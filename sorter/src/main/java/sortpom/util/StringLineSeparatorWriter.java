package sortpom.util;

import java.io.StringWriter;
import java.io.Writer;

/**
 * Makes sure that all line endings are written in the same way. Keeps a buffer
 * which is flushed when newline is encountered.
 * Removes the final trailing newline by delaying it.
 *
 * @author Bjorn
 */
public class StringLineSeparatorWriter extends Writer {
    private static final char NEWLINE = '\n';
    private final String lineSeparator;
    private boolean wasNewLine = false;
    private final StringBuffer lineBuffer = new StringBuffer();
    private final StringWriter out = new StringWriter();

    public StringLineSeparatorWriter(final String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    @Override
    public void write(String str) {
        char[] chars = str.toCharArray();
        for (char ch : chars) {
            writeOneCharacter(ch);
        }
    }

    @Override
    public void write(int c) {
        writeOneCharacter((char) c);
    }

    @Override
    public void close() {
        writeCharacterBuffer();
    }

    private void writeOneCharacter(char ch) {
        writeDelayedNewline();
        if (ch == NEWLINE) {
            writeCharacterBuffer();
            wasNewLine = true;
        } else {
            lineBuffer.append(ch);
        }
    }

    private void writeDelayedNewline() {
        if (wasNewLine) {
            writeLineSeparator();
            wasNewLine = false;
        }
    }

    private void writeCharacterBuffer() {
        out.write(lineBuffer.toString());
        clearLineBuffer();
    }

    private void writeLineSeparator() {
        out.write(lineSeparator);
    }

    public void clearLineBuffer() {
        lineBuffer.delete(0, lineBuffer.length());
    }

    @Override
    public String toString() {
        return out.toString();
    }

    @Override
    public void write(char[] cbuf) {
        throw new UnsupportedOperationException("Only write string is allowed");
    }

    @Override
    public void write(String str, int off, int len) {
        throw new UnsupportedOperationException("Only write string is allowed");
    }

    @Override
    public void write(char[] cbuf, int off, int len) {
        throw new UnsupportedOperationException("Only write string is allowed");
    }

    @Override
    public void flush() {
        // Nope, no manual flushing
    }
}
