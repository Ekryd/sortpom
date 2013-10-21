package sortpom.parameter;

import java.util.Arrays;

/**
 * The plugin parameter 'sortDependencies' is parsed by this class to determine if dependencies should be sorted
 * and in that case in which order.
 *
 * @author bjorn
 * @since 2012-09-14
 */
public class DependencySortOrder {
    private final String childElementNameList;
    private String[] childElementNames;

    /**
     * Create an instance of the DependencySortOrder
     *
     * @param childElementNameList the plugin parameter argument
     */
    public DependencySortOrder(String childElementNameList) {
        this.childElementNameList = childElementNameList == null ? "" : childElementNameList;
    }

    /**
     * Gets a list of which elements the dependencies should be sorted after. Example:
     * If the list returns "scope, groupId" then the dependencies should be sorted first by scope and then by
     * groupId.
     *
     * @return a list of xml element names
     */
    public String[] getChildElementNames() {
        if (childElementNames == null) {
            childElementNames = parseChildElementNameList();
        }
        return childElementNames;
    }

    private String[] parseChildElementNameList() {
        if (isDeprecatedValueFalse()) {
            return new String[0];
        }
        if (isDeprecatedValueTrue()) {
            return new String[]{"groupId", "artifactId"};
        }
        String list = childElementNameList.replaceAll("\\s", "");
        return list.split(";|,|:");
    }

    /** Earlier versions only accepted the values 'true' and 'false' as parameter values */
    public boolean isDeprecatedValueTrue() {
        return childElementNameList.equalsIgnoreCase("true");
    }

    /** Earlier versions only accepted the values 'true' and 'false' as parameter values */
    public boolean isDeprecatedValueFalse() {
        return childElementNameList.equalsIgnoreCase("false");
    }

    /** If the dependencies should be unsorted */
    public boolean isNoSorting() {
        return getChildElementNames().length == 0;
    }

    @Override
    public String toString() {
        return "DependencySortOrder{" +
                "childElementNames=" + Arrays.asList(getChildElementNames()) +
                '}';
    }
}
