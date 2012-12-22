package sortpom.wrapper;

import org.jdom.Content;

import java.util.List;

/**
 * Specifies a number of operations that work with wrapper structures.
 *
 * @author Bjorn Ekryd
 */
public interface WrapperOperations {

    void createWrappedStructure(WrapperFactory factory);

    void detachStructure();

    List<Content> getWrappedStructure();

    /** Sorts the attributes of the xml elements */
    void sortStructureAttributes();

    void sortStructureElements();

}
