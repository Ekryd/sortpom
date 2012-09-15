package sortpom.wrapper;

import org.jdom.Element;
import org.jdom.Text;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author bjorn
 * @since 2012-09-13
 */
public class ElementSortOrderMapTest {
    private ElementSortOrderMap map = new ElementSortOrderMap();
    private int sortOrderCounter = 1;
    private Element project;
    private Element exclArtifactId1;
    private Element exclArtifactId2;
    private Element exclArtifactId3;

    @Before
    public void setup() {
        project = new Element("project");

        Element dependencies = new Element("dependencies");

        Element dependency1 = new Element("dependency");
        Element groupId1 = new Element("groupId");
        Element artifactId1 = new Element("artifactId");
        Element version1 = new Element("version");
        Element scope1 = new Element("scope");
        Element exclusions1 = new Element("exclusions");
        exclArtifactId1 = new Element("artifactId");
        Element exclGroupId1 = new Element("groupId");
        Element exclusion1 = new Element("exclusion").addContent(exclArtifactId1).addContent(exclGroupId1);
        exclusions1.addContent(exclusion1);
        dependency1.addContent(groupId1).addContent(artifactId1).addContent(version1).addContent(scope1).addContent(exclusions1);

        Element dependency2 = new Element("dependency");
        // Here we insert a text
        Element groupId2 = new Element("groupId").addContent(new Text("junit"));
        Element artifactId2 = new Element("artifactId");
        Element version2 = new Element("version");
        // Here we insert a text
        Element scope2 = new Element("scope").addContent(new Text("test"));
        Element exclusions2 = new Element("exclusions");
        exclArtifactId2 = new Element("artifactId");
        Element exclGroupId2 = new Element("groupId");
        Element exclusion2 = new Element("exclusion").addContent(exclArtifactId2).addContent(exclGroupId2);
        exclusions2.addContent(exclusion2);
        dependency2.addContent(groupId2).addContent(artifactId2).addContent(version2).addContent(scope2).addContent(exclusions2);

        Element dependency3 = new Element("dependency");
        Element groupId3 = new Element("groupId");
        Element artifactId3 = new Element("artifactId");
        Element version3 = new Element("version");
        // Here we insert a text
        Element scope3 = new Element("scope").addContent(new Text("test"));
        Element exclusions3 = new Element("exclusions");
        exclArtifactId3 = new Element("artifactId");
        Element exclGroupId3 = new Element("groupId");
        Element exclusion3 = new Element("exclusion").addContent(exclArtifactId3).addContent(exclGroupId3);
        exclusions3.addContent(exclusion3);
        dependency3.addContent(groupId3).addContent(artifactId3).addContent(version3).addContent(scope3).addContent(exclusions3);

        dependencies.addContent(dependency1).addContent(dependency2).addContent(dependency3);
        project.addContent(dependencies);

        addSortOrder(project);
    }

    private void addSortOrder(Element element) {
        map.addElement(element, sortOrderCounter++);
        List<Element> children = (List<Element>) element.getChildren();
        for (Element child : children) {
            addSortOrder(child);
        }
    }

    @Test
    public void testSetupStructureShouldWork() {
        assertThat(map.getDeepName(exclArtifactId1), is("/project/dependencies/dependency/exclusions/exclusion/artifactId"));
        assertThat(map.getDeepName(exclArtifactId2), is("/project/dependencies/dependency/exclusions/exclusion/artifactId"));
        assertThat(map.getDeepName(exclArtifactId3), is("/project/dependencies/dependency/exclusions/exclusion/artifactId"));
        assertThat(map.getDeepName(project), is("/project"));
    }
    
    @Test
    public void firstLevelfindSimpleShouldWork() {
        assertThat(map.getSortOrder(new Element("project")), is(1));
    }

    @Test
    public void secondLevelFindSimpleShouldWork() {
        Element dependencies1 = new Element("dependencies");
        new Element("project").addContent(dependencies1);
        
        assertThat(map.getSortOrder(dependencies1), is(2));
    }

    @Test
    public void thirdLevelFindMultipleChoiceShouldSelectFirst() {
        Element dependency = new Element("dependency");
        Element dependencies = new Element("dependencies").addContent(dependency);
        new Element("project").addContent(dependencies);
        
        assertThat(map.getSortOrder(dependency), is(3));
    }

    @Test
    public void thirdLevelFindMultipleChoiceShouldSelectTextMatch() {
        Element groupId = new Element("groupId").addContent(new Text("gurka"));
        Element scope = new Element("scope").addContent(new Text("test"));
        Element dependency = new Element("dependency").addContent(scope).addContent(groupId);
        Element dependencies = new Element("dependencies").addContent(dependency);
        new Element("project").addContent(dependencies);
        
        assertThat(map.getSortOrder(dependency), is(21));
    }

    @Test
    public void thirdLevelFindMultipleChoiceShouldSelectNoTextMatch() {
        Element scope = new Element("scope").addContent(new Text("runtime"));
        Element dependency = new Element("dependency").addContent(scope);
        Element dependencies = new Element("dependencies").addContent(dependency);
        new Element("project").addContent(dependencies);
        
        assertThat(map.getSortOrder(dependency), is(3));
    }

    @Test
    public void fourthLevelFindMultipleChoiceShouldSelectTextMatch() {
        Element groupId = new Element("groupId").addContent(new Text("gurka"));
        Element scope = new Element("scope").addContent(new Text("test"));
        Element dependency = new Element("dependency").addContent(scope).addContent(groupId);
        Element dependencies = new Element("dependencies").addContent(dependency);
        new Element("project").addContent(dependencies);

        assertThat(map.getSortOrder(scope), is(25));
    }

    @Test
    public void fourthLevelFindMultipleChoiceShouldSelectMoreExactTextMatch() {
        Element groupId = new Element("groupId").addContent(new Text("junit"));
        Element scope = new Element("scope").addContent(new Text("test"));
        Element dependency = new Element("dependency").addContent(scope).addContent(groupId);
        Element dependencies = new Element("dependencies").addContent(dependency);
        new Element("project").addContent(dependencies);

        assertThat(map.getSortOrder(scope), is(16));
    }

    @Test
    public void fourthLevelFindMultipleChoiceShouldSelectNoTextMatch() {
        Element scope = new Element("scope").addContent(new Text("runtime"));
        Element dependency = new Element("dependency").addContent(scope);
        Element dependencies = new Element("dependencies").addContent(dependency);
        new Element("project").addContent(dependencies);
        
        assertThat(map.getSortOrder(scope), is(7));
    }

    @Test
    public void fourthLevelFindMultipleChoiceShouldSelectTextMatchBasedOnParent() {
        Element groupId = new Element("groupId").addContent(new Text("gurka"));
        Element scope = new Element("scope").addContent(new Text("test"));
        Element dependency = new Element("dependency").addContent(scope).addContent(groupId);
        Element dependencies = new Element("dependencies").addContent(dependency);
        new Element("project").addContent(dependencies);

        assertThat(map.getSortOrder(groupId), is(22));
    }

    @Test
    public void fourthLevelFindMultipleChoiceShouldSelectNoTextMatchBasedOnParent() {
        Element groupId = new Element("groupId");
        Element scope = new Element("scope").addContent(new Text("runtime"));
        Element dependency = new Element("dependency").addContent(scope).addContent(groupId);
        Element dependencies = new Element("dependencies").addContent(dependency);
        new Element("project").addContent(dependencies);

        assertThat(map.getSortOrder(groupId), is(4));
    }

    @Test
    public void fifthLevelFindMultipleChoiceShouldSelectTextMatchBasedOnParent() {
        Element exclusion = new Element("exclusion");
        Element exclusions = new Element("exclusions").addContent(exclusion);
        Element groupId = new Element("groupId").addContent(new Text("gurka"));
        Element scope = new Element("scope").addContent(new Text("test"));
        Element dependency = new Element("dependency").addContent(scope).addContent(groupId).addContent(exclusions);
        Element dependencies = new Element("dependencies").addContent(dependency);
        new Element("project").addContent(dependencies);

        assertThat(map.getSortOrder(exclusion), is(27));
    }

    @Test
    public void fifthLevelFindMultipleChoiceShouldSelectMoreExactTextMatchBasedOnParent() {
        Element exclusion = new Element("exclusion");
        Element exclusions = new Element("exclusions").addContent(exclusion);
        Element groupId = new Element("groupId").addContent(new Text("junit"));
        Element scope = new Element("scope").addContent(new Text("test"));
        Element dependency = new Element("dependency").addContent(scope).addContent(groupId).addContent(exclusions);
        Element dependencies = new Element("dependencies").addContent(dependency);
        new Element("project").addContent(dependencies);

        assertThat(map.getSortOrder(exclusion), is(18));
    }

    @Test
    public void fifthLevelFindMultipleChoiceShouldSelectNoTextMatchBasedOnParent() {
        Element exclusion = new Element("exclusion");
        Element exclusions = new Element("exclusions").addContent(exclusion);
        Element scope = new Element("scope").addContent(new Text("runtime"));
        Element dependency = new Element("dependency").addContent(scope).addContent(exclusions);
        Element dependencies = new Element("dependencies").addContent(dependency);
        new Element("project").addContent(dependencies);

        assertThat(map.getSortOrder(exclusion), is(9));
    }
}
