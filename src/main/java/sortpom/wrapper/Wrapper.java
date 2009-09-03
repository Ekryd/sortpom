package sortpom.wrapper;

import org.jdom.Content;

/**
 * En inkapsling av ett xml-fragment. Fragmentetet kan vara t.ex. ett element eller en kommentar.
 *
 * @param <T>  *
 * @author Bjorn
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
	 *
	 * @return true, if is before
	 */
	boolean isBefore(Wrapper<? extends Content> wrapper);

	/**
	 * Checks if wrapper has higher sortorder than another
	 *
	 * @param wrapper the wrapper
	 *
	 * @return true, if is bigger sort order
	 */
	boolean isBiggerSortOrder(Wrapper<? extends Content> wrapper);

	/**
	 * Checks if is content is of type Element.
	 *
	 * @return true, if is content element
	 */
	boolean isContentElement();

	/**
	 * Checks if wrapper should be sorted.
	 *
	 * @return true, if is resortable
	 */
	boolean isResortable();
}
