package sortpom.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * Utilityclass to instantiate javabeans.
 *
 * @param <T>
 * @author exbjek
 */
final class HelperInstance<T> {
    private final Class<T> clazz;

    private HelperInstance(final Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Instantiate with public empty constructor.
     *
     * @param clazz the clazz to instantiate
     * @return the instance
     */
    public static <T> T instantiate(final Class<T> clazz) {
        try {
            return new HelperInstance<T>(clazz).getDefaultInstance(false);
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Class: %s constructor", clazz), ex);
        }
    }

    /**
     * Instantiate with any constructor.
     *
     * @param clazz the clazz to instantiate
     * @return the instance
     */
    public static <T> T instantiateForced(final Class<T> clazz) {
        try {
            return new HelperInstance<T>(clazz).getAnyInstance();
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Class: %s constructor", clazz), ex);
        }
    }

    /**
     * Instantiate private empty constructor.
     *
     * @param clazz the clazz to instantiate
     * @return the instance
     */
    public static <T> T instantiatePrivate(final Class<T> clazz) {
        try {
            return new HelperInstance<T>(clazz).getDefaultInstance(true);
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Class: %s constructor", clazz), ex);
        }
    }

    private static Object getDefaultValue(final Class<?> clazz) {
        if (String.class == clazz) {
            return "";
        }
        if (long.class == clazz || Long.class == clazz) {
            return (long) 0;
        }
        if (double.class == clazz || Double.class == clazz) {
            return (double) 0;
        }
        if (float.class == clazz || Float.class == clazz) {
            return (float) 0;
        }
        if (int.class == clazz || Integer.class == clazz) {
            return (int) 0;
        }
        if (char.class == clazz || Character.class == clazz) {
            return (char) 0;
        }
        if (short.class == clazz || Short.class == clazz) {
            return (short) 0;
        }
        if (Date.class == clazz) {
            return new Date();
        }
        if (boolean.class == clazz || Boolean.class == clazz) {
            return true;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    T getAnyInstance() throws InstantiationException {
        Constructor<?>[] constructors = getSortedConstructors();
        for (Constructor<?> constructor : constructors) {
            try {
                return tryToinstantiate((Constructor<T>) constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new InstantiationException("Cannot instantiatiate class");
    }

    T getDefaultInstance(final boolean accessIfPrivate) throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        if (accessIfPrivate) {
            constructor.setAccessible(true);
        }
        return constructor.newInstance();
    }

    private Constructor<?>[] getSortedConstructors() {
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        Comparator<Constructor<?>> comparator = new Comparator<Constructor<?>>() {

            @Override
            public int compare(final Constructor<?> o1, final Constructor<?> o2) {
                int compare = o1.getParameterTypes().length - o2.getParameterTypes().length;
                if (compare == 0) {
                    for (int i = 0; i < o1.getParameterTypes().length; i++) {
                        compare = o1.getParameterTypes()[i].getSimpleName().compareTo(
                                o2.getParameterTypes()[i].getSimpleName());
                        if (compare != 0) {
                            return compare;
                        }
                    }
                }
                return 0;
            }
        };
        Arrays.sort(declaredConstructors, comparator);
        return declaredConstructors;
    }

    private T tryToinstantiate(final Constructor<T> constructor) throws InstantiationException, IllegalAccessException,
            InvocationTargetException {
        constructor.setAccessible(true);
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] arguments = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            arguments[i] = getDefaultValue(parameterTypes[i]);
        }
        return constructor.newInstance(arguments);
    }

}
