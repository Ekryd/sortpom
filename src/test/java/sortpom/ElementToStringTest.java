package sortpom;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.jdom.Document;
import org.jdom.JDOMException;
import sortpom.parameter.PluginParameters;
import sortpom.parameter.PluginParametersBuilder;
import sortpom.util.FileUtil;
import sortpom.util.ReflectionHelper;
import sortpom.wrapper.WrapperFactory;
import sortpom.wrapper.WrapperFactoryImpl;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author bjorn
 * @since 2012-06-13
 */
public class ElementToStringTest {
    private static final String UTF_8 = "UTF-8";


    private Document getDocument(String inputFileName) throws IllegalAccessException, MojoFailureException, IOException, UnsupportedEncodingException, JDOMException {
        PluginParameters pluginParameters = new PluginParametersBuilder().setPomFile(null).setBackupInfo(false, ".bak")
                .setFormatting("UTF-8", "\r\n", true, true)
                .setIndent("  ", false)
                .setSortOrder("default_0_4_0.xml", null)
                .setSortEntities(false, false, false).createPluginParameters();

        FileUtil fileUtil = new FileUtil();
        fileUtil.setup(pluginParameters);
        
        WrapperFactoryImpl wrapperFactory = new WrapperFactoryImpl(fileUtil);
        wrapperFactory.setup(pluginParameters);

        final String xml = IOUtils.toString(new FileInputStream(inputFileName), UTF_8);

        
        final XmlProcessor xmlProcessor = new XmlProcessor(wrapperFactory);
        xmlProcessor.setup(pluginParameters);
        new ReflectionHelper(wrapperFactory).setField(fileUtil);
        wrapperFactory.initialize();
        new ReflectionHelper(xmlProcessor).setField(wrapperFactory);
        xmlProcessor.setOriginalXml(new ByteArrayInputStream(xml.getBytes(UTF_8)));
        xmlProcessor.sortXml();
        
    }
}
