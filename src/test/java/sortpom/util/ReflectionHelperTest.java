package sortpom.util;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@SuppressWarnings("unused")
public class ReflectionHelperTest {

    @Test
    public void testConstructor() throws Exception {
        Instance3 instance = ReflectionHelper.instantiatePrivateConstructor(ReflectionHelperTest.Instance3.class);
        assertNotNull(instance);
    }

    @Test
    public void testSetField() throws Exception {
        FieldClass class1 = new FieldClass();
        Instance1 instance = new Instance1();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField(class1);
        assertEquals(class1, instance.getFieldInterface());
    }

    @Test
    @Ignore("Does not work with primitives")
    public void testSetNumberField() throws Exception {
        Instance1 instance = new Instance1();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField(42);
        assertEquals(42, instance.getNumber());
    }

    @Test
    public void testSetField2() throws Exception {
        FieldClass class1 = new FieldClass();
        Instance0 instance = new Instance0();
        try {
            ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
            reflectionHelper.setField(class1);
            fail();
        } catch (Exception ex) {

        }
    }

    @Test
    public void testSetField3() throws Exception {
        FieldClass class1 = new FieldClass();
        Instance2 instance = new Instance2();
        try {
            ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
            reflectionHelper.setField(class1);
            fail();
        } catch (Exception ex) {

        }
    }

    @Test
    public void testSetNamedField() throws SecurityException, IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException {
        FieldClass class1 = new FieldClass();
        Instance1 instance = new Instance1();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField("fieldInterface", class1);
        assertEquals(class1, instance.getFieldInterface());
    }

    @Test
    public void testSetNamedField2() throws Exception {
        FieldClass class1 = new FieldClass();
        Instance0 instance = new Instance0();
        try {
            ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
            reflectionHelper.setField("fieldInterface", class1);
            fail();
        } catch (Exception ex) {

        }
    }

    @Test
    public void testSetNamedField3() throws Exception {
        FieldClass class1 = new FieldClass();
        Instance2 instance = new Instance2();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField("fieldInterface2", class1);
        assertEquals(class1, instance.getFieldInterface2());
    }

    private class FieldClass implements FieldInterface {

    }

    private interface FieldInterface {

    }

    private class Instance0 {
    }

    private class Instance1 {
        private FieldInterface fieldInterface;
        private int number;

        public FieldInterface getFieldInterface() {
            return fieldInterface;
        }

        public int getNumber() {
            return number;
        }
    }

    private class Instance2 {
        private FieldInterface fieldInterface1;
        private FieldInterface fieldInterface2;

        public FieldInterface getFieldInterface1() {
            return fieldInterface1;
        }

        public FieldInterface getFieldInterface2() {
            return fieldInterface2;
        }

        public void setFieldInterface1(final FieldInterface fieldInterface1) {
            this.fieldInterface1 = fieldInterface1;
        }

        public void setFieldInterface2(final FieldInterface fieldInterface2) {
            this.fieldInterface2 = fieldInterface2;
        }

    }

    private static class Instance3 {
        private Instance3() {

        }
    }

}
