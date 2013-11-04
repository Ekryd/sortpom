package sortpom.wrapper.operation;

import org.jdom.Element;
import sortpom.wrapper.content.Wrapper;

import java.util.List;

/**
 * Xml hierarchy operation that all xml elements. Used by 
 * Used in HierarchyWrapper.processOperation(HierarchyWrapperOperation operation)
 * @author bjorn
 * @since 2013-11-01
 */
public class SortChildrenOperation extends HierarchyWrapperOperation {
    
    /** Sort all children of an element */
    @Override
    public void manipulateChildElements(List<HierarchyWrapper> children) {
        for (int i = 0; i < children.size(); i++) {
            HierarchyWrapper wrapperImpl = children.get(i);
            final Wrapper<Element> wrapper = wrapperImpl.getElementContent();

            if (wrapper != null && wrapper.isSortable()) {
                boolean done = false;
                for (int j = 0; !done && j < i; j++) {
                    if (wrapper.isBefore(children.get(j).getElementContent())) {
                        children.remove(i);
                        children.add(j, wrapperImpl);
                        done = true;
                    }
                }
            }
        }

    }
}
