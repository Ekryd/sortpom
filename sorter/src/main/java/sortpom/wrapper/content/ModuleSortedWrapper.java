package sortpom.wrapper.content;

import org.dom4j.Element;
import org.dom4j.Node;

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
   * @param element the element
   * @param sortOrder the sort order
   */
  public ModuleSortedWrapper(Element element, int sortOrder) {
    super(element, sortOrder);
    text = element.getTextTrim();
  }

  @Override
  public boolean isBefore(Wrapper<? extends Node> wrapper) {
    if (wrapper instanceof ModuleSortedWrapper) {
      return isBeforeAlphabeticalTextSortedWrapper((ModuleSortedWrapper) wrapper);
    }
    return super.isBefore(wrapper);
  }

  private boolean isBeforeAlphabeticalTextSortedWrapper(ModuleSortedWrapper wrapper) {
    // SortOrder will always be same for both ModuleSortedWrapper because there is only one tag
    // under modules
    // that is named module, see sortpom.wrapper.ElementWrapperCreator.isModuleElement.
    // So comparing getSortOrder is not needed.

    return wrapper.text.compareTo(text) >= 0;
  }

  @Override
  public String toString() {
    var element = getContent();
    return "ModuleSortedWrapper{content="
        + (element == null ? "null" : element.toString().replaceAll(".+@[^ ]+ ", ""))
        + '}';
  }
}
