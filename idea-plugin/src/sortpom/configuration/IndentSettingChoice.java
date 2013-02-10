package sortpom.configuration;

/**
 * @author bjorn
 * @since 2013-01-02
 */
public enum IndentSettingChoice {
    TAB(-1, "Tab character"),
    SPACE_0(0, "Nothing"),
    SPACE_1(1, "1 space"),
    SPACE_2(2, "2 spaces"),
    SPACE_3(3, "3 spaces"),
    SPACE_4(4, "4 spaces"),
    SPACE_6(6, "6 spaces"),
    SPACE_8(8, "8 spaces");

    public final int parameterValue;
    public final String dropDownValue;

    private IndentSettingChoice(int parameterValue, String dropDownValue) {
        this.parameterValue = parameterValue;
        this.dropDownValue = dropDownValue;
    }

    public static String[] getDropDownValues() {
        return new String[]{
                TAB.dropDownValue, SPACE_0.dropDownValue,
                SPACE_1.dropDownValue, SPACE_2.dropDownValue,
                SPACE_3.dropDownValue, SPACE_4.dropDownValue,
                SPACE_6.dropDownValue, SPACE_8.dropDownValue};
    }

    public static IndentSettingChoice fromParameter(int parameterValue) {
        for (IndentSettingChoice indentSettingChoice : values()) {
            if (indentSettingChoice.parameterValue == parameterValue)
                return indentSettingChoice;
        }
        throw new IllegalArgumentException("Cannot find Indent: " + parameterValue);
    }

    public static IndentSettingChoice fromDropDown(String dropDownValue) {
        for (IndentSettingChoice indentSettingChoice : values()) {
            if (indentSettingChoice.dropDownValue.equalsIgnoreCase(dropDownValue))
                return indentSettingChoice;
        }
        throw new IllegalArgumentException("Cannot find Indent: " + dropDownValue);
    }

}
