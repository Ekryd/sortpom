package sortpom.wrapper.content;

import org.jdom.Content;
import org.jdom.Element;
import sortpom.parameter.DependencySortOrder;

import java.util.List;

/**
 * A wrapper that contains a module element. The module is sorted alphabetically.
 *
 * @author msbg
 */
public class ModuleSortedWrapper extends SortedWrapper {
    private final String text;
    /**
     * Instantiates a new child element sorted wrapper with a module element.
     *
     * @param element   the element
     * @param sortOrder the sort order
     */
    public ModuleSortedWrapper(final Element element, final int sortOrder) {
        super(element, sortOrder);
        text = element.getTextTrim();
    }
    

    @Override
    public boolean isBefore(final Wrapper<? extends Content> wrapper) {
        if (wrapper instanceof ModuleSortedWrapper) {
            return isBeforeDependencySortedWrapper((ModuleSortedWrapper) wrapper);
        }
        return super.isBefore(wrapper);
    }

    private boolean isBeforeDependencySortedWrapper(final ModuleSortedWrapper wrapper) {
        // Sort order rules before sorting alphabetic contents.
        if (wrapper.getSortOrder() != getSortOrder()) {
            return super.isBefore(wrapper);
        }

        return isBeforeAlphabeticalTextSortedWrapper(wrapper);
    }

    private boolean isBeforeAlphabeticalTextSortedWrapper(ModuleSortedWrapper wrapper) {
        return wrapper.text.compareTo(text) >= 0;
    }

    @Override
    public String toString() {
        return "ModuleSortedWrapper{content=" +
                getContent() +
                '}';
    }

}
