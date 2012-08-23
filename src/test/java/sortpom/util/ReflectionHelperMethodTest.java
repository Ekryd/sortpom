package sortpom.util;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("unused")
public class ReflectionHelperMethodTest {
    private final Instance0 instance = new Instance0();


    @Test
    public void simpleMethodNameShouldWork() throws Exception {
        assertEquals(null, new ReflectionHelper(instance).executeMethod("pelle"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongMethodNameShouldNotWork() throws Exception {
        assertEquals(null, new ReflectionHelper(instance).executeMethod("stina"));
    }

    @Test
    public void noParameterChoiceMethodNameShouldWork() throws Exception {
        assertEquals(42, new ReflectionHelper(instance).executeMethod("kalle"));
    }

    @Test
    public void stringParameterChoiceMethodNameShouldWork() throws Exception {
        assertEquals(1, new ReflectionHelper(instance).executeMethod("kalle", "3"));
    }

    @Ignore("Primitives does not work well")
    public void oneParameterChoiceMethodNameShouldWork() throws Exception {
        assertEquals(3, new ReflectionHelper(instance).executeMethod("kalle", 3));
    }

    @Test
    public void nullParameterChoiceMethodNameShouldWork() throws Exception {
        assertEquals(5, new ReflectionHelper(instance).executeMethod("kalle", 4, null));
    }

    @Test
    public void twoParameterChoiceMethodNameShouldWork() throws Exception {
        ParameterInterface p = new ParameterClass();
        assertEquals(5, new ReflectionHelper(instance).executeMethod("kalle", 4, p));
    }

    @Test
    public void subParameterChoiceMethodNameShouldWork() throws Exception {
        SubParameterClass p = new SubParameterClass();
        assertEquals(5, new ReflectionHelper(instance).executeMethod("kalle", 4, p));
    }

    private static interface ParameterInterface {
    }

    private static class ParameterClass implements ParameterInterface {
    }

    private static class SubParameterClass extends ParameterClass {
    }


    private static class Instance0 {
        private void pelle() {
        }

        private int kalle() {
            return 42;
        }

        private int kalle(String a) {
            return a.length();
        }

        private int kalle(int a) {
            return a;
        }

        private int kalle(int a, ParameterInterface parameter) {
            return a + 1;
        }
    }

}
