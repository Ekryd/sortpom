package idgenerator;

import idgenerator.file.FileList;
import idgenerator.file.XmlFileFilter;
import idgenerator.util.IdGenerator;
import idgenerator.xml.AddEmptyIdFileOperation;
import idgenerator.xml.AddIdOperation;
import idgenerator.xml.CheckDuplicateOperation;
import idgenerator.xml.CheckEmptyIdOperation;
import idgenerator.xml.GeneratedFile;
import idgenerator.xml.XmlModifier;
import idgenerator.xml.XmlParser;

import java.io.File;
import java.util.List;

import junit.framework.Assert;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Test;

public class XmlParserTest {
	@Test
	public void testDuplicateIdFailParse() throws MojoFailureException {
		Log log = new SystemStreamLog();
		XmlParser xmlParser = new XmlParser();
		FileList xhtmlFiles = new FileList(new File("src/test/resources/fail"));
		xhtmlFiles.findFiles(new XmlFileFilter(".xhtml"));
		boolean actual = xmlParser.parse(xhtmlFiles, new CheckDuplicateOperation(new IdGenerator(log, "gen")));
		Assert.assertEquals(true, actual);
	}

	@Test
	public void testDuplicateIdOkParse() throws MojoFailureException {
		Log log = new SystemStreamLog();
		XmlParser xmlParser = new XmlParser();
		FileList xhtmlFiles = new FileList(new File("src/test/resources/ok"));
		xhtmlFiles.findFiles(new XmlFileFilter(".xhtml"));
		boolean actual = xmlParser.parse(xhtmlFiles, new CheckDuplicateOperation(new IdGenerator(log, "gen")));
		Assert.assertEquals(false, actual);
	}

	@Test
	public void testGenerate1FindFiles() throws MojoFailureException {
		XmlParser xmlParser = new XmlParser();
		FileList xhtmlFiles = new FileList(new File("src/test/resources/gen"));
		xhtmlFiles.findFiles(new XmlFileFilter(".xhtml"));
		List<File> files = xmlParser.parse(xhtmlFiles, new AddEmptyIdFileOperation());
		Assert.assertEquals(2, files.size());
		Assert.assertEquals("out.xhtml", files.get(0).getName());
		Assert.assertEquals("out4.xhtml", files.get(1).getName());
	}

	@Test
	public void testGenerate2FindIds() throws MojoFailureException {
		Log log = new SystemStreamLog();
		XmlParser xmlParser = new XmlParser();
		FileList xhtmlFiles = new FileList(new File("src/test/resources/gen"));
		xhtmlFiles.findFiles(new XmlFileFilter(".xhtml"));
		IdGenerator idGenerator = new IdGenerator(log, "gen");
		xmlParser.parse(xhtmlFiles, new AddIdOperation(idGenerator));
		Assert.assertEquals(3, idGenerator.getIdSet().size());
		Assert.assertEquals(true, idGenerator.contains("test"));
		Assert.assertEquals(true, idGenerator.contains("test2"));
		Assert.assertEquals(true, idGenerator.contains("test3"));
	}

	@Test
	public void testGenerate3GenerateFiles() throws MojoFailureException {
		Log log = new SystemStreamLog();
		XmlParser xmlParser = new XmlParser();
		FileList xhtmlFiles = new FileList(new File("src/test/resources/gen"));
		xhtmlFiles.findFiles(new XmlFileFilter(".xhtml"));
		List<File> files = xmlParser.parse(xhtmlFiles, new AddEmptyIdFileOperation());
		IdGenerator idGenerator = new IdGenerator(log, "gen");
		xmlParser.parse(xhtmlFiles, new AddIdOperation(idGenerator));
		XmlModifier xmlModifier = new XmlModifier(idGenerator);
		List<GeneratedFile> parseFiles = xmlModifier.parseFiles(files);
		Assert.assertEquals(2, parseFiles.size());
		Assert.assertEquals("out.xhtml", parseFiles.get(0).getFileName().getName());
		Assert.assertEquals(
				"src/test/resources/gen/out.xhtml\n"
						+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
						+ "\n"
						+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:ui=\"http://java.sun.com/jsf/facelets\" xmlns:h=\"http://java.sun.com/jsf/html\" xmlns:f=\"http://java.sun.com/jsf/core\" xmlns:a4j=\"http://richfaces.org/a4j\" xmlns:rich=\"http://richfaces.org/rich\" xmlns:c=\"http://java.sun.com/jstl/core\" xmlns:fn=\"http://java.sun.com/jsp/jstl/functions\" xml:lang=\"en\" lang=\"en\">\n"
						+ "  <body>\n" + "    <ui:composition>\n"
						+ "      <h:outputText id=\"test\" value=\"Out\" />\n"
						+ "      <h:outputText id=\"test2\" value=\"Out\" />\n"
						+ "      <h:outputText id=\"gen1\" value=\"Out\" />\n" + "    </ui:composition>\n"
						+ "  </body>\n" + "</html>\n\n\n", parseFiles.get(0).toString());

		Assert.assertEquals("out4.xhtml", parseFiles.get(1).getFileName().getName());
		Assert.assertEquals(
				"src/test/resources/gen/out4.xhtml\n"
						+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
						+ "\n"
						+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:ui=\"http://java.sun.com/jsf/facelets\" xmlns:h=\"http://java.sun.com/jsf/html\" xmlns:f=\"http://java.sun.com/jsf/core\" xmlns:a4j=\"http://richfaces.org/a4j\" xmlns:rich=\"http://richfaces.org/rich\" xmlns:c=\"http://java.sun.com/jstl/core\" xmlns:fn=\"http://java.sun.com/jsp/jstl/functions\" xml:lang=\"en\" lang=\"en\">\n"
						+ "  <body>\n" + "    <ui:composition>\n"
						+ "      <h:outputText id=\"gen2\" value=\"Out\" />\n" + "    </ui:composition>\n"
						+ "  </body>\n" + "</html>\n\n\n", parseFiles.get(1).toString());
	}

	@Test
	public void testNoIdFailParse() throws MojoFailureException {
		Log log = new SystemStreamLog();
		XmlParser xmlParser = new XmlParser();
		FileList xhtmlFiles = new FileList(new File("src/test/resources/fail"));
		xhtmlFiles.findFiles(new XmlFileFilter(".xhtml"));
		boolean actual = xmlParser.parse(xhtmlFiles, new CheckEmptyIdOperation(log));
		Assert.assertEquals(true, actual);
	}

	@Test
	public void testNoIdOkParse() throws MojoFailureException {
		Log log = new SystemStreamLog();
		XmlParser xmlParser = new XmlParser();
		FileList xhtmlFiles = new FileList(new File("src/test/resources/ok"));
		xhtmlFiles.findFiles(new XmlFileFilter(".xhtml"));
		boolean actual = xmlParser.parse(xhtmlFiles, new CheckEmptyIdOperation(log));
		Assert.assertEquals(false, actual);
	}
}
