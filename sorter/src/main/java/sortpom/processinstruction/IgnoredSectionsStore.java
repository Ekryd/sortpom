package sortpom.processinstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Replaces ignored sections with a token. The ignored sections look like this: <?sortpom
 * ignore?>... whatever ... <?sortpom resume?>
 *
 * <p>The tokens look like this: <?sortpom token='0'?>
 *
 * <p>The number in the token identifies the ignored section so that it can find it in a stored
 * list.
 *
 * @author bjorn
 * @since 2013-12-28
 */
class IgnoredSectionsStore {
  private final List<String> ignoredSections = new ArrayList<>();

  public String replaceIgnoredSections(String originalXml) {
    var matcher = InstructionType.IGNORE_SECTIONS_PATTERN.matcher(originalXml);

    var returnValue = new StringBuilder();
    var i = 0;

    while (matcher.find()) {
      ignoredSections.add(matcher.group());
      matcher.appendReplacement(returnValue, String.format("<?sortpom token='%d'?>", i));
      i++;
    }
    matcher.appendTail(returnValue);

    return returnValue.toString();
  }

  public String revertIgnoredSections(String sortedXml) {
    var matcher = InstructionType.TOKEN_PATTERN.matcher(sortedXml);

    var returnValue = new StringBuilder();

    while (matcher.find()) {
      var index = Integer.parseInt(matcher.group(1));
      var replacement = ignoredSections.get(index);
      var oneBackslashBeforeBackslash = replacement.replace("\\", "\\\\");
      var oneBackslashBeforeDollar = oneBackslashBeforeBackslash.replace("$", "\\$");

      matcher.appendReplacement(returnValue, oneBackslashBeforeDollar);
    }
    matcher.appendTail(returnValue);

    return returnValue.toString();
  }
}
