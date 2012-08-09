package sortpom.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class is used to set protected fields in classes and access private
 * constructors.
 */
public final class ReflectionHelper {
    private static final String SETTER_PREFIX;
    private static final int SETTER_PREFIX_LENGTH;

    /** The object that contains the field. */
    private final Object instance;

    static {
        SETTER_PREFIX = "set";
        SETTER_PREFIX_LENGTH = SETTER_PREFIX.length();
    }

    /**
     * Instantiates a new reflection helper.
     *
     * @param instance the instance
     */
    public ReflectionHelper(final Object instance) {
        this.instance = instance;
    }

    /**
     * Instantiate the first constructor found and set dummy values directly on
     * the fields. This method will try all constructors until it finds one that
     * works.
     *
     * @param clazz the clazz to instantiate
     * @return the new instance
     */
    public static <T> T forceWithDummyValues(final Class<T> clazz) {
        T newInstance = HelperInstance.instantiateForced(clazz);
        new ReflectionHelper(newInstance).fillWithDummyFields();
        return newInstance;
    }

    /**
     * Instantiate private empty constructor.
     *
     * @param clazz the clazz to instantiate
     * @return the new instance
     */
    public static <T> T instantiatePrivateConstructor(final Class<T> clazz) {
        return HelperInstance.instantiatePrivate(clazz);
    }

    /**
     * Create new JavaBean with dummy values using setter methods.
     *
     * @param clazz the clazz to instantiate
     * @return the new instance
     */
    public static <T> T instantiateWithDummyValues(final Class<T> clazz) {
        T instance = HelperInstance.instantiate(clazz);
        new ReflectionHelper(instance).fillWithDummyProperties();
        return instance;
    }

    /**
     * Sets a private or protected field for an object. This method uses
     * type-matchning to set the field. This method can be used if a class only
     * has one field of the specified type.
     *
     * @param fieldValue The value that the field should be set to.
     * @throws IllegalAccessException Thrown if the field is final
     */
    public void setField(final Object fieldValue) throws IllegalAccessException {
        FieldHelper fieldHelper = new FieldHelper(instance.getClass(), fieldValue.getClass());
        fieldHelper.getField().set(instance, fieldValue);
        fieldHelper.restoreAccessibleState();
    }

    /**
     * Sets a named private or protected field for an object. Use the
     * type-matching setter if possible. This method can be used if a class only
     * has more than one field of the specified type.
     *
     * @param fieldName  The name of the field
     * @param fieldValue The value that the field should be set to.
     * @throws NoSuchFieldException   Thrown if the fieldname is incorrect
     * @throws IllegalAccessException Thrown if the field is final
     */
    public void setField(final String fieldName, final Object fieldValue) throws NoSuchFieldException,
            IllegalAccessException {
        FieldHelper fieldHelper = new FieldHelper(instance.getClass(), fieldName);
        fieldHelper.getField().set(instance, fieldValue);
        fieldHelper.restoreAccessibleState();
    }

    public Object getField(String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        FieldHelper fieldHelper = new FieldHelper(instance.getClass(), fieldName);
        Object returnValue = fieldHelper.getField().get(instance);
        fieldHelper.restoreAccessibleState();
        return returnValue;
    }

    @SuppressWarnings("unchecked")
    public <T> T getField(Class<T> fieldClass) throws IllegalArgumentException, IllegalAccessException {
        FieldHelper fieldHelper = new FieldHelper(instance.getClass(), fieldClass);
        Object returnValue = fieldHelper.getField().get(instance);
        fieldHelper.restoreAccessibleState();
        return (T) returnValue;
    }

    public Object executeMethod(String methodName, Object... invocationValues) throws InvocationTargetException, IllegalAccessException {
        MethodHelper methodHelper = new MethodHelper(instance.getClass(), methodName, invocationValues);
        Object returnValue = methodHelper.getMethod().invoke(instance, invocationValues);
        methodHelper.restoreAccessibleState();
        return returnValue;
    }

    /** Fills the contained JavaBean, using fields directly, with dummy values */
    public void fillWithDummyFields() {
        fillFields(this.instance);
    }

    private void fillFields(final Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            String propertyName = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            Type type = field.getGenericType();
            Object value = createDummyValue(type instanceof ParameterizedType ? (ParameterizedType) type : null,
                    field.getType(), propertyName);
            try {
                field.setAccessible(true);
                field.set(obj, value);
            } catch (Exception e) {
                // Silently ignore
            }
        }
    }

    /** Fills the contained JavaBean, using setter methods, with dummy values */
    public void fillWithDummyProperties() {
        fillProperties(this.instance);
    }

    private void fillProperties(final Object obj) {
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith(SETTER_PREFIX) && (method.getParameterTypes().length == 1)) {
                String propertyName = method.getName().substring(SETTER_PREFIX_LENGTH);
                Type type = method.getGenericParameterTypes()[0];
                Object value = createDummyValue(type instanceof ParameterizedType ? (ParameterizedType) type : null,
                        method.getParameterTypes()[0], propertyName);
                try {
                    method.invoke(obj, value);
                } catch (Exception e) {
                    throw new RuntimeException(String.format("Method: %s, Value: %s", method.getName(), value), e);
                }
            }
        }
    }

    private Object createDummyValue(final ParameterizedType genericParameterType, final Class<?> clazz,
                                    final String propertyName) {
        if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            Object array = Array.newInstance(componentType, 2);
            Array.set(array, 0, createDummyValue(null, componentType, propertyName + "0"));
            Array.set(array, 1, createDummyValue(null, componentType, propertyName + "1"));
            return array;
        }
        if (List.class == clazz) {
            Type[] actualTypeArguments = genericParameterType.getActualTypeArguments();
            if (actualTypeArguments.length == 1 && actualTypeArguments[0] instanceof Class<?>) {
                List<Object> collection = new ArrayList<Object>();
                Class<?> componentType = (Class<?>) actualTypeArguments[0];
                Object value = createDummyValue(null, componentType, propertyName + "0");
                collection.add(value);
                value = createDummyValue(null, componentType, propertyName + "1");
                collection.add(value);
                return collection;
            }
            throw new RuntimeException(String.format("%s %s must use simple generics. Ie List<String>",
                    clazz.getSimpleName(), genericParameterType));
        }
        if (Set.class == clazz) {
            Type[] actualTypeArguments = genericParameterType.getActualTypeArguments();
            if (actualTypeArguments.length == 1 && actualTypeArguments[0] instanceof Class<?>) {
                Set<Object> collection = new TreeSet<Object>();
                Class<?> componentType = (Class<?>) actualTypeArguments[0];
                Object value = createDummyValue(null, componentType, propertyName + "0");
                collection.add(value);
                value = createDummyValue(null, componentType, propertyName + "1");
                collection.add(value);
                return collection;
            }
            throw new RuntimeException("List<???> must use simple generics. Ie List<String>");
        }
        if (clazz.isEnum()) {
            Object[] enumConstants = clazz.getEnumConstants();
            if (enumConstants.length == 0) {
                return null;
            }
            return enumConstants[0];
        }
        if (String.class == clazz) {
            return propertyName;
        }
        if (long.class == clazz || Long.class == clazz) {
            return (long) propertyName.hashCode();
        }
        if (double.class == clazz || Double.class == clazz) {
            return (double) propertyName.hashCode();
        }
        if (float.class == clazz || Float.class == clazz) {
            return (float) propertyName.hashCode();
        }
        if (int.class == clazz || Integer.class == clazz) {
            return (int) propertyName.hashCode();
        }
        if (char.class == clazz || Character.class == clazz) {
            return propertyName.charAt(0);
        }
        if (short.class == clazz || Short.class == clazz) {
            return (short) propertyName.hashCode();
        }
        if (Date.class == clazz) {
            return new Date(propertyName.hashCode());
        }
        if (boolean.class == clazz || Boolean.class == clazz) {
            return true;
        }
        return instantiateWithDummyValues(clazz);
    }

}
