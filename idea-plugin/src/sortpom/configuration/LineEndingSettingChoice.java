package sortpom.configuration;

/**
 * @author bjorn
 * @since 2013-01-02
 */
public enum LineEndingSettingChoice {
    UNIX("\\n", "\\n (Unix style)"), 
    WIN("\\r\\n", "\\r\\n (Win style)"), 
    OLD_MAC("\\r", "\\r (Old Mac style)");
    
    public final String parameterValue;
    public final String dropDownValue;

    private LineEndingSettingChoice(String parameterValue, String dropDownValue) {
        this.parameterValue = parameterValue;
        this.dropDownValue = dropDownValue;
    }

    public static String[] getDropDownValues() {
        return new String[]{ UNIX.dropDownValue, WIN.dropDownValue, OLD_MAC.dropDownValue };
    }

    public static LineEndingSettingChoice fromParameter(String parameterValue) {
        for (LineEndingSettingChoice lineEndingSettingChoice : values()) {
            if (lineEndingSettingChoice.parameterValue.equalsIgnoreCase(parameterValue))
                return lineEndingSettingChoice;
        }
        throw new IllegalArgumentException("Cannot find LineEnding: " + parameterValue);
    }
    
    public static LineEndingSettingChoice fromDropDown(String dropDownValue) {
        for (LineEndingSettingChoice lineEndingSettingChoice : values()) {
            if (lineEndingSettingChoice.dropDownValue.equalsIgnoreCase(dropDownValue))
                return lineEndingSettingChoice;
        }
        throw new IllegalArgumentException("Cannot find LineEnding: " + dropDownValue);
    }
}
