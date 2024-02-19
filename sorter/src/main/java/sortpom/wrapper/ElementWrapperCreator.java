package sortpom.wrapper;

import static sortpom.wrapper.ElementUtil.*;

import org.dom4j.Element;
import sortpom.parameter.DependencySortOrder;
import sortpom.parameter.PluginParameters;
import sortpom.wrapper.content.*;

/**
 * Create wrappers around xml elements. The wrappers help to sort the XML element among themselves.
 */
public class ElementWrapperCreator {
  private DependencySortOrder sortDependencies;
  private DependencySortOrder sortDependencyExclusions;
  private DependencySortOrder sortDependencyManagement;
  private DependencySortOrder sortPlugins;
  private boolean sortProperties;
  private boolean sortModules;
  private boolean sortExecutions;

  private final ElementSortOrderMap elementNameSortOrderMap;

  ElementWrapperCreator(ElementSortOrderMap elementNameSortOrderMap) {
    this.elementNameSortOrderMap = elementNameSortOrderMap;
  }

  public void setup(PluginParameters pluginParameters) {
    this.sortDependencies = pluginParameters.sortDependencies;
    this.sortDependencyExclusions = pluginParameters.sortDependencyExclusions;
    this.sortDependencyManagement = pluginParameters.sortDependencyManagement;
    this.sortPlugins = pluginParameters.sortPlugins;
    this.sortProperties = pluginParameters.sortProperties;
    this.sortModules = pluginParameters.sortModules;
    this.sortExecutions = pluginParameters.sortExecutions;
  }

  Wrapper<Element> createWrapper(Element element) {
    var sortedBySortOrderFile = elementNameSortOrderMap.containsElement(element);
    if (sortedBySortOrderFile) {
      if (isDependencyElement(element)) {
        return createdDependencySortedWrapper(element);
      }
      if (isExclusionElement(element)) {
        var exclusionSortedWrapper =
            new ExclusionSortedWrapper(element, elementNameSortOrderMap.getSortOrder(element));
        exclusionSortedWrapper.setSortOrder(sortDependencyExclusions);
        return exclusionSortedWrapper;
      }
      if (isPluginElement(element)) {
        var pluginSortedWrapper =
            new PluginSortedWrapper(element, elementNameSortOrderMap.getSortOrder(element));
        pluginSortedWrapper.setSortOrder(sortPlugins);
        return pluginSortedWrapper;
      }
      if (isModuleElement(element)) {
        return new ModuleSortedWrapper(element, elementNameSortOrderMap.getSortOrder(element));
      }
      if (isExecutionElement(element)) {
        return new ExecutionSortedWrapper(element, elementNameSortOrderMap.getSortOrder(element));
      }
      return new SortedWrapper(element, elementNameSortOrderMap.getSortOrder(element));
    }
    if (isPropertyElement(element)) {
      return new AlphabeticalSortedWrapper(element);
    }
    return new UnsortedWrapper<>(element);
  }

  /** Create separate wrapper for dependency and dependency mgmt. Dependency setting is fallback */
  private DependencySortedWrapper createdDependencySortedWrapper(Element element) {
    var dependencySortedWrapper =
        new DependencySortedWrapper(element, elementNameSortOrderMap.getSortOrder(element));
    if (isDependencyElementInManagement(element) && !sortDependencyManagement.isNoSorting()) {
      dependencySortedWrapper.setSortOrder(sortDependencyManagement);
    } else {
      dependencySortedWrapper.setSortOrder(sortDependencies);
    }
    return dependencySortedWrapper;
  }

  private boolean isDependencyElementInManagement(Element element) {
    return isElementParentName(element.getParent(), "dependencyManagement");
  }

  private boolean isDependencyElement(Element element) {
    if (sortDependencies.isNoSorting() && sortDependencyManagement.isNoSorting()) {
      return false;
    }
    return isElementName(element, "dependency") && isElementParentName(element, "dependencies");
  }

  private boolean isExclusionElement(Element element) {
    if (sortDependencyExclusions.isNoSorting()) {
      return false;
    }
    return isElementName(element, "exclusion") && isElementParentName(element, "exclusions");
  }

  private boolean isPluginElement(Element element) {
    if (sortPlugins.isNoSorting()) {
      return false;
    }
    return isElementName(element, "plugin")
        && (isElementParentName(element, "plugins")
            || isElementParentName(element, "reportPlugins"));
  }

  private boolean isModuleElement(Element element) {
    if (!sortModules) {
      return false;
    }
    return isElementName(element, "module") && isElementParentName(element, "modules");
  }

  private boolean isExecutionElement(Element element) {
    if (!sortExecutions) {
      return false;
    }
    return isElementName(element, "execution") && isElementParentName(element, "executions");
  }

  private boolean isPropertyElement(Element element) {
    if (!sortProperties) {
      return false;
    }
    var deepName = getDeepName(element);
    var inTheRightPlace =
        deepName.startsWith("/project/properties/")
            || deepName.startsWith("/project/profiles/profile/properties/");
    return inTheRightPlace && isElementParentName(element, "properties");
  }
}
