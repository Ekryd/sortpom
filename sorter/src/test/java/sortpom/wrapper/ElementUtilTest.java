package sortpom.wrapper;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import refutils.ReflectionHelper;

/**
 * @author bjorn
 * @since 2016-06-22
 */
class ElementUtilTest {

  private Element parent;
  private Element child;

  @BeforeEach
  void setUp() {
    parent = new BaseElement("Parent");
    child = new BaseElement("Child");
    parent.add(child);
  }

  @Test
  void testConstructor() {
    var elementUtil = ReflectionHelper.instantiatePrivateConstructor(ElementUtil.class);
    assertThat(elementUtil, not(nullValue()));
  }

  @Test
  void parentElementNameShouldBeMatched() {
    assertThat(ElementUtil.isElementParentName(child, "Parent"), is(true));
    assertThat(ElementUtil.isElementParentName(child, "Gurka"), is(false));
    assertThat(ElementUtil.isElementParentName(parent, "Parent"), is(false));
  }
}
