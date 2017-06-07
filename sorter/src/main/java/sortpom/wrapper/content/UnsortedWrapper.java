package sortpom.wrapper.content;

import org.jdom.Content;
import org.jdom.Element;

/**
 * A wrapper that lets is element be unsorted
 *
 * @param <T>
 * @author Bjorn
 */
public class UnsortedWrapper<T extends Content> implements Wrapper<T> {

    /** The wrapped dom content. */
    private final T content;

    /**
     * Instantiates a new unsorted wrapper.
     *
     * @param content the content
     */
    public UnsortedWrapper(final T content) {
        this.content = content;
    }

    /** @see Wrapper#getContent() */
    @Override
    public T getContent() {
        return content;
    }

    /** @see Wrapper#isBefore(Wrapper) */
    @Override
    public boolean isBefore(final Wrapper<? extends Content> wrapper) {
        throw new UnsupportedOperationException("Cannot be sorted");
    }

    /** @see Wrapper#isContentElement() */
    @Override
    public boolean isContentElement() {
        return content instanceof Element;
    }

    /** @see Wrapper#isSortable() */
    @Override
    public boolean isSortable() {
        return false;
    }

    @Override
    public String toString() {
        return "UnsortedWrapper{" +
                "content=" + content +
                '}';
    }
}
