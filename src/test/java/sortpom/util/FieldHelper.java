package sortpom.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class FieldHelper {
    private boolean accessibleState;
    private final Field field;

    public FieldHelper(Class<?> instanceClass, String fieldName) throws SecurityException,
            NoSuchFieldException {
        field = instanceClass.getDeclaredField(fieldName);
    }

    public FieldHelper(Class<?> instanceClass, Class<?> valueClass) {
        Field[] fields = instanceClass.getDeclaredFields();
        List<Field> fieldMatches = new ArrayList<Field>();
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(valueClass)) {
                fieldMatches.add(field);
            }
        }
        if (fieldMatches.size() == 0) {
            throw new IllegalArgumentException(String.format("Cannot find field for %s", valueClass));
        }
        if (fieldMatches.size() != 1) {
            throw new IllegalArgumentException(String.format("Found %s matches for field %s", fieldMatches.size(),
                    valueClass));
        }

        field = fieldMatches.get(0);
    }

    public Field getField() {
        accessibleState = field.isAccessible();
        field.setAccessible(true);
        return field;
    }

    public void restoreAccessibleState() {
        field.setAccessible(accessibleState);
    }

}
