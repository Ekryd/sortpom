package sortpom.parameter;

import sortpom.exception.FailureException;

/**
 * @author bjorn
 * @since 2012-08-11
 */
public enum VerifyFailType {
    SORT, WARN, STOP;

    static VerifyFailType fromString(String verifyFail) {
        if (verifyFail == null) {
            throw new FailureException("verifyFail must be either SORT, WARN or STOP. Was: " + verifyFail);
        }
        if (verifyFail.equalsIgnoreCase("SORT")) {
            return SORT;
        }
        if (verifyFail.equalsIgnoreCase("WARN")) {
            return WARN;
        }
        if (verifyFail.equalsIgnoreCase("STOP")) {
            return STOP;
        }
        throw new FailureException("verifyFail must be either SORT, WARN or STOP. Was: " + verifyFail);
    }
}
