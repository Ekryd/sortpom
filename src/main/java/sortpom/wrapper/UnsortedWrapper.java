package sortpom.wrapper;

import org.jdom.Content;
import org.jdom.Element;

/**
 * A wrapper that lets is element be unsorted
 *
 * @author Bjorn
 * @param <T>
 */
public class UnsortedWrapper<T extends Content> implements Wrapper<T> {

    /**
     * The content.
     */
    private final T content;

    /**
     * Instantiates a new unsorted wrapper.
     *
     * @param content the content
     */
    public UnsortedWrapper(final T content) {
        this.content = content;
    }

    /* (non-Javadoc)
      * @see sortpom.wrapper.Wrapper#getContent()
      */

    @Override
    public T getContent() {
        return content;
    }

    /* (non-Javadoc)
      * @see sortpom.wrapper.Wrapper#isBefore(sortpom.wrapper.Wrapper)
      */

    @Override
    public boolean isBefore(final Wrapper<? extends Content> wrapper) {
        return false;
    }

    /* (non-Javadoc)
      * @see sortpom.wrapper.Wrapper#isBiggerSortOrder(sortpom.wrapper.Wrapper)
      */

    @Override
    public boolean isBiggerSortOrder(final Wrapper<? extends Content> maxSortValue) {
        return false;
    }

    /* (non-Javadoc)
      * @see sortpom.wrapper.Wrapper#isContentElement()
      */

    @Override
    public boolean isContentElement() {
        return content instanceof Element;
    }

    /* (non-Javadoc)
      * @see sortpom.wrapper.Wrapper#isResortable()
      */

    @Override
    public boolean isResortable() {
        return false;
    }

}
