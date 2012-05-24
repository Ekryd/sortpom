package sortpom.wrapper;

import org.jdom.Element;
import org.jdom.Parent;
import sortpom.PluginParameters;

/**
 * @author bjorn
 * @since 2012-05-19
 */
public class ElementWrapperCreator {
    private boolean sortDependencies;
    private boolean sortPlugins;
    private boolean sortProperties;
    private final ElementSortOrderMap elementNameSortOrderMap;


    public ElementWrapperCreator(ElementSortOrderMap elementNameSortOrderMap) {
        this.elementNameSortOrderMap = elementNameSortOrderMap;
    }

    public void setup(PluginParameters pluginParameters) {
        this.sortDependencies = pluginParameters.sortDependencies;
        this.sortPlugins = pluginParameters.sortPlugins;
        this.sortProperties = pluginParameters.sortProperties;
    }

    public Wrapper<Element> createWrapper(Element element) {
        boolean sortedBySortOrderFile = elementNameSortOrderMap.containsElement(element);
        if (sortedBySortOrderFile) {
            if (isDependencyElement(element)) {
                return new GroupAndArtifactSortedWrapper(element, elementNameSortOrderMap.getSortOrder(element));
            }
            if (isPluginElement(element)) {
                return new GroupAndArtifactSortedWrapper(element, elementNameSortOrderMap.getSortOrder(element));
            }
            return new SortedWrapper(element, elementNameSortOrderMap.getSortOrder(element));
        }
        if (isPropertyElement(element)) {
            return new AlphabeticalSortedWrapper(element);
        }
        return new UnsortedWrapper<Element>(element);
    }

    private boolean isDependencyElement(final Element element) {
        if (!sortDependencies) {
            return false;
        }
        return isElementName(element, "dependency") && isElementParentName(element, "dependencies");
    }

    private boolean isPluginElement(final Element element) {
        if (!sortPlugins) {
            return false;
        }
        return isElementName(element, "plugin") && isElementParentName(element, "plugins");
    }

    private boolean isPropertyElement(final Element element) {
        if (!sortProperties) {
            return false;
        }
        String deepName = elementNameSortOrderMap.getDeepName(element);
        boolean inTheRightPlace = deepName.startsWith("/project/properties/")
                || deepName.startsWith("/project/profiles/profile/properties/");
        return inTheRightPlace && isElementParentName(element, "properties");
    }

    private boolean isElementParentName(Element element, String name) {
        Parent parent = element.getParent();
        if (parent instanceof Element) {
            return isElementName((Element) parent, name);
        }
        return false;
    }

    private boolean isElementName(Element element, String name) {
        return element.getName().equals(name);
    }

}
