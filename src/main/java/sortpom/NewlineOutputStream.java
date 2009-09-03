package sortpom;

import java.io.IOException;
import java.io.OutputStream;

import sortpom.util.FileUtil;

/**
 * Makes sure that all newlines are stored in the same way.
 *
 * @author Bjorn
 *
 */
public class NewlineOutputStream extends OutputStream {
	private final OutputStream wrappedStream;
	private boolean lastWasCarrageReturn = false;
	private final char[] lineSeparator;
	private static final int CARRAGE_RETURN = '\r';
	private static final int NEWLINE = '\n';

	public NewlineOutputStream(final FileUtil fileUtil, final OutputStream wrappedStream) {
		this.lineSeparator = fileUtil.getLineSeparator().toCharArray();
		this.wrappedStream = wrappedStream;
	}

	@Override
	public void close() throws IOException {
		if (lastWasCarrageReturn) {
			writeLineSeparator();
		}
		wrappedStream.close();
	}

	@Override
	public void write(final int b) throws IOException {
		switch (b) {
			case CARRAGE_RETURN:
				if (lastWasCarrageReturn) {
					writeLineSeparator();
				} else {
					lastWasCarrageReturn = true;
				}
				return;
			case NEWLINE:
				lastWasCarrageReturn = false;
				writeLineSeparator();
				return;
			default:
				lastWasCarrageReturn = false;
				if (lastWasCarrageReturn) {
					writeLineSeparator();
				}
				wrappedStream.write(b);
		}

	}

	private void writeLineSeparator() throws IOException {
		for (char ch : lineSeparator) {
			wrappedStream.write(ch);
		}
	}
}
