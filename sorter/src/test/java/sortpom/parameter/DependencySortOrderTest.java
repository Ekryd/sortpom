package sortpom.parameter;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author bjorn
 * @since 2016-07-29
 */
public class DependencySortOrderTest {

    @Test
    public void emptySortOrderShouldWork() {
        assertThat(new DependencySortOrder(null).toString(), is("DependencySortOrder{childElementNames=[]}"));
        assertThat(new DependencySortOrder("").toString(), is("DependencySortOrder{childElementNames=[]}"));
    }

    @Test
    public void singleSortOrderShouldWork() {
        assertThat(new DependencySortOrder("Gurka").toString(), is("DependencySortOrder{childElementNames=[Gurka]}"));
    }

    @Test
    public void multipleSortOrderShouldWork() {
        assertThat(new DependencySortOrder("Gurka,Tomat,Melon").toString(), is("DependencySortOrder{childElementNames=[Gurka, Tomat, Melon]}"));
    }
}