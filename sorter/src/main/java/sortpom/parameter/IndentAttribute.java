package sortpom.parameter;

import sortpom.exception.FailureException;

public enum IndentAttribute {
  NONE,
  SCHEMA_LOCATION,
  ALL;

  static IndentAttribute fromString(String indentAttribute) {
    if (indentAttribute == null) {
      return NONE;
    }
    if ("schemaLocation".equalsIgnoreCase(indentAttribute)) {
      return SCHEMA_LOCATION;
    }
    if ("all".equalsIgnoreCase(indentAttribute)) {
      return ALL;
    }
    throw new FailureException(
        String.format(
            "verifyFail must be either SCHEMA_LOCATION or ALL. Was: %s", indentAttribute));
  }
}
