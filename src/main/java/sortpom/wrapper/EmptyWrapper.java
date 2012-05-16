package sortpom.wrapper;

import org.jdom.*;

/**
 * A wrapper that lets is element be unsorted
 *
 * @author Bjorn
 */
public class EmptyWrapper implements Wrapper<Text> {

    /**
     * The wrapped dom content.
     */
    private final Text content;

    /**
     * Instantiates a new unsorted wrapper.
     *
     * @param content the content
     */
    public EmptyWrapper(final Text content) {
        this.content = content;
    }

    /**
      * @see Wrapper#getContent()
      */
    @Override
    public Text getContent() {
        return content;
    }

    /**
      * @see Wrapper#isBefore(Wrapper)
      */
    @Override
    public boolean isBefore(final Wrapper<? extends Content> wrapper) {
        return false;
    }

    /**
      * @see Wrapper#isContentElement()
      */
    @Override
    public boolean isContentElement() {
        return false;
    }

    /**
      * @see Wrapper#isResortable()
      */
    @Override
    public boolean isResortable() {
        return false;
    }

    @Override
    public String toString() {
        return "EmptyWrapper{" +
                "content=" + content +
                '}';
    }

    @Override
    public String toString(String indent) {
        return indent + toString();
    }
}
