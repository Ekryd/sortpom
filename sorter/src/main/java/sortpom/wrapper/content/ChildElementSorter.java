package sortpom.wrapper.content;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.dom4j.Element;
import sortpom.parameter.DependencySortOrder;

/**
 * @author bjorn
 * @since 2012-09-20
 */
public class ChildElementSorter {
  static final ChildElementSorter EMPTY_SORTER = new ChildElementSorter();
  private static final String GROUP_ID_NAME = "GROUPID";
  private static final String EMPTY_PLUGIN_GROUP_ID_VALUE = "org.apache.maven.plugins";

  private final LinkedHashMap<String, String> childElementTextMappedBySortedNames =
      new LinkedHashMap<>();

  public ChildElementSorter(DependencySortOrder dependencySortOrder, List<Element> children) {
    var childElementNames = dependencySortOrder.getChildElementNames();

    childElementNames.forEach(
        name -> childElementTextMappedBySortedNames.put(name.toUpperCase(), ""));

    children.forEach(
        element ->
            childElementTextMappedBySortedNames.replace(
                element.getName().toUpperCase(), element.getTextTrim()));
  }

  private ChildElementSorter() {}

  boolean compareTo(ChildElementSorter otherChildElementSorter) {
    Function<Map.Entry<String, String>, String> getOtherTextFunc =
        entry -> otherChildElementSorter.childElementTextMappedBySortedNames.get(entry.getKey());

    int compare =
        childElementTextMappedBySortedNames.entrySet().stream()
            .map(
                entry ->
                    compareTexts(entry.getKey(), entry.getValue(), getOtherTextFunc.apply(entry)))
            .filter(i -> i != 0)
            .findFirst()
            .orElse(0);

    return compare < 0;
  }

  private int compareTexts(String key, String text, String otherText) {
    if ("scope".equalsIgnoreCase(key)) {
      return compareScope(text, otherText);
    }
    return text.compareToIgnoreCase(otherText);
  }

  private int compareScope(String childElementText, String otherChildElementText) {
    return Scope.getScope(childElementText).compareTo(Scope.getScope(otherChildElementText));
  }

  void emptyGroupIdIsFilledWithDefaultMavenGroupId() {
    childElementTextMappedBySortedNames.computeIfPresent(
        GROUP_ID_NAME,
        (k, oldValue) -> oldValue.isEmpty() ? EMPTY_PLUGIN_GROUP_ID_VALUE : oldValue);
  }

  @Override
  public String toString() {
    return "ChildElementSorter{"
        + "childElementTexts="
        + childElementTextMappedBySortedNames.values()
        + '}';
  }

  private enum Scope {
    IMPORT,
    COMPILE,
    PROVIDED,
    SYSTEM,
    RUNTIME,
    TEST,
    OTHER;

    static Scope getScope(String scope) {
      if (scope == null || scope.isEmpty()) {
        return COMPILE;
      }
      var values = Scope.values();
      for (var value : values) {
        if (scope.equalsIgnoreCase(value.name())) {
          return value;
        }
      }
      return OTHER;
    }
  }
}
