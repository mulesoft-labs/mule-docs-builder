package org.mule.docs.model;

import org.junit.Test;
import org.mule.docs.processor.DocBuildException;
import org.mule.docs.utils.TestUtilities;
import org.mule.docs.utils.Utilities;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Created by sean.osterberg on 2/22/15.
 */
public class SectionTest {

    @Test
    public void fromDirectory_WithValidSectionDirectory_ReturnsNewInstance() {
        Section section = getValidSection();
        assertThat(section, instanceOf(Section.class));
    }

    @Test(expected = DocBuildException.class)
    public void fromDirectory_WithInvalidSectionDirectory_ThrowsException() {
        File invalidDirectory = new File(Utilities.getConcatPath(new String[] { TestUtilities.getTestResourcesPath(), "empty" }));
        Section.fromDirectory(invalidDirectory);
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_HasCorrectNumberOfPages() {
        Section section = getValidSection();
        assertEquals(3, section.getPages().size());
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_HasCorrectUrl() {
        Section section = getValidSection();
        assertTrue(section.getUrl().contentEquals("cloudhub"));
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_HasCorrectRootNode() {
        Section section = getValidSection();
        assertTrue(section.getRootNode().getTitle().contentEquals("CloudHub"));
        assertEquals(12, section.getRootNode().getChildren().size());
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_HasCorrectFilepath() {
        Section section = getValidSection();
        assertEquals(TestUtilities.getPathToMasterFolder() + "/cloudhub", section.getFilepath());
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_HasOneVersion() {
        Section section = getValidSection();
        assertTrue(section.getVersions().size() == 2);
        assertTrue(section.getVersions().get(0).getVersions().isEmpty());
        String expected = TestUtilities.getPathToMasterFolder() + "/cloudhub/v/4.0";
        String actual = section.getVersions().get(0).getFilepath();
        assertEquals(expected, actual);
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_VersionHasProperUrl() {
        Section section = getValidSection();
        assertTrue(section.getVersions().size() == 2);
        assertTrue(section.getVersions().get(0).getUrl().contentEquals("cloudhub/v/4.0"));
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_VersionCorrectPrettyName() {
        Section section = getValidSection();
        assertTrue(section.getPrettyName().contentEquals("October 2014"));
    }

    @Test(expected = DocBuildException.class)
    public void fromDirectory_WithValidSectionDirectoryWithoutVersionFile_ThrowsException() {
        File invalidDirectory = new File(Utilities.getConcatPath(new String[] { TestUtilities.getPathToMasterFolder(), "_templates" }));
        Section.fromDirectory(invalidDirectory);
    }

    @Test
    public void fromDirectory_WithValidSectionButInvalidVersion_DoesNotHaveVersion() {
        File validDirectory = new File(Utilities.getConcatPath(new String[] { TestUtilities.getPathToMasterFolder(), "anypoint-platform" }));
        Section section = Section.fromDirectory(validDirectory);
        assertTrue(section.getVersions().isEmpty());
    }

    @Test
    public void fromMasterDirectory_WithValidMasterDirectory_IsValid() {
        File validDirectory = new File(Utilities.getConcatPath(new String[] { TestUtilities.getPathToMasterFolder() }));
        List<Section> sections = Section.fromMasterDirectory(validDirectory);
        assertEquals(2, sections.size());
    }

    @Test(expected = DocBuildException.class)
    public void fromMasterDirectory_WithInvalidMasterDirectory_ThrowsException() {
        File invalidDirectory = new File(Utilities.getConcatPath(new String[] { TestUtilities.getTestResourcesPath(), "text-files" }));
        Section.fromMasterDirectory(invalidDirectory);
    }

    @Test
    public void getVersionUrl_WithValidParams_ReturnsValidVersionUrl() {
        String versionPath = TestUtilities.getPathToMasterFolder() + "/cloudhub/v/4.0";
        String sectionName = "cloudhub";
        String result = Section.getVersionUrl(versionPath, sectionName);
        assertTrue(result.contentEquals("cloudhub/v/4.0"));
    }

    public Section getValidSection() {
        File validDirectory = new File(Utilities.getConcatPath(new String[] { TestUtilities.getPathToMasterFolder(), "cloudhub" }));
        return Section.fromDirectory(validDirectory);

    }
}
