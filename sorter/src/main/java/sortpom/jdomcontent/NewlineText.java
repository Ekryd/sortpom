package sortpom.jdomcontent;

import org.jdom.Comment;

/**
 * @author bjorn
 * @since 2012-05-17
 *        <p/>
 *        The NewlineText is not really a special case of comment. Its just that comments
 *        are not subjected to trimming by jdom. The special handling of NewlineText is done
 *        in XmlProcessor.PatchedXMLOutputter
 */
public class NewlineText extends Comment {
    private static final long serialVersionUID = -7552189498553321263L;

    /**
     * This returns a <code>String</code> representation of the
     * <code>NewlineText</code>, suitable for debugging. If the XML
     * representation of the <code>Comment</code> is desired,
     * {@link org.jdom.output.XMLOutputter#outputString(org.jdom.Comment)}
     * should be used.
     *
     * @return <code>String</code> - information about the
     *         <code>Attribute</code>
     */
    public String toString() {
        return "[NewLine]";
    }

}
