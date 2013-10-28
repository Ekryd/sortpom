package sortpom.wrapper;

import org.jdom.Content;
import org.jdom.Element;

/**
 * Creates wrappers around xml fragments.
 *
 * @author Bjorn Ekryd
 */
public interface WrapperFactory {

    /** Creates wrapper around a root element. */
    GroupWrapper createFromRootElement(final Element rootElement);

    /** Creates wrapper around xml content. */
    <T extends Content> Wrapper<T> create(final T content);

}
