package sortpom.wrapper;

import org.jdom.Element;
import sortpom.parameter.DependencySortOrder;
import sortpom.parameter.PluginParameters;

/**
 * @author bjorn
 * @since 2012-05-19
 */
public class ElementWrapperCreator {
    private DependencySortOrder sortDependencies;
    private DependencySortOrder sortPlugins;
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
                GroupAndArtifactSortedWrapper groupAndArtifactSortedWrapper = new GroupAndArtifactSortedWrapper(element, elementNameSortOrderMap.getSortOrder(element));
                groupAndArtifactSortedWrapper.setSortOrder(sortDependencies.getChildElementNames());
                return groupAndArtifactSortedWrapper;
            }
            if (isPluginElement(element)) {
                GroupAndArtifactSortedWrapper groupAndArtifactSortedWrapper = new GroupAndArtifactSortedWrapper(element, elementNameSortOrderMap.getSortOrder(element));
                groupAndArtifactSortedWrapper.setSortOrder(sortPlugins.getChildElementNames());
                return groupAndArtifactSortedWrapper;
            }
            return new SortedWrapper(element, elementNameSortOrderMap.getSortOrder(element));
        }
        if (isPropertyElement(element)) {
            return new AlphabeticalSortedWrapper(element);
        }
        return new UnsortedWrapper<Element>(element);
    }

    private boolean isDependencyElement(final Element element) {
        if (!sortDependencies.hasSortValues()) {
            return false;
        }
        return isElementName(element, "dependency") && isElementParentName(element, "dependencies");
    }

    private boolean isPluginElement(final Element element) {
        if (!sortPlugins.hasSortValues()) {
            return false;
        }
        if (isElementName(element, "plugin")) {
            return isElementParentName(element, "plugins") || isElementParentName(element, "reportPlugins");
        }
        return false;
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
        Element parent = element.getParentElement();
        if (parent == null) {
            return false;
        }
        return isElementName(parent, name);
    }

    private boolean isElementName(Element element, String name) {
        return element.getName().equals(name);
    }

}
