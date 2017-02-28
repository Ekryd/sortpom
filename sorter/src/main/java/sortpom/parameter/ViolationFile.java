package sortpom.parameter;

import java.io.File;

/**
 * Encapulates the behaviour of the violation file. I.e. the file that will contain the unsorted element during verify
 * @author bjorn
 * @since 2017-02-28
 */
public class ViolationFile {

    private final File file;

    public ViolationFile(String filename) {
        this.file = new File(filename);
        if (file.exists()) {
            String message = String.format("Violation file %s already exists", file.getAbsolutePath());
            throw new RuntimeException(message);
        }
    }

    public void store(File pomFile, String errorMessage) {
        
    }
}
