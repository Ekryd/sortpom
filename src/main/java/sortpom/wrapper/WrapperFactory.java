package sortpom.wrapper;

import org.jdom.Content;
import org.jdom.Element;

/**
 * Skapar wrappers kring xml-fragment
 *
 * @author Bjorn
 *
 */
public interface WrapperFactory {

	/**
	 * @param properties
	 */
	public void initialize();

	WrapperOperations create(final Element rootElement);

	<T extends Content> Wrapper<T> create(final T content);
}
