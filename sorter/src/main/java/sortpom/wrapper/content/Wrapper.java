package sortpom.wrapper.content;

import org.jdom.Content;

/**
 * A wrapping of an xml fragment. The fragment might be an element or a comment etc.
 *
 * @param <T> *
 * @author Bjorn Ekryd
 */
public interface Wrapper<T extends Content> {

    /**
     * Gets the wrapped content.
     *
     * @return the content
     */
    T getContent();

    /**
     * Checks if wrapper should be placed before another wrapper.
     *
     * @param wrapper the wrapper
     * @return true, if is before
     */
    boolean isBefore(Wrapper<? extends Content> wrapper);

    /**
     * Checks if is content is of type Element.
     *
     * @return true, if is content element
     */
    boolean isContentElement();

    /**
     * Checks if wrapper should be sorted.
     *
     * @return true, if is sortable
     */
    boolean isSortable();

    /**
     * Output debug-friendly string
     *
     * @param indent The indentation indicates nested elements
     * @return The debug string
     */
    String toString(String indent);
}
