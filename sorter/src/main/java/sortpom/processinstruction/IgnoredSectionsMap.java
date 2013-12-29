package sortpom.processinstruction;

import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * @author bjorn
 * @since 2013-12-28
 */
class IgnoredSectionsMap {
    ArrayList<String> ignoredSections = new ArrayList<String>();

    public String replaceIgnoredSections(String originalXml) {
        Matcher matcher = InstructionType.IGNORE_SECTIONS_PATTERN.matcher(originalXml);

        StringBuffer returnValue = new StringBuffer();
        int i = 0;

        while (matcher.find()) {
            String contentWithOneBackslashBeforeDollar = matcher.group().replaceAll("[$]", "\\\\\\$");
            ignoredSections.add(contentWithOneBackslashBeforeDollar);
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
            matcher.appendReplacement(returnValue, ignoredSections.get(index));
        }
        matcher.appendTail(returnValue);

        return returnValue.toString();
    }
}
