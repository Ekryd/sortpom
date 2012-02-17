package sortpom.wrapper;

import java.io.*;
import java.util.*;

import org.apache.commons.io.*;
import org.jdom.*;
import org.jdom.input.*;

import sortpom.*;
import sortpom.util.*;

/**
 * Concrete implementation of a wrapper factory that sorts xml according to
 * sort order from fileUtil.
 * 
 * Thank you Christian Haelg for your sortProperties patch.
 * 
 * @author Bjorn Ekryd
 */
public class WrapperFactoryImpl implements WrapperFactory {

	/** How much the sort order index should increase for each element type */
	private static final int SORT_ORDER_INCREMENT = 100;

	/** Start value for sort order index. */
	private static final int SORT_ORDER_BASE = 1000;

	/** Contains sort order element names and their index. */
	private final Map<String, Integer> elementNameSortOrderMap = new HashMap<String, Integer>();

	private final FileUtil fileUtil;

	private boolean sortDependencies;
	private boolean sortPlugins;
	private boolean sortProperties;

	/**
	 * Instantiates a new wrapper factory impl.
	 * 
	 * @param fileUtil
	 *            the file util
	 */
	public WrapperFactoryImpl(final FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	/** Initializes the class with sortpom parameters. */
	public void setup(PluginParameters pluginParameters) {
		this.sortDependencies = pluginParameters.sortDependencies;
		this.sortPlugins = pluginParameters.sortPlugins;
		this.sortProperties = pluginParameters.sortProperties;
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
			Wrapper<Element> wrapper = createElementWrapper((Element) content);
			return (Wrapper<T>) wrapper;
		}
		return new UnsortedWrapper<T>(content);
	}

	private Wrapper<Element> createElementWrapper(final Element element) {
		String deepName = getDeepName(element);
		boolean sortedBySortOrderFile = elementNameSortOrderMap.containsKey(deepName);
		if (sortedBySortOrderFile) {
			if (isDependencyElement(element)) {
				return new GroupAndArtifactSortedWrapper(element, elementNameSortOrderMap.get(deepName));
			}
			if (isPluginElement(element)) {
				return new GroupAndArtifactSortedWrapper(element, elementNameSortOrderMap.get(deepName));
			}
			return new SortedWrapper(element, elementNameSortOrderMap.get(deepName));
		}
		if (isPropertyElement(deepName, element)) {
			return new AlphabeticalSortedWrapper(element);
		}
		return new UnsortedWrapper<Element>(element);
	}

	private boolean isDependencyElement(final Element element) {
		if (!sortDependencies) {
			return false;
		}
		return isElementName(element, "dependency") && isElementParentName(element, "dependencies");
	}

	private boolean isPluginElement(final Element element) {
		if (!sortPlugins) {
			return false;
		}
		return isElementName(element, "plugin") && isElementParentName(element, "plugins");
	}

	private boolean isPropertyElement(String deepName, final Element element) {
		if (!sortProperties) {
			return false;
		}
		boolean inTheRightPlace = deepName.startsWith("/project/properties/")
				|| deepName.startsWith("/project/profiles/profile/properties/");
		return inTheRightPlace && isElementParentName(element, "properties");
	}

	/**
	 * Processes the chosen sort order. Adds sort order element and sort index to
	 * a map.
	 */
	void addElementsSortOrderMap(final Element element, int sortOrder) {
		final String deepName = getDeepName(element);
		elementNameSortOrderMap.put(deepName, sortOrder);
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

	private String getDeepName(final Element element) {
		if (element == null) {
			return "";
		}
		return new StringBuilder().append(getDeepName(element.getParentElement())).append('/')
				.append(element.getName()).toString();
	}

	private boolean isElementName(Element element, String name) {
		return element.getName().equals(name);
	}

	private boolean isElementParentName(Element element, String name) {
		Parent parent = element.getParent();
		if (parent != null && parent instanceof Element) {
			return isElementName((Element) parent, name);
		}
		return false;
	}
}
