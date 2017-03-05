package sortpom.parameter;

import org.jdom.Document;
import org.jdom.Element;

import java.io.File;

/**
 * Encapulates the behaviour of the violation file. I.e. the file that will contain which element was unsorted element during verify
 */
public class ViolationFile {

    private final File file;

    ViolationFile(String filename) {
        this.file = new File(filename);
        if (file.exists()) {
            String message = String.format("Violation file %s already exists", file.getAbsolutePath());
            throw new RuntimeException(message);
        }
    }

    public Document createViolationXmlContent(File pomFileLocation, String violationMessage) {
        Element violationElement = new Element("violation");
        violationElement.setText(violationMessage);
        
        Element fileElement = new Element("file");
        fileElement.addContent(violationElement);
        fileElement.setAttribute("filename", pomFileLocation.getAbsolutePath());
        
        Element rootElement = new Element("sortpom");
        rootElement.addContent(fileElement);
        
        return new Document(rootElement);
    }

    public File getViolationFile() {
        return file;
    }
}
