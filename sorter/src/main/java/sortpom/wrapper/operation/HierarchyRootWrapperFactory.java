package sortpom.wrapper.operation;

import org.dom4j.Element;
import sortpom.wrapper.content.Wrapper;

public class HierarchyRootWrapperFactory {
    public HierarchyRootWrapper createFromWrapper(Wrapper<Element> wrapper) {
        return new HierarchyRootWrapper(wrapper);
    }
}