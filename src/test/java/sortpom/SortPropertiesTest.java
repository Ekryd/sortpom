package sortpom;

import org.junit.*;
import org.junit.rules.*;

public class SortPropertiesTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public final void namedParametersInSortFileShouldSortThemFirst() throws Exception {
		SortOrderFilesUtil.testFilesWithCustomSortOrder("/SortedProperties_input.xml", "/SortedProperties_output.xml",
				"difforder/sortedPropertiesOrder.xml");
	}

}
