package sortpom.util;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

@SuppressWarnings("unused")
public class HelperInstanceTest {

    @Test
    public void testForceInstantiate() throws Exception {
        assertNotNull(HelperInstance.instantiateForced(Class1.class));
        assertNotNull(HelperInstance.instantiateForced(Class2.class));
        assertNotNull(HelperInstance.instantiateForced(Class3.class));
        assertNotNull(HelperInstance.instantiateForced(Class4.class));
    }

    private static class Class1 {
        private Class1() {
        }
    }

    private static class Class2 {
        private Class2(final int a) {
        }
    }

    private static class Class3 {
        String[] names;

        private Class3(final String[] names) {
            this.names = names;
        }
    }

    private static class Class4 {
        int[] names;

        private Class4(final int[] names) {
            this.names = names;
        }
    }

}
