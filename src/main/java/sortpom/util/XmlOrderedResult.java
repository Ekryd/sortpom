package sortpom.util;

/**
 * @author bjorn
 * @since 2012-08-11
 */
public class XmlOrderedResult {
    private final boolean ordered;
    private final String originalElementName;
    private final String newElementName;

    public static XmlOrderedResult ordered() {
        return new XmlOrderedResult(true, null, null);
    }

    public static XmlOrderedResult notOrdered(String originalElementName, String newElementName) {
        return new XmlOrderedResult(false, originalElementName, newElementName);
    }

    private XmlOrderedResult(boolean ordered, String originalElementName, String newElementName) {
        this.ordered = ordered;
        this.originalElementName = originalElementName;
        this.newElementName = newElementName;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public String getOriginalElementName() {
        return originalElementName;
    }

    public String getNewElementName() {
        return newElementName;
    }
}
