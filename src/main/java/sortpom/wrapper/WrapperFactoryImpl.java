package sortpom.wrapper;

import org.apache.commons.io.IOUtils;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import sortpom.util.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Concrete implementation of a wrapper factory that sorts xml according to sortorder from fileutil.
 *
 * @author Bjorn Ekryd
 */
public class WrapperFactoryImpl implements WrapperFactory {

    /** How much the sortorder index should increase for each element type */
    private static final int SORT_ORDER_INCREMENT = 100;

    /** Startvalue for sortorder index. */
    private static final int SORT_ORDER_BASE = 1000;

    /** Contains sortorder element names and their index. */
    private Map<String, Integer> elementNameSortOrderMap = new HashMap<String, Integer>();

    /** The fileutility. */
    private final FileUtil fileUtil;

    /**
     * Instantiates a new wrapper factory impl.
     *
     * @param fileUtil the file util
     */
    public WrapperFactoryImpl(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    /**
     * @see sortpom.wrapper.WrapperFactory#create(org.jdom.Element)
     */
    @Override
    public WrapperOperations create(final Element rootElement) {
        return new GroupWrapper(create((Content) rootElement));
    }

    /**
     * @see sortpom.wrapper.WrapperFactory#create(org.jdom.Content)
     */
    @Override
    public <T extends Content> Wrapper<T> create(final T content) {
        if (content instanceof Element) {
            Element element = (Element) content;
            String deepName = getDeepName(element);
            if (elementNameSortOrderMap.containsKey(deepName)) {
                return createSortedWrapper(element, deepName);
            }
        }
        return new UnsortedWrapper<T>(content);
    }

    /**
     * Creates sortorder map from choosen sortorder.
     * @see sortpom.wrapper.WrapperFactory#initialize()
     */
    @Override
    public void initialize() {
        ByteArrayInputStream inputStream = null;
        try {
            // properties.load(new
            // StringReader(FILE_TEMPLATE.fetchAsString("sortorder_elements.properties")));
            SAXBuilder parser = new SAXBuilder();
            inputStream = new ByteArrayInputStream(fileUtil.getDefaultSortOrderXml().getBytes(fileUtil.getEncoding()));
            Document document = parser.build(inputStream);
            addElementsSortOrderMap(document.getRootElement(), SORT_ORDER_BASE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JDOMException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }


    /**
     * Processes the choosen sortorder. Adds sortorder element and sort index to a map.
     *
     * @param element the element
     * @param sortOrder the sort order
     */
    void addElementsSortOrderMap(final Element element, int sortOrder) {
        final String deepName = getDeepName(element);
        elementNameSortOrderMap.put(deepName, sortOrder);
        // System.out.println(deepName + " : " + sortOrder);
        final ArrayList<Element> castToChildElementList = castToChildElementList(element);
        // Increments the sortorder index for each element
        for (Element child : castToChildElementList) {
            sortOrder += SORT_ORDER_INCREMENT;
            addElementsSortOrderMap(child, sortOrder);
        }
    }

    /**
     * Performs getChildren for an element and casts the result to ArrayList of Elements.
     *
     * @param element the element
     * @return the array list
     */
    @SuppressWarnings("unchecked")
    private ArrayList<Element> castToChildElementList(final Element element) {
        return new ArrayList<Element>(element.getChildren());
    }

    /**
     * Creates sorted wrapper around an element and casts the result to generic Wrapper.
     *
     * @param <T> the generic type
     * @param element the element
     * @param deepName the deep name
     * @return the wrapper
     */
    @SuppressWarnings("unchecked")
    private <T extends Content> Wrapper<T> createSortedWrapper(final Element element, final String deepName) {
        return (Wrapper<T>) new SortedWrapper(element, elementNameSortOrderMap.get(deepName));
    }

    private String getDeepName(final Element element) {
        if (element == null) {
            return "";
        }
        return new StringBuilder().append(getDeepName(element.getParentElement())).append('/')
                .append(element.getName()).toString();
    }

}
