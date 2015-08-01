package sortpom.processinstruction;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Replaces ignored sections with a token. The ignored sections look like this:
 * <?sortpom ignore?>... whatever ... <?sortpom resume?>
 *
 * The tokens look like this:
 * <?sortpom token='0'?>
 *
 * The number in the token identifies the ignored section so that it can find it in a stored list.
 *
 * @author bjorn
 * @since 2013-12-28
 */
class IgnoredSectionsStore {
    private final List<String> ignoredSections = new ArrayList<>();

    public String replaceIgnoredSections(String originalXml) {
        Matcher matcher = InstructionType.IGNORE_SECTIONS_PATTERN.matcher(originalXml);

        StringBuffer returnValue = new StringBuffer();
        int i = 0;

        while (matcher.find()) {
            ignoredSections.add(matcher.group());
            matcher.appendReplacement(returnValue, String.format("<?sortpom token='%d'?>", i));
            i++;
        }
        matcher.appendTail(returnValue);

        return returnValue.toString();
    }

    public String revertIgnoredSections(String sortedXml) {
        Matcher matcher = InstructionType.TOKEN_PATTERN.matcher(sortedXml);

        StringBuffer returnValue = new StringBuffer();

        while (matcher.find()) {
            int index = Integer.parseInt(matcher.group(1));
            String replacement = ignoredSections.get(index);
            String oneBackslashBeforeBackslash = replacement.replace("\\", "\\\\");
            String oneBackslashBeforeDollar = oneBackslashBeforeBackslash.replace("$", "\\$");

            matcher.appendReplacement(returnValue, oneBackslashBeforeDollar);
        }
        matcher.appendTail(returnValue);

        return returnValue.toString();
    }
}
