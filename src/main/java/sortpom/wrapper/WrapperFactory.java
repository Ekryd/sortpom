package sortpom.wrapper;

import org.jdom.Content;
import org.jdom.Element;

/**
 * Creates wrappers around xml fragments.
 *
 * @author Bjorn Ekryd
 *
 */
public interface WrapperFactory {

	/**
	 * Creates wrapper around a root element.
	 * @param rootElement
	 * @return
	 */
	WrapperOperations createFromRootElement(final Element rootElement);

	/**
	 * Creates wrapper around xml content.
	 */
	<T extends Content> Wrapper<T> create(final T content);

	/**
	 * Initializes the factory before the sorting the pom.
	 */
	void initialize();
}
