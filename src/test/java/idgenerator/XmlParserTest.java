package idgenerator;

import java.io.File;
import java.util.List;

import junit.framework.Assert;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Test;

public class XmlParserTest {
	@Test(expected = MojoFailureException.class)
	public void testDuplicateIdFail() throws MojoFailureException {
		Log log = new SystemStreamLog();
		XmlParser xmlParser = new XmlParser(log, "gen");
		FileList xhtmlFiles = new FileList(new File("src/test/resources/fail"));
		xhtmlFiles.findFiles(new XmlFileFilter(".xhtml"));
		xmlParser.checkForDuplicateIds(xhtmlFiles);
	}

	@Test
	public void testDuplicateIdOk() throws MojoFailureException {
		Log log = new SystemStreamLog();
		XmlParser xmlParser = new XmlParser(log, "gen");
		FileList xhtmlFiles = new FileList(new File("src/test/resources/ok"));
		xhtmlFiles.findFiles(new XmlFileFilter(".xhtml"));
		xmlParser.checkForDuplicateIds(xhtmlFiles);
	}

	@Test
	public void testGenerateIdOk() throws MojoFailureException {
		Log log = new SystemStreamLog();
		XmlParser xmlParser = new XmlParser(log, "gen");
		FileList xhtmlFiles = new FileList(new File("src/test/resources/gen"));
		xhtmlFiles.findFiles(new XmlFileFilter(".xhtml"));
		List<GeneratedFile> files = xmlParser.parseFiles(xhtmlFiles);
		Assert.assertEquals(2, files.size());
		Assert.assertEquals("out.xhtml", files.get(0).getFileName().getName());
		Assert.assertEquals("out4.xhtml", files.get(1).getFileName().getName());
	}

	@Test(expected = MojoFailureException.class)
	public void testNoIdFail() throws MojoFailureException {
		Log log = new SystemStreamLog();
		XmlParser xmlParser = new XmlParser(log, "gen");
		FileList xhtmlFiles = new FileList(new File("src/test/resources/fail"));
		xhtmlFiles.findFiles(new XmlFileFilter(".xhtml"));
		xmlParser.checkForNoIds(xhtmlFiles);
	}

	@Test
	public void testNoIdOk() throws MojoFailureException {
		Log log = new SystemStreamLog();
		XmlParser xmlParser = new XmlParser(log, "gen");
		FileList xhtmlFiles = new FileList(new File("src/test/resources/ok"));
		xhtmlFiles.findFiles(new XmlFileFilter(".xhtml"));
		xmlParser.checkForNoIds(xhtmlFiles);
	}
}
