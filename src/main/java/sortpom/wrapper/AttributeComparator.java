package sortpom.wrapper;

import org.jdom.Attribute;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Compares two attributes for an xml element.
 *
 * @author Bjorn
 */
final class AttributeComparator implements Comparator<Attribute>, Serializable {
    private static final long serialVersionUID = -4513084031755508007L;

    @Override
    public int compare(final Attribute o1, final Attribute o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
