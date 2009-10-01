package sortpom.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Used to interface with file system
 *
 * @author Bjorn
 *
 */
public class FileUtil {
	private static final String DEFAULT_SORT_ORDER_FILENAME = "defaultOrder.xml";
	private File pomFile;
	private String backupFileExtension;
	private String encoding;
	private File defaultOrderFileName;

	public void backupFile() throws MojoFailureException {
		final String newName = pomFile.getAbsolutePath() + backupFileExtension;
		final File backupFile = new File(newName);
		if (backupFile.exists() && !backupFile.delete()) {
			throw new MojoFailureException("Could not remove old backup file, filename: " + newName);
		}
		FileInputStream source = null;
		FileOutputStream newFile = null;
		try {
			source = new FileInputStream(pomFile);
			newFile = new FileOutputStream(backupFile);
			IOUtils.copy(source, newFile);
		} catch (IOException e) {
			throw new MojoFailureException("Could not create backup file to filename: " + newName, e);
		} finally {
			IOUtils.closeQuietly(newFile);
			IOUtils.closeQuietly(source);
		}
	}

	public String getDefaultSortOrderXml() {
		InputStream inputStream = null;
		try {
			if (defaultOrderFileName == null) {
				URL resource = this.getClass().getClassLoader().getResource(DEFAULT_SORT_ORDER_FILENAME);
				inputStream = resource.openStream();
			} else {
				inputStream = getFileFromRelativeOrClassPath();
			}
			return IOUtils.toString(inputStream, encoding);
		} catch (IOException ioex) {
			throw new RuntimeException(ioex);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	public String getEncoding() {
		return encoding;
	}

	public String getPomFileContent() throws MojoFailureException {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(pomFile);
			return IOUtils.toString(inputStream, encoding);
		} catch (IOException ioex) {
			throw new MojoFailureException("Could not read pomfile: " + pomFile.getAbsolutePath(), ioex);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	public void savePomFile(final String sortedXml) throws MojoFailureException {
		FileOutputStream saveFile = null;
		try {
			saveFile = new FileOutputStream(pomFile);
			IOUtils.write(sortedXml, saveFile, encoding);
		} catch (IOException e) {
			throw new MojoFailureException("Could not save sorted pomfile: " + pomFile, e);
		} finally {
			IOUtils.closeQuietly(saveFile);
		}
	}

	/**
	 * @param pomFile
	 * @param backupFileExtension
	 * @param encoding
	 */
	public void setup(final File pomFile, final String backupFileExtension, final String encoding,
			final File defaultOrderFileName) {
		this.pomFile = pomFile;
		this.backupFileExtension = backupFileExtension;
		this.encoding = encoding;
		this.defaultOrderFileName = defaultOrderFileName;
	}

	private InputStream getFileFromRelativeOrClassPath() throws IOException {
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(defaultOrderFileName);
		} catch (FileNotFoundException fnfex) {
			// try classpath
			try {
				URL resource = this.getClass().getClassLoader().getResource(defaultOrderFileName.getPath());
				if (resource == null) {
					throw new IOException("Cannot find resource");
				}
				inputStream = resource.openStream();
			} catch (IOException e1) {
				throw new FileNotFoundException(String.format("Could not find %s or %s in classpath",
						defaultOrderFileName.getAbsolutePath(), defaultOrderFileName.getPath()));
			}
		}
		return inputStream;
	}
}
