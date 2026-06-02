package sortpom.wrapper.content;

import java.util.ArrayList;
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

  private final List<String> priorityGroupIds = new ArrayList<>();

  public ChildElementSorter(DependencySortOrder dependencySortOrder, List<Element> children) {
    this.priorityGroupIds.addAll(dependencySortOrder.getPriorityGroupIds());
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
    if ("groupId".equalsIgnoreCase(key)) {
      return compareGroupId(text, otherText);
    }
    return text.compareToIgnoreCase(otherText);
  }

  private int compareGroupId(String childElementText, String otherChildElementText) {
    String groupId = resolveComparableGroupId(childElementText);
    String otherGroupId = resolveComparableGroupId(otherChildElementText);
    return groupId.compareToIgnoreCase(otherGroupId);
  }

  /**
   * When <code>groupId</code> is in the priority list, adds a prefix to ensure proper sort order.
   */
  private String resolveComparableGroupId(String groupId) {
    int priority = priorityGroupIds.indexOf(groupId);
    return (priority < 0) ? groupId : "!%02d:%s".formatted(priority, groupId);
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
    StringBuilder builder = new StringBuilder("ChildElementSorter{");
    builder.append("childElementTexts=").append(childElementTextMappedBySortedNames.values());
    if (!priorityGroupIds.isEmpty()) {
      builder.append(", priorityGroupIds=").append(priorityGroupIds);
    }
    builder.append("}");
    return builder.toString();
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
