package sortpom.sort;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import sortpom.util.SortPomImplUtil;

class IndentationTest {

  @ParameterizedTest()
  @ValueSource(ints = {0, 1, 4, -1})
  void differentIndentsShouldWork(int indent) {
    var expectedFile = "/SortModules_expectedIndent" + indent + ".xml";
    SortPomImplUtil.create()
        .sortProperties()
        .sortPlugins("groupId,artifactId")
        .sortModules()
        .sortDependencies("groupId,artifactId")
        .lineSeparator("\n")
        .nrOfIndentSpace(indent)
        .testFiles("/SortModules_input.xml", expectedFile);
  }

  @ParameterizedTest()
  @ValueSource(ints = {0, 1, 4, -1})
  void indentSchemaLocationShouldBeIndentTimesTwoPlusOne(int indent) {
    var expectedFile = "/SortModules_expectedSchemaIndent" + indent + ".xml";
    SortPomImplUtil.create()
        .sortProperties()
        .sortPlugins("groupId,artifactId")
        .sortModules()
        .sortDependencies("groupId,artifactId")
        .lineSeparator("\n")
        .nrOfIndentSpace(indent)
        .indentAttribute("schemaLocation")
        .testFiles("/SortModules_input.xml", expectedFile);
  }
}
