package sortpom;

import org.jdom.Document;
import org.jdom.Element;

import java.io.File;

/**
 * Used to store an external violation file
 *
 * @author bjorn
 * @since 2017-03-24
 */
class ViolationXmlProcessor {
    Document createViolationXmlContent(File pomFileLocation, String violationMessage) {
        Element violationElement = new Element("violation");
        violationElement.setText(violationMessage);

        Element fileElement = new Element("file");
        fileElement.addContent(violationElement);
        fileElement.setAttribute("filename", pomFileLocation.getAbsolutePath());

        Element rootElement = new Element("sortpom");
        rootElement.addContent(fileElement);

        return new Document(rootElement);
    }
}
