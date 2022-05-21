package sortpom.parameter;

import java.util.Arrays;
import sortpom.exception.FailureException;

public enum VerifyFailOnType {
  XMLELEMENTS,
  STRICT;

  static VerifyFailOnType fromString(String verifyFailOn) {
    if (verifyFailOn == null) {
      throw new FailureException("verifyFailOn must be either xmlElements or strict. Was: null");
    }
    return Arrays.stream(VerifyFailOnType.values())
        .filter(e -> e.toString().equalsIgnoreCase(verifyFailOn))
        .findAny()
        .orElseThrow(
            () ->
                new FailureException(
                    "verifyFailOn must be either xmlElements or strict. Was: " + verifyFailOn));
  }
}
