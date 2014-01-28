package sortpom.processinstruction;

import java.util.regex.Pattern;

/**
 * Contains the different processing instruction commands that sortpom supports
 *
 * @author bjorn
 * @since 2013-12-28
 */
enum InstructionType {
    IGNORE, RESUME;

    /** non-xml compliant pattern, see http://www.cs.sfu.ca/~cameron/REX.html#IV.2 */
    static final Pattern INSTRUCTION_PATTERN = Pattern.compile("(?i)<\\?sortpom\\s+([\\w\"'*= ]*)\\s*\\?>");
    static final Pattern IGNORE_SECTIONS_PATTERN = Pattern.compile(
            "(?is)<\\?sortpom\\s+" + IGNORE + "\\s*\\?>.*?<\\?sortpom\\s+" + RESUME + "\\s*\\?>");
    static final Pattern TOKEN_PATTERN = Pattern.compile("(?i)<\\?sortpom\\s+token='(\\d+)'\\s*\\?>");

    public InstructionType next() {
        if (this == IGNORE) {
            return RESUME;
        }
        return IGNORE;
    }

    public static boolean containsType(String instruction) {
        return IGNORE.name().equalsIgnoreCase(instruction) || RESUME.name().equalsIgnoreCase(instruction);
    }

    public boolean matches(String instruction) {
        return this.name().equalsIgnoreCase(instruction);
    }
}
