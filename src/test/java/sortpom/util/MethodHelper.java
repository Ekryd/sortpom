package sortpom.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class MethodHelper {
    private boolean accessibleState;
    private final Method method;

    public MethodHelper(Class<?> instanceClass, String methodName, Object... invocationValues) {
        List<Method> methodMatches = getMatchingMethods(instanceClass, methodName, invocationValues);
        if (methodMatches.size() > 1) {
            throw new IllegalArgumentException(String.format("Found %s matches for method %s", methodMatches.size(),
                    methodName));
        }
        method = methodMatches.get(0);
    }

    private List<Method> getMatchingMethods(Class<?> instanceClass, String methodName, Object... invocationValues) {
        List<Method> methodMatches = getMethodNameMatches(instanceClass, methodName);
        if (methodMatches.size() == 0) {
            throw new IllegalArgumentException(String.format("Cannot find method named %s", methodName));
        }
        if (methodMatches.size() == 1) {
            return methodMatches;
        }

        methodMatches = getArgumentNumberMatches(methodMatches, invocationValues.length);
        if (methodMatches.size() == 0) {
            throw new IllegalArgumentException(String.format("Cannot find method named %s with correct number of parameters", methodName));
        }
        if (methodMatches.size() == 1) {
            return methodMatches;
        }

        methodMatches = getArgumentMatches(methodMatches, invocationValues);
        if (methodMatches.size() == 0) {
            throw new IllegalArgumentException(String.format("Cannot find method named %s with correct parameter types ", methodName));
        }

        return methodMatches;
    }

    private List<Method> getMethodNameMatches(Class<?> instanceClass, String methodName) {
        Method[] methods = instanceClass.getDeclaredMethods();
        List<Method> methodMatches = new ArrayList<Method>();
        for (Method method : methods) {
            if (method.getName().equals(methodName))
                methodMatches.add(method);
        }
        return methodMatches;
    }

    private List<Method> getArgumentNumberMatches(List<Method> methodNameMatches, int valuesLength) {
        List<Method> returnValue = new ArrayList<Method>();
        for (Method methodNameMatch : methodNameMatches) {
            if (methodNameMatch.getParameterTypes().length == valuesLength) {
                returnValue.add(methodNameMatch);
            }
        }
        return returnValue;
    }

    private List<Method> getArgumentMatches(List<Method> methodNearMatches, Object... invocationValues) {
        List<Method> returnValue = new ArrayList<Method>();
        for (Method methodNearMatch : methodNearMatches) {
            int a = 0;
            boolean foundMatch = true;
            for (Class<?> parameterType : methodNearMatch.getParameterTypes()) {
                foundMatch = foundMatch && parameterType.isAssignableFrom(invocationValues[a].getClass());
                a++;
            }
            if (foundMatch) {
                returnValue.add(methodNearMatch);
            }
        }
        return returnValue;
    }

    public Method getMethod() {
        accessibleState = method.isAccessible();
        method.setAccessible(true);
        return method;
    }

    public void restoreAccessibleState() {
        method.setAccessible(accessibleState);
    }

}
