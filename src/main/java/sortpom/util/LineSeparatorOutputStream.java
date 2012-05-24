package sortpom.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Makes sure that all line endings are written in the same way.
 *
 * @author Bjorn
 */
public class LineSeparatorOutputStream extends OutputStream {
    private static final int NEWLINE = '\n';
    private final OutputStream wrappedStream;
    private final char[] lineSeparator;
    private boolean wasNewLine = false;

    public LineSeparatorOutputStream(final String lineSeparator, final OutputStream streamWithNewlinesAsLineSeparator) {
        this.lineSeparator = lineSeparator.toCharArray();
        this.wrappedStream = streamWithNewlinesAsLineSeparator;
    }

    @Override
    public void close() throws IOException {
        wrappedStream.close();
    }

    @Override
    public void write(final int b) throws IOException {
        writeDelayedNewline();
        switch (b) {
            case NEWLINE:
                wasNewLine = true;
                return;
            default:
                wrappedStream.write(b);
        }

    }

    private void writeDelayedNewline() throws IOException {
        if (wasNewLine) {
            writeLineSeparator();
            wasNewLine = false;
        }
    }

    private void writeLineSeparator() throws IOException {
        for (char ch : lineSeparator) {
            wrappedStream.write(ch);
        }
    }
}
