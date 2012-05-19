package sortpom.wrapper;

import org.apache.commons.io.*;
import org.jdom.*;
import org.jdom.input.*;
import sortpom.*;
import sortpom.util.*;

import java.io.*;
import java.util.*;

/**
 * Concrete implementation of a wrapper factory that sorts xml according to
 * sort order from fileUtil.
 * <p/>
 * Thank you Christian Haelg for your sortProperties patch.
 *
 * @author Bjorn Ekryd
 */
public class WrapperFactoryImpl implements WrapperFactory {

	/** How much the sort order index should increase for each element type */
	private static final int SORT_ORDER_INCREMENT = 100;

	/** Start value for sort order index. */
	private static final int SORT_ORDER_BASE = 1000;

    private final FileUtil fileUtil;

    private final ElementSortOrderMap elementSortOrderMap = new ElementSortOrderMap();
    private final ElementWrapperCreator elementWrapperCreator = new ElementWrapperCreator(elementSortOrderMap);
    private final TextWrapperCreator textWrapperCreator = new TextWrapperCreator();

    /**
     * Instantiates a new wrapper factory impl.
     *
     * @param fileUtil the file util
     */
    public WrapperFactoryImpl(final FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

	/** Initializes the class with sortpom parameters. */
    public void setup(PluginParameters pluginParameters) {
        elementWrapperCreator.setup(pluginParameters);
        textWrapperCreator.setup(pluginParameters);
    }

    /**
     * Creates sort order map from chosen sort order.
     *
     * @see sortpom.wrapper.WrapperFactory#initialize()
     */
    @Override
    public void initialize() {
        ByteArrayInputStream inputStream = null;
        try {
            inputStream = putSortOrderFileElementsInSortingMap(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JDOMException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private ByteArrayInputStream putSortOrderFileElementsInSortingMap(ByteArrayInputStream inputStream)
            throws JDOMException, IOException {
        inputStream = new ByteArrayInputStream(fileUtil.getDefaultSortOrderXmlBytes());
        SAXBuilder parser = new SAXBuilder();
        Document document = parser.build(inputStream);
        addElementsSortOrderMap(document.getRootElement(), SORT_ORDER_BASE);
        return inputStream;
    }

    /**
     * @see sortpom.wrapper.WrapperFactory#createFromRootElement(org.jdom.Element)
     */
    @Override
    public WrapperOperations createFromRootElement(final Element rootElement) {
        return new GroupWrapper(create((Content) rootElement));
    }

    /**
     * @see sortpom.wrapper.WrapperFactory#create(org.jdom.Content)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Content> Wrapper<T> create(final T content) {
        if (content instanceof Element) {
            Wrapper<Element> wrapper = elementWrapperCreator.createWrapper((Element) content);
            return (Wrapper<T>) wrapper;
        }
        if (content instanceof Comment) {
            return new UnsortedWrapper<T>(content);
        }
        if (content instanceof Text) {
            Wrapper<? extends Content> wrapper = textWrapperCreator.createWrapper((Text) content);
            return (Wrapper<T>) wrapper;
        }
        return new UnsortedWrapper<T>(content);
    }

    /**
     * Processes the chosen sort order. Adds sort order element and sort index to
     * a map.
     */
    void addElementsSortOrderMap(final Element element, int sortOrder) {
        elementSortOrderMap.addElement(element, sortOrder);
        final ArrayList<Element> castToChildElementList = castToChildElementList(element);
        // Increments the sort order index for each element
        for (Element child : castToChildElementList) {
            sortOrder += SORT_ORDER_INCREMENT;
            addElementsSortOrderMap(child, sortOrder);
        }
    }

    /**
     * Performs getChildren for an element and casts the result to ArrayList of
     * Elements.
     */
    @SuppressWarnings("unchecked")
    private ArrayList<Element> castToChildElementList(final Element element) {
        return new ArrayList<Element>(element.getChildren());
    }

}
