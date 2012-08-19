package sortpom.util;

/**
 * @author bjorn
 * @since 2012-08-11
 */
public final class XmlOrderedResult {
    private final boolean ordered;
    private final String message;

    public static XmlOrderedResult ordered() {
        return new XmlOrderedResult(true, "");
    }

    public static XmlOrderedResult nameDiffers(String originalElementName, String newElementName) {
        return new XmlOrderedResult(false, String.format("The xml element <%s> should be placed before <%s>",
                newElementName, originalElementName));
    }

    public static XmlOrderedResult childElementDiffers(String name, int originalSize, int newSize) {
        return new XmlOrderedResult(false, String.format(
                "The xml element <%s> with %s child elements should be placed before element <%s> with %s child elements",
                name, newSize, name, originalSize));
    }

    public static XmlOrderedResult textContentDiffers(String name, String originalElementText, String newElementText) {
        return new XmlOrderedResult(false, String.format("The xml element <%s>%s</%s> should be placed before <%s>%s</%s>",
                name, newElementText, name, name, originalElementText, name));
    }

    private XmlOrderedResult(boolean ordered, String message) {
        this.ordered = ordered;
        this.message = message;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public String getMessage() {
        return message;
    }

}
