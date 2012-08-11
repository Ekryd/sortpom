package sortpom.verify;

import org.junit.Test;
import sortpom.verify.util.VerifyOrderFilesUtil;

public class SortPropertiesTest {

    @Test
    public final void namedParametersInSortFileShouldNotAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .defaultOrderFileName("difforder/sortedPropertiesOrder.xml")
                .lineSeparator("\n")
                .testVerifyXmlIsOrdered("/SortedProperties_output.xml");
    }

    @Test
    public final void sortPropertyParameterShouldNotAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .sortProperties()
                .lineSeparator("\n")
                .predefinedSortOrder("custom_1")
                .testVerifyXmlIsOrdered("/SortedProperties_output_alfa.xml");
    }

    @Test
    public final void testBothNamedParametersInSortFileAndSortPropertyParameterTestNotAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .lineSeparator("\n")
                .defaultOrderFileName("difforder/sortedPropertiesOrder.xml")
                .sortProperties()
                .testVerifyXmlIsOrdered("/SortedProperties_output_alfa2.xml");
    }

    @Test
    public final void sortingOfFullPomFileShouldNotAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .sortProperties()
                .sortPlugins()
                .sortDependencies()
                .lineSeparator("\n")
                .testVerifyXmlIsOrdered("/SortProp_expected.xml");
    }



    @Test
    public final void namedParametersInSortFileShouldAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .defaultOrderFileName("difforder/sortedPropertiesOrder.xml")
                .lineSeparator("\n")
                .testVerifyXmlIsNotOrdered("/SortedProperties_input.xml", null);
    }

    @Test
    public final void sortPropertyParameterShouldAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .sortProperties()
                .lineSeparator("\n")
                .predefinedSortOrder("custom_1")
                .testVerifyXmlIsNotOrdered("/SortedProperties_input.xml", null);
    }

    @Test
    public final void testBothNamedParametersInSortFileAndSortPropertyParameterTestAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .lineSeparator("\n")
                .defaultOrderFileName("difforder/sortedPropertiesOrder.xml")
                .sortProperties()
                .testVerifyXmlIsNotOrdered("/SortedProperties_input.xml", null);
    }

    @Test
    public final void sortingOfFullPomFileShouldAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .sortProperties()
                .sortPlugins()
                .sortDependencies()
                .lineSeparator("\n")
                .testVerifyXmlIsNotOrdered("/SortProp_input.xml", null);
    }
}
