package sortpom.util;

import org.apache.maven.plugin.MojoFailureException;

/**
 * @author bjorn
 * @since 2012-08-11
 */
public enum VerifyFailType {
    SORT, WARN, STOP;

    public static VerifyFailType fromString(String verifyFail) throws MojoFailureException {
        if (verifyFail == null) {
            return null;
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
        throw new MojoFailureException("verifyFail must be either SORT, WARN or STOP. Was: " + verifyFail);
    }
}
