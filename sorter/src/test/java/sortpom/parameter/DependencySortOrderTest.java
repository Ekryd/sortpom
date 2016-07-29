package sortpom.parameter;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author bjorn
 * @since 2016-07-29
 */
public class DependencySortOrderTest {

    @Test
    public void emptySortOrderShouldWork() throws Exception {
        assertThat(new DependencySortOrder(null).toString(), is("DependencySortOrder{childElementNames=[]}"));
        assertThat(new DependencySortOrder("").toString(), is("DependencySortOrder{childElementNames=[]}"));
    }

    @Test
    public void singleSortOrderShouldWork() throws Exception {
        assertThat(new DependencySortOrder("Gurka").toString(), is("DependencySortOrder{childElementNames=[Gurka]}"));
    }

    @Test
    public void multipleSortOrderShouldWork() throws Exception {
        assertThat(new DependencySortOrder("Gurka,Tomat,Melon").toString(), is("DependencySortOrder{childElementNames=[Gurka, Tomat, Melon]}"));
    }
}