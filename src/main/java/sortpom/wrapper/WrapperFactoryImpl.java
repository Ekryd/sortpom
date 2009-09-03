package sortpom.wrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import se.mine.mindif.Dependency;
import sortpom.util.FileUtil;

/**
 *  Skapar wrappers kring xml-fragment. Konkret implementation
 *
 * @author Bjorn
 */
public class WrapperFactoryImpl implements WrapperFactory {
	private static final int SORT_ORDER_INCREMENT = 100;
	private static final int SORT_ORDER_BASE = 1000;
	private Map<String, Integer> elementNameSortOrderMap = new HashMap<String, Integer>();

	@Dependency
	private FileUtil fileUtil;

	@Override
	public WrapperOperations create(final Element rootElement) {
		return new GroupWrapper(create((Content) rootElement));
	}

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
	 * @param properties
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

	void addElementsSortOrderMap(final Element element, int sortOrder) {
		final String deepName = getDeepName(element);
		elementNameSortOrderMap.put(deepName, sortOrder);
		// System.out.println(deepName + " : " + sortOrder);
		final ArrayList<Element> castToChildElementList = castToChildElementList(element);
		for (Element child : castToChildElementList) {
			sortOrder += SORT_ORDER_INCREMENT;
			addElementsSortOrderMap(child, sortOrder);
		}
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Element> castToChildElementList(final Element element) {
		return new ArrayList<Element>(element.getChildren());
	}

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
