package sortpom.parameter;

import sortpom.exception.FailureException;

/**
 * @author bjorn
 * @since 2012-08-11
 */
public enum VerifyFailType {
  SORT,
  WARN,
  STOP;

  static VerifyFailType fromString(String verifyFail) {
    if (verifyFail == null) {
      throw new FailureException("verifyFail must be either SORT, WARN or STOP. Was: null");
    }
    if ("SORT".equalsIgnoreCase(verifyFail)) {
      return SORT;
    }
    if ("WARN".equalsIgnoreCase(verifyFail)) {
      return WARN;
    }
    if ("STOP".equalsIgnoreCase(verifyFail)) {
      return STOP;
    }
    throw new FailureException("verifyFail must be either SORT, WARN or STOP. Was: " + verifyFail);
  }
}
