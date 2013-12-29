package sortpom.processinstruction;

import sortpom.logger.SortPomLogger;

import java.util.regex.Matcher;

import static sortpom.processinstruction.InstructionType.*;

/**
 * @author bjorn
 * @since 2013-12-28
 */
class SortpomPiScanner {
    private final SortPomLogger logger;
    private InstructionType expectedNextInstruction = IGNORE;
    private String errorString;
    private boolean containsIgnoredSections = false;

    public SortpomPiScanner(SortPomLogger logger) {
        this.logger = logger;
    }

    public void scan(String originalXml) {
        Matcher matcher = INSTRUCTION_PATTERN.matcher(originalXml);
        while (matcher.find()) {
            scanOneInstruction(matcher.group(1));
            containsIgnoredSections = true;
        }
        if (expectedNextInstruction != IGNORE) {
            addError(String.format("Xml processing instructions for sortpom was not properly terminated. Every <?sortpom %s?> must be followed with <?sortpom %s?>",
                    IGNORE, RESUME));
        }
    }

    private void scanOneInstruction(String instruction) {
        if (!InstructionType.containsType(instruction)) {
            addError(String.format("Xml contained unknown sortpom instruction '%s'. Please use <?sortpom %s?> or <?sortpom %s?>",
                    instruction, IGNORE, RESUME));
        } else {
            if (!expectedNextInstruction.matches(instruction)) {
                addError(String.format("Xml contained unexpected sortpom instruction '%s'. Please use expected instruction <?sortpom %s?>",
                        instruction, expectedNextInstruction));
            } else {
                expectedNextInstruction = expectedNextInstruction.next();
            }
        }
    }

    private void addError(String msg) {
        if (errorString == null) {
            errorString = msg;
        }
        logger.error(msg);
    }

    public boolean isScanError() {
        return errorString != null;
    }

    public String getFirstError() {
        return errorString;
    }

    public boolean containsIgnoredSections() {
        return containsIgnoredSections;
    }
}
