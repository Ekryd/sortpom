package sortpom;

import static org.hamcrest.Matchers.*;

import org.junit.*;
import org.junit.rules.*;

public class SortOrderFilesTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public final void correctCustomSortOrderShouldSortThePm() throws Exception {
		SortOrderFilesUtil.testFilesWithCustomSortOrder("/full_unsorted_input.xml",
				"/sortOrderFiles/sorted_differentOrder.xml", "difforder/differentOrder.xml");
	}

	@Test
	public final void incorrectCustomSortOrderShouldThrowException() throws Exception {
		thrown.expect(RuntimeException.class);
		thrown.expectMessage(endsWith("VERYdifferentOrder.xml in classpath"));
		SortOrderFilesUtil.testFilesWithCustomSortOrder("/full_unsorted_input.xml",
				"/sortOrderFiles/sorted_differentOrder.xml", "difforder/VERYdifferentOrder.xml");
	}

	@Test
	public final void incorrectPredefinedSortOrderShouldThrowException() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Cannot find abbie_normal_brain.xml among the predefined plugin resources");
		SortOrderFilesUtil.testFilesWithPredefinedSortOrder("/full_unsorted_input.xml",
				"/sortOrderFiles/sorted_default_0_4_0.xml", "abbie_normal_brain");
	}

	@Test
	public final void default040ShouldWorkAsPredefinedSortOrder() throws Exception {
		SortOrderFilesUtil.testFilesWithPredefinedSortOrder("/full_unsorted_input.xml",
				"/sortOrderFiles/sorted_default_0_4_0.xml", "default_0_4_0");
	}

	@Test
	public final void custom1ShouldWorkAsPredefinedSortOrder() throws Exception {
		SortOrderFilesUtil.testFilesWithPredefinedSortOrder("/full_unsorted_input.xml",
				"/sortOrderFiles/sorted_custom_1.xml", "custom_1");
	}

	@Test
	public final void recommended2008_06ShouldWorkAsPredefinedSortOrder() throws Exception {
		SortOrderFilesUtil.testFilesWithPredefinedSortOrder("/full_unsorted_input.xml",
				"/sortOrderFiles/sorted_recommended_2008_06.xml", "recommended_2008_06");
	}

	@Test
	public final void default100ShouldWorkAsPredefinedSortOrder() throws Exception {
		SortOrderFilesUtil.testFilesWithPredefinedSortOrder("/full_unsorted_input.xml",
				"/sortOrderFiles/sorted_default_1_0_0.xml", "default_1_0_0");
	}

	@Test
	public final void defaultPredefinedSortOrderShouldWork() throws Exception {
		SortOrderFilesUtil.testFilesWithPredefinedSortOrder("/full_unsorted_input.xml",
				"/sortOrderFiles/sorted_default_1_0_0.xml", null);
	}

}
