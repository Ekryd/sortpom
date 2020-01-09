package sortpom.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;

/**
 * Utility class that encapsulates methods dealing with file attributes (in
 * particular, timestamps). Moving this functionality into separate class allows
 * for easier testing.
 */
class FileAttributeUtil {

	/**
	 * Retrieves the timestamp of last modification of given file.
	 *
	 * @param file The file to be examined
	 * @return Timestamp (in millis) of file's last modification
	 */
	public long getLastModifiedTimestamp(File file) {
		return file.lastModified();
	}

	/**
	 * Sets the access dates (creation, last modification, last access) for the
	 * given file all to the same provided value.
	 *
	 * @param file   The file to set the dates for
	 * @param millis The value for the access dates
	 * @throws IOException If any I/O error occurs
	 */
	public void setTimestamps(File file, long millis) throws IOException {
		BasicFileAttributeView attributes = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
		FileTime time = FileTime.fromMillis(millis);
		attributes.setTimes(time, time, time);
	}
}
