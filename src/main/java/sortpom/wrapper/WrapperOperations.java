package sortpom.wrapper;

import java.util.List;

import org.jdom.Content;

/**
 * Specificerar ett antal övergripande funktioner som arbetar mot en struktur av Wrappers
 *
 * @author Bjorn
 *
 */
public interface WrapperOperations {

	void createWrappedStructure(WrapperFactory factory);

	void detachStructure();

	List<Content> getWrappedStructure();

	void sortStructureAttributes();

	void sortStructureElements();

}
