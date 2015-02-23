import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.instanceOf;
/**
 * Created by sean.osterberg on 2/22/15.
 */
public class SectionTests {

    @Test
    public void fromDirectory_WithValidSectionDirectory_ReturnsNewInstance() {
        Section section = getValidSection();
        assertThat(section, instanceOf(Section.class));
    }

    @Test(expected = DocBuildException.class)
    public void fromDirectory_WithInvalidSectionDirectory_ThrowsException() {
        File invalidDirectory = new File(Utilities.getConcatPath(new String[] {TestUtilities.getTestResourcesPath(), "empty"}));
        Section section = Section.fromDirectory(invalidDirectory);
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_HasCorrectNumberOfPages() {
        Section section = getValidSection();
        assertTrue(section.getPages().size() == 2);
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_HasCorrectRootNode() {
        Section section = getValidSection();
        assertTrue(section.getRootNode().getTitle().contentEquals("CloudHub"));
        assertTrue(section.getRootNode().getChildren().size() == 12);
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_HasCorrectFilepath() {
        Section section = getValidSection();
        assertTrue(section.getFilepath().contentEquals("/Users/sean.osterberg/mulesoft-docs/dev/src/test/resources/master-folder/cloudhub"));
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_HasOneVersion() {
        Section section = getValidSection();
        assertTrue(section.getVersions().size() == 1);
        assertTrue(section.getVersions().get(0).getVersions().size() == 0);
        assertTrue(section.getVersions().get(0).getFilepath().contentEquals("/Users/sean.osterberg/mulesoft-docs/dev/src/test/resources/master-folder/cloudhub/v/4.0"));
    }

    @Test
    public void fromDirectory_WithValidSectionButInvalidVersion_DoesNotHaveVersion() {
        File validDirectory = new File(Utilities.getConcatPath(new String[] {TestUtilities.getPathToMasterFolder(), "anypoint-platform"}));
        Section section = Section.fromDirectory(validDirectory);
        assertTrue(section.getVersions().size() == 0);
    }

    public Section getValidSection() {
        File validDirectory = new File(Utilities.getConcatPath(new String[] {TestUtilities.getPathToMasterFolder(), "cloudhub"}));
        return Section.fromDirectory(validDirectory);
    }
}
