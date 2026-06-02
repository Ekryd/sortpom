package sortpom.parameter;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The plugin parameter 'sortDependencies' is parsed by this class to determine if dependencies
 * should be sorted and in that case in which order.
 *
 * @author bjorn
 * @since 2012-09-14
 */
public class DependencySortOrder {

  private static final Pattern SPLITTER = Pattern.compile("[;,:]");

  private final String childElementNameList;
  private final Collection<String> childElementNames;
  private final List<String> priorityGroupIds;

  /**
   * Create an instance of the DependencySortOrder
   *
   * @param childElementNameList the plugin parameter argument
   */
  public DependencySortOrder(String childElementNameList) {
    this(childElementNameList, "");
  }

  public DependencySortOrder(String childElementNameList, String priorityGroupIdList) {
    this.childElementNameList = childElementNameList == null ? "" : childElementNameList;
    this.childElementNames = split(childElementNameList);
    this.priorityGroupIds = split(priorityGroupIdList);
  }

  /**
   * Gets a list of which elements the dependencies should be sorted after. Example: If the list
   * returns "scope, groupId" then the dependencies should be sorted first by scope and then by
   * groupId.
   *
   * @return a list of xml element names
   */
  public Collection<String> getChildElementNames() {
    return childElementNames;
  }

  public List<String> getPriorityGroupIds() {
    return priorityGroupIds;
  }

  /** Earlier versions only accepted the values 'true' and 'false' as parameter values */
  public boolean isDeprecatedValueTrue() {
    return "true".equalsIgnoreCase(childElementNameList);
  }

  /** Earlier versions only accepted the values 'true' and 'false' as parameter values */
  public boolean isDeprecatedValueFalse() {
    return "false".equalsIgnoreCase(childElementNameList);
  }

  /** If the dependencies should be unsorted */
  public boolean isNoSorting() {
    return getChildElementNames().isEmpty();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("DependencySortOrder{");
    builder.append("childElementNames=").append(getChildElementNames());
    if (!priorityGroupIds.isEmpty()) {
      builder.append(", priorityGroupIds=").append(priorityGroupIds);
    }
    builder.append("}");
    return builder.toString();
  }

  private static List<String> split(String list) {
    return SPLITTER
        .splitAsStream(list == null ? "" : list)
        .filter(s -> !s.isBlank())
        .map(String::trim)
        .toList();
  }
}
