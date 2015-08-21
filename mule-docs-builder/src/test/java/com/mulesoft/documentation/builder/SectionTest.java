package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.model.SectionVersion;
import com.mulesoft.documentation.builder.util.Utilities;
import org.junit.Test;

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
        File invalidDirectory = new File(Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "empty"}));
        Section section = Section.fromDirectoryAndSectionVersion(invalidDirectory, getValidSectionVersion());
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_HasCorrectNumberOfPages() {
        Section section = getValidSection();
        assertEquals(2, section.getPages().size());
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_HasCorrectUrl() {
        Section section = getValidSection();
        assertTrue(section.getUrl().equals("cloudhub"));
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_HasCorrectRootNode() {
        Section section = getValidSection();
        assertTrue(section.getRootNode().getTitle().equals("CloudHub"));
        assertEquals(12, section.getRootNode().getChildren().size());
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_HasCorrectFilepath() {
        Section section = getValidSection();
        assertEquals(TestUtilities.getPathToMasterFolder() + "/cloudhub", section.getFilepath());
    }

    /*
    @Test
    public void fromDirectory_WithValidSectionDirectory_HasOneVersion() {
        Section section = getValidSection();
        assertTrue(section.getVersions().size() == 1);
        assertTrue(section.getVersions().get(0).getVersions().isEmpty());
        String expected = TestUtilities.getPathToMasterFolder() + "/cloudhub/v/4.0";
        String actual = section.getVersions().get(0).getFilepath();
        assertEquals(expected, actual);
    }

    @Test
    public void fromDirectory_WithValidSectionDirectory_VersionHasProperUrl() {
        Section section = getValidSection();
        assertTrue(section.getVersions().size() == 1);
        assertTrue(section.getVersions().get(0).getUrl().equals("cloudhub/v/4.0"));
    }*/

    @Test
    public void fromDirectory_WithValidSectionDirectory_VersionCorrectPrettyName() {
        Section section = getValidSection();
        assertTrue(section.getVersionPrettyName().equals("Current"));
    }

    @Test(expected = DocBuildException.class)
    public void fromDirectory_WithValidSectionDirectoryWithoutVersionFile_ThrowsException() {
        File invalidDirectory = new File(Utilities.getConcatPath(new String[] {TestUtilities.getPathToMasterFolder(), "_templates"}));
        Section section = Section.fromDirectoryAndSectionVersion(invalidDirectory, getValidSectionVersion());
    }

    /*
    @Test
    public void fromDirectory_WithValidSectionButInvalidVersion_DoesNotHaveVersion() {
        File validDirectory = new File(Utilities.getConcatPath(new String[] {TestUtilities.getPathToMasterFolder(), "anypoint-platform"}));
        Section section = Section.fromDirectory(validDirectory);
        assertTrue(section.getVersions().size() == 0);
    }*/

    @Test
    public void getVersionUrl_WithValidParams_ReturnsValidVersionUrl() {
        String versionPath = TestUtilities.getPathToMasterFolder() + "/cloudhub/v/4.0";
        String sectionName = "cloudhub";
        String result = Section.getVersionUrl(versionPath, sectionName);
        assertTrue(result.contentEquals("cloudhub/v/4.0"));
    }

    public Section getValidSection() {
        File validDirectory = new File(Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub"}));
        return Section.fromDirectoryAndSectionVersion(validDirectory, getValidSectionVersion());

    }

    private SectionVersion getValidSectionVersion() {
        return new SectionVersion("CloudHub", "cloudhub", "cloudhub/v/", "latest", true);
    }
}
