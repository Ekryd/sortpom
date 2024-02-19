package sortpom.util;

import java.io.StringWriter;
import java.io.Writer;

/**
 * Makes sure that all line endings are written in the same way. Keeps a buffer which is flushed
 * when newline is encountered. Removes the final trailing newline by delaying it.
 *
 * @author Bjorn
 */
public class StringLineSeparatorWriter extends Writer {
  private static final char NEWLINE = '\n';
  private final String lineSeparator;
  private boolean wasNewLine = false;
  private final StringBuilder lineBuffer = new StringBuilder();
  private final StringWriter out;

  public StringLineSeparatorWriter(StringWriter out, String lineSeparator) {
    this.out = out;
    this.lineSeparator = lineSeparator;
  }

  @Override
  public void write(String str) {
    var chars = str.toCharArray();
    for (var ch : chars) {
      writeOneCharacter(ch);
    }
  }

  @Override
  public void write(int c) {
    writeOneCharacter((char) c);
  }

  @Override
  public void write(char[] cb, int off, int len) {
    throw new UnsupportedOperationException();
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

  public void writeDelayedNewline() {
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

  /**
   * Remove everything that has happened since last line break. Used to clear empty lines in the XML
   */
  public void clearLineBuffer() {
    lineBuffer.delete(0, lineBuffer.length());
  }

  @Override
  public String toString() {
    writeCharacterBuffer();
    return out.toString();
  }

  /** Nope, no manual flushing */
  @Override
  public void flush() {
    throw new UnsupportedOperationException();
  }
}
