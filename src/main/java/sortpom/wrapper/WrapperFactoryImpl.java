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
 * sortorder from fileutil.
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
				if ((sortDependencies && isDependencyElement(element)) || sortPlugins && isPluginElement(element)) {
					return createGroupAndArtifactSortedElement(element, deepName);
				}
				return createSortedWrapper(element, deepName);
			}
		}
		return new UnsortedWrapper<T>(content);
	}

	private boolean isDependencyElement(final Element element) {
		if (element.getName().equals("dependency")) {
			Parent parent = element.getParent();
			if (parent != null && parent instanceof Element) {
				Element parentElement = (Element) parent;
				return parentElement.getName().equals("dependencies");
			}
		}
		return false;
	}

	private boolean isPluginElement(final Element element) {
		if (element.getName().equals("plugin")) {
			Parent parent = element.getParent();
			if (parent != null && parent instanceof Element) {
				Element parentElement = (Element) parent;
				return parentElement.getName().equals("plugins");
			}
		}
		return false;
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
			// properties.load(new
			// StringReader(FILE_TEMPLATE.fetchAsString("sortorder_elements.properties")));
			SAXBuilder parser = new SAXBuilder();
			inputStream = new ByteArrayInputStream(fileUtil.getDefaultSortOrderXmlBytes());
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
	 * Processes the choosen sortorder. Adds sortorder element and sort index to
	 * a map.
	 * 
	 * @param element
	 *            the element
	 * @param sortOrder
	 *            the sort order
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
	 * Performs getChildren for an element and casts the result to ArrayList of
	 * Elements.
	 * 
	 * @param element
	 *            the element
	 * @return the array list
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<Element> castToChildElementList(final Element element) {
		return new ArrayList<Element>(element.getChildren());
	}

	/**
	 * Creates sorted wrapper around an element and casts the result to generic
	 * Wrapper.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param element
	 *            the element
	 * @param deepName
	 *            the deep name
	 * @return the wrapper
	 */
	@SuppressWarnings("unchecked")
	private <T extends Content> Wrapper<T> createSortedWrapper(final Element element, final String deepName) {
		return (Wrapper<T>) new SortedWrapper(element, elementNameSortOrderMap.get(deepName));
	}

	@SuppressWarnings("unchecked")
	private <T extends Content> Wrapper<T> createGroupAndArtifactSortedElement(final Element element,
			final String deepName) {
		return (Wrapper<T>) new GroupAndArtifactSortedWrapper(element, elementNameSortOrderMap.get(deepName));
	}

	private String getDeepName(final Element element) {
		if (element == null) {
			return "";
		}
		return new StringBuilder().append(getDeepName(element.getParentElement())).append('/')
				.append(element.getName()).toString();
	}

}
