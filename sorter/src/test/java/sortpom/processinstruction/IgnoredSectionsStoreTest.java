package sortpom.processinstruction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import refutils.ReflectionHelper;

/**
 * @author bjorn
 * @since 2013-12-28
 */
class IgnoredSectionsStoreTest {

  private IgnoredSectionsStore ignoredSectionsStore;
  private ArrayList<String> ignoredSections;

  @SuppressWarnings("unchecked")
  @BeforeEach
  void setUp() {
    ignoredSectionsStore = new IgnoredSectionsStore();
    ignoredSections = new ReflectionHelper(ignoredSectionsStore).getField(ArrayList.class);
  }

  @Test
  void replaceNoSectionShouldReturnSameXml() {
    var xml =
        """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
              <artifactId>sortpom</artifactId>
              <description name="pelle" id="id" other="övrigt">Här använder vi åäö</description>
              <groupId>sortpom</groupId>
              <modelVersion>4.0.0</modelVersion>
              <name>SortPom</name>
              <!-- Egenskaper för projektet -->
              <properties>
                <compileSource>1.6</compileSource>
                <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
              </properties>
              <reporting />
              <version>1.0.0-SNAPSHOT</version>
            </project>""";
    var replaced = ignoredSectionsStore.replaceIgnoredSections(xml);

    assertThat(replaced, is(xml));
    assertThat(ignoredSections.size(), is(0));
  }

  @Test
  void replaceOneSectionShouldCreateOneToken() {
    var xml = "abc<?sortpom ignore?>def<?sortpom resume?>cba";
    var replaced = ignoredSectionsStore.replaceIgnoredSections(xml);

    assertThat(replaced, is("abc<?sortpom token='0'?>cba"));
    assertThat(ignoredSections.size(), is(1));
    assertThat(ignoredSections.get(0), is("<?sortpom ignore?>def<?sortpom resume?>"));
  }

  @Test
  void replaceMultipleSectionShouldCreateManyTokens() {
    var xml =
        "abc<?sortpom ignore?>def1<?sortpom resume?>cbaabc<?SORTPOM Ignore?>def2<?sortPom reSUME?>cba";
    var replaced = ignoredSectionsStore.replaceIgnoredSections(xml);

    assertThat(replaced, is("abc<?sortpom token='0'?>cbaabc<?sortpom token='1'?>cba"));
    assertThat(ignoredSections.size(), is(2));
    assertThat(ignoredSections.get(0), is("<?sortpom ignore?>def1<?sortpom resume?>"));
    assertThat(ignoredSections.get(1), is("<?SORTPOM Ignore?>def2<?sortPom reSUME?>"));
  }

  @Test
  void replaceMultipleLineXmlShouldCreateManyTokens() {
    var xml =
        """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

              <modelVersion>4.0.0</modelVersion>

              <!-- Basics -->
                <groupId>something</groupId>
              <artifactId>maven-sortpom-sorter</artifactId>
                <packaging>jar</packaging>
              <name>SortPom Sorter</name>
              <description>The sorting functionality</description>

              <dependencies>
                <dependency>
                  <groupId>org.jdom</groupId>
                  <artifactId>jdom</artifactId>
                  <version>1.1.3</version>
                </dependency>
                <dependency>
                  <groupId>junit</groupId>
                  <artifactId>junit</artifactId>
                    <?sortpom ignore?>
                  <version>4.11</version><!--$NO-MVN-MAN-VER$ -->
                    <?sortpom resume?>
                  <scope>test</scope>
                </dependency>
                <dependency>
                  <groupId>commons-io</groupId>
                  <artifactId>commons-io</artifactId>
                    <?sortpom ignore?>
                  <version>2.1</version><!--$NO-MVN-MAN-VER$ -->
                    <?sortpom resume?>
                </dependency>

                <!-- Test dependencies -->
                <dependency>
                  <groupId>com.google.code.reflection-utils</groupId>
                  <artifactId>reflection-utils</artifactId>
                  <version>0.0.1</version>
                  <scope>test</scope>
                </dependency>
              </dependencies>

            </project>
            """;
    ignoredSectionsStore.replaceIgnoredSections(xml);

    assertThat(ignoredSections.size(), is(2));
    assertThat(
        ignoredSections.get(0),
        is(
            """
                <?sortpom ignore?>
                      <version>4.11</version><!--$NO-MVN-MAN-VER$ -->
                        <?sortpom resume?>"""));
    assertThat(
        ignoredSections.get(1),
        is(
            """
                <?sortpom ignore?>
                      <version>2.1</version><!--$NO-MVN-MAN-VER$ -->
                        <?sortpom resume?>"""));
  }

  @Test
  void revertTokensInOrderShouldWork() {
    var xml = "abc<?sortpom token='0'?>cbaabc<?sortpom token='1'?>cba";
    ignoredSections.add("<?sortpom ignore?>def1<?sortpom resume?>");
    ignoredSections.add("<?SORTPOM Ignore?>def2<?sortPom reSUME?>");

    var replaced = ignoredSectionsStore.revertIgnoredSections(xml);
    assertThat(
        replaced,
        is(
            "abc<?sortpom ignore?>def1<?sortpom resume?>cbaabc<?SORTPOM Ignore?>def2<?sortPom reSUME?>cba"));
  }

  @Test
  void revertTokensInRearrangedOrderShouldPlaceTextInRightOrder() {
    var xml = "abc<?sortpom token='1'?>cbaabc<?sortpom token='0'?>cba";
    ignoredSections.add("<?sortpom ignore?>def0<?sortpom resume?>");
    ignoredSections.add("<?SORTPOM Ignore?>def1<?sortPom reSUME?>");

    var replaced = ignoredSectionsStore.revertIgnoredSections(xml);
    assertThat(
        replaced,
        is(
            "abc<?SORTPOM Ignore?>def1<?sortPom reSUME?>cbaabc<?sortpom ignore?>def0<?sortpom resume?>cba"));
  }

  @Test
  void revertTokensInMultipleLinesShouldPlaceTextInRightOrder() {
    var xml =
        """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

              <modelVersion>4.0.0</modelVersion>

              <!-- Basics -->
              <groupId>something</groupId>
              <artifactId>maven-sortpom-sorter</artifactId>
              <packaging>jar</packaging>

              <dependencies>
                <dependency>
                  <groupId>commons-io</groupId>
                  <artifactId>commons-io</artifactId>
                  <?sortpom token='1'?>
                </dependency>
                <dependency>
                  <groupId>org.jdom</groupId>
                  <artifactId>jdom</artifactId>
                  <version>1.1.3</version>
                </dependency>

                <!-- Test dependencies -->
                <dependency>
                  <groupId>com.google.code.reflection-utils</groupId>
                  <artifactId>reflection-utils</artifactId>
                  <version>0.0.1</version>
                  <scope>test</scope>
                </dependency>
                <dependency>
                  <groupId>junit</groupId>
                  <artifactId>junit</artifactId>
                  <?sortpom token='0'?>
                  <scope>test</scope>
                </dependency>
              </dependencies>
              <name>SortPom Sorter</name>
              <description>The sorting functionality</description>

            </project>
            """;
    ignoredSections.add(
        """
            <?sortpom ignore?>
                  <version>4.11</version><!--$NO-MVN-MAN-VER$ -->
                    <?sortpom resume?>""");
    ignoredSections.add(
        """
            <?sortpom ignore?>
                  <version>2.1</version><!--$NO-MVN-MAN-VER$ -->
                    <?sortpom resume?>""");

    var replaced = ignoredSectionsStore.revertIgnoredSections(xml);
    assertThat(
        replaced,
        is(
            """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

                  <modelVersion>4.0.0</modelVersion>

                  <!-- Basics -->
                  <groupId>something</groupId>
                  <artifactId>maven-sortpom-sorter</artifactId>
                  <packaging>jar</packaging>

                  <dependencies>
                    <dependency>
                      <groupId>commons-io</groupId>
                      <artifactId>commons-io</artifactId>
                      <?sortpom ignore?>
                      <version>2.1</version><!--$NO-MVN-MAN-VER$ -->
                        <?sortpom resume?>
                    </dependency>
                    <dependency>
                      <groupId>org.jdom</groupId>
                      <artifactId>jdom</artifactId>
                      <version>1.1.3</version>
                    </dependency>

                    <!-- Test dependencies -->
                    <dependency>
                      <groupId>com.google.code.reflection-utils</groupId>
                      <artifactId>reflection-utils</artifactId>
                      <version>0.0.1</version>
                      <scope>test</scope>
                    </dependency>
                    <dependency>
                      <groupId>junit</groupId>
                      <artifactId>junit</artifactId>
                      <?sortpom ignore?>
                      <version>4.11</version><!--$NO-MVN-MAN-VER$ -->
                        <?sortpom resume?>
                      <scope>test</scope>
                    </dependency>
                  </dependencies>
                  <name>SortPom Sorter</name>
                  <description>The sorting functionality</description>

                </project>
                """));
  }
}
