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

	WrapperOperations create(final Element rootElement);

	<T extends Content> Wrapper<T> create(final T content);

	void initialize();
}
