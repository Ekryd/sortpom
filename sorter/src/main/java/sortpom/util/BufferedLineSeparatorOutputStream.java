package sortpom.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Makes sure that all line endings are written in the same way. Keeps a buffer
 * which is flushed when newline is encountered.
 * Removes the final trailing newline by delaying it.
 *
 * @author Bjorn
 */
public class BufferedLineSeparatorOutputStream extends OutputStream {
    private static final int NEWLINE = '\n';
    private final OutputStream wrappedStream;
    private final char[] lineSeparator;
    private boolean wasNewLine = false;
    private final List<Integer> lineBuffer = new ArrayList<Integer>();

    public BufferedLineSeparatorOutputStream(final String lineSeparator, final OutputStream streamWithNewlinesAsLineSeparator) {
        this.lineSeparator = lineSeparator.toCharArray();
        this.wrappedStream = streamWithNewlinesAsLineSeparator;
    }

    @Override
    public void close() throws IOException {
        writeCharacterBuffer();
        wrappedStream.close();
    }

    public void clearLineBuffer() {
        lineBuffer.clear();
    }

    @Override
    public void write(final int b) throws IOException {
        writeDelayedNewline();
        if (b == NEWLINE) {
            writeCharacterBuffer();
            wasNewLine = true;
        } else {
            lineBuffer.add(b);
        }

    }

    private void writeDelayedNewline() throws IOException {
        if (wasNewLine) {
            writeLineSeparator();
            wasNewLine = false;
        }
    }

    private void writeCharacterBuffer() throws IOException {
        for (Integer ch : lineBuffer) {
            wrappedStream.write(ch);
        }
        lineBuffer.clear();
    }

    private void writeLineSeparator() throws IOException {
        for (char ch : lineSeparator) {
            wrappedStream.write(ch);
        }
    }
}
