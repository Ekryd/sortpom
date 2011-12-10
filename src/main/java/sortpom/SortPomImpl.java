package sortpom;

import java.io.*;

import org.apache.commons.io.*;
import org.apache.maven.plugin.*;
import org.apache.maven.plugin.logging.*;
import org.jdom.*;

import sortpom.util.*;
import sortpom.wrapper.*;

/**
 * The implementation of the Mojo (Maven plugin) that sorts the pomfile for a
 * maven project.
 * 
 * @author Bjorn Ekryd
 */
public class SortPomImpl {

	private final FileUtil fileUtil;
	private final XmlProcessor xmlProcessor;
	private final WrapperFactoryImpl wrapperFactory;
	private PluginParameters pluginParameters;
	private Log log;

	/**
	 * Instantiates a new sort pom mojo and initiates dependencies to other
	 * classes.
	 * 
	 */
	public SortPomImpl() {
		fileUtil = new FileUtil();
		wrapperFactory = new WrapperFactoryImpl(fileUtil);
		xmlProcessor = new XmlProcessor(wrapperFactory);
	}

	public void setup(Log log, PluginParameters pluginParameters) throws MojoFailureException {
		this.log = log;
		this.pluginParameters = pluginParameters;
		fileUtil.setup(pluginParameters);
		wrapperFactory.setup(pluginParameters);
		xmlProcessor.setup(pluginParameters);
	}

	/**
	 * Sorts the pomfile.
	 * 
	 * @throws MojoFailureException
	 *             the mojo failure exception
	 */
	void sortPom() throws MojoFailureException {
		log.info("Sorting file " + pluginParameters.pomFile.getAbsolutePath());

		String originalXml = fileUtil.getPomFileContent();
		String sortedXml = getSortedXml(originalXml);
		if (pomFileIsSorted(originalXml, sortedXml)) {
			log.info("Pomfile is already sorted, exiting");
			return;
		}
		createBackupFile();
		saveSortedPomFile(sortedXml);
	}

	/**
	 * Sorts the incoming xml.
	 * 
	 * @param xml
	 *            the xml that should be sorted.
	 * @return the sorted xml
	 * @throws MojoFailureException
	 *             the mojo failure exception
	 */
	private String getSortedXml(final String xml) throws MojoFailureException {
		ByteArrayInputStream originalXmlInputStream = null;
		ByteArrayOutputStream sortedXmlOutputStream = null;
		try {
			originalXmlInputStream = new ByteArrayInputStream(xml.getBytes(pluginParameters.encoding));
			xmlProcessor.setOriginalXml(originalXmlInputStream);
			xmlProcessor.sortXml();
			sortedXmlOutputStream = xmlProcessor.getSortedXml();
			return sortedXmlOutputStream.toString(pluginParameters.encoding);
		} catch (JDOMException e) {
			throw new MojoFailureException("Could not sort pomfiles content: " + xml, e);
		} catch (IOException e) {
			throw new MojoFailureException("Could not sort pomfiles content: " + xml, e);
		} finally {
			IOUtils.closeQuietly(originalXmlInputStream);
			IOUtils.closeQuietly(sortedXmlOutputStream);
		}

	}

	private boolean pomFileIsSorted(String xml, String sortedXml) {
		return xml.replaceAll("\\n|\\r", "").equals(sortedXml.replaceAll("\\n|\\r", ""));
	}

	/**
	 * Creates the backup file for pom.
	 * 
	 * @throws MojoFailureException
	 *             the mojo failure exception
	 */
	private void createBackupFile() throws MojoFailureException {
		if (pluginParameters.createBackupFile) {
			if (pluginParameters.backupFileExtension.trim().length() == 0) {
				throw new MojoFailureException("Could not create backup file, extension name was empty");
			}
			fileUtil.backupFile();
			log.info("Saved backup of " + pluginParameters.pomFile.getAbsolutePath() + " to "
					+ pluginParameters.pomFile.getAbsolutePath() + pluginParameters.backupFileExtension);
		}
	}

	/**
	 * Saves the sorted pom file.
	 * 
	 * @param sortedXml
	 *            the sorted xml
	 * @throws MojoFailureException
	 *             the mojo failure exception
	 */
	private void saveSortedPomFile(final String sortedXml) throws MojoFailureException {
		fileUtil.savePomFile(sortedXml);
		log.info("Saved sorted pomfile to " + pluginParameters.pomFile.getAbsolutePath());
	}

}
