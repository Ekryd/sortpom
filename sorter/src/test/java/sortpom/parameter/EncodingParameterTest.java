package sortpom.parameter;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sortpom.XmlOutputGenerator;
import sortpom.util.SortPomImplUtil;

import static sortpom.sort.ExpandEmptyElementTest.createXmlFragment;

public class EncodingParameterTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void illegalEncodingWhenGettingPomFileShouldNotWork() throws Exception {
        thrown.expectMessage("gurka-2000");

        SortPomImplUtil.create()
                .encoding("gurka-2000")
                .testFiles("/Xml_deviations_input.xml", "/Xml_deviations_output.xml");
    }

    @Test
    public void illegalEncodingWhenGeneratingPomFileShouldNotWork() throws Exception {
        thrown.expectMessage("Could not format pom files content");

        XmlOutputGenerator xmlOutputGenerator = new XmlOutputGenerator();
        xmlOutputGenerator.setup(new PluginParametersBuilder()
                .setEncoding("gurka-2000")
                .setFormatting("\n", true, false)
                .setIndent(2, false)
                .createPluginParameters());

        String actual = xmlOutputGenerator.getSortedXml(createXmlFragment());
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka></Gurka>\n", actual);
    }
}
