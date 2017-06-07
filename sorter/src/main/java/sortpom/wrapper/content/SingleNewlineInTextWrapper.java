package sortpom.wrapper.content;

import org.jdom.Content;
import org.jdom.Text;

/**
 * A wrapper that contains a single newline, that will be thrown away.
 *
 * @author Bjorn
 */
public final class SingleNewlineInTextWrapper implements Wrapper<Content> {
    public static final SingleNewlineInTextWrapper INSTANCE = new SingleNewlineInTextWrapper();

    /** Instantiates a new wrapper, whose content will be thrown away. */
    private SingleNewlineInTextWrapper() {
    }

    @Override
    public Text getContent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBefore(final Wrapper<? extends Content> wrapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isContentElement() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSortable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "SingleNewlineInTextWrapper";
    }
}
