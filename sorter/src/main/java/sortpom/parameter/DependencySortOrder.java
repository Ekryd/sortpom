package sortpom.parameter;

import java.util.Arrays;

/**
 * @author bjorn
 * @since 2012-09-14
 */
public class DependencySortOrder {
    private final String childElementNameList;
    private String[] childElementNames;

    public DependencySortOrder(String childElementNameList) {
        this.childElementNameList = childElementNameList == null ? "" : childElementNameList;
    }

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

    public boolean isDeprecatedValueTrue() {
        return childElementNameList.equalsIgnoreCase("true");
    }

    public boolean isDeprecatedValueFalse() {
        return childElementNameList.equalsIgnoreCase("false");
    }

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
